from client_fixture import *
from client import Client
from utils import random_string
import pytest


@pytest.mark.invite
def test_empty_invites(client: Client):
    token = client.login('admin', 'password').json()['token']
    response = client.invites(token)
    assert response.status_code == 200


@pytest.mark.invite
def test_create_invite(client: Client):
    token = client.login('admin', 'password').json()['token']
    response = client.create_invite(token, 60 * 30)
    invite = response.json()['invite']
    assert invite in client.invites(token).json()['invites']


@pytest.mark.invite
def test_invited_user_roles(client: Client):
    token = client.login('admin', 'password').json()['token']
    invite = client.create_invite(token, 60 * 30).json()['invite']

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
    ).json()

    user_token = response['token']
    me = client.me(user_token)
    assert me.status_code == 200
    me = me.json()
    assert 'roles' in me
    assert 'allAvailableRoles' in me
    assert len(me['roles']) == 0
    assert len(me['allAvailableRoles']) == 0


@pytest.mark.invite
def test_role_changes(client: Client):
    token = client.login('admin', 'password').json()['token']
    invite = client.create_invite(token, 60 * 30).json()['invite']

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
    ).json()

    user_token = response['token']
    me = client.me(user_token)
    assert me.status_code == 200
    me = me.json()
    assert 'roles' in me
    assert 'allAvailableRoles' in me
    assert len(me['roles']) == 0
    assert len(me['allAvailableRoles']) == 0

    response = client.managed_users(token)
    current_user = None
    for user in response.json()['users']:
        if user['login'] == login:
            current_user = user
    assert current_user is not None

    roles = [
        {'id': 'role.manager'},
        {'id': 'invite.manager'},
    ]

    response = client.update_roles(token, {'user': current_user, 'roles': roles})
    assert response.status_code == 200

    me = client.me(user_token)
    assert me.status_code == 200
    me = me.json()
    assert 'roles' in me
    assert 'allAvailableRoles' in me
    assert len(me['roles']) == 2
    assert len(me['allAvailableRoles']) == 2
