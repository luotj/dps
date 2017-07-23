import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by luotj on 2017/7/23.
 */
public class ApiChecker {

    private static String getToken() throws Exception {
        String token = "";
        String fileUrl = ApiChecker.class.getResource("/token").getPath();
        final LineIterator it = FileUtils.lineIterator(new File(fileUrl), "UTF-8");
        try {
            while (it.hasNext()) {
                final String line = it.nextLine();
                if (!line.equals("")) {
                    String[] apiString = line.split(" ");
                    HttpResponse response = HttpUtil.sendPost(apiString[1], new HashMap<String, String>(), apiString[2]);
                    Header[] headers = response.getHeaders("token");
                    if (headers.length == 0) {
                        throw new Exception("token获取失败！");
                    }
                    token = headers[0].getValue();
                    break;
                }
            }
            return token;

        } finally {
            it.close();
        }
    }

    public static void checkApi(String api, String token) throws UnsupportedEncodingException {
        HashMap<String,String> head = new HashMap<String, String>();
        head.put("token", token);
        if (!api.equals("")) {
            String[] strings = api.split(" ");
            String method = strings[0];
            String url = strings[1];
            String content = "";
            if (strings.length == 3) {
                content = strings[2];
            }

            if (method.equals("GET")) {
                HttpResponse response = HttpUtil.sendGet(url,head);
                System.out.println(url + "  " + response.getStatusLine().getStatusCode());
            }

            if (method.equals("POST")) {
                HttpResponse response = HttpUtil.sendPost(url,head,content);
                System.out.println(url + "  " + response.getStatusLine().getStatusCode());
            }
        }
    }

    public static void main (String[] args) throws Exception {

        String token = getToken();
        if (token.equals("")) {
            throw new Exception("token为空！");
        }

        String fileUrl = ApiChecker.class.getResource("/api").getPath();
        final LineIterator it = FileUtils.lineIterator(new File(fileUrl), "UTF-8");
        try {
            while (it.hasNext()) {
                final String line = it.nextLine();
                checkApi(line,token);
            }
        } finally {
            it.close();
        }

    }


}
