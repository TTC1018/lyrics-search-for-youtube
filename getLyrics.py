from flask import Flask
from flask_restful import Resource, Api
from flask_restful import reqparse
import requests
import urllib.parse
import re
from bs4 import BeautifulSoup
app =  Flask(__name__)
api = Api(app)

class getLyrics(Resource):
    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('link', type = str)
        args = parser.parse_args()

        link = args['link']


        html = requests.get(link).text
        soup = BeautifulSoup(html, 'html.parser')

        lyrics = soup.xmp.string
        return {'lyrics': lyrics}

api.add_resource(getLyrics, '/lyrics')
if __name__ =='__main__':
    app.run(debug=True, host='0.0.0.0', port=70)
