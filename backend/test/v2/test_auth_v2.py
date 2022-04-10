import pytest
from client import Client
from client_fixture import client
from v2.user_fixture import user, User

@pytest.mark.v2
def test_auth_v2(client: Client, user: User):
    response = client.check_auth(user.token)
    assert response.status_code == 200
