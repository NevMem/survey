import pytest
from client import Client
from client_fixture import *
from utils import random_string
from v2.user_fixture import *
from v2.project_factory_fixture import project_factory, Project
import time

@pytest.mark.v2
@pytest.mark.task
def test_simple_task(client: Client, user: User, project_factory):
    # user = User('nevmem', 'password', client.login('nevmem', 'password').json()['token']) ## just for testing purposes

    project: Project = project_factory(user)
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

        responses = []
        for _ in range(12):
            with open('tmp.txt', 'w') as out:
                for _ in range(16):
                    out.write(random_string(15) + '\n')
            responses.append(
                client.upload_media(open('tmp.txt', 'rb')).json()
            )

        response = client.create_gallery(responses)

        response = client.publish_answer({
            'answer': {
                'surveyId': surveyLiteral,
                'timestamp': int(time.time()),
                'uid': {'uuid': publisherId},
                'answers': [
                    {
                        'type': 'stars',
                        'stars': 4,
                    }
                ],
                'gallery': response.json()['gallery'],
            }
        })
        assert response.status_code == 200

    response = client.create_export_data_task(user.token, surveyId)
    assert response.status_code == 200
    taskId = response.json()['id']

    response = client.task(user.token, taskId)
    assert response.status_code == 200
    assert response.json()['state'] == 'Executing' or response.json()['state'] == 'Waiting' or response.json()['state'] == 'Success'

    time.sleep(6)

    response = client.task(user.token, taskId)
    assert response.status_code == 200
    assert response.json()['state'] == 'Success'
