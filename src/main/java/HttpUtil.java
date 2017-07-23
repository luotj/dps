import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class HttpUtil {

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    /**
     * 发送HttpGet请求
     * @param url
     * @return
     */
    public static HttpResponse sendGet(String url, Map<String, String> heads) {

        HttpGet httpget = new HttpGet(url);

        if (!heads.isEmpty()) {
            for (String key : heads.keySet()) {
                httpget.addHeader(key, heads.get(key));
            }
        }
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return response;
    }


    /**
     * 发送不带参数的HttpPost请求
     * @param url
     * @return
     */
    public static HttpResponse sendPost(String url, Map<String, String> heads, String content) throws UnsupportedEncodingException {

        HttpPost httppost = new HttpPost(url);
        CloseableHttpResponse response = null;

        if (!heads.isEmpty()) {
            for (String key : heads.keySet()) {
                httppost.addHeader(key, heads.get(key));
            }
        }

        if (content != null) {
            StringEntity s = new StringEntity(content);
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            httppost.setEntity(s);
        }

        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}