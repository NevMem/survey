import jinja2
from machines import get_machines_for_service

TEMPLATE = jinja2.Template("""\
global:
  scrape_interval: 5s
  evaluation_interval: 5s

scrape_configs:
  - job_name: 'core'
    scrape_interval: 5s
    static_configs:
      - targets: [{% for instance in core_instances %}'{{instance}}'{{ ", " if not loop.last else "" }}{% endfor %}]
        labels:
          group: 'production'

  - job_name: 'worker'
    scrape_interval: 5s
    static_configs:
      - targets: [{% for instance in worker_instances %}'{{instance}}'{{ ", " if not loop.last else "" }}{% endfor %}]
        labels:
          group: 'production'

""")


def main():
    core_machines = get_machines_for_service('core')
    worker_machines = get_machines_for_service('worker')
    core_instances = list(map(lambda x: x.ip, core_machines))
    worker_instances = list(map(lambda x: x.ip, worker_machines))

    with open('tmp/prometheus.yml', 'w') as out:
        out.write(TEMPLATE.render(core_instances=core_instances, worker_instances=worker_instances))


if __name__ == '__main__':
    main()
