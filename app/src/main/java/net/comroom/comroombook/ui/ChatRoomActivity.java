package net.comroom.comroombook.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.comroom.comroombook.R;
import net.comroom.comroombook.core.ComroomRestClient;
import net.comroom.comroombook.core.MemberVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by younghokim on 16. 5. 22.
 */
public class ChatRoomActivity extends Activity{

    private final String TAG = "ChatRoomActivity";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = new Intent();


        GetUserDataFromServer(new DataHandler() {
            @Override
            public void onData(MemberVO[] members) {

            }
        });
    }

    private void GetUserDataFromServer(final DataHandler handler){
        ComroomRestClient.get(context, "/chat/", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String body = new String(responseBody);
                    JSONArray members = new JSONArray(body);
                    MemberVO[] results = new MemberVO[members.length()];

                    for (int i = 0; i < members.length(); i++) {
                        JSONObject member = members.getJSONObject(i);

                        String name = member.getString("name");
                        String email = member.getString("email");
                        String id = member.getString("_id");

                        results[i] = new MemberVO(name, email, id);
                    }
                    handler.onData(results);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    interface DataHandler {
        abstract void onData(MemberVO[] members);
    }
}
