import pytest
from client_fixture import client
from client import Client
from utils import random_string

@pytest.mark.v2
def test_register_simple_v2(client: Client):
    response = client.register_v2(
        login=random_string(),
        password=random_string(),
        name=random_string(),
        surname=random_string(),
        email=random_string(),
    )
    assert response.status_code == 200
    json = response.json()
    assert 'type' in json
    assert json['type'] == 'success'
    assert 'token' in json
