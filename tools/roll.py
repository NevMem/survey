from machines import Machine, get_machines_for_service
from typing import List
import subprocess as sp
from machines import get_machines_list


class RemoteCommandExecutor:
    username: str
    ip: str
    identity_file_path: str
    
    def __init__(self, username: str, ip: str, identity_file_path: str):
        self.username = username
        self.ip = ip
        self.identity_file_path = identity_file_path

    def launch_command(self, command: List[str]) -> int:
        proc = sp.Popen([
            'ssh',
            '-o',
            'StrictHostKeyChecking=no',
            f"{self.username}@{self.ip}",
            '-i',
            self.identity_file_path,
            *command
        ])
        proc.wait()
        print(command, 'exit code:', proc.returncode)
        return proc.returncode

    def is_docker_installer(self):
        return self.launch_command(['docker', '-v']) == 0

    def install_docker(self):
        self.launch_command(['sudo', 'apt-get', 'update'])
        self.launch_command(['sudo', 'apt-get', 'install', 'ca-certificates', 'curl', 'gnupg', 'lsb-release'])
        self.launch_command(['bin/bash', 'curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg'])
        # self.launch_command(['sudo', 'apt-get', 'update'])
        # self.launch_command(['sudo', 'apt-get', 'install', 'docker-ce', 'docker-ce-cli', 'containerd.io'])



def main():
    machines = get_machines_list()
    print(machines)


if __name__ == '__main__':
    main()
