package net.comroom.comroombook.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import net.comroom.comroombook.R;
import net.comroom.comroombook.ui.FragmentMain;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private Toolbar toolbar;
    private FragmentMain mainFragment;
    private FragmentChat chatFragment;
    private FragmentTimeTable timeTableFragment;
    private FragmentMessage messageFragment;

    static final int REQUSET_LOGIN = 1;
    static final int RESULT_LOGIN = 2;
    public static String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigationView();
        setupToolbar();
        setupFragments();

        /* Login Page */
        startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUSET_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUSET_LOGIN){
            if(resultCode == RESULT_LOGIN){
                user_id = data.getStringExtra("user_id");
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, mainFragment).commit();
                Log.d("MainActivity",user_id);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout != null)
                    drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigation = (NavigationView) findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemid = menuItem.getItemId();
                switch (itemid) {
                    case R.id.navigation_item_0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, mainFragment).commit();
                        break;
                    case R.id.navigation_item_1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, chatFragment).commit();
                        break;
                    case R.id.navigation_item_2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, timeTableFragment).commit();
                        break;
                    case R.id.navigation_item_3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, messageFragment).commit();
                        break;
                    case R.id.navigation_time_4:
                        startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUSET_LOGIN);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, new Fragment()).commit();
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });
    }

    private void setupFragments() {
        mainFragment = new FragmentMain();
        chatFragment = new FragmentChat();
        timeTableFragment = new FragmentTimeTable();
        messageFragment = new FragmentMessage();
        //getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, mainFragment).commit();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);

        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);
    }
}

