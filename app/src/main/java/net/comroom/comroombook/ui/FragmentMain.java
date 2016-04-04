package net.comroom.comroombook.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.comroom.comroombook.R;
import net.comroom.comroombook.core.ComroomRestClient;
import net.comroom.comroombook.core.MemberVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Member;
import java.util.logging.Handler;

import cz.msebera.android.httpclient.Header;


/**
 * Created by younghokim on 16. 3. 18.
 */
public class FragmentMain extends Fragment{
    final String TAG = "FragmentMain";
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);
        getDataFromServer(new DataHandler() {
            @Override
            public void onData(MemberVO[] members) {
                for(int i=0;i<members.length;i++){
                    Log.d(TAG,"name : " + members[i].getName() + " email : " + members[i].getEmail() + " userid : " + members[i].getId());
                }
            }
        });

        return v;
    }


    public void getDataFromServer(final DataHandler handler) {
        ComroomRestClient.get(getContext(), "/users", new AsyncHttpResponseHandler() {
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

    interface DataHandler{
        abstract void onData(MemberVO[] members);
    }
}


