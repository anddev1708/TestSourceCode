package com.qvqstudio.autofacebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.internal.ShareFeedContent;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private SessionData mSessionData;

    private final static String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);


        mSessionData = new SessionData(this);
        loginButton = findViewById(R.id.loginButton);

        // Init callback
        setCallbackManager();

        if(isLoggedIn())
            goToMainScreen();

    }

    private void sharePhotoToFacebook(){
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("Give me my codez or I will ... you know, do that thing you don't like!")
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareFeedContent feed = new ShareFeedContent.Builder()
                .setLinkCaption("Xin chao ca ban, ahihi")
                .setLinkName("aaaaa")
                .setLink("http://google.com")
                .setLinkDescription("dalskjdaslkdjsadklasjd")
                .build();

        ShareDialog.show(this, feed);

    }

    private void getProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());

                        // Application code
                        try {
                            Log.d("tttttt", object.getString("id"));
                            String birthday = "";
                            if (object.has("birthday")) {
                                birthday = object.getString("birthday"); // 01/31/1980 format
                            }

                            String fnm = object.getString("first_name");
                            String lnm = object.getString("last_name");
                            String mail = object.getString("email");
                            String gender = object.getString("gender");
                            String fid = object.getString("id");

                            String result = "Name: " + fnm + " " + lnm + " \n" + "Email: " + mail + " \n" + "Gender: " + gender + " \n" + "ID: " + fid + " \n" + "Birth Date: " + birthday;
                            Log.d("INFO", "profile info = " + result);
                            //https://graph.facebook.com/143990709444026/picture?type=large
                            Log.d("aswwww", "https://graph.facebook.com/" + fid + "/picture?type=large");

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
        List<String> readPermission = Arrays.asList("public_profile", "email");
        final List<String> publicPermission = Collections.singletonList("publish_actions");

        // Set login permission to get token to post message to wall
        // LoginManager.getInstance().logInWithPublishPermissions(this, publicPermission);
        loginButton.setReadPermissions(readPermission);
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

                        if (!AccessToken.getCurrentAccessToken().getPermissions().contains("publish_actions")) {
                            LoginManager.getInstance().logInWithPublishPermissions(LoginActivity.this, publicPermission);
                        }

                        mSessionData.setObjectAsString(SessionData.TOKEN, accessToken.getToken());
                        //getProfile(loginResult);
                        goToMainScreen();
                    }

                    @Override
                    public void onCancel() {
                    }

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


    private void goToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
