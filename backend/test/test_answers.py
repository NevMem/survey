from client import Client
from client_fixture import *
from utils import random_string
import pytest


@pytest.mark.answer
def test_simple_answer(client: Client):
    token = client.login('admin', 'password').json()['token']
    response = client.create_survey(
        token,
        {
            'name': 'first survey',
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
    surveyId = response.json()['survey']['surveyId']
    publisherId = random_string(12)
    response = client.publish_answer({
        'answer': {
            'surveyId': surveyId,
            'publisherId': publisherId,
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

    response = client.load_answers(surveyId)
    print(response.text)
    assert response.status_code == 200


@pytest.mark.answer
def test_survey_metadata(client: Client):
    token = client.login('admin', 'password').json()['token']
    response = client.create_survey(
        token,
        {
            'name': 'first survey',
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

    response = client.survey_metadata(token, surveyId)
    assert response.status_code == 200
    assert response.json()['surveyMetadata']['answersCount'] == 0

    for _ in range(10):
        publisherId = random_string(12)
        response = client.publish_answer({
            'answer': {
                'surveyId': surveyLiteral,
                'publisherId': publisherId,
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
    
    response = client.survey_metadata(token, surveyId)
    assert response.status_code == 200
    assert response.json()['surveyMetadata']['answersCount'] == 10

