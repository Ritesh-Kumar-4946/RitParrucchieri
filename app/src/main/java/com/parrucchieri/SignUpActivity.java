package com.parrucchieri;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.parrucchieri.Constant.HttpUrlPath;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 12/1/17.
 */
@SuppressWarnings("deprecation")
public class SignUpActivity extends EasyLocationAppCompatActivity {

    private static final String[] ANDROID_VERSIONS = {
            "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich", "Jelly Bean", "KitKat",
            "Lollipop", "Marshmallow"
    };
    @BindView(R.id.tv_signup_login)
    TextView tv_login;
    @BindView(R.id.btn_signup)
    Button Btn_Singup;
    @BindView(R.id.edt_signup_username)
    EditText Edt_Signup_username;
    @BindView(R.id.edt_signup_email_address)
    EditText Edt_Signup_email_address;
    @BindView(R.id.edt_signup_password)
    EditText Edt_Signup_password;
    @BindView(R.id.edt_signup_confirm_password)
    EditText Edt_Signup_confirm_password;
    boolean iserror = false;
    //    boolean spinnerBooleanValue = false;
    String result = "", error = "", strHairStyledefault, strHairStyleSelectedValue = "", strHairStyle = "",
            strHairStyleListSendValue, Str_user_Latitude = "", Str_user_Longitude = "";
    String str_username = "", str_emailid = "", str_password = "", str_confirm_password = "",
            STR_userID = "", STR_user_name = "", STR_user_email = "", STR_user_hair_style = "",
            STR_user_latitude = "", STR_user_longitude = "";
    ArrayList<String> hairstyletypelist = new ArrayList<String>();

    @BindView(R.id.rl_signup_progress)
    RelativeLayout signupProgress;
    @BindView(R.id.rl_main_spinner)
    RelativeLayout RlSpinner;

    CircularProgressBar mCircularProgressBar;

    @BindView(R.id.sp_spinner)
    MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);


        /*circular progress bar (Start)*/
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.signup_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/







        /*get current lat-long (Start)*/
        locationData();
        /*get current lat-long (End)*/








        /*go to login page (Start)*/
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentsignup = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intentsignup);
            }
        });
        /*go to login page (End)*/





        /*spinner click method and not clicked methos (Start)*/
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(getApplicationContext(), "Position :" + "  " + position + "  Clicked " + "" + item,
                        Toast.LENGTH_SHORT).show();
                strHairStyleSelectedValue = item;

            }
        });
        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Toast.makeText(getApplicationContext(), "Nothing selected ",
                        Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(), "Default selected :" + "" + strHairStyledefault,
                        Toast.LENGTH_SHORT).show();

            }
        });
        /*spinner click method and not clicked methos (End)*/


        if (Utils.isConnected(getApplicationContext())) {
            HairStyleListJsontask task = new HairStyleListJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(SignUpActivity.this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }







        /* signup button click method (Start)*/
        Btn_Singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_username = Edt_Signup_username.getText().toString().trim();
                str_emailid = Edt_Signup_email_address.getText().toString().trim();
                str_password = Edt_Signup_password.getText().toString().trim();
                str_confirm_password = Edt_Signup_confirm_password.getText().toString().trim();

                Log.e(" Sign Up Fields data :", "\n"
                        + "Str_user_Latitude :" + "" + Str_user_Latitude + "\n"
                        + "Str_user_Longitude :" + "" + Str_user_Longitude + "\n"
                        + "strHairStyleSelectedValue :" + "" + strHairStyleSelectedValue + "\n"
                        + "str_username :" + "" + str_username + "\n"
                        + "str_emailid :" + "" + str_emailid + "\n"
                        + "str_password :" + "" + str_password + "\n"
                        + "str_confirm_password :" + "" + str_confirm_password);

                if (spinner.getSelectedIndex() == 0) {
                    Log.e("error", "ok");
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(RlSpinner);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter more than 5 character in password.",
                            Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignUpActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please Select Value"));

                }else if (str_username.equals("")) {
                    iserror = true;
                    /**************** Start Animation ****************/
                /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/

                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(Edt_Signup_username);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter your Name",
                            Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignUpActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter your Name"));


                } else if (str_emailid.equals("")) {
                    iserror = true;
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(Edt_Signup_email_address);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignUpActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter your Email Id"));

                } else if (!isValidEmail(str_emailid)) {
                    iserror = true;
                    //	emailedit.requestFocus();
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(Edt_Signup_email_address);
                    /**************** End Animation ****************/
                    /*Toast.makeText(getApplicationContext(),
                            "Please enter valid email address.", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignUpActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter valid email address."));


                } else if (str_password.equals("")) {
                    iserror = true;
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(Edt_Signup_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Password", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignUpActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter your Password"));

                } else if (str_password.length() < 5) {
                    iserror = true;
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(Edt_Signup_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter more than 5 character in password.",
                            Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignUpActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter more than 5 character in password."));


                } else if (!str_confirm_password.equals(str_password)) {
                    iserror = true;
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(Edt_Signup_password);

                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(Edt_Signup_confirm_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "oopsss....\n Password not Match Please try again", Toast.LENGTH_SHORT).show();*/


                    SnackbarManager.show(
                            Snackbar.with(SignUpActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("oopsss....\\n Password not Match Please try again"));

                } else if (!iserror) {

                    /*Toast.makeText(getApplicationContext(),
                            "Good", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignUpActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Good All Value Correct"));

                    if (Utils.isConnected(getApplicationContext())) {
                        SignUpJsontask task = new SignUpJsontask();
                        task.execute();
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(SignUpActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please Your Internet Connectivity..!!"));

                    }

                }

            }
        });
        /* signup button click method (End)*/


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
        mCircularProgressBar.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularProgressBar.getWidth(),
                mCircularProgressBar.getHeight());
        mCircularProgressBar.setVisibility(View.INVISIBLE);
        mCircularProgressBar.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


    public class HairStyleListJsontask extends AsyncTask<String, Void, ArrayList<String>> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* HairStyleListJsontask IS RUNNING *******", "YES");
            Log.e("******* HairStyleListJsontask IS RUNNING *******", "YES");
            signupProgress.setVisibility(View.VISIBLE);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            Log.e("******* NOW BACKGROUND TASK IS RUNNING *******", "YES");
            Log.e("******* NOW BACKGROUND TASK IS RUNNING *******", "YES");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpUrlPath.urlPathlist + "hairstyle_list");

            try {
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("doinBackgrouns Main List Responce :", "" + object);
                Log.e("doinBackgrouns Main List Responce :", "" + object);
                Log.e("doinBackgrouns Main List Responce :", "" + object);

                hairstyletypelist.add("Please select");
                JSONArray jsonArray = new JSONArray(object);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    strHairStyle = jsonObject.getString("name");
                    hairstyletypelist.add(strHairStyle);
                    Log.e(" doinBackgrouns strHairStyle   name   :", "" + strHairStyle);
                    Log.e(" doinBackgrouns strHairStyle   name   :", "" + strHairStyle);
                    Log.e(" doinBackgrouns strHairStyle   name   :", "" + strHairStyle);
                }

                return hairstyletypelist;

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            signupProgress.setVisibility(View.GONE);
            if (!iserror) {

                if (result1 == null) {
                    Log.e("result1 :", "Null");
                } else if (result1.isEmpty()) {

                    Log.e("result1 :", "empty");
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, result1);
                    spinner.setItems(result1);
                    strHairStyledefault = result1.get(0);
                    Log.e("strHairStyledefault ", "" + strHairStyledefault);

                    Log.e(" hair list result :", "" + result1.size());
                    Log.e(" hair list result :", "" + result1
                            + "\n" + "item 0 :" + "" + result1.get(0)
                            + "\n" + "item 1 :" + "" + result1.get(1));
                }

            }

        }

    }


    public class SignUpJsontask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            signupProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpUrlPath.urlPathlogin + "sign_up&");
            Log.e("SignUp Parameter \n\n:", HttpUrlPath.urlPathlogin + "sign_up&"
                    + "&email=" + str_emailid + "&username=" + str_username +
                    "&password=" + str_password + "&hair_style=" + strHairStyleListSendValue +
                    "&latitude=" + Str_user_Latitude + "&longitude=" + Str_user_Longitude);


            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        7);
                nameValuePairs.add(new BasicNameValuePair("username",
                        str_username));
                nameValuePairs.add(new BasicNameValuePair("email",
                        str_emailid));
                nameValuePairs.add(new BasicNameValuePair("password",
                        str_password));
                nameValuePairs.add(new BasicNameValuePair("hair_style",
                        strHairStyleSelectedValue));
                nameValuePairs.add(new BasicNameValuePair("latitude",
                        Str_user_Latitude));
                nameValuePairs.add(new BasicNameValuePair("longitude",
                        Str_user_Longitude));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("************ object holds our data ************ :", "" + object);
                //JSONArray js = new JSONArray(object);


                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("result");
                if (result.equalsIgnoreCase("successfully")) {
                    STR_userID = jobect.getString("id");
                    STR_user_name = jobect.getString("username");
                    STR_user_email = jobect.getString("email");
                    STR_user_hair_style = jobect.getString("hair_style");
                    STR_user_latitude = jobect.getString("latitude");
                    STR_user_longitude = jobect.getString("longitude");

                } else {
                    if (result.equalsIgnoreCase("unsuccessfully")) {
                        error = jobect.getString("error");
                    }
                }
            } catch (Exception e) {
                Log.e(" ******** Exception **********", "************ Exception ************" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);

            //if (!iserror == false)
            if (!iserror) {
                if (result.equalsIgnoreCase("successfully")) {
                    signupProgress.setVisibility(View.GONE);

                    /*Appconstant.editor.putString("ID", STR_userID);
                    Appconstant.editor.putString("username", STR_user_name);
                    Appconstant.editor.putString("email", STR_user_email);
                    Appconstant.editor.putString("hair_style", STR_user_hair_style);
                    Appconstant.editor.putString("latitude", STR_user_latitude);
                    Appconstant.editor.putString("longitude", STR_user_longitude);
                    Appconstant.editor.commit();
*/

                    Log.e("STR_userID :", "" + STR_userID);
                    Log.e("STR_user_name :", "" + STR_user_name);
                    Log.e("STR_user_email :", "" + STR_user_email);
                    Log.e("STR_user_hair_style :", "" + STR_user_hair_style);
                    Log.e("STR_user_latitude :", "" + STR_user_latitude);
                    Log.e("STR_user_longitude :", "" + STR_user_longitude);

                    Intent GoLoginScreen = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(GoLoginScreen);

                    Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_SHORT).show();

                } else if (result.equalsIgnoreCase("unsuccessfully")){
                    signupProgress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
                }
            } else {
                signupProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Oops!! Please check server connection .",
                        Toast.LENGTH_SHORT).show();
//
            }
        }

    }

}
