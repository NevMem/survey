import json
import subprocess as sp
from typing import List
import re


class Machine:
    name: str
    ip: str
    tags: List[str]

    def __init__(self, name: str, ip: str, tags: List[str]):
        self.name = name
        self.ip = ip
        self.tags = tags

    def __str__(self):
        return f"Machine(name: {self.name}, ip: {self.ip}, tags: {self.tags})"

    def __repr__(self):
        return str(self)


def get_machines_list() -> List[Machine]: 
    output = sp.check_output(['yc', 'compute', 'instance', 'list', '--format', 'json'])
    data = json.loads(output)
    result = []
    for vm in data:
        name = vm['name']
        ip = vm['network_interfaces'][0]['primary_v4_address']['one_to_one_nat']['address']
        if 'description' in vm:
            tags = vm['description'].split(',')
        else:
            tags = []
        result.append(Machine(name, ip, tags))
    return result


def filter_core_service_machines(machines: List[Machine]) -> List[Machine]:
    result = []
    for machine in machines:
        if re.match(r"survey-[0-9]+", machine.name):
            result.append(machine)
    return result


def filter_metrics_service_machines(machines: List[Machine]) -> List[Machine]:
    result = []
    for machine in machines:
        if re.match(r"survey-metrics", machine.name):
            result.append(machine)
    return result


def filter_worker_service_machines(machines: List[Machine]) -> List[Machine]:
    result = []
    for machine in machines:
        if 'worker' in machine.tags:
            result.append(machine)
    return result


def filter_push_service_machines(machines: List[Machine]) -> List[Machine]:
    def is_push_machine(machine: Machine):
        return 'push' in machine.tags
    return list(filter(is_push_machine, machines))

def get_machines_for_service(service: str) -> List[Machine]:
    machines = get_machines_list()
    if service == 'core':
        return filter_core_service_machines(machines)
    if service == 'metrics':
        return filter_metrics_service_machines(machines)
    if service == 'worker':
        return filter_worker_service_machines(machines)
    if service == 'push':
        return filter_push_service_machines(machines)
    
    raise ValueError(f"Unknown service {service}")
