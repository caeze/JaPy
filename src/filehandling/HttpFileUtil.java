package filehandling;

import console.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import util.ThreadPool;

/**
 * Http connector to the backend.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class HttpFileUtil {

    private static HttpFileUtil instance;

    private HttpFileUtil() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static HttpFileUtil getInstance() {
        if (instance == null) {
            instance = new HttpFileUtil();
        }
        return instance;
    }

    public void getFileViaHttp(String addressToAccess, IHttpCallback callback) {
        httpGet(addressToAccess, new HashMap<>(), callback);
    }

    private void httpGet(String addressToAccess, Map<String, String> getParams, IHttpCallback callback) {
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL obj = new URL(addressToAccess + buildHttpParamsString(getParams));
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    StringBuffer response = new StringBuffer();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    callback.success(response.toString());
                } catch (Exception e) {
                    Log.error(HttpFileUtil.class, "Error on downloading file " + addressToAccess + ": " + e.getMessage());
                    callback.failure("Error on downloading file " + addressToAccess + ": " + e.getMessage());
                }
            }
        });
    }

    private String buildHttpParamsString(Map<String, String> params) {
        String retStr = "";
        if (params.isEmpty()) {
            return retStr;
        }
        for (String param : params.keySet()) {
            if (!retStr.equals("")) {
                retStr = retStr + "&";
            }
            retStr = retStr + param + "=" + params.get(param);
        }
        return "?" + retStr;
    }

    public interface IHttpCallback {

        /**
         * Called, if the operation was successful.
         *
         * @param data the message to this callback
         */
        public void success(String data);

        /**
         * Called, if the operation was not successful.
         *
         * @param data the message to this callback
         */
        public void failure(String data);
    }
}
