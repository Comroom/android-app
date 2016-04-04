package net.comroom.comroombook.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) v.findViewById(R.id.listView_main);
        mAdapter = new ListViewAdapter(getContext());
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mAdapter.addItem(getResources().getDrawable(R.drawable.ic_launcher),
                "확인이 완료되었습니다",
                "2014-02-18");

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                ListData mData = mAdapter.mListData.get(position);
                Toast.makeText(getActivity(), mData.mName, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        getDataFromServer(new DataHandler() {
            @Override
            public void onData(MemberVO[] members) {
                for (int i = 0; i < members.length; i++) {
                    Log.d(TAG, "name : " + members[i].getName() + " email : " + members[i].getEmail() + " userid : " + members[i].getId());
                }
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
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(Drawable icon, String mName, String mEmail) {
            ListData addInfo = null;
            addInfo = new ListData();
            //addInfo.mIcon = icon;
            addInfo.mName = mName;
            addInfo.mEmail = mEmail;

            mListData.add(addInfo);
        }

        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }

        public void sort() {
            Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
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

            ListData mData = mListData.get(position);

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


