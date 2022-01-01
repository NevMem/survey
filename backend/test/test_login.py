from client import Client
from client_fixture import *

def test_login(client: Client):
    assert client.login('admin', 'password').status_code == 200

    assert client.login('admin1', 'password').status_code != 200
    assert client.login('admin2', 'password').status_code != 200
    assert client.login('admin3', 'password').status_code != 200

    assert client.login('a1', 'password').status_code != 200
    assert client.login('ksfnfn', 'password').status_code != 200
    assert client.login('sljlsgml', 'password').status_code != 200


def test_login_reponse_type(client: Client):
    response = client.login('admin', 'password').json()
    assert 'type' in response

    response = client.login('admin', '').json()
    assert 'type' in response
    assert 'error' in response
