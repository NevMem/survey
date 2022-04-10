import pytest
from client import Client
from client_fixture import client
from v2.user_fixture import *
from v2.project_factory_fixture import project_factory

@pytest.mark.v2
def test_create_survey(client: Client, user: User, project_factory):
    project = project_factory(user)
    response = client.create_survey_v2(
        user.token,
        {
            'name': 'first survey',
            'projectId': project.id,
            'questions': [
                {
                    'type': 'stars',
                    'stars': 5,
                    'title': 'Please rate:'
                },
            ],
            'commonQuestions': [
                {
                    'id': 'age',
                },
                {
                    'id': 'school',
                },
            ],
        }
    )
    assert response.status_code == 200
    json = response.json()
    assert len(json['survey']['questions']) == 1
    assert len(json['survey']['commonQuestions']) == 2
    assert json['survey']['projectId'] == project.id

    response = client.surveys_v2(user.token, project.id)
    assert response.status_code == 200
    json = response.json()
    assert len(json['surveys']) == 1
