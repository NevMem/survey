import json
import requests

class Client:
    def __init__(self, base_url: str):
        self.base_url = base_url

    def login(self, login: str, password: str):
        return self._post('/v1/login', {'login': login, 'password': password})

    def register(self, login: str, password: str, name: str, surname: str, email: str, inviteId: str):
        return self._post(
            '/v1/register',
            {
                'login': login,
                'password': password,
                'name': name,
                'surname': surname,
                'email': email,
                'inviteId': inviteId
            }
        )

    def surveys(self, token: str):
        return self._get('/v1/surveys', headers={'Authorization': 'Bearer ' + token})

    def create_survey(self, token: str, body):
        return self._post('/v1/create_survey', body=body, headers={'Authorization': 'Bearer ' + token})

    def active_survey(self):
        return self._get('/v1/active_survey')

    def activate_survey(self, token: str, survey_id: int):
        return self._post('/v1/activate_survey', {'surveyId': survey_id}, headers={'Authorization': 'Bearer ' + token})

    def delete_survey(self, token: str, survey_id: int):
        return self._post('/v1/delete_survey', {'surveyId': survey_id}, headers={'Authorization': 'Bearer ' + token})

    def me(self, token: str):
        return self._get('/v1/me', headers={'Authorization': 'Bearer ' + token})

    def invites(self, token: str):
        return self._get('/v1/my_invites', headers={'Authorization': 'Bearer ' + token})

    def create_invite(self, token: str, expirationSeconds: int):
        return self._post('/v1/create_invite', {'expirationTimeSeconds': expirationSeconds}, headers={'Authorization': 'Bearer ' + token})

    def ping(self):
        return self._get('/ping')

    def roles(self):
        return self._get('/roles')

    def publish_answer(self, body):
        return self._post('/v1/answers/publish', body)

    def load_answers(self, surveyId: str):
        return self._post('/v1/answers/load', {'surveyId': surveyId})

    def _post(self, addr: str, body, headers={}):
        return requests.post(self.base_url + addr, json=body, headers=headers)

    def _get(self, addr: str, headers={}):
        return requests.get(self.base_url + addr, headers=headers)
