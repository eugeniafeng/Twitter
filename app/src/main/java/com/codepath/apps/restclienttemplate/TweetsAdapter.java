package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.w3c.dom.Text;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweets;

    // Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTweetBinding binding = ItemTweetBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemTweetBinding binding;

        public ViewHolder(@NonNull ItemTweetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Tweet tweet) {
            binding.tvBody.setText(tweet.body);
            // bold the user's name, but not the screen name
            binding.tvScreenName.setText(Html.fromHtml("<b>" + tweet.user.name + "</b> @" + tweet.user.screenName));
            binding.tvRelativeTimestamp.setText(tweet.relativeTimestamp);
            Glide.with(context).load(tweet.user.profileImageUrl).into(binding.ivProfileImage);

            // Load embedded media if present
            if (tweet.mediaUrl != null) {
                binding.ivEmbed.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.mediaUrl).into(binding.ivEmbed);
            } else {
                binding.ivEmbed.setVisibility(View.GONE);
            }
        }
    }

}
