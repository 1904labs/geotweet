package com.geobentea.geotweet;

import com.geobentea.geotweet.data.Tweet;
import com.google.gson.Gson;

public class NullTweetLogger implements LoggableTweet {

	private Gson gson;

	public NullTweetLogger() {
		gson = new Gson();
	}
	
	@Override
	public void log(String tweetstr) throws LogException {
		// Do nothing but write to the console and throw the tweet on the floor. 
	    Tweet tweet = gson.fromJson(tweetstr, Tweet.class);
	    System.out.println("NullTweetLogger: " + tweet.toString());
	}

}
