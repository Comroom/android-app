package net.comroom.comroombook.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import net.comroom.comroombook.core.ComroomRestClient;
import net.comroom.comroombook.core.MemberListData;
import net.comroom.comroombook.core.MemberVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by younghokim on 16. 3. 18.
 */
public class FragmentMain extends Fragment {
    View v;
    final String TAG = "FragmentMain";

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    private MemberVO[] memberList;
    private TextView tv_myname;
    private TextView tv_myemail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) v.findViewById(R.id.listView_main);
        tv_myemail = (TextView) v.findViewById(R.id.my_email);
        tv_myname = (TextView) v.findViewById(R.id.my_name);

        mAdapter = new ListViewAdapter(getContext());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                MemberListData mData = mAdapter.mMemberListData.get(position);
                Toast.makeText(getActivity(), mData.mName, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        getDataFromServer(new DataHandler() {
            @Override
            public void onData(MemberVO[] members) {
                memberList = members;

                for (int i = 0; i < members.length; i++) {
                    if(!MainActivity.user_id.equals(members[i].getId())){
                        Log.d(TAG, "name : " + members[i].getName() + " email : " + members[i].getEmail() + " userid : " + members[i].getId());
                        mAdapter.addItem(getResources().getDrawable(R.drawable.ic_launcher),members[i].getName(),members[i].getEmail());
                    }else{
                        tv_myname.setText(members[i].getName());
                        tv_myemail.setText(members[i].getEmail());
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        return v;
    }

    private class ViewHolder {
        //public ImageView mIcon;

        public TextView mName;

        public TextView mEmail;
    }
    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<MemberListData> mMemberListData = new ArrayList<MemberListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mMemberListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mMemberListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(Drawable icon, String mName, String mEmail) {
            MemberListData addInfo = null;
            addInfo = new MemberListData();
            //addInfo.mIcon = icon;
            addInfo.mName = mName;
            addInfo.mEmail = mEmail;

            mMemberListData.add(addInfo);
        }

        public void remove(int position) {
            mMemberListData.remove(position);
            dataChange();
        }

        public void sort() {
            Collections.sort(mMemberListData, MemberListData.ALPHA_COMPARATOR);
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
                holder.mName = (TextView) convertView.findViewById(R.id.mName);
                holder.mEmail = (TextView) convertView.findViewById(R.id.mEmail);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MemberListData mData = mMemberListData.get(position);

//            if (mData.mIcon != null) {
//                holder.mIcon.setVisibility(View.VISIBLE);
//                holder.mIcon.setImageDrawable(mData.mIcon);
//            }else{
//                holder.mIcon.setVisibility(View.GONE);
//            }

            holder.mName.setText(mData.mName);
            holder.mEmail.setText(mData.mEmail);

            return convertView;
        }
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


