import random
import string

def random_string(length: str) -> str:
    return ''.join([random.choice(string.ascii_letters + string.digits) for _ in range(length)])
