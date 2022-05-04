import pytest
from client import Client
from client_fixture import *
from v2.user_fixture import user, user_factory, User
from v2.project_factory_fixture import project_factory, Project


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


@pytest.mark.v2
def test_join_survey(client: Client, user_factory, project_factory):
    user: User = user_factory()
    project = project_factory(user)

    created = client.create_survey_v2(
        user.token,
        {
            'projectId': project.id,
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
    assert client.join_survey(json['survey']['surveyId'], '1').status_code == 200
