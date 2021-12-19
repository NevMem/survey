import requests

from client import Client
from client_fixture import *

def test_ping():
    assert requests.get("http://core:8080/ping").status_code == 200


def test_ping_with_client(client: Client):
    assert client.ping().status_code == 200
