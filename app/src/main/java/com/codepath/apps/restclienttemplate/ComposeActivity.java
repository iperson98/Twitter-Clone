package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;


public class ComposeActivity extends AppCompatActivity {

    // instance variables
    TwitterClient client;
    EditText editTweet;
    TextView tvCharacterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // set the client
        client = TwitterApp.getRestClient();
        editTweet = (EditText) findViewById(R.id.et_simple);
        tvCharacterCount = (TextView) findViewById(R.id.tvCharacterCount);

        updateCharacterCount();

    }

    public void tweetButton(View v) {
        // get the text from the edit text
        String message = editTweet.getText().toString();

        client.sendTweet(message, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Tweet tweet = Tweet.fromJSON(response);
                            Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                            intent.putExtra(Tweet.class.getName(), Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Failed to send tweet", "wow");
                    }
                }

        );

    }

    public void updateCharacterCount() {
        final TextWatcher textCounter = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int max_count = s.length();
                int counter = 140 - max_count;
                if (counter >= 0)
                    tvCharacterCount.setTextColor(Color.BLACK);
                else if (count < 0)
                    tvCharacterCount.setTextColor(Color.RED);



                //This sets a textview to the current length
                tvCharacterCount.setText(String.valueOf(counter));
            }

            public void afterTextChanged(Editable s) {
            }
        };

        editTweet.addTextChangedListener(textCounter);



    }

}



