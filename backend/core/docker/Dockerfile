FROM ubuntu:20.04

COPY build_dir /
COPY runner.sh /
COPY env.sh /

RUN apt update && \
    DEBIAN_FRONTEND=noninteractive \
    apt-get install -y --allow-downgrades --allow-remove-essential --allow-change-held-packages \
    vim \
    telnet \
    lsof \
    syslog-ng \
    curl \
    openjdk-8-jdk \
    nginx

CMD [ "./runner.sh" ]

EXPOSE 80 443 8080
