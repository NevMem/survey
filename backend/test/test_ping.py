import requests

def test_ping():
    print('Testing ping')
    print(requests.get("http://core:8080/ping"))
