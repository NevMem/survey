from utils import random_string
import pytest
from client_fixture import client
from client import Client

class User:
    login: str
    password: str
    token: str

    def __init__(self, login: str, password: str, token: str):
        self.login = login
        self.password = password
        self.token = token

@pytest.fixture
def user_factory(client: Client):
    def factory() -> User:
        login, password, name, surname, email = [random_string() for _ in range(5)]
        response = client.register_v2(
            login=login,
            password=password,
            name=name,
            surname=surname,
            email=email,
        )
        assert response.status_code == 200
        return User(login=login, password=password, token=response.json()['token'])
    return factory

@pytest.fixture
def user(user_factory) -> User:
    return user_factory()

