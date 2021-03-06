package br.ufc.insta;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import br.ufc.insta.frames.CameraFragment;
import br.ufc.insta.frames.SearchByDateFragment;
import br.ufc.insta.frames.ProfileFragment;
import br.ufc.insta.frames.SearchFragment;
import br.ufc.insta.models.User;


public class MainActivity extends AppCompatActivity {

    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_CODE = 1;
    public static User user;

    public static Context mainContext;
    public  static MainActivity mainActivity;
    public static BottomNavigationView bottomNavigationView;

    private FrameLayout frameLayout;
    private Toolbar toolbar;

    private Fragment searchByDateFragment, cameraFragment, profileFragment, searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.home_frame);
        toolbar = findViewById(R.id.main_toolbar);

        if( getIntent().getExtras() != null)
        {
            user = getIntent().getExtras().getParcelable("user");
        }

        searchByDateFragment = new SearchByDateFragment();
        cameraFragment = new CameraFragment();
        profileFragment = new ProfileFragment();
        searchFragment = new SearchFragment();

        mainActivity = this;
        mainContext = this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        bottomNavigationView.setSelectedItemId(R.id.bottomnav_profile);
        loadFragment(profileFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.bottomnav_home:
                        loadFragment(searchByDateFragment);
                        return true;
                    case R.id.bottomnav_search:
                        loadFragment(searchFragment);
                        return true;
                    case R.id.bottomnav_camera:
                        loadFragment(cameraFragment);
                        return true;
                    case R.id.bottomnav_profile:
                        loadFragment(profileFragment);
                        return true;
                }
                return false;
            }
        });

        checkPermissions();
    }

    private void checkPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.toolbar_edit:
                Intent activity = new Intent(MainActivity.this, EditActivity.class);
                startActivity(activity);
                return true;

            default:return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbatitems,menu);
        return true;
    }

    public void loadFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home_frame, fragment);
        transaction.commit();

    }

    public void loadProfileFragment(User user) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle arguments = new Bundle();
        arguments.putParcelable("user", user);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ProfileFragment newPF = new ProfileFragment();
        newPF.setArguments(arguments);

        transaction.replace(R.id.home_frame, newPF);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

}
