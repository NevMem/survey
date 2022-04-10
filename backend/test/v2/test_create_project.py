import pytest
from utils import random_string
from client import Client
from client_fixture import client
from v2.user_fixture import user, User

@pytest.mark.v2
def test_create_project(client: Client, user: User):
    name = random_string()
    response = client.create_project(user.token, name)
    assert response.status_code == 200
    json = response.json()
    assert 'id' in json
    assert 'name' in json
    assert 'owner' in json
    assert json['name'] == name
    assert user.login == json['owner']['login']
