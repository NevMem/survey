from client import Client
from client_fixture import *


def test_questions_parsing(client: Client):
    token = client.login('admin', 'password').json()['token']
    response = client.test_question(token, {'type': 'rating', 'title': 'Some title', 'min': -10, 'max': 10})
    print(response.status_code)
    print(response.text)
    response = client.questions(token)
    print(response.status_code)
    print(response.text)