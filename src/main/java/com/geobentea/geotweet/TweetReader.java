package com.geobentea.geotweet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.core.endpoint.Location.Coordinate;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TweetReader {

	Client client = null;
	private LoggableTweet[] loggableTweeters;
	
	static {
		Exception ex = null;
		FileInputStream propsinput;
		final Properties props = new Properties();
		try {
			propsinput = new FileInputStream(new File("geotweet.properties"));
			
			props.load(propsinput);
			System.getProperties().putAll(props);
		} catch (FileNotFoundException e) {
			ex = e;
		} catch (IOException e) {
			ex = e;
		} finally {
			if (ex != null) throw new RuntimeException(ex);
		}				
	}
	
	public TweetReader() {
		this(new LoggableTweet[] {new NullTweetLogger(), new KafkaTweetLogger()});
	}
	
	/**
	 * @TODO: allow overriding the key and token file locations.
	 * @TODO: add a standard properties file for the application
	 */
	public TweetReader(LoggableTweet[] tweeters) {
		this.loggableTweeters = tweeters;
		
		Runnable runnable = () -> {
			shutdown();
		};
		
		Runtime.getRuntime().addShutdownHook(new Thread(runnable));
	}
	
	public void start() throws InterruptedException {
		int errorCount = 0;
	    BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
	    StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
	    // add some track terms
	    endpoint.trackTerms(Lists.newArrayList("#weather", "#location", "#station"));

	    Location missouri = new Location(new Coordinate(-95.77, 35.99), new Coordinate(-89.77, 40.99));
	    
	    endpoint.locations(Lists.newArrayList(missouri));
	    
	    Authentication auth = new OAuth1(
	    		System.getProperty("API_KEY").toString(), 
	    		System.getProperty("API_SECRET_KEY").toString(), 
	    		System.getProperty("ACCESS_TOKEN").toString(), 
	    		System.getProperty("ACCESS_TOKEN_SECRET").toString());

	    // Create a new BasicClient. By default gzip is enabled.
	    client = new ClientBuilder()
	            .hosts(Constants.STREAM_HOST)
	            .endpoint(endpoint)
	            .authentication(auth)
	            .processor(new StringDelimitedProcessor(queue))
	            .build();

	    // Establish a connection
	    client.connect();

	    // Do whatever needs to be done with messages
	    for (int msgRead = 0; msgRead < 25; msgRead++) {
			String msg = queue.take();
			try {
				for (LoggableTweet loggableTweet : loggableTweeters) {
					  loggableTweet.log(msg);					
				}
			} catch (LogException e) {
				errorCount++;
			    e.printStackTrace();
			    if (errorCount > 5) break;
			}
	    }

		client.stop();
	}
	
	public void shutdown() {
		if (client != null) {
		    client.stop();					
		}
	}
}
