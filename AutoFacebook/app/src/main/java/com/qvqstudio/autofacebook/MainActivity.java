package com.qvqstudio.autofacebook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.internal.ShareFeedContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvProfileInfo;
    private EditText etContent;
    private Button btnPost, btnProfile;

    private SessionData mSessionData;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvProfileInfo = findViewById(R.id.tv_profile_info);
        etContent = findViewById(R.id.et_post_content);
        btnPost = findViewById(R.id.btn_post);
        btnProfile = findViewById(R.id.btn_get_profile);

        mSessionData = new SessionData(this);
        // Get profile info

        btnPost.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_post:
                postNewFeed();
                break;

            case R.id.btn_get_profile:
                getProfile();
                break;
        }
    }

    private void getProfile(){
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.e(TAG, "res = "+ response.toString());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void postNewFeed(){

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Hello Guys")
                    .setContentDescription(
                            "Coder who learned and share")
                    .setContentUrl(Uri.parse("http://instinctcoder.com"))
                    .setImageUrl(Uri.parse("https://scontent-sin1-1.xx.fbcdn.net/hphotos-xap1/v/t1.0-9/12936641_845624472216348_1810921572759298872_n.jpg?oh=72421b8fa60d05e68c6fedbb824adfbf&oe=577949AA"))
                    .build();

            ShareDialog.show(this, linkContent);
        }


        if(hasPublishPermission()){

            ShareFeedContent shareFeedContent = new ShareFeedContent.Builder()
                    .setLinkName("link name example")
                    .setLink("www.google.com")
                    .setLinkCaption("Google")
                    .build();


            ShareDialog.show(this, shareFeedContent);

            /*Bundle params = new Bundle();
            params.putString("message", "This is a test message");
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/feed",
                    params,
                    HttpMethod.POST,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            *//* handle the result *//*
                            Log.e("TAG", "Result = "+ response.toString());
                        }
                    }
            ).executeAsync();*/



        } else {
            Log.e("TAG", "Token is null #");
        }

    }

    //checking for publish permissions
    private boolean hasPublishPermission() {
        return AccessToken.isCurrentAccessTokenActive()
                && AccessToken.getCurrentAccessToken().getPermissions().contains("publish_actions");
    }

    private void readPost(String postId){
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + postId,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                    }
                }
        ).executeAsync();
    }

    private void upPhoto2Facebook(AccessToken accessToken){
        Bitmap bitmap = null;
        if(bitmap == null){
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Bundle params = new Bundle();
        params.putString("caption", "Test upload image");
        params.putByteArray("object_attachment", byteArray);

        GraphRequest request = new GraphRequest(accessToken, "/me/photos", params, HttpMethod.POST, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                Log.d("Facebook debug", graphResponse.toString());
                if (graphResponse.getError() == null){
                    Toast.makeText(MainActivity.this, "Success!",Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText(MainActivity.this, graphResponse.getError().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.executeAsync();

    }
}
