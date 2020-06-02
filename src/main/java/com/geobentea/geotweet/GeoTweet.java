package com.geobentea.geotweet;

public class GeoTweet {

	public static void main(String[] args) throws InterruptedException {
		TweetReader treader = new TweetReader();
		
		treader.start();
	}

}
