package net.comroom.comroombook.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.comroom.comroombook.R;
import net.comroom.comroombook.core.ComroomRestClient;
import net.comroom.comroombook.core.MemberVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by younghokim on 16. 5. 6.
 */
public class CreateChatActivity  extends Activity{
    private final String TAG = "CreateChatActivity";
    private Context context;
    private LinearLayout layout_checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        this.context = getApplicationContext();
        this.layout_checkbox = (LinearLayout) findViewById(R.id.layout_friend_checkbox);

        GetUserDataFromServer(new DataHandler() {
            @Override
            public void onData(MemberVO[] members) {
                for(int i=0;i<members.length;i++){
                    if(!members[i].getId().equals(MainActivity.user_id)){
                        Log.d(TAG, members[i].getName());
                        CheckBox checkBox = new CheckBox(context);
                        checkBox.setId(i);
                        checkBox.setTextColor(Color.BLACK);
                        checkBox.setTextSize(20);
                        checkBox.setButtonDrawable(R.drawable.custom_checkbox);
                        checkBox.setText("  "+members[i].getName());
                        layout_checkbox.addView(checkBox);
                    }
                }
            }
        });
    }

    private void GetUserDataFromServer(final DataHandler handler){
        ComroomRestClient.get(context, "/users", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String body = new String(responseBody);
                    JSONArray members = new JSONArray(body);
                    MemberVO[] results = new MemberVO[members.length()];

                    for(int i=0;i<members.length();i++){
                        JSONObject member = members.getJSONObject(i);

                        String name = member.getString("name");
                        String email = member.getString("email");
                        String id = member.getString("_id");

                        results[i] = new MemberVO(name,email,id);
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
