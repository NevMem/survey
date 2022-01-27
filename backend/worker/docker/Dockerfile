FROM ubuntu:20.04

RUN apt update && \
    DEBIAN_FRONTEND=noninteractive \
    apt-get install -y --allow-downgrades --allow-remove-essential --allow-change-held-packages \
    vim \
    telnet \
    lsof \
    syslog-ng \
    curl \
    openjdk-8-jdk \
    unzip

COPY /tmp/ /
COPY testing_runner.sh .
COPY env.sh .
RUN unzip worker-0.0.1.zip

CMD ["./runner.sh"]

EXPOSE 80 443
