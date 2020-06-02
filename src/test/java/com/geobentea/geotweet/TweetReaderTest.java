package com.geobentea.geotweet;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

public class TweetReaderTest {

	@Test
	public void testTweetReaderPopulatesKeysPropertiesBasedOnExternalFiles() {
		TweetReader treader = new TweetReader();
		
		assertTrue(System.getProperties().containsKey("API_KEY"));
		assertTrue(System.getProperties().containsKey("API_SECRET_KEY"));
		assertTrue(System.getProperties().containsKey("ACCESS_TOKEN"));
		assertTrue(System.getProperties().containsKey("ACCESS_TOKEN_SECRET"));
		
	}

}
