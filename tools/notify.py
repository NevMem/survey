import os
import requests

class Notificator:
    def __init__(self):
        self._bot_token = os.getenv('BOT_TOKEN')
        self._chat_ids = os.getenv('NOTIFY_CHAT_IDS', '').split(',')

    def send_message(self, message: str):
        for chat_id in self._chat_ids:
            self._send(message, chat_id)

    def _send(self, message: str, chat_id: str):
        url = 'https://api.telegram.org/bot' + self.bot_token + '/sendMessage?chat_id=' + chat_id + '&text=' + message
        response = requests.get(url)
        assert(response.status_code == 200)
