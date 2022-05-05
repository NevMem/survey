#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import requests

def print_request(request):
    req = "{method} {path_url} HTTP/1.1\r\n{headers}\r\n{body}".format(
        method = request.method,
        path_url = request.path_url,
        headers = ''.join('{0}: {1}\r\n'.format(k, v) for k, v in request.headers.items()),
        body = request.body.decode() or "",
    )
    return "{req_size} upload_media\n{req}\r\n".format(req_size = len(req), req = req)

#POST multipart form data
def post_multipart(host, port, namespace, files, headers, payload):
    req = requests.Request(
        'POST',
        'https://{host}:{port}{namespace}'.format(
            host = host,
            port = port,
            namespace = namespace,
        ),
        headers = headers,
        data = payload,
        files = files
    )
    prepared = req.prepare()
    return print_request(prepared)

if __name__ == "__main__":
    host = '51.250.72.136'
    port = '80'
    namespace = '/v1/media/upload'
    headers = {
        'Host': 'hostname.com',
    }
    payload = {
    }
    files = {
        'file': ('text.txt', open('./files/text.txt', 'rb'), 'text/plain')
    }
    print(post_multipart(host, port, namespace, files, headers, payload))
