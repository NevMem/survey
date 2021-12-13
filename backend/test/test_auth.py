from client import Client
from client_fixture import *


def test_login_admin(client: Client):
    response = client.login('admin', 'password')
    assert response.status_code == 200
    assert 'token' in response.json()


def test_me(client: Client):
    token = client.login('admin', 'password').json()['token']
    response = client.me(token)
    json = response.json()
    assert 'login' in json
    assert 'id' in json
    assert 'name' in json
    assert 'surname' in json
    assert 'email' in json
    assert 'roles' in json
