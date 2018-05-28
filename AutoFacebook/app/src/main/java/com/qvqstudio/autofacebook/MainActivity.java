package com.qvqstudio.autofacebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvProfileInfo;
    private EditText etContent;
    private Button btnPost;

    private SessionData mSessionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvProfileInfo = findViewById(R.id.tv_profile_info);
        etContent = findViewById(R.id.et_post_content);
        btnPost = findViewById(R.id.btn_post);

        mSessionData = new SessionData(this);
        // Get profile info

        btnPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_post:
                postNewFeed();
                break;
        }
    }

    private void postNewFeed(){
        String token = mSessionData.getObjectAsString(SessionData.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            /* make the API call */
            Bundle params = new Bundle();
            params.putString("message", "This is a test message");
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/feed",
                    params,
                    HttpMethod.POST,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            /* handle the result */
                            Log.e("TAG", "Result = "+ response.toString());
                        }
                    }
            ).executeAsync();
        } else {
            Log.e("TAG", "Token is null #");
        }

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
}
