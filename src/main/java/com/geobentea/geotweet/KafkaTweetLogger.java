package com.geobentea.geotweet;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.locationtech.geomesa.kafka.data.KafkaDataStore;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.geobentea.geotweet.data.Tweet;
import com.google.gson.Gson;

public class KafkaTweetLogger implements LoggableTweet {

	// private Producer<Long, String> producer = null;
	private DataStore dataStore;
	private Gson gson;
	private SimpleFeatureType featureType;

	/**
	 * Public no argument constructor 
	 */
	public KafkaTweetLogger() {
		// Default Kafka Setup 
		// producer  = initializeProducer();
		try {
			dataStore = initializeDataStore();
			featureType = createSchemaAndReturnType();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		gson = new Gson();
	}
	
	@Override
	public void log(String tweetstr) throws LogException {
//		ProducerRecord<Long, String> record = new ProducerRecord<>(System.getProperty("KAFKA_TOPIC"), null, tweetstr);
		Tweet tweetobj = gson.fromJson(tweetstr, Tweet.class);
		
		if (tweetobj.getGeo() != null) {
			try {
				FeatureWriter<SimpleFeatureType,SimpleFeature> featureWriter = dataStore.getFeatureWriterAppend(featureType.getTypeName(), Transaction.AUTO_COMMIT);

		    
				SimpleFeature next = featureWriter.next();
				
				next.setAttribute("geom", getPoint(tweetobj));
				next.setAttribute("text", tweetobj.getText());
				next.setAttribute("user", tweetobj.getUser().getName());
				next.setAttribute("place", tweetobj.getPlace().getName());
				next.setAttribute("tweet_id", tweetobj.getId());

				featureWriter.write();
				
				featureWriter.close();
				
				System.out.println("Writing twitter message to kafka: " + tweetobj);
//				producer.send(record, new Callback() {
	//	
//					@Override
//					public void onCompletion(RecordMetadata metadata, Exception exception) {
//				        if (exception == null) {
//				            System.out.printf("Message with offset %d acknowledged by partition %d\n",
//				                    metadata.offset(), metadata.partition());
//				        } else {
//				            System.out.println(exception.getMessage());
//				        }				
//					}
//					
//				});
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}

//	private Producer<Long, String> initializeProducer() {
//        Properties properties = new Properties();
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("KAFKA_SERVERS"));
//        properties.put(ProducerConfig.ACKS_CONFIG, System.getProperty("KAFKA_ACKS_CONFIG"));
//        properties.put(ProducerConfig.LINGER_MS_CONFIG, Integer.valueOf(System.getProperty("KAFKA_LINGER_MS_CONFIG")));
//        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.valueOf(System.getProperty("KAFKA_RETRIES_CONFIG")));
//        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1);
//        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 35000);
//
//        // @TODO: Can the serializers be configurable?
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//
//        return new KafkaProducer<>(properties);
//    }

	private DataStore initializeDataStore() throws IOException {
        Map<String, Serializable> parameters = new HashMap<String, Serializable>();
        
        parameters.put("kafka.zookeepers", "127.0.0.1:2181");
        parameters.put("kafka.brokers", "127.0.0.1:9092");
        parameters.put("kafka.consumer.count", "0");
        
        KafkaDataStore dataStore = (KafkaDataStore) DataStoreFinder.getDataStore(parameters);	
        
        if (dataStore == null) {
        	throw new RuntimeException("Datastore not found.");
        }
        return dataStore;
	}
	
	private SimpleFeatureType createSchemaAndReturnType() throws IOException {
		SimpleFeatureType feature = SimpleFeatureTypes.createType("Twitter1", "geom:Point:srid=4326, text:String, user:String, place:String, tweet_id:String");

		dataStore.createSchema(feature);

	    return feature;
	}
	
	private Point getPoint(Tweet tweet) {
		Point point = null;
		GeometryFactory gf = new GeometryFactory();
		
		if (tweet.getGeo() != null) {
			Coordinate c = new Coordinate(tweet.getGeo().getCoordinates().get(1), tweet.getGeo().getCoordinates().get(0));
			
			point = gf.createPoint(c);			
		}
		
		return point;
	}
}
