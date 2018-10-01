package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.UserType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String FINISH_ORDER_CREATION = "com.spidertechnosoft.app.xeniamobi_v2.FINISH_ORDER_CREATION";

   // private Database mDatabase;
    SessionManager sessionManager;

    CardView cvUserList;

    CardView cvItemList;

    CardView cvCategoryList;

    CardView cvPOS;

    CardView cvReports;

    CardView cvSettings;

    GridLayout glHome;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //mDatabase=Database.getInstance();
        sessionManager=new SessionManager(getApplicationContext());

        //mDatabase.clearCart();
        sessionManager.clearCart();

//        getSupportActionBar().setCustomView(R.id.action_bar_title);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //user=mDatabase.getLoggedInUser();
        user=sessionManager.getLoggedInUser();
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtLoggedInUser);
        if(user==null||user.getUsername()==null||user.getUsername().isEmpty())
            txtProfileName.setText("");
        else
            txtProfileName.setText(user.getUsername());

      /*  TextView txtLoggedInSystem=(TextView)navigationView.getHeaderView(0).findViewById(R.id.txtLoggedInSystem);

        String loggedInSystem=mDatabase.getBaseIp();

        if(loggedInSystem==null||loggedInSystem.isEmpty())
            txtLoggedInSystem.setText("");
        else
            txtLoggedInSystem.setText(loggedInSystem);*/

        configView();

    }

    public void configView(){

        glHome=(GridLayout)findViewById(R.id.glHome);

        cvUserList=(CardView) findViewById(R.id.cvUserList);

        cvItemList=(CardView) findViewById(R.id.cvItemList);

        cvCategoryList=(CardView)findViewById(R.id.cvCategoryList);

        cvPOS=(CardView)findViewById(R.id.cvPOS);

        cvReports=(CardView)findViewById(R.id.cvReports);

        cvSettings=(CardView)findViewById(R.id.cvSettings);

        if(user.getType().equals(UserType.STAFF)||user.getType().equals(UserType.USER)){

            glHome.removeView(cvUserList);
            glHome.removeView(cvItemList);
            glHome.removeView(cvCategoryList);
            glHome.removeView(cvSettings);

        }

        cvUserList.setOnClickListener(onDashMenuClick);

        cvItemList.setOnClickListener(onDashMenuClick);

        cvCategoryList.setOnClickListener(onDashMenuClick);

        cvPOS.setOnClickListener(onDashMenuClick);

        cvSettings.setOnClickListener(onDashMenuClick);

        cvReports.setOnClickListener(onDashMenuClick);

    }
    View.OnClickListener onDashMenuClick = new View.OnClickListener() {
        public void onClick(View v) {


            switch (v.getId()){

                case R.id.cvUserList:
                    Intent intentUserList = new Intent(getApplicationContext(),UserListActivity.class);
                    startActivity(intentUserList);
                    break;
                case R.id.cvItemList:
                    Intent intentItems = new Intent(getApplicationContext(),ItemListActivity.class);
                    startActivity(intentItems);
                    break;
                case R.id.cvCategoryList:
                    Intent intentCategory = new Intent(getApplicationContext(),AddCategoryActivity.class);
                    startActivity(intentCategory);
                    break;
                case R.id.cvPOS:
                    Intent intentBill = new Intent(getApplicationContext(),PointOfSaleActivity.class);
                    startActivity(intentBill);
                    break;
                case R.id.cvSettings:
                    Intent intentSetting = new Intent(getApplicationContext(),SettingsActivity.class);
                    startActivity(intentSetting);
                    break;
                case R.id.cvReports:
                    Intent intentReport = new Intent(getApplicationContext(),ReportActivity.class);
                    startActivity(intentReport);
                    break;


            }

        }
    };


    @Override
    public void onResume(){
        //mDatabase.clearCart();
        sessionManager.clearCart();
        super.onResume();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.nav_logout) {
           // if(mDatabase.removeLoggedInUser()) {
            if(sessionManager.removeLoggedInUser()){
                Intent intent = new Intent(HomeActivity.this, LauncherActivity.class);
                startActivity(intent);
                finish();
            }
        }else if(id==R.id.nav_settings) {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
