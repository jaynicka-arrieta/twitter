package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private static List<Tweet> mTweets;
    static Context context;
    //pass in tweets arr into constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    //for each row, inflate layout and pass into ViewHolder class
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    //bind the values based on the position of the element
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get data according to position
        Tweet tweet = mTweets.get(position);
        String time = getRelativeTimeAgo(tweet.createdAt);

        //populate the views according to data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTime.setText(time);

        GlideApp.with(context)
                .load(tweet.user.profileImageUrl)
                .transform(new RoundedCornersTransformation(15, 0))
                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    //create ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvBody.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            //get item position
            int position = getAdapterPosition();
            // ensure that the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet tweet = mTweets.get(position);
                // create intent for the new activity
                Intent i = new Intent(context, DetailsActivity.class);
                // serialize the movie using the parceler, use its short name as a key
                i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // show the activity
                context.startActivity(i);
            }
        }
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

}
