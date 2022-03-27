import pytest
from client import Client

@pytest.fixture
def client():
    return Client('http://core:8080')
