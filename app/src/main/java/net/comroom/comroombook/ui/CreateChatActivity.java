package net.comroom.comroombook.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

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
public class CreateChatActivity  extends Activity implements View.OnClickListener{
    private final String TAG = "CreateChatActivity";
    private Context context;
    private LinearLayout layout_checkbox;
    private Button bt_submit;
    private Button bt_cancel;
    private EditText et_name;
    private MemberVO[] memberList = null;
    private Boolean[] memberChecked = null;

    private View.OnClickListener onClick_checkbox = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckBox checkBox = (CheckBox) v;
            if( checkBox.isChecked()){
                memberChecked[v.getId()] = true;
            }else{
                memberChecked[v.getId()] = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        this.context = getApplicationContext();
        this.layout_checkbox = (LinearLayout) findViewById(R.id.layout_friend_checkbox);
        this.bt_submit = (Button) findViewById(R.id.create_chat_submit);
        this.et_name = (EditText) findViewById(R.id.et_chatname);
        this.bt_cancel = (Button) findViewById(R.id.bt_create_chat_cancel);

        this.bt_cancel.setOnClickListener(this);
        this.bt_submit.setOnClickListener(this);

        GetUserDataFromServer(new DataHandler() {
            @Override
            public void onData(MemberVO[] members) {
                memberList = members;
                memberChecked = new Boolean[members.length];
                initMemberChecked(memberChecked);

                for (int i = 0; i < members.length; i++) {
                    if (!members[i].getId().equals(MainActivity.user_id)) {
                        Log.d(TAG, members[i].getName());
                        CheckBox checkBox = new CheckBox(context);
                        checkBox.setId(i);
                        checkBox.setTextColor(Color.BLACK);
                        checkBox.setTextSize(20);
                        checkBox.setOnClickListener(onClick_checkbox);
                        checkBox.setButtonDrawable(R.drawable.custom_checkbox);
                        checkBox.setText("  " + members[i].getName());
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.create_chat_submit:
                Log.d(TAG,"Create Chat Submit Button Event");
                MakeChatRoom();
                break;
            case R.id.bt_create_chat_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    private void initMemberChecked(Boolean[] list){
        for(int i=0;i<list.length;i++){
            list[i] = false;
        }
    }

    private void MakeChatRoom(){
        String name = et_name.getText().toString();

        if( name.equals("")){
            Toast.makeText(context,"채팅방 이름을 입력해주세요",Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            JSONArray member = new JSONArray();
            member.put(MainActivity.user_id);
            for(int i=0;i<memberList.length;i++){
                if( memberChecked[i] == true){
                    member.put(memberList[i].getId());
                }
            }

            if(member.length() == 1){
                Toast.makeText(context,"초대할 멤버를 선택해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject body = new JSONObject();
            body.put("userid",MainActivity.user_id);
            body.put("name",name);
            body.put("member",member.toString());

            ComroomRestClient.post(context, "/chat/list", body, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Toast.makeText(context, "채팅방이 완성되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }catch (Exception e){

        }
    }
    interface DataHandler {
        abstract void onData(MemberVO[] members);
    }
}
