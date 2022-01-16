from utils import random_string
import pytest
from client import Client
from client_fixture import *


@pytest.mark.slow
def test_simple_media_uploading(client: Client):
    with open('tmp.txt', 'w') as out:
        for _ in range(256):
            out.write(random_string(256) + '\n')
    response = client.upload_media(open('tmp.txt', 'rb'))
    assert response.status_code == 200


@pytest.mark.slow
def test_media_large_file(client: Client):
    with open('tmp.txt', 'w') as out:
        for _ in range(1024):
            out.write(random_string(1024) + '\n')
    response = client.upload_media(open('tmp.txt', 'rb'))
    assert response.status_code == 200


@pytest.mark.slow
def test_media_gallery_creation(client: Client):
    with open('tmp.txt', 'w') as out:
        for _ in range(16):
            out.write(random_string(15) + '\n')

    responses = []
    for _ in range(2):
        responses.append(
            client.upload_media(open('tmp.txt', 'rb')).json()
        )

    response = client.create_gallery(responses)
    assert response.status_code == 200
