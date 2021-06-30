package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 280;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityComposeBinding binding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        client = TwitterApplication.getRestClient(this);

        // Set click listener on button
        binding.btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String tweetContent = binding.etCompose.getText().toString();
            if (tweetContent.isEmpty()) {
                Snackbar.make(binding.rlCompose, R.string.empty_tweet, Snackbar.LENGTH_LONG).show();
                return;
            }
            if (tweetContent.length() > MAX_TWEET_LENGTH) {
                Snackbar.make(binding.rlCompose, R.string.long_tweet, Snackbar.LENGTH_LONG).show();
                return;
            }
            // Make an API call to Twitter to publish the tweet
            client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess to publish tweet");
                    try {
                        Tweet tweet = Tweet.fromJson(json.jsonObject);
                        Log.i(TAG, "Published tweet says: " + tweet.body);
                        Intent intent = new Intent();
                        intent.putExtra("tweet", Parcels.wrap(tweet));
                        // set result code and bundle data for response
                        setResult(RESULT_OK, intent);
                        // closes the activity, pass data to parent
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure to publish tweet", throwable);
                }
            });

            }
        });
    }
}