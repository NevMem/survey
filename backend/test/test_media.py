from utils import random_string
import requests


def test_simple_media_uploading():
    with open('tmp.txt', 'w') as out:
        for _ in range(256):
            out.write(random_string(256) + '\n')
    response = requests.post('http://core:8080/v1/upload', files={'file': open('tmp.txt', 'rb')})
    assert response.status_code == 200


def test_media_large_file():
    with open('tmp.txt', 'w') as out:
        for _ in range(1024):
            out.write(random_string(1024) + '\n')
    response = requests.post('http://core:8080/v1/upload', files={'file': open('tmp.txt', 'rb')})
    assert response.status_code == 200
