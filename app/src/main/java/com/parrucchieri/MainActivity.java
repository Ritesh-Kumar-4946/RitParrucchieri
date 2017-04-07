package com.parrucchieri;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parrucchieri.Constant.Appconstant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ritesh on 13/1/17.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.containerView)
    FrameLayout frame_container;

    @BindView(R.id.rl_dr_home)
    RelativeLayout rl_Dr_Home;

    @BindView(R.id.rl_dr_book_appointment)
    RelativeLayout rl_Dr_Book_Appointment;

    @BindView(R.id.rl_dr_my_appointment)
    RelativeLayout rl_Dr_My_Appointment;

    @BindView(R.id.rl_dr_my_acoount)
    RelativeLayout rl_Dr_My_Account;

    @BindView(R.id.rl_dr_style_book)
    RelativeLayout rl_Dr_Style_Book;

    @BindView(R.id.rl_dr_faq)
    RelativeLayout rl_Dr_FAQ;

    @BindView(R.id.rl_dr_contact_us)
    RelativeLayout rl_Dr_Contact_Us;

    @BindView(R.id.rl_dr_reload_app)
    RelativeLayout rl_Dr_Reload_App;

    @BindView(R.id.rl_dr_logout)
    RelativeLayout rl_Dr_Logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        rl_Dr_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Home Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Book_Appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Book Appintment Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_My_Appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "My Appointment Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_My_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "My Account Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Style_Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Style Book Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_FAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "FAQ Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Contact_Us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Contact Us Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Reload_App.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Reload App Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You have successfully logout",
                        Toast.LENGTH_SHORT).show();
                Appconstant.editor.remove("loginTest");
                Appconstant.editor.commit();

                Intent GoLoginscreen = new Intent(getApplicationContext(),
                        LoginActivity.class);

                startActivity(GoLoginscreen);
            }
        });


    }

    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MainActivity.this,
                    SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }


}
