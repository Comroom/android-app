package net.comroom.comroombook.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.comroom.comroombook.R;

/**
 * Created by younghokim on 16. 3. 18.
 */
public class FragmentChat extends Fragment {
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_chat, container, false);

        return v;
    }
}
