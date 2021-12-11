from client_fixture import *

def test_roles(client: Client):
    print(client.roles().status_code)
    print(client.roles().text)
    print(client.roles().json())
