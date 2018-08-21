package com.example.lenovo.dra.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.lenovo.dra.HospitalRecyclerList;
import com.example.lenovo.dra.ListBloodRequestsNotifications;
import com.example.lenovo.dra.MedicineAvailibility;
import com.example.lenovo.dra.R;
import com.example.lenovo.dra.tabPages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    private FirebaseAuth mAuth;
    String USERNAME;
    String EMAIL;
    String PHONE;
    TextView txtUserName;
    TextView txtEmail;
    TextView txtPhone;
    DrawerLayout drawer;
    SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;

    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        //getting values of current user using shared preferences
        sharedPreferences = getSharedPreferences("currentUserData", Context.MODE_PRIVATE);
        USERNAME = sharedPreferences.getString("USERNAME","");
        EMAIL = sharedPreferences.getString("EMAIL","");
        PHONE = sharedPreferences.getString("PHONE","");
        Log.i("error", "in mainactivity "+USERNAME+EMAIL+PHONE);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);

        txtUserName = (TextView)header.findViewById(R.id.txtUSERNAME);
        txtEmail = (TextView)header.findViewById(R.id.txtEMAIL);
        txtPhone = (TextView)header.findViewById(R.id.txtPHONE);
        txtUserName.setText(USERNAME);
        txtEmail.setText(EMAIL);
        txtPhone.setText(PHONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabPages tabPages = new tabPages(getSupportFragmentManager());

        viewPager.setAdapter(tabPages);
        //tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setupDrawerContent(navigationView);
    }

    public void SelectItemDrawer(MenuItem menuItem){
        fragment= null;
        Class fragmentClass;
        switch(menuItem.getItemId()){
            case R.id.medAvail:
                fragmentClass= MedicineAvailibility.class;
                break;
            case R.id.bloodreq:
                fragmentClass= SendBloodRequest.class;
                break;
            default:
                fragmentClass= MedicineAvailibility.class;
        }
        try{
            fragment= (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        fragmentManager= getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main,fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawers();
    }
    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                SelectItemDrawer(item);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        fragmentManager = getSupportFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);}
            else if((!drawer.isDrawerOpen(GravityCompat.START))&&(fragment.isVisible())){
                startActivity(new Intent(this,MainActivity.class));
            }else {
            super.onBackPressed();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //handling menu options
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, HospitalRecyclerList.class));
        }else if(id== R.id.logout) {
            logOut();
        }else if(id == R.id.btnBroadcast){
            startActivity(new Intent(MainActivity.this, sendNotification.class));
        }else if(id == R.id.btnAllBloodRequests){
            startActivity(new Intent(MainActivity.this, ListBloodRequestsNotifications.class));
        }
        return super.onOptionsItemSelected(item);
    }

    //logging out will clear the shared preferences and remove device token from firebase
    public  void  logOut(){
        SharedPreferences preferences = getSharedPreferences("currentUserData", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        String currentUID = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("tokens").child(currentUID).removeValue();
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

}