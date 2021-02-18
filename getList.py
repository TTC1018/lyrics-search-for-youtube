from flask import Flask
from flask_restful import Resource, Api
from flask_restful import reqparse
import requests
import urllib.parse
import re
from bs4 import BeautifulSoup
app =  Flask(__name__)
api = Api(app)

class getList(Resource):
    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('title', type = str)
        args = parser.parse_args()

        title = args['title']

        url = urllib.parse.quote(title)
        html = requests.get('https://music.bugs.co.kr/search/integrated?q=' + url).text
        soup = BeautifulSoup(html, 'html.parser')

        title = soup.select('.lyrics .title a')
        artist = soup.select('.lyrics .artist a')
        titles = []
        artists = []
        lyrics_link = []
        for i in range(0, 5):
            lyrics_link.append(soup.select('tr .lyrics a')[i]['href'])

        for i in title:
            titles.append(i.text)

        for i in artist:
            artists.append(i.text)
        return {'result': {'titles': titles, 'artists': artists, 'lyrics_link': lyrics_link}}

api.add_resource(getList, '/list')
if __name__ =='__main__':
    app.run(debug=True, host='0.0.0.0', port=80)
