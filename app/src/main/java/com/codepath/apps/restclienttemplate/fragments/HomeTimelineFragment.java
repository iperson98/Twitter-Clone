package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by amade002 on 7/3/17.
 */

public class HomeTimelineFragment extends TweetsListFragment {

    TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        populateTimeline();
    }

    @Override
    public void populateTimeline() {
        // when we are populating the timeline we want the progress bar to show
        //showProgressBar();
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //   Log.d("TwitterClient", response.toString());

                // clear the items
                tweetAdapter.clear();

                // add new items
                addItems(response);

                // refresh
                swipeContainer.setRefreshing(false);

                // once we finish the progress bar can go away
                //hideProgressBar();
            }
        });
    }
    public void appendTweet(Tweet tweet) {
        // add a tweet
        tweets.add(0, tweet);
        // inserted at position 0
        tweetAdapter.notifyItemInserted(0);
        // do work
        rvTweets.scrollToPosition(0);
    }

}

