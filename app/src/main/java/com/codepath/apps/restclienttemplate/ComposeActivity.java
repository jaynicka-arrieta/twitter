package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    TextView tvChar;
    EditText etCompose;
    Button btSubmit;

    TwitterClient client;
    ArrayList<Tweet> tweets;
    TweetAdapter tweetAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        //init the arraylist (data source)
        tweets = new ArrayList<>();
        //construct adapter from data source
        tweetAdapter = new TweetAdapter(tweets);

        client = TwitterApplication.getRestClient(this);
        etCompose = (EditText) findViewById(R.id.etCompose);
        btSubmit = (Button) findViewById(R.id.btSubmit);
        tvChar = (TextView) findViewById(R.id.tvChar);

        editTextCounter();
        sendToTimeline();

    }

    private void sendToTimeline() {
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etCompose.getText().toString();
                String charCount = tvChar.getText().toString();
                boolean valid = false;
                for (int i = 0; i < charCount.length(); i++) {
                    if (charCount.contains("-")) {
                        valid = false;
                        Toast toast = Toast.makeText(getApplicationContext(), "Character count too high", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        TextView text = (TextView) view.findViewById(android.R.id.message);
                        text.setTextColor(Color.WHITE);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    } else {
                        valid = true;
                    }
                }
                if (valid) {
                    client.sendTweet(message, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            Tweet tweet = new Tweet();
                            tweets.add(tweet);
                            tweetAdapter.notifyItemInserted(tweets.size()-1);

                            Intent i = new Intent(getApplicationContext(), TimelineActivity.class);
                            Toast toast = Toast.makeText(getApplicationContext(), "Successfully tweeted", Toast.LENGTH_SHORT);
                            View view = toast.getView();
                            TextView text = (TextView) view.findViewById(android.R.id.message);
                            text.setTextColor(Color.WHITE);
                            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
                            toast.show();
                            startActivity(i);
                        }
                    });
                }

            }
        });
    }

    private void editTextCounter() {
        etCompose.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String charCount = tvChar.getText().toString();
                boolean tooHigh = false;
                for (int i = 0; i < charCount.length(); i++) {
                    if (charCount.contains("-")) {
                        tooHigh = true;
                    } else {
                        tooHigh = false;
                    }
                }
                tvChar.setText(String.valueOf(140-etCompose.length()));
                if (tooHigh) {
                    tvChar.setTextColor(Color.RED);
                } else {
                    tvChar.setTextColor(Color.DKGRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
