FROM python:3.8
RUN mkdir /app/
WORKDIR /app/
COPY test/requirements.txt .
RUN pip install -r requirements.txt
COPY test/ .
COPY wait-for-it.sh .
ENTRYPOINT ["./runner.sh"]
