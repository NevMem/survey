from client_fixture import *
from client import Client


def test_empty_invites(client: Client):
    token = client.login('admin', 'password').json()['token']
    response = client.invites(token)
    assert response.status_code == 200


def test_create_invite(client: Client):
    token = client.login('admin', 'password').json()['token']
    response = client.create_invite(token, 60 * 30)
    invite = response.json()['invite']
    assert invite in client.invites(token).json()['invites']
