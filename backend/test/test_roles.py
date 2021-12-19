from client_fixture import *

def test_roles(client: Client):
    assert client.roles().status_code == 200
    assert 'admin' in client.roles().json()
