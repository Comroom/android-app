package net.comroom.comroombook.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.comroom.comroombook.R;
import net.comroom.comroombook.ui.FragmentMain;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private Toolbar toolbar;
    private FragmentMain mainFragment;
    private FragmentChat chatFragment;
    private FragmentTimeTable timeTableFragment;
    private FragmentMessage messageFragment;

    private FloatingActionMenu faMenu;
    private FloatingActionButton fabCreateChat;
    private Handler mUiHandler = new Handler();

    static final int REQUSET_LOGIN = 1;
    static final int RESULT_LOGIN = 2;
    public static String user_id;

    private View.OnTouchListener fabActionListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_create_chat:
                    Intent intent = new Intent(MainActivity.this, CreateChatActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "채팅방 개설");
                    Snackbar
                            .make(findViewById(R.id.coordinatelayout), "채팅방이 개설됐습니다.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", this)
                            .show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFabComponents();
        initFabActionListner();

        setupNavigationView();
        setupToolbar();
        setupFragments();
        setupFab();

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
                        setFabVisibilityGone();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, mainFragment).commit();
                        break;
                    case R.id.navigation_item_1:
                        setFabVisibility();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, chatFragment).commit();
                        break;
                    case R.id.navigation_item_2:
                        setFabVisibilityGone();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, timeTableFragment).commit();
                        break;
                    case R.id.navigation_item_3:
                        setFabVisibilityGone();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, messageFragment).commit();
                        break;
                    case R.id.navigation_time_4:
                        setFabVisibilityGone();
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

    private void initFabComponents(){
        faMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        fabCreateChat = (FloatingActionButton)findViewById(R.id.fab_create_chat);
    }

    private void initFabActionListner(){
        fabCreateChat.setOnClickListener(clickListener);
    }

    private void setupFab() {
        faMenu.setVisibility(View.GONE);
        faMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faMenu.isOpened()) {
                    Toast.makeText(MainActivity.this, faMenu.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                faMenu.toggle(true);
            }
        });
        faMenu.hideMenuButton(false);
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                faMenu.showMenuButton(true);
            }
        }, 150);
        faMenu.setClosedOnTouchOutside(true);
    }

    public void setFabVisibility(){
        faMenu.setVisibility(View.VISIBLE);
    }

    public void setFabVisibilityGone(){
        faMenu.setVisibility(View.GONE);
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

