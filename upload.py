#! /usr/bin/python

import json,httplib,glob,re

connection = httplib.HTTPSConnection('api.parse.com', 443)
i = 1
for filename in glob.glob('crds/*.crd'):
	path = "/1/files/%s.crd" % i
	f = open(filename, 'r')
	title = f.readline().rstrip()
	title = re.sub('{title:', '', title)
	title = re.sub('}', '', title)
	subtitle = f.readline().rstrip()
	subtitle = re.sub('{subtitle:', '', subtitle)
	subtitle = re.sub('}', '', subtitle)
	f.seek(0)
	content = f.read()
	f.close()
	connection.connect()
	connection.request('POST', path, content, {
       		"X-Parse-Application-Id": "H05QKYUchFlipNBlI3lPzUpnyECHbs8f5ThKETRv",
       		"X-Parse-REST-API-Key": "V2BrgTmdOYrl8AudwTtwME6PvtsXjs8wyWqB5R68",
       		"Content-Type": "text/plain"
     	})
	result = json.loads(connection.getresponse().read())
	print result
	connection.connect()
	connection.request('POST', '/1/classes/Song', json.dumps({
       		"name": title,
		"subtitle": subtitle,
		"tags": title.lower() + " " + subtitle.lower(),
       		"file": {
         		"name": result.get(u'name'),
         		"__type": "File"
       		}
     	}), {
       		"X-Parse-Application-Id": "H05QKYUchFlipNBlI3lPzUpnyECHbs8f5ThKETRv",
       		"X-Parse-REST-API-Key": "V2BrgTmdOYrl8AudwTtwME6PvtsXjs8wyWqB5R68",
       		"Content-Type": "application/json"
     	})
	result = json.loads(connection.getresponse().read())
        print result
	i += 1
