import pytest
from client import Client
from client_fixture import *
from utils import random_string
import time


@pytest.mark.task
def test_simple_task(client: Client):
    token = client.login('admin', 'password').json()['token']
    
    response = client.create_survey(
        token,
        {
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
            'publisherId': publisherId,
            'answer': {
                'surveyId': surveyLiteral,
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

    response = client.create_export_data_task(token, surveyId)
    assert response.status_code == 200
    taskId = response.json()['id']

    response = client.tasks(token)
    assert response.status_code == 200

    response = client.task(token, taskId)
    assert response.status_code == 200
    assert response.json()['state'] == 'Executing' or response.json()['state'] == 'Waiting'

    time.sleep(2)

    response = client.task(token, taskId)
    assert response.status_code == 200
    assert response.json()['state'] == 'Executing' or response.json()['state'] == 'Success'