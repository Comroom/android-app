package net.comroom.comroombook.ui;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.comroom.comroombook.R;
import net.comroom.comroombook.ui.FragmentMain;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigation;
    Toolbar toolbar;
    FragmentMain mainFragment;
    FragmentChat chatFragment;
    FragmentTimeTable timeTableFragment;
    FragmentMessage messageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigationView();
        setupToolbar();
        setupFragments();
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
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, mainFragment).commit();
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

