from client import Client
from client_fixture import client
from utils import assert_ok_and_get_json
from v2.project_factory_fixture import project_factory, Project
from v2.user_fixture import *
import pytest

@pytest.mark.v2
def test_invite_simple(client: Client, user_factory, project_factory):
    user_1: User = user_factory()
    user_2: User = user_factory()

    project: Project = project_factory(user_1)

    for user in [user_1, user_2]:
        response = client.incoming_invites(user.token)
        assert response.status_code == 200
        assert len(response.json()['invites']) == 0

        response = client.outgoing_invites(user.token)
        assert response.status_code == 200
        assert len(response.json()['invites']) == 0

    response = client.create_invite_v2(
        token=user_1.token,
        projectId=project.id,
        login=user_2.login,
        expirationSeconds=10,
    )
    assert response.status_code == 200
    invite = response.json()['invite']

    response = assert_ok_and_get_json(client.outgoing_invites(user_1.token))
    assert len(response['invites']) == 1
    assert response['invites'][0]['id'] == invite['id']

    response = assert_ok_and_get_json(client.incoming_invites(user_2.token))
    assert len(response['invites']) == 1
    assert response['invites'][0]['id'] == invite['id']


@pytest.mark.v2
def test_accept_invite(client: Client, user_factory, project_factory):
    user_1: User = user_factory()
    user_2: User = user_factory()

    project: Project = project_factory(user_1)

    invite = assert_ok_and_get_json(client.create_invite_v2(
        token=user_1.token,
        projectId=project.id,
        login=user_2.login,
        expirationSeconds=10,
    ))['invite']

    json = assert_ok_and_get_json(client.accept_invite(user_2.token, invite['id']))
    assert 'status' in json
    assert json['status'] == 'Ok'

    projects_1 = assert_ok_and_get_json(client.projects(user_1.token))['projects']
    projects_2 = assert_ok_and_get_json(client.projects(user_2.token))['projects']
    assert projects_1[0]['id'] == project.id
