from typing import List
import subprocess as sp
import json
import sys
import argparse


class Machine:
    name: str
    ip: str

    def __init__(self, name: str, ip: str):
        self.name = name
        self.ip = ip

    def __str__(self):
        return f"Machine(name: {self.name}, ip: {self.ip})"

    def __repr__(self):
        return str(self)


def get_machines_list() -> List[Machine]: 
    output = sp.check_output(['yc', 'compute', 'instance', 'list', '--format', 'json'])
    data = json.loads(output)
    result = []
    for vm in data:
        name = vm['name']
        ip = vm['network_interfaces'][0]['primary_v4_address']['one_to_one_nat']['address']
        result.append(Machine(name, ip))
    return result


def launch_remote_command(user_name: str, ip: str, identity_file_path: str, command: List[str]):
    sp.check_call([
        'ssh',
        '-o',
        'StrictHostKeyChecking=no',
        f"{user_name}@{ip}",
        '-i',
        identity_file_path,
        *command
    ])


def deploy_to_one_machine(identity_file_path: str, container_name: str, image_tag: str, machine: Machine):
    def launch(command: List[str]):
        launch_remote_command('nevmem', machine.ip, identity_file_path, command)

    launch(['sudo', 'docker', 'pull', image_tag])
    launch(['sudo', 'docker', 'stop', container_name])
    launch(['sudo', 'docker', 'rm', container_name])
    launch(['sudo', 'docker', 'run', '-d', '-p', '80:80', '--name', container_name, image_tag])


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('service')
    parser.add_argument('path_to_revisions_file')
    parser.add_argument('identity_file_path')
    args = parser.parse_args()

    with open(args.path_to_revisions_file, 'r') as inp:
        revisions = json.loads(inp.read())
        docker_image_tag = revisions[args.service]

    machines = get_machines_list()
    machines = list(filter(lambda x: x.name.startswith('survey'), machines))
    
    if len(machines) == 0:
        print('Machines not found exiting')
        sys.exit(1)
        

    if len(machines) == 1:
        print('Running in one machine mode')
        deploy_to_one_machine(args.identity_file_path, args.service, docker_image_tag, machines[0])
        return

    print('Multi machines mode not supported')
    sys.exit(1)


if __name__ == '__main__':
    main()
