from client import Client
from client_fixture import *


def test_questions_loading(client: Client):
    token = client.login('admin', 'password').json()['token']
    surveys = client.surveys(token)
    assert surveys.status_code == 200
    assert 'surveys' in surveys.json()

def test_create_survey(client: Client):
    token = client.login('admin', 'password').json()['token']
    surveys_before = client.surveys(token).json()['surveys']

    created = client.create_survey(
        token,
        {
            'name': 'first survey',
            'questions': [
                {
                    'type': 'stars',
                    'stars': 5,
                    'title': 'Please rate:'
                }
            ],
            'commonQuestions': []
        }
    )
    assert created.status_code == 200
    
    surveys_after = client.surveys(token).json()['surveys']
    assert len(surveys_after) == len(surveys_before) + 1
