import json
import time

data = {
    "answer": {
        "uid": {"uuid": "1"},
        "timestamp": 1651681987,
        "surveyId": "OMLCA",
        "answers": [{"type": "rating", "number": 7}, {"type": "text", "text": "Some school name"}, {"type": "text", "text": "Some region"}, {"type": "rating", "number": 10}],
        "gallery": None
    }
}

with open('data.txt', 'w') as out:
    out.write('POST||/v1/answers/publish||publish_answer||' + json.dumps(data))
