package com.parrucchieri;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.parrucchieri.Constant.Appconstant;

import me.anshulagarwal.simplifypermissions.MarshmallowSupportActivity;
import me.anshulagarwal.simplifypermissions.Permission;

public class SplashActivity extends MarshmallowSupportActivity implements Permission.PermissionCallback {


    private static final int SPLASH_ACTIVITY_REQUEST_INTERNET_PERMISSION = 5;
    private static final String[] SPLASH_ACTIVITY_INTERNET_PERMISSIONS =
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };


//    private static final String[] SPLASH_ACTIVITY_INTERNET_PERMISSIONS ={Manifest.permission.INTERNET};


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        checkPermissionInternet();


        // here initializing the shared preference
        Appconstant.sh = getSharedPreferences("myprefe", 0);
        Appconstant.editor = Appconstant.sh.edit();

        // check here if user is login or not
        Appconstant.str_login_test = Appconstant.sh.getString("loginTest", null);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                /* if user login test is true on oncreate then redirect the user to result page */

                if (Appconstant.str_login_test != null
                        && !Appconstant.str_login_test.toString().trim().equals("")) {

                    Log.e("Login detail found :", "Go to Main Screen");
                    Intent Gomainscreen = new Intent(getApplicationContext(),
                            MainActivity.class);
                    startActivity(Gomainscreen);
                }
				/* if user login test is false on oncreate then redirect the user to login & registration page */
                else {

                    Log.e("Login detail not found :", "Go to Login Screen");
                    Intent Gologinscreen = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    startActivity(Gologinscreen);

                }
            }

        }, 1000);

    }

    private void checkPermissionInternet() {

        Permission.PermissionBuilder permissionBuilder = new Permission.PermissionBuilder(
                SPLASH_ACTIVITY_INTERNET_PERMISSIONS,
                SPLASH_ACTIVITY_REQUEST_INTERNET_PERMISSION, this);
        permissionBuilder.enableDefaultRationalDialog("Please Allow all access",
                "This will help us to improve our Services & serve you best.")
                .enableDefaultSettingDialog("Permission Error", "Setting dialog message");
        requestAppPermissions(permissionBuilder.build());
    }

    @Override
    public void onPermissionGranted(int i) {


    }

    @Override
    public void onPermissionDenied(int i) {

    }

    @Override
    public void onPermissionAccessRemoved(int i) {

    }


}
