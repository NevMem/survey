from client import Client
from client_fixture import *
from utils import random_string

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
        'publisherId': publisherId,
        'answer': {
            'surveyId': surveyId,
            'answers': [
                {
                    'type': 'stars',
                    'stars': 4,
                }
            ],
        }
    })
    print(response.status_code)
    print(response.text)
