from client import Client
from client_fixture import *


def common_question(id: str):
    return {
        'id': id,
    }


def stars_question(title: str, stars: int):
    return {
        'type': 'stars',
        'title': title,
        'stars': stars,
    }


def text_question(title: str, max_length: int):
    return {
        'type': 'text',
        'title': title,
        'maxLength': max_length,
    }


def rating_question(title: str, min: int, max: int):
    return {
        'type': 'rating',
        'title': title,
        'min': min,
        'max': max,
    }


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
    assert created.status_code == 200
    json = created.json()
    assert len(json['survey']['questions']) == 1
    assert len(json['survey']['commonQuestions']) == 2
    
    surveys_after = client.surveys(token).json()['surveys']
    assert len(surveys_after) == len(surveys_before) + 1


def test_get_survey(client: Client):
    token = client.login('admin', 'password').json()['token']

    created = client.create_survey(
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
    assert created.status_code == 200
    json = created.json()
    assert client.get_survey(json['survey']['surveyId']).status_code == 200


def test_create_surveys(client: Client):
    token = client.login('admin', 'password').json()['token']

    surveys = client.surveys(token).json()['surveys']
    for survey in surveys:
        assert client.delete_survey(token, survey['id']).status_code == 200

    survey_names = [
        'First survey',
        'Second survey',
        'Third survey',
    ]

    for name in survey_names:
        response = client.create_survey(
            token,
            {
                'name': name,
                'questions': [
                    text_question('text question', 2000),
                    stars_question('stars question', 5),
                    rating_question('rating question', -5, 5),
                ],
                'commonQuestions': [
                    common_question('age'),
                ],
            }
        )
        assert response.status_code == 200
        json = response.json()
