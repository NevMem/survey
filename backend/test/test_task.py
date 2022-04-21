import pytest
from client import Client
from client_fixture import *
from utils import random_string
from v2.project_factory_fixture import project_factory
from v2.user_fixture import *
import time


@pytest.mark.task
def test_simple_task(client: Client, user_factory, project_factory):
    user = user_factory()
    project = project_factory(user)
    
    response = client.create_survey_v2(
        user.token,
        {
            'projectId': project.id,
            'name': 'Some survey',
            'questions': [
                {
                    'type': 'stars',
                    'stars': 5,
                    'title': 'Please rate:'
                },
            ],
            'commonQuestions': [],
        }
    )
    assert response.status_code == 200
    surveyId = response.json()['survey']['id']
    surveyLiteral = response.json()['survey']['surveyId']

    for _ in range(10):
        publisherId = random_string(12)
        response = client.publish_answer({
            'answer': {
                'surveyId': surveyLiteral,
                'uid': {'uuid': publisherId},
                'answers': [
                    {
                        'type': 'stars',
                        'stars': 4,
                    }
                ],
                'gallery': None,
            }
        })
        assert response.status_code == 200

    response = client.create_export_data_task(user.token, surveyId)
    print(response.json())
    assert response.status_code == 200
    taskId = response.json()['id']

    response = client.task(user.token, taskId)
    print(response.text)
    assert response.status_code == 200
    assert response.json()['state'] == 'Executing' or response.json()['state'] == 'Waiting' or response.json()['state'] == 'Success'

    time.sleep(6)

    response = client.task(user.token, taskId)
    print(response.json())
    assert response.status_code == 200
    assert response.json()['state'] == 'Success'
