package net.comroom.comroombook.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.comroom.comroombook.R;
import net.comroom.comroombook.core.ChatRoomListData;
import net.comroom.comroombook.core.ChatVO;
import net.comroom.comroombook.core.ComroomRestClient;
import net.comroom.comroombook.core.MemberListData;
import net.comroom.comroombook.core.MemberVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;

/**
 * Created by younghokim on 16. 3. 18.
 */
public class FragmentChat extends Fragment {
    private final String TAG = "FragmentChat";

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    private MemberVO[] memberList;

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_chat, container, false);

        mListView = (ListView) v.findViewById(R.id.listView_chat);
        mAdapter = new ListViewAdapter(getContext());

        mListView.setAdapter(mAdapter);
        mListView.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                ChatRoomListData mData = mAdapter.mChatRoomListData.get(position);
                Toast.makeText(getActivity(), mData.mRoomName, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        getDataFromServer(new DataHandler() {
            @Override
            public void onData(ChatVO[] chats) {
                //Chat list 전달

                for(int i =0;i<chats.length;i++){
                    Log.d(TAG,"name : " + chats[i].getName());

                    String[] members = chats[i].getMembers();
                    for(int j=0;i<members.length;i++){
                        Log.d(TAG,"member : " + members[i]);
                    }
                }
            }
        });

        return v;
    }

    private class ViewHolder {
        //public ImageView mIcon;

        public TextView mRoomName;

        public TextView mRoomNumber;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ChatRoomListData> mChatRoomListData = new ArrayList<ChatRoomListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mChatRoomListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mChatRoomListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(Drawable icon, String mRoomName, String mRoomNumber) {
            ChatRoomListData addInfo = null;
            addInfo = new ChatRoomListData();
            //addInfo.mIcon = icon;
            addInfo.mRoomName = mRoomName;
            addInfo.mRoomNumber = mRoomNumber;

            mChatRoomListData.add(addInfo);
        }

        public void remove(int position) {
            mChatRoomListData.remove(position);
            dataChange();
        }

        public void sort() {
            Collections.sort(mChatRoomListData, ChatRoomListData.ALPHA_COMPARATOR);
            dataChange();
        }

        public void dataChange() {
            //mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, null);

                //holder.mIcon = (ImageView) convertView.findViewById(R.id.mImage);
                holder.mRoomName = (TextView) convertView.findViewById(R.id.mName);
                holder.mRoomNumber = (TextView) convertView.findViewById(R.id.mEmail);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ChatRoomListData mData = mChatRoomListData.get(position);

//            if (mData.mIcon != null) {
//                holder.mIcon.setVisibility(View.VISIBLE);
//                holder.mIcon.setImageDrawable(mData.mIcon);
//            }else{
//                holder.mIcon.setVisibility(View.GONE);
//            }
            holder.mRoomName.setText(mData.mRoomName);
            holder.mRoomNumber.setText(mData.mRoomNumber);

            return convertView;
        }
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
                        String[] member = new String[0];

                        try{
                            JSONArray chat_member = chat.getJSONArray("member");
                            member = new String[chat_member.length()];

                            for(int j=0;j<chat_member.length();j++){
                                member[j] = chat_member.getString(j);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        chats[i] = new ChatVO(id,name,member);
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
