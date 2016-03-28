package net.comroom.comroombook.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.comroom.comroombook.R;
import net.comroom.comroombook.core.ComroomRestClient;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by younghokim on 16. 3. 28.
 */
public class SignUpActivity extends Activity{
    final String TAG = "SignUpActivity";

    EditText et_name;
    EditText et_email;
    EditText et_password;
    Button bt_signUp;

    private ProgressDialog progressDialog;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signup_signup:
                    bt_signUp.setEnabled(false);
                    signUp();
                    bt_signUp.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setInit();
    }

    private void setInit(){
        et_name = (EditText)findViewById(R.id.signup_name);
        et_email = (EditText)findViewById(R.id.signup_email);
        et_password = (EditText)findViewById(R.id.signup_password);
        bt_signUp = (Button)findViewById(R.id.signup_signup);
        bt_signUp.setOnClickListener(clickListener);
    }

    private void signUp(){
        String buffName = et_name.getText().toString();
        String buffEmail = et_email.getText().toString();
        String buffPasswordd = et_password.getText().toString();

        if(buffName.length() == 0 || buffEmail.length() == 0 || buffPasswordd.length() == 0){
            Toast.makeText(SignUpActivity.this, "회원가입 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            JSONObject signUp_json = new JSONObject();
            signUp_json.put("name", buffName);
            signUp_json.put("email", buffEmail);
            signUp_json.put("password", buffPasswordd);

            progressDialog = ProgressDialog.show(SignUpActivity.this,"","회원가입 중입니다.");
            ComroomRestClient.post(getApplicationContext(), "/users/signup", signUp_json, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(),"회원가입에 성공했습니다",Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
