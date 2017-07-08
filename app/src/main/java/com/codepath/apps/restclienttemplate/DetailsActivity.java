package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_heart;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_heart_stroke;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_retweet;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_retweet_stroke;

public class DetailsActivity extends AppCompatActivity {

    // instance fields
    AsyncHttpClient clientele;
    // the tweet to display
    Tweet tweet;
    // twitter client
    TwitterClient client;

    @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @Nullable@BindView(R.id.ivImage) ImageView ivImage;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.tvDate) TextView tvDate;
    @BindView(R.id.tvRetweets) TextView tvRetweets;
    @BindView(R.id.tvLikes) TextView tvLikes;
    @BindView(R.id.ibComment) ImageButton ibComment;
    @BindView(R.id.ibRetweet) ImageButton ibRetweet;
    @BindView(R.id.ibLike) ImageButton ibLike;
    @BindView(R.id.ibMessage) ImageButton imMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        // initialize the client
        clientele = new AsyncHttpClient();
        client = TwitterApp.getRestClient();

        // unwrap the movie passed in the intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("DetailsActivity", String.format("Showing details for %s", tweet.body));


        int favorite = (tweet.favorited) ? ic_vector_heart : ic_vector_heart_stroke;
        int retweet = (tweet.retweeted) ? ic_vector_retweet : ic_vector_retweet_stroke;

        final String favString = (tweet.favorites.equals("0")) ? "" : tweet.favorites;
        final String retString = (tweet.retweets.equals("0")) ? "" : tweet.retweets;

        // set the title and overview
        tvBody.setText(tweet.body);
        tvDate.setText(tweet.createdAt);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvUserName.setText(tweet.user.name);
        tvRetweets.setText(tweet.retweets + " Retweets");
        tvLikes.setText(tweet.favorites + " Likes");
        ibLike.setImageResource(favorite);
        ibRetweet.setImageResource(retweet);

        // ivTrailer -- set the backdrop image
        String imageUrl = tweet.user.profileImageUrl;
        String url = tweet.imageUrl;


        Glide.with(getApplicationContext())
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 150, 0))
                .into(ivProfileImage);

        Glide.with(getApplicationContext())
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 25, 0))
                .into(ivImage);


        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.favorited) {
                    client.unlikeTweet(tweet.uid, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // set boolean to false
                            tweet.favorited = false;
                            // set the new image
                            ibLike.setImageResource(ic_vector_heart_stroke);
                            // set the new favorites count
                            tweet.favorites = Integer.toString(Integer.parseInt(tweet.favorites) - 1);
                            // set the new text
                            tvLikes.setText(tweet.favorites + " Likes");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("TwitterClient", responseString);
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }
                    });





                } else {
                    client.likeTweet(tweet.uid, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // set boolean to true
                            tweet.favorited = true;
                            // set the new image
                            ibLike.setImageResource(ic_vector_heart);
                            // set the new favorites count
                            tweet.favorites = Integer.toString(Integer.parseInt(tweet.favorites) + 1);
                            // set the new text
                            tvLikes.setText(tweet.favorites + " Likes");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("TwitterClient", responseString);
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }
                    });


                }
            }
        });
    }

}
