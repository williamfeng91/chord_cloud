#! /usr/bin/python

import sys,json,httplib
connection = httplib.HTTPSConnection('api.parse.com', 443)
path = "/1/files/%s" % sys.argv[1]
connection.connect()
connection.request('DELETE', path, '', {
       "X-Parse-Application-Id": "H05QKYUchFlipNBlI3lPzUpnyECHbs8f5ThKETRv",
       "X-Parse-Master-Key": "8f901xZVPYMFzbkffwPsorD8pcaOCiqoQsS97oyH"
     })
result = json.loads(connection.getresponse().read())
