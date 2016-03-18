package net.comroom.comroombook;

import android.content.Context;
import com.loopj.android.http.*;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

public class ComroomRestClient {
    private static final String BASE_URL = "http://61.43.139.23:3000";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), null, responseHandler);
    }

    public static void post(Context context,String url, JSONObject jsonParams, AsyncHttpResponseHandler responseHandler) {
        try {
            StringEntity entity = new StringEntity(jsonParams.toString());
            client.post(context,getAbsoluteUrl(url),entity,"application/json",responseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
