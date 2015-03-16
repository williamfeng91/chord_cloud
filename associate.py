#! /usr/bin/python

import json,httplib,re
connection = httplib.HTTPSConnection('api.parse.com', 443)
f = open('crds/%E3%81%8F%E3%81%BE%E3%81%AE%E3%83%97%E3%83%BC%E3%81%95%E3%82%93%E3%81%AE%E3%83%9B%E3%83%BC%E3%83%A0%E3%83%A9%E3%83%B3%E3%83%80%E3%83%BC%E3%83%93%E3%83%BC%EF%BC%81.crd', 'r')
title = f.readline().rstrip()
title = re.sub('{title:', '', title)
title = re.sub('}', '', title)
connection.connect()
connection.request('POST', '/1/classes/Song', json.dumps({
       "name": title,
       "file": {
         "name": "tfss-f1a17684-df7e-487c-befc-c5e412ae651e-1.crd",
         "__type": "File"
       }
     }), {
       "X-Parse-Application-Id": "H05QKYUchFlipNBlI3lPzUpnyECHbs8f5ThKETRv",
       "X-Parse-REST-API-Key": "V2BrgTmdOYrl8AudwTtwME6PvtsXjs8wyWqB5R68",
       "Content-Type": "application/json"
     })
result = json.loads(connection.getresponse().read())
print result
