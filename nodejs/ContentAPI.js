var express = require('express');
var app = express();

/**
 * 요청 주소: http://localhost:3000/hub/categories/:category/contents
 *
 * -- 카테고리 리스트 --
 * life: 라이프
 * culture: 컬쳐
 * trip : 여행
 * it: 테크
 * food : 푸드
 * biz: 비즈
 */
app.get('/hub/categories/:category/contents', function(req, res) {

    //줌에서 발급받은 access token 입력
    var access_token = '발급받은 Aceess 토큰';

    //카테고리
    var category = req.params.category;

    //API url
    var api_url = 'https://capi.zum.com/hub/categories/' + category + '/contents';

    //Http header Accept
    //application/vnd.zum.resource-$VERSION+json;charset=utf-8
    //$VERSION 부분에 해당 API 버전이 명시된다. ex) 1.0, 1.1, 1.2, 1.3 ...
    //ex) 'application/vnd.zum.resource-1.0+json;charset=utf-8' json 형태로 response
    //    'application/vnd.zum.resource-1.0+xml;charset=utf-8'  xml rss 형태로 response
    var accept = 'application/vnd.zum.resource-2.0+json;charset=utf-8';

    var request = require('request');
    var options = {
        url: api_url,
        headers: {
            'Accept': accept,
            'Authorization': access_token
        }
    };
    request.get(options, function(error, response, body) {
        if(!error && response.statusCode === 200) {
            res.writeHead(response.statusCode, {'Content-Type': 'text/json;charset=utf-8'});
            console.log(body);
            res.end(body);
        } else {
            if(response != null) {
                if(response.statusCode === 404) {
                    console.log('error :: {}', 'API 가 존재하지 않거나 지원하지 않는 Version');
                } else if(response.statusCode === 401) {
                    console.log('error :: {}', '인증실패');
                } else if(response.statusCode === 422) {
                    console.log('error :: {}', '부적합한 파라미터 또는 필수 값 누락');
                } else if(response.statusCode === 500 || response.statusCode === 503) {
                    console.log('error :: {}', '서버에러');
                }
                res.status(response.statusCode).end();
            }
        }
    });
});

app.listen(4000, function() {
    console.log('http://localhost:3000/hub/categories/:category/contents');
});