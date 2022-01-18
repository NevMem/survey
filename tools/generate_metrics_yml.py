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
      - targets: [{% for instance in instances %}'{{instance}}'{{ ", " if not loop.last else "" }}{% endfor %}]
        labels:
          group: 'production'

""")


def main():
    machines = get_machines_for_service('core')
    instances = list(map(lambda x: x.ip, machines))

    with open('tmp/prometheus.yml', 'w') as out:
        out.write(TEMPLATE.render(instances=instances))


if __name__ == '__main__':
    main()
