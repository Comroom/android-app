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

import net.comroom.comroombook.R;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by younghokim on 16. 3. 18.
 */
public class FragmentMain extends Fragment {
    View v;

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

        public void addItem(Drawable icon, String mName, String mEmail){
            ListData addInfo = null;
            addInfo = new ListData();
            //addInfo.mIcon = icon;
            addInfo.mName = mName;
            addInfo.mEmail = mEmail;

            mListData.add(addInfo);
        }

        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void sort(){
            Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
            dataChange();
        }

        public void dataChange(){
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
            }else{
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

}


