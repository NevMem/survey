from utils import assert_ok_and_get_json
from v2.project_factory_fixture import project_factory, Project
from v2.user_fixture import User, user_factory
from client import Client
from client_fixture import client
import time

def test_answers(client: Client, user_factory, project_factory):
    admin = user_factory()
    project: Project = project_factory(admin)

    response = client.create_survey_v2(
        admin.token,
        {
            'name': 'first survey',
            'projectId': project.id,
            'questions': [
                {
                    'type': 'stars',
                    'stars': 5,
                    'title': 'Please rate:'
                },
                {
                    'type': 'text',
                    'title': 'enter some text',
                    'maxLength': 200,
                },
                {
                    'type': 'rating',
                    'title': 'rate app',
                    'min': 10,
                    'max': 20,
                },
                {
                    'type': 'radio',
                    'title': 'please select variant',
                    'variants': [
                        {
                            'id': '1',
                            'variant': 'first',
                        },
                        {
                            'id': '2',
                            'variant': 'second',
                        },
                        {
                            'id': '3',
                            'variant': 'third',
                        },
                        {
                            'id': '4',
                            'variant': 'forth',
                        },
                    ]
                }
            ],
            'commonQuestions': [
                {
                    'id': 'age',
                },
                {
                    'id': 'school_name',
                },
                {
                    'id': 'grade',
                },
                {
                    'id': 'region',
                },
            ],
        }
    )
    json = assert_ok_and_get_json(response)
    survey = json['survey']

    response = client.survey_metadata(admin.token, survey['id'])
    response = assert_ok_and_get_json(response)
    assert response['surveyMetadata']['answersCount'] == 0

    answers_count = 100

    for _ in range(answers_count):
        response = client.publish_answer(
            {
                'answer': {
                    'uid': {'uuid': '1'},
                    'timestamp': int(time.time()),
                    'surveyId': survey['surveyId'],
                    'answers': [
                        {
                            'type': 'rating',
                            'number': 7,
                        },
                        {
                            'type': 'text',
                            'text': 'Some school name',
                        },
                        {
                            'type': 'rating',
                            'number': 10,
                        },
                        {
                            'type': 'text',
                            'text': 'Some region',
                        },

                        {
                            'type': 'stars',
                            'stars': 2,
                        },
                        {
                            'type': 'text',
                            'text': 'some text' * 14,
                        },
                        {
                            'type': 'rating',
                            'number': 20,
                        },
                        {
                            'type': 'radio',
                            'id': '3',
                        },
                    ],
                    'gallery': None,
                }
            }
        )

    response = assert_ok_and_get_json(client.survey_metadata(admin.token, survey['id']))
    assert response['surveyMetadata']['answersCount'] == answers_count
