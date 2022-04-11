import random
import string

def random_string(length: int = 10) -> str:
    return ''.join([random.choice(string.ascii_letters + string.digits) for _ in range(length)])

def assert_ok_and_get_json(response):
    if response.status_code != 200:
        print(response.text, response.status_code)
        assert False
    return response.json()
