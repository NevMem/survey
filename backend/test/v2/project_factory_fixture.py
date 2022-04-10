import pytest
from client import Client
from client_fixture import client
from user_fixture import User
from utils import random_string

class Project:
    id: int
    name: int
    
    def __init__(self, id, name):
        self.id = id
        self.name = name


@pytest.fixture
def project_factory(client: Client):
    def factory(user: User, name: str=random_string()) -> Project:
        response = client.create_project(user.token, name)
        assert response.status_code == 200
        return Project(response.json()['id'], response.json()['name'])
    return factory
