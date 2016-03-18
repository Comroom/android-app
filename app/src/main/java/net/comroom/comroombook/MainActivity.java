package net.comroom.comroombook;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigation;
    Toolbar toolbar;
    FragmentMain mainfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, mainfragment).commit();
                    case R.id.navigation_item_1:
                        //getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, fragmentHistory).commit();
                        break;
                    case R.id.navigation_item_2:
//                        Snackbar
//                                .make(findViewById(R.id.coordinatelayout), "another snacbar test", Snackbar.LENGTH_LONG)
//                                .setAction("Action", this)
//                                .show(); // Don’t forget to show!
                        break;
                    case R.id.navigation_item_3:
                        //getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, fragmentSetting).commit();
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });
    }

    private void setupFragments() {
        mainfragment = new FragmentMain();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfc, mainfragment).commit();
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

