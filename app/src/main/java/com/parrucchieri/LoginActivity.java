package com.parrucchieri;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.location.LocationRequest;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.parrucchieri.Constant.Appconstant;
import com.parrucchieri.Constant.HttpUrlPath;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 11/1/17.
 */
@SuppressWarnings("deprecation")
public class LoginActivity extends EasyLocationAppCompatActivity {

    @BindView(R.id.tv_login_register)
    TextView tv_register;

    @BindView(R.id.btn_login)
    Button btnlogin;

    @BindView(R.id.edt_login_user_email)
    EditText Edt_Login_email_address;
    @BindView(R.id.edt_login_user_password)
    EditText Edt_Login_password;

    @BindView(R.id.rl_login_progress)
    RelativeLayout pv_login_progressview;

    CircularProgressBar mCircularProgressBarlogin;

    boolean iserror = false;
    String Str_login_email = "", Str_login_password = "" , result = "", error = "" ,
            Str_server_id = "", Str_Email_id = "", Str_User_Name = "", Str_User_Latitude = "",
            Str_User_Longitude = "", Str_User_Password = ""  ,Str_user_Latitude = "", Str_user_Longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

          /*circular progress bar (Start)*/
        mCircularProgressBarlogin = (CircularProgressBar) findViewById(R.id.login_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBarlogin.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);

         /*get current lat-long (Start)*/
        locationData();
        /*get current lat-long (End)*/

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentsignup = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intentsignup);
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intentmain = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentmain);*/

                Str_login_email = Edt_Login_email_address.getText().toString().trim();
                Str_login_password = Edt_Login_password.getText().toString().trim();

                Log.e(" Login Fields data :", "\n"
                        + "Str_login_email :" + "" + Str_login_email + "\n"
                        + "Str_login_password :" + "" + Str_login_password + "\n");

                if (Str_login_email.equals("")) {
                    iserror = true;
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(Edt_Login_email_address);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter your Email Id"));

                } else if (!isValidEmail(Str_login_email)) {
                    iserror = true;
                    //	emailedit.requestFocus();
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(Edt_Login_email_address);
                    /**************** End Animation ****************/
                    /*Toast.makeText(getApplicationContext(),
                            "Please enter valid email address.", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter valid email address."));


                } else if (Str_login_password.equals("")) {
                    iserror = true;
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(Edt_Login_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Password", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter your Password"));

                } else if (Str_login_password.length() < 5) {
                    iserror = true;
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(Edt_Login_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter more than 5 character in password.",
                            Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Wrong password."));


                } else if (!iserror) {

                    /*Toast.makeText(getApplicationContext(),
                            "Good", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Good All Value Correct"));

                    if (Utils.isConnected(getApplicationContext())) {
                        LoginJsontask task = new LoginJsontask();
                        task.execute();
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(LoginActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please Your Internet Connectivity..!!"));

                    }

                }
            }
        });


    }



    /*email address field validator (Start)*/
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }
    /*email address field validator (End)*/








    /*progressbar data (Start)*/
    private void updateValues() {
        CircularProgressDrawable circularProgressDrawable;
        CircularProgressDrawable.Builder b = new CircularProgressDrawable.Builder(this)
                .colors(getResources().getIntArray(R.array.gplus_colors))
                /*.sweepSpeed(mSpeed)
                .rotationSpeed(mSpeed)
                .strokeWidth(dpToPx(mStrokeWidth))*/
                .style(CircularProgressDrawable.STYLE_ROUNDED);
       /* if (mCurrentInterpolator != null) {
            b.sweepInterpolator(mCurrentInterpolator);
        }*/
        mCircularProgressBarlogin.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularProgressBarlogin.getWidth(),
                mCircularProgressBarlogin.getHeight());
        mCircularProgressBarlogin.setVisibility(View.INVISIBLE);
        mCircularProgressBarlogin.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/







    /*location data (Start)*/
    @Override
    public void onLocationPermissionGranted() {
        showToast("Location permission granted");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationPermissionDenied() {
        showToast("Location permission denied");
    }

    @Override
    public void onLocationReceived(Location location) {
//        showToast(location.getProvider() + "," + location.getLatitude() + "," + location.getLongitude());

        Toast.makeText(getApplicationContext(), "Latitude :" + "  " + location.getLatitude()
                + "Longitude " + "" + location.getLongitude(), Toast.LENGTH_SHORT).show();


        Str_user_Latitude = Double.toString(location.getLatitude());
        Str_user_Longitude = Double.toString(location.getLongitude());

        Log.e("Str_user_Latitude :", "" + Str_user_Latitude);
        Log.e("Str_user_Longitude :", "" + Str_user_Longitude);


    }

    @Override
    public void onLocationProviderEnabled() {
        showToast("Location services are now ON");
    }

    @Override
    public void onLocationProviderDisabled() {
        showToast("Location services are still Off");
    }

    /*get current lat lon of the user (Start)*/
    public void locationData() {
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(5000);
        EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
                .setFallBackToLastLocationTime(3000)
                .build();
        requestSingleLocationFix(easyLocationRequest);
    }
    /*get current lat lon of the user (Start)*/


    /*location data (End)*/



    public class LoginJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW WEB SERVICE IS RUNNING *******", "YES");
            pv_login_progressview.setVisibility(View.VISIBLE);

        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW BACKGROUND TASK IS RUNNING *******", "YES");
            Log.e("******* NOW BACKGROUND TASK IS RUNNING *******", "YES");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpUrlPath.urlPathlogin + "login&");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("email", Str_login_email));
                nameValuePairs.add(new BasicNameValuePair("password", Str_login_password));
                nameValuePairs.add(new BasicNameValuePair("latitude", Str_user_Latitude));
                nameValuePairs.add(new BasicNameValuePair("longitude", Str_user_Longitude));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("*******object******** :", "" + object);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("mes");
                if (result.equalsIgnoreCase("Success")) {
                    Str_server_id = jobect.getString("id");
                    Str_Email_id = jobect.getString("email");
                    Str_User_Name = jobect.getString("username");
                    Str_User_Latitude = jobect.getString("latitude");
                    Str_User_Longitude = jobect.getString("longitude");

                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            pv_login_progressview.setVisibility(View.GONE);

            if (!iserror) {
                if (result.equalsIgnoreCase("Success")) {

                    Appconstant.editor.putString("id", Str_server_id);
                    Appconstant.editor.putString("email", Str_Email_id);
                    Appconstant.editor.putString("username", Str_User_Name);
                    Appconstant.editor.putString("latitude", Str_User_Latitude);
                    Appconstant.editor.putString("longitude", Str_user_Longitude);
                    Appconstant.editor.putString("loginTest", "true");
                    Appconstant.editor.commit();


                    Toast.makeText(LoginActivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                    in.putExtra("EXIT", "0");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    LoginActivity.this.startActivity(in);
                    finish();
//                    ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).stop();

//                    ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).progressiveStop();


                } else {

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(Edt_Login_email_address);

                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(Edt_Login_password);
                    /**************** End Animation ****************/

                    Log.e("********* Login Detail SEND *********", "NOT Success USERNAME PASSWORD ERROR");
                    Log.e("********* Login Detail SEND *********", "NOT Success USERNAME PASSWORD ERROR");
                    Log.e("********* Login Detail SEND *********", "NOT Success USERNAME PASSWORD ERROR");
//                    ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).stop();

//                    ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).progressiveStop();

                    Toast.makeText(getApplicationContext(), "Username or Password is wrong",
                            Toast.LENGTH_SHORT).show();
//									// TODO Auto-generated method stub
                }
            } else {
                Log.e("********* Login Detail SEND *********", "NOT Success");
                Log.e("********* Login Detail SEND *********", "NOT Success");
                Log.e("********* Login Detail SEND *********", "NOT Success");

                Toast.makeText(getApplicationContext(), "Oops!! Please check server connection .",
                        Toast.LENGTH_SHORT).show();

            }
        }

    }



}
