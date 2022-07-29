package com.wicoding.winwebradio;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.Iterator;
import java.util.Set;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    IndexFragment indexFragment;


    //private String CurrentTrack = "https://www.radioking.com/widgets/currenttrack.php?radio=42109&format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        indexFragment = new IndexFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout_content, indexFragment).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        //navigationView.getMenu().getItem(2).setEnabled(false);
        //navigationView.getMenu().getItem(3).setEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.support.v4.app.Fragment fragment = null;
        if (id == R.id.nav_accueil) {
            this.setTitle(getString(R.string.app_name));
            fragment = indexFragment;
        } else if (id == R.id.nav_emission) {
            this.setTitle(getString(R.string.menu_emission));
            fragment = new EmissionFragment();
            //} else if (id == R.id.nav_agenda) {
            //this.setTitle(getString(R.string.menu_emission));
            //fragment = new EmissionFragment();
        } else if (id == R.id.nav_chat) {
            this.setTitle(getString(R.string.menu_chat));
            fragment = new ChatFragment();
        } else if (id == R.id.nav_rate) {
            actionRate();
        } else if (id == R.id.nav_partage) {
            actionShare();
        }
        else if (id == R.id.nav_propos) {
            this.setTitle(getString(R.string.menu_propos));
            fragment = new InfoFragment();
        }

        if(null!=fragment)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frameLayout_content, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void Quit() {
        super.finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getExtras() != null && getIntent().getExtras().getBoolean("close",false))
            Quit();
    }


    public void actionRate()
    {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void actionShare()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,  getString(R.string.text_partage)+"\n" +
                "\n http://play.google.com/store/apps/details?id=" + getPackageName());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.menu_partage)));
    }
}
