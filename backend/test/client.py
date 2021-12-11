import requests

class Client:
    def __init__(self, base_url: str):
        self.base_url = base_url

    def ping(self):
        return self._get('/ping')

    def roles(self):
        return self._get('/roles')

    def _post(self, addr: str, body, headers={}):
        return requests.post(self.base_url + addr, json=body, headers=headers)

    def _get(self, addr: str, headers={}):
        return requests.get(self.base_url + addr, headers=headers)
