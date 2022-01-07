from client_fixture import *

def test_roles(client: Client):
    assert client.roles().status_code == 200
    assert 'admin' in list(map(lambda x: x['id'], client.roles().json()['roles']))
