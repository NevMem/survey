import pytest
from client_fixture import client
from v2.user_fixture import *
from client import Client

@pytest.mark.v2
def test_login_v2(client: Client, user: User):
    response = client.login_v2(user.login, user.password)
    print(response.text)
    print(response.status_code)
    assert response.status_code == 200
    json = response.json()
    assert 'type' in json
    assert json['type'] == 'success'
    assert 'token' in json
