import requests

class Client:
    def __init__(self, base_url: str):
        self.base_url = base_url

    def login(self, login: str, password: str):
        return self._post('/v1/login', {'login': login, 'password': password})

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

    def _post(self, addr: str, body, headers={}):
        return requests.post(self.base_url + addr, json=body, headers=headers)

    def _get(self, addr: str, headers={}):
        return requests.get(self.base_url + addr, headers=headers)
