FROM openjdk:8-jdk-alpine

COPY build_dir /
COPY runner.sh /
COPY env.sh /

RUN apk update && apk add bash
RUN apk -U add vim

CMD [ "./runner.sh" ]

EXPOSE 80
