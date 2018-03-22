# -*- coding: utf-8 -*-
import json
import requests


class ContentAPISample:
    def __init__(self, url, version, data_type):
        # 줌에서 발급받은 access token 입력
        self.access_token = '발급받은 Aceess 토큰'
        self.api_url = url
        # http header 값에 Accept, Authorization 는 필수
        self.headers = {
            # Http header - Accept
            # application/vnd.zum.resource-{version}+{type};charset=utf-8
            # {version} 부분에 해당 API 버전이 명시된다. ex) 1.0, 1.1, 1.2, 1.3 ...
            # {type} 부분에 해당 Data Type이 명시된다. ex) json, xml
            # ex) "application/vnd.zum.resource-1.0+json;charset=utf-8"   json 형태로 response
            #     "application/vnd.zum.resource-1.0+xml;charset=utf-8"    xml rss 형태로 response
            'Accept': 'application/vnd.zum.resource-{}+{};charset=utf-8'.format(version, data_type),

            # http header Authorization ex) "Bearer {줌에서 발급받은 access token}"
            'Authorization': 'Bearer {}'.format(self.access_token)
        }
        self.response = None
        self.result = None

    # HTTP 상태코드로 예외처리 진행
    def error(self):
        if self.response.status_code == 200:
            self.result = json.dumps(json.loads(self.response.content.decode('utf-8')), ensure_ascii=False)
        elif self.response.status_code == 401:
            print('인증 실패')
        elif self.response.status_code == 404:
            print('API 가 존재하지 않거나 지원하지 않는 Version')
        elif self.response.status_code == 422:
            print('부적합한 파라미터 또는 필수 값 누락')
        elif self.response.status_code == 500 or self.response.status_code == 503:
            print('서버에러')

    # API 호출
    def request(self):
        try:
            self.response = requests.get(self.api_url, headers=self.headers)
        except requests.exceptions.HTTPError as err:
            print(err)

        self.error()

        return self.result


if __name__ == '__main__':
    # api url
    api_url = 'https://capi.zum.com/hub/categories/it/contents'
    # APISample 초기화
    apiSample = ContentAPISample(api_url, '1.0', 'json')
    # API 요청
    result = apiSample.request()
    # 결과
    print(result)