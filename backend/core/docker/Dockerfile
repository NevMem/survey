FROM ubuntu:20.04

COPY build_dir /
COPY runner.sh /
COPY env.sh /
COPY nginx-production-config.conf /

RUN apt update && \
    DEBIAN_FRONTEND=noninteractive \
    apt-get install -y --allow-downgrades --allow-remove-essential --allow-change-held-packages \
    vim \
    telnet \
    lsof \
    syslog-ng \
    curl \
    openjdk-8-jdk \
    nginx \
    python3 \
    python3-venv \
    libaugeas0
    
RUN python3 -m venv /opt/certbot/
RUN /opt/certbot/bin/pip install --upgrade pip
RUN /opt/certbot/bin/pip install certbot certbot-nginx
RUN ln -s /opt/certbot/bin/certbot /usr/bin/certbot
RUN certbot certonly --nginx

CMD [ "./runner.sh" ]

EXPOSE 80 443
