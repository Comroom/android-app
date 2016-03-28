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

import net.comroom.comroombook.R;


/**
 * Created by younghokim on 16. 3. 18.
 */
public class FragmentMain extends Fragment {
        View v;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                v = inflater.inflate(R.layout.fragment_main, container, false);
                return v;
        }


}


