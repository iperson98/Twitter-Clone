package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity {

    TweetsListFragment fragmentTweetsList;
    //public SwipeRefreshLayout swipeContainer;
    MenuItem miActionProgressItem;
    static final int REQUEST_CODE = 20;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timelinemenu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        //populateTimeline();
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                composeMessage();
                return true;
            case R.id.miProfile:
                showProfileView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra(Tweet.class.getName()));
            fragmentTweetsList.tweets.add(0, tweet);
            fragmentTweetsList.tweetAdapter.notifyItemInserted(0);
            fragmentTweetsList.rvTweets.scrollToPosition(0);
        }
    }

    private void composeMessage() {
        // create an intent for the new activity
        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
        //
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void showProfileView() {
        Toast.makeText(this, "TODO: ProfileView", Toast.LENGTH_SHORT).show();

    }


}
