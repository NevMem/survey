import argparse
import json
from machines import Machine, get_machines_for_service
from typing import List
import subprocess as sp
from machines import get_machines_list
from notify import Notificator, with_exception_notificator


class RemoteCommandExecutor:
    username: str
    ip: str
    identity_file_path: str
    
    def __init__(self, username: str, ip: str, identity_file_path: str):
        self.username = username
        self.ip = ip
        self.identity_file_path = identity_file_path

    def launch_command(self, command: List[str]) -> int:
        proc = sp.Popen(
            [
                'ssh',
                '-o',
                'StrictHostKeyChecking=no',
                f"{self.username}@{self.ip}",
                '-i',
                self.identity_file_path,
                *command
            ],
            stdout=sp.PIPE
        )
        proc.wait()
        stdout = []
        for line in proc.stdout:
            stdout.append(line.decode())
        print(command, 'exit code:', proc.returncode)
        print(stdout)
        return proc.returncode, stdout

    def is_docker_installer(self):
        return self.launch_command(['docker', '-v'])[0] == 0

    def install_docker(self, token):
        self.launch_command(['curl', '-fsSL', 'https://get.docker.com', '-o', 'get-docker.sh'])
        self.launch_command(['sudo', 'sh', 'get-docker.sh'])
        self.launch_command(['sudo', 'docker', 'login', '--username', 'oauth', '--password', token, 'cr.yandex'])

    def get_containers(self):
        return json.loads('[' + ', '.join(self.launch_command(['sudo', 'docker', 'ps', '--format="{{json .}}"'])[1]) + ']')


def deploy(executor: RemoteCommandExecutor, image_tag: str, machine: Machine, ports: List[int], container_name: str):
    executor.launch_command(['sudo', 'docker', 'pull', image_tag])
    executor.launch_command(['sudo', 'docker', 'stop', container_name])
    executor.launch_command(['sudo', 'docker', 'rm', container_name])

    ports_arguments = []
    for port in ports:
        ports_arguments.append('-p')
        ports_arguments.append(f"{port}:{port}")

    executor.launch_command(['sudo', 'docker', 'run', '-d', *ports_arguments, '--name', container_name, image_tag])


def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("services")
    parser.add_argument("services_file")
    parser.add_argument("identity_file")
    parser.add_argument("token")
    return parser.parse_args()


@with_exception_notificator
def main():
    args = parse_args()

    with open(args.services_file, 'r') as inp:
        services_config = json.load(inp)

    services = args.services.split(',')

    logs = []

    for service in services:
        assert service in services_config

    for service in services:
        service_config = services_config[service]

        logs.append(f"Working with service {service}")

        machines = get_machines_list()
        
        for machine in machines:
            index = 0
            for tag in machine.tags:
                if tag != service:
                    continue
                index += 1
                executor = RemoteCommandExecutor('nevmem', machine.ip, args.identity_file)
                if not executor.is_docker_installer():
                    executor.install_docker(args.token)
                containers = executor.get_containers()
                need_update = True
                for container in containers:
                    if container['Names'] == f"{service}-{index}" and container['Image'] == service_config['container-tag']:
                        need_update = False
                
                if need_update:
                    print('Deploying')
                    logs.append(f"⚠️ Need to redeploy {machine.name} by tag: {tag}\n")
                    deploy(
                        executor=executor,
                        image_tag=service_config['container-tag'],
                        machine=machine,
                        ports=service_config['ports'],
                        container_name=f"{service}-{index}",
                    )
                else:
                    logs.append(f"✅ Not need to redeploy {machine.name} by tag: {tag}\n")
                    print('No need for redeploy')

    Notificator().send_message('\n'.join(logs))


if __name__ == '__main__':
    main()
