package net.comroom.comroombook.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.comroom.comroombook.R;
import net.comroom.comroombook.core.ComroomRestClient;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends Activity implements View.OnClickListener{
    private final String TAG = "LoginActivity";
    private Button bt_login;
    private Button bt_signup;
    private EditText et_email;
    private EditText et_password;
    private Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_signup = (Button) findViewById(R.id.bt_signup);

        bt_login.setOnClickListener(this);
        bt_signup.setOnClickListener(this);

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);

        context = getApplicationContext();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_login:
                login();
                break;
            case R.id.bt_signup:
                Toast.makeText(getApplicationContext(),"R.bt.signup",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    private void login(){
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if(email.length() == 0  && password.length() == 0){
            Toast.makeText(getApplicationContext(),"로그인 정보를 입력 해주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            JSONObject login = new JSONObject();
            login.put("email",email);
            login.put("password",password);

            progressDialog = ProgressDialog.show(LoginActivity.this,"","로그인 중입니다.");
            ComroomRestClient.post(context,"/users/login",login,new AsyncHttpResponseHandler(){
                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String data = new String(responseBody);
                    progressDialog.cancel();

                    String user_id = parseLoginData(data);
                    Intent resultData = new Intent();
                    resultData.putExtra("user_id", user_id);
                    setResult(MainActivity.RESULT_LOGIN, resultData);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    progressDialog.cancel();
                    Toast.makeText(context,"로그인에 실패했습니다",Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String parseLoginData(String data){
        try {
            JSONObject reader = new JSONObject(data);
            String userid = reader.getString("user_id");
            return userid;
        } catch (JSONException e) {
            e.printStackTrace();
            return  "";
        }
    }
}
