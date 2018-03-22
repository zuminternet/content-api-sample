import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ContentAPI {
    private static final int HTTP_OK = 200;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int UNPROCESSABLE_ENTITY = 422;
    private static final int HTTP_INTERNAL_ERROR = 500;
    private static final int HTTP_UNAVAILABLE = 503;

    public static void main(String[] args) throws Exception {
        //줌에서 발급받은 access token 입력
        String accessToken = "발급받은 Aceess 토큰";

        //api url
        String apiUrl = "https://capi.zum.com/hub/categories/it/contents";

        //Http header Accept
        //application/vnd.zum.resource-$VERSION+json;charset=utf-8
        //$VERSION 부분에 해당 API 버전이 명시된다. ex) 1.0, 1.1, 1.2, 1.3 ...
        //ex) "application/vnd.zum.resource-1.0+json;charset=utf-8"   json 형태로 response
        //    "application/vnd.zum.resource-1.0+xml;charset=utf-8"    xml rss 형태로 response
        String accept = "application/vnd.zum.resource-1.0+json;charset=utf-8";

        //http header Authorization ex) "Bearer {줌에서 발급받은 access token}"
        String authorization = "Bearer" + " " + accessToken;

        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");    //default GET
        //http header 값에 Accept, Authorization 는 필수.
        con.setRequestProperty("Accept", accept);
        con.setRequestProperty("Authorization", authorization);

        int responseCode = con.getResponseCode();

        System.out.println("Sending 'GET' request to URL : " + apiUrl);
        System.out.println("Response Code : " + responseCode);

        BufferedReader br;
        if(responseCode == HTTP_OK) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            //HTTP 상태코드로 예외처리 진행
            if(responseCode == HTTP_UNAUTHORIZED) {
                //401 error
                System.out.println("인증실패");
            } else if (responseCode == HTTP_NOT_FOUND) {
                //404 error
                System.out.println("API 가 존재하지 않거나 지원하지 않는 Version");
            } else if (responseCode == UNPROCESSABLE_ENTITY){
                //422 error
                System.out.println("부적합한 파라미터 또는 필수 값 누락");
            } else if (responseCode == HTTP_INTERNAL_ERROR || responseCode == HTTP_UNAVAILABLE ) {
                //500 or 503 error
                System.out.println("서버에러");
            }
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }

        br.close();

        //print result
        System.out.println(response.toString());
    }
}
