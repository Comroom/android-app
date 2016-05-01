package net.comroom.comroombook.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.comroom.comroombook.R;
import net.comroom.comroombook.core.ChatVO;
import net.comroom.comroombook.core.ComroomRestClient;
import net.comroom.comroombook.core.MemberVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by younghokim on 16. 3. 18.
 */
public class FragmentChat extends Fragment {
    private final String TAG = "FragmentChat";

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_chat, container, false);

        getDataFromServer(new DataHandler() {
            @Override
            public void onData(ChatVO[] chats) {
                //Chat list 전달

                for(int i =0;i<chats.length;i++){
                    Log.d(TAG,"name : " + chats[i].getName());
                }
            }
        });

        return v;
    }

    public void getDataFromServer(final DataHandler handler) {
        ComroomRestClient.get(getContext(), "/chat/list", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String body = new String(responseBody);
                    JSONArray json = new JSONArray(body);
                    ChatVO[] chats = new ChatVO[json.length()];

                    for(int i=0;i<json.length();i++){
                        JSONObject chat = json.getJSONObject(i);

                        String id = chat.getString("_id");
                        String name = chat.getString("name");

                        chats[i] = new ChatVO(id,name,null);
                    }

                    handler.onData(chats);
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
        abstract void onData(ChatVO[] chats);
    }
}
