package com.qvqstudio.autofacebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    private TextView tvStatus;
    private Button btnLoadData;
    private ProgressBar progressBar;
    private SessionData mSessionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                updateProfileView(newProfile);
            }
        };
        profileTracker.startTracking();

        setContentView(R.layout.activity_login);


        mSessionData = new SessionData(this);
        progressBar = findViewById(R.id.pbLoading);
        loginButton = findViewById(R.id.loginButton);
        tvStatus = findViewById(R.id.tv_status);


        if (isLoggedIn()) {
            Profile profile = Profile.getCurrentProfile();
            Log.d("TAG", profile.getFirstName());
            AccessToken token = AccessToken.getCurrentAccessToken();
            mSessionData.setObjectAsString(SessionData.TOKEN, token.getToken());
            goToMainScreen();
        }

        /*try {
            Profile profile = Profile.getCurrentProfile();
            updateProfileView(profile);
        } catch (NullPointerException e) {
            tvStatus.setText("Status: ready");
        }*/
        setCallbackManager();
    }

    private void getProfile(LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());

                        // Application code
                        try {
                            Log.d("tttttt",object.getString("id"));
                            String birthday="";
                            if(object.has("birthday")){
                                birthday = object.getString("birthday"); // 01/31/1980 format
                            }

                            String fnm = object.getString("first_name");
                            String lnm = object.getString("last_name");
                            String mail = object.getString("email");
                            String gender = object.getString("gender");
                            String fid = object.getString("id");

                            String result = "Name: "+fnm+" "+lnm+" \n"+"Email: "+mail+" \n"+"Gender: "+gender+" \n"+"ID: "+fid+" \n"+"Birth Date: "+birthday;
                            Log.d("INFO", "profile info = "+ result);
                            //https://graph.facebook.com/143990709444026/picture?type=large
                            Log.d("aswwww","https://graph.facebook.com/"+fid+"/picture?type=large");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void setCallbackManager() {

        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("publish_actions, public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    AccessToken token = loginResult.getAccessToken();
                    Toast.makeText(LoginActivity.this, String.format("User ID: %s\n Auth Token: %s",
                            token.getUserId(), token.getToken()), Toast.LENGTH_SHORT).show();

                    mSessionData.setObjectAsString(SessionData.TOKEN, token.getToken());
                    getProfile(loginResult);
                }

                @Override
                public void onCancel() {}

                @Override
                public void onError(FacebookException e) {
                    Log.d("TAG", e.toString());
                }
            });
    }


    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void logOut() {
        LoginManager.getInstance().logOut();
    }


    private void goToMainScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void onStop() {
        super.onStop();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        updateProfileView(profile);
    }

    private void updateProfileView(Profile profile) {
        if (profile != null) {
            String status = String.format("Status: logged!\nUser: %s", profile.getName());
            Log.d("DBG", profile.getProfilePictureUri(200,200).toString());
            tvStatus.setText(status);
        } else {
            tvStatus.setText("Status: ready");
        }
    }
}
