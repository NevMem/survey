from client_fixture import *
from client import Client
from utils import random_string

def test_register(client: Client):
    token = client.login('admin', 'password').json()['token']
    invite = client.create_invite(token, 38 * 60).json()['invite']

    login = random_string(10)
    password = random_string(10)
    name = random_string(10)
    surname = random_string(10)
    email = random_string(10) + '@gmail.com'

    response = client.register(
        login=login,
        password=password,
        name=name,
        surname=surname,
        email=email,
        inviteId=invite['inviteId']
    )
    assert response.status_code == 200
    assert 'token' in response.json()
    me = client.me(response.json()['token']).json()
    assert me['login'] == login
    assert me['name'] == name
    assert me['surname'] == surname
    assert me['email'] == email
    assert len(me['roles']) == 0
