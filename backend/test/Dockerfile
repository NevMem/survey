FROM python:3.8
RUN mkdir /app/
WORKDIR /app/
COPY . .
RUN pip install -r requirements.txt
ENTRYPOINT ["pytest", "-vs", "test_ping.py"]
