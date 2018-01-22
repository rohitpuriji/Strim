package com.mystrimz.android.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mystrimz.android.R;
import com.mystrimz.android.bean.RegisterBean;
import com.mystrimz.android.http.apicallback.UserCallBack;
import com.mystrimz.android.http.requests.UserRequest;
import com.mystrimz.android.util.AppSharedPrefrences;
import com.mystrimz.android.util.Constants;
import com.mystrimz.android.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    private ImageView facebookLogin;
    private ImageView googleLogin;
    private ImageView login;
    private ImageView signUp;
    private GoogleApiClient mGoogleApiClient;
    private static final List<String> permissionNeeds = Arrays.asList("public_profile", "email", "user_birthday");
    private static CallbackManager callbackManager;
    private JSONObject facebookJson = null;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private ProgressDialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        
        init();
        onClickListener();
    }

    private void onClickListener() {
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);
        facebookLogin.setOnClickListener(this);
        googleLogin.setOnClickListener(this);
    }

    private void init() {
        signUp = (ImageView)findViewById(R.id.singUp);
        login = (ImageView)findViewById(R.id.login);
        facebookLogin = (ImageView)findViewById(R.id.facebookLogin);
        googleLogin = (ImageView)findViewById(R.id.googleLogin);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        if (mGoogleApiClient == null) {
            // Configure sign-in to request the user's ID, email address, and basic profile. ID and
            // basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addConnectionCallbacks(this)
                    .build();
        }
        myDialog = Utility.showProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.singUp:
                if (Utility.getInstance().isOnline(this)) {
                    callIntent(SignUpActivity.class);
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.login:
                if (Utility.getInstance().isOnline(this)) {
                    callIntent(LoginActivity.class);
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.facebookLogin:
                if (Utility.getInstance().isOnline(this)) {
                    callFacebookLogin();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.googleLogin:
                if (Utility.getInstance().isOnline(this)) {
                    callGoogleLogin();
                }else {
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * login with google account
     */
    private void callGoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * login with facebook account
     */
    private void callFacebookLogin() {

        if (AccessToken.getCurrentAccessToken() == null) {
            System.out.println("prezerocheck");

            LoginManager.getInstance().logInWithReadPermissions(WelcomeActivity.this, permissionNeeds);
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    getUserInfo(loginResult.getAccessToken());

                }

                @Override
                public void onCancel() {
                    // App code
                    Toast.makeText(WelcomeActivity.this, "login cancelled by user!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    exception.printStackTrace();
                    Toast.makeText(WelcomeActivity.this, "Login unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {

            // if user is already logged in previously
            System.out.println("prezerocheck2");
            loginViaFacebookWithCurrentAccessToken();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("result success", "handle result:" + result.isSuccess());
        String imagePath = "";

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct.getPhotoUrl() != null) {
                imagePath = acct.getPhotoUrl().toString();
                Log.i("Image", imagePath);
            }

            String social_token = String.valueOf(result.getSignInAccount());
            checkUserExistOrNot(acct.getId(), "google", acct.getGivenName(), acct.getFamilyName(), acct.getEmail(), acct,imagePath, social_token);

        } else {
            Toast.makeText(WelcomeActivity.this, "Login unsuccessful!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * if user already logged in
     */
    private void loginViaFacebookWithCurrentAccessToken() {


        if (AccessToken.getCurrentAccessToken() != null) {

            final String access = AccessToken.getCurrentAccessToken().toString();
            System.out.println("Access token loginViaFacebookWithCurrentAccessToken.." + access);
            getUserInfo(AccessToken.getCurrentAccessToken());
        }
    }

    /**
     * Getting the user information
     * from facebook
     *
     * @param currentAccessToken
     */
    private void getUserInfo(final AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                       // Log.e("response", object.toString());
                        facebookJson = object;

                        try {
                            String email, firstName, lastName, image_path;
                            // check if email exist
                            if (object.has("email")) {
                                email = object.getString("email");
                            } else {
                                email = null;
                            }

                            // get firstName, lastname and image path
                            firstName = object.getString("first_name");
                            lastName = object.getString("last_name");
                            try {
                                image_path = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                Log.i("Image", image_path);
                            } catch (Exception ex) {
                                image_path = "";
                            }
                            //Session session = Session.getActiveSession();

                            String social_token = AccessToken.getCurrentAccessToken().getToken();
                            checkUserExistOrNot(facebookJson.getString("id"), "facebook", firstName, lastName, email, null, image_path, social_token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,name,link,email,gender,picture,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkUserExistOrNot(String id, String resigtrationtype, String firstName,
                                     String lastName, String email, Object o, String image_path, String token) {

        String devicetype = Constants.DEVICE_TYPE_ANDROID;
        String devicetoken = FirebaseInstanceId.getInstance().getToken();

        myDialog.show();

        Log.e("details", devicetoken);
        UserRequest userRequest = UserRequest.getInstance();
        userRequest.callSocialApi(firstName, lastName, devicetype, devicetoken, resigtrationtype,
                id, token, email, image_path, new UserCallBack() {
            @Override
            public void onSuccess(RegisterBean baseBean) {
                myDialog.dismiss();
                saveUserDetails(baseBean);
            }

                    @Override
            public void onError(String error) {
                myDialog.dismiss();
                Toast.makeText(WelcomeActivity.this, error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveUserDetails(RegisterBean baseBean) {

        AppSharedPrefrences appSharedPrefrences = AppSharedPrefrences.getInstance(this);
        String name = baseBean.getFirstname()+" " + baseBean.getLastname();
        appSharedPrefrences.setPreference(Constants.KEY_USER_NAME, name);
        appSharedPrefrences.setPreference(Constants.KEY_USER_PROFILE, baseBean.getImage());
        appSharedPrefrences.setPreference(Constants.KEY_ACCESS_TOKEN,baseBean.getAccessToken());
        appSharedPrefrences.setPreference(Constants.KEY_EMAIL, baseBean.getEmail());
        Intent intent = new Intent(WelcomeActivity.this, DiscoverBaseActivity.class);
        startActivity(intent);

    }

    /**
     * go to the login and signup screen
     * @param activity
     */
    private void callIntent(Class activity) {
        Intent intent = new Intent(WelcomeActivity.this, activity);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        if (intent != null) {
            signOut();
        }
        else {
            Log.e(TAG, "Google account is not logged in");
        }
    }

    private void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
