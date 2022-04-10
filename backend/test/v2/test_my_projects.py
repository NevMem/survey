import pytest
from client import Client
from client_fixture import client
from v2.user_fixture import user, User
from v2.project_factory_fixture import project_factory, Project

@pytest.mark.v2
def test_my_projects(client: Client, user: User, project_factory):
    projects_before = client.projects(user.token).json()

    project_factory(user)

    projects_after = client.projects(user.token).json()

    assert len(projects_after['projects']) == len(projects_before['projects']) + 1
