import os
import requests
import sys

class Notificator:
    def __init__(self):
        self._bot_token = os.getenv('BOT_TOKEN')
        self._chat_ids = os.getenv('NOTIFY_CHAT_IDS', '').split(',')

    def send_message(self, message: str):
        for chat_id in self._chat_ids:
            self._send(message, chat_id)

    def _send(self, message: str, chat_id: str):
        url = 'https://api.telegram.org/bot' + self._bot_token + '/sendMessage?chat_id=' + chat_id + '&text=' + message
        response = requests.get(url)
        assert(response.status_code == 200)


def with_exception_notificator(func):
    def wrapper():
        try:
            func()
        except Exception as exception:
            Notificator().send_message(f"❌ Exception in {sys.argv[0]} occurred:\n{str(exception)}")
            raise exception
    return wrapper
