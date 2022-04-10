import requests
import time
import logging


logger = logging.getLogger(__name__)
logger.setLevel('DEBUG')


def timed(func):
    def inner(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        delta = time.time() - start
        logger.debug(args[1:])
        logger.debug(f"Time passed: {delta}")
        return result
    return inner


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
        return self._get('/v1/survey/surveys', headers={'Authorization': 'Bearer ' + token})

    def create_survey(self, token: str, body):
        return self._post('/v1/survey/create_survey', body=body, headers={'Authorization': 'Bearer ' + token})

    def delete_survey(self, token: str, survey_id: int):
        return self._post('/v1/survey/delete_survey', {'surveyId': survey_id}, headers={'Authorization': 'Bearer ' + token})

    def me(self, token: str):
        return self._get('/v1/me', headers={'Authorization': 'Bearer ' + token})

    def invites(self, token: str):
        return self._get('/v1/invite/my_invites', headers={'Authorization': 'Bearer ' + token})

    def create_invite(self, token: str, expirationSeconds: int):
        return self._post('/v1/invite/create_invite', {'expirationTimeSeconds': expirationSeconds}, headers={'Authorization': 'Bearer ' + token})

    def ping(self):
        return self._get('/ping')

    def roles(self):
        return self._get('/roles')

    def publish_answer(self, body):
        return self._post('/v1/answers/publish', body)

    def load_answers(self, surveyId: str):
        return self._post('/v1/answers/load', {'surveyId': surveyId})

    def create_gallery(self, medias):
        return self._post('/v1/media/create_gallery', {'gallery': medias})

    def managed_users(self, token):
        return self._get('/v1/role/managed_users', headers={'Authorization': 'Bearer ' + token})

    def update_roles(self, token, body):
        return self._post('/v1/role/update_roles', body, headers={'Authorization': 'Bearer ' + token})

    def survey_metadata(self, token, surveyId):
        return self._post('/v1/survey/metadata', {'surveyId': surveyId}, headers={'Authorization': 'Bearer ' + token})

    def tasks(self, token):
        return self._get('/v1/task/tasks', headers={'Authorization': 'Bearer ' + token})

    def get_survey(self, survey_id):
        return self._post('/v1/survey/get', {'surveyId': survey_id})

    def task(self, token, id):
        return self._post('/v1/task/task', {'id': id}, headers={'Authorization': 'Bearer ' + token})

    def create_export_data_task(self, token, surveyId):
        return self._post('/v1/task/create_export_data_task', {'surveyId': surveyId}, headers={'Authorization': 'Bearer ' + token})

    def register_v2(self, login: str, password: str, name: str, surname: str, email: str):
        return self._post(
            '/v2/register',
            {
                'login': login,
                'password': password,
                'name': name,
                'surname': surname,
                'email': email,
            }
        )

    def login_v2(self, login: str, password: str):
        return self._post('/v2/login', {'login': login, 'password': password})

    def check_auth(self, token):
        return self._get('/v2/check_auth', headers={'Authorization': 'Bearer ' + token})

    def create_project(self, token, name):
        return self._post('/v2/projects/create', body={'name': name}, headers={'Authorization': 'Bearer ' + token})

    def projects(self, token):
        return self._get('/v2/projects/all', headers={'Authorization': 'Bearer ' + token})

    def create_survey_v2(self, token: str, body):
        return self._post('/v2/survey/create_survey', body=body, headers={'Authorization': 'Bearer ' + token})

    def surveys_v2(self, token: str, projectId: int):
        return self._post('/v2/survey/surveys', body={'projectId': projectId}, headers={'Authorization': 'Bearer ' + token})

    @timed
    def upload_media(self, stream):
        return requests.post(self.base_url + '/v1/media/upload', files={'file': stream})

    @timed
    def _post(self, addr: str, body, headers={}):
        return requests.post(self.base_url + addr, json=body, headers=headers)

    @timed
    def _get(self, addr: str, headers={}):
        return requests.get(self.base_url + addr, headers=headers)
