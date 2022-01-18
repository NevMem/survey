from typing import List
import subprocess as sp
import json
import sys
import argparse
from machines import Machine, get_machines_for_service


def launch_remote_command(user_name: str, ip: str, identity_file_path: str, command: List[str]):
    proc = sp.Popen([
        'ssh',
        '-o',
        'StrictHostKeyChecking=no',
        f"{user_name}@{ip}",
        '-i',
        identity_file_path,
        *command
    ])
    proc.wait()
    print(command, 'exit code:', proc.returncode)


def deploy_to_one_machine(identity_file_path: str, container_name: str, image_tag: str, machine: Machine, ports: List[int]):
    def launch(command: List[str]):
        launch_remote_command('nevmem', machine.ip, identity_file_path, command)

    launch(['sudo', 'docker', 'pull', image_tag])
    launch(['sudo', 'docker', 'stop', container_name])
    launch(['sudo', 'docker', 'rm', container_name])

    ports_arguments = []
    for port in ports:
        ports_arguments.append('-p')
        ports_arguments.append(f"{port}:{port}")

    launch(['sudo', 'docker', 'run', '-d', *ports_arguments, '--name', container_name, image_tag])


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('service')
    parser.add_argument('path_to_revisions_file')
    parser.add_argument('identity_file_path')
    parser.add_argument('--port', nargs='+')
    args = parser.parse_args()

    ports = list(map(lambda x: int(x), args.port))

    with open(args.path_to_revisions_file, 'r') as inp:
        revisions = json.loads(inp.read())
        docker_image_tag = revisions[args.service]

    machines = get_machines_for_service(args.service)
    
    if len(machines) == 0:
        print('Machines not found exiting')
        sys.exit(1)
        

    if len(machines) == 1:
        print('Running in one machine mode')
        deploy_to_one_machine(args.identity_file_path, args.service, docker_image_tag, machines[0], ports)
        return

    print('Multi machines mode not supported')
    sys.exit(1)


if __name__ == '__main__':
    main()
