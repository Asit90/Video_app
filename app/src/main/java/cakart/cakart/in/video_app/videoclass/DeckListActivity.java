package cakart.cakart.in.video_app.videoclass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import cakart.cakart.in.video_app.R;
import cakart.cakart.in.video_app.db.MyDatabaseHelper;
import cakart.cakart.in.video_app.register.LoginActivity;

public class DeckListActivity extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    public int stack_count = 0;
    Fragment sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);


        NavigationView nvDrawer = findViewById(R.id.nv);
        mDrawerlayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);

        if(getIntent().getExtras().getString("show_type").equals("videoclasses"))
        {
            sf = new VideoClassesTypeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flcontent, sf).commit();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerlayout.closeDrawers();
                selectedItemDrawer(item);
                return true;
            }
        });
    }

    private void selectedItemDrawer(MenuItem menuItem) {
        android.support.v4.app.Fragment myFragment = null;
        Class fragmentClass;

        switch (menuItem.getItemId()) {

            case R.id.video_classes:
                fragmentClass = VideoClassesTypeFragment.class;
                break;
            case R.id.logout:
                SharedPreferences s = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = s.edit();
                ed.clear();
                ed.commit();
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                        "app_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = sharedPref.edit();
                edt.clear();
                edt.commit();
                getApplicationContext().deleteDatabase(MyDatabaseHelper.DATABASE_NAME);
                Intent i = new Intent(DeckListActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return;
            default:
                fragmentClass = VideoClassesTypeFragment.class;
        } try {
            myFragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            if(myFragment instanceof  VideoClassesTypeFragment){
                sf = (VideoClassesTypeFragment) myFragment;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
    }
    public void onBackPressed(){
        if(sf instanceof VideoClassesTypeFragment) {
            VideoClassesTypeFragment k = (VideoClassesTypeFragment) sf;
            if (sf != null && stack_count > 0) {
                stack_count = stack_count - 1;
                k.goback();
            } else if (sf != null && !k.is_showing) {
                sf = new VideoClassesTypeFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent, sf).commit();
            } else {
                finish();
            }
        }
        else {
            finish();
        }
    }
    protected void onResume() {
        super.onResume();
//        showList();
    }
        @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


