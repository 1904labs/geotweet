package com.geobentea.geotweet;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaTweetLogger implements LoggableTweet {

	private Producer<Long, String> producer = null;

	/**
	 * Public no argument constructor 
	 */
	public KafkaTweetLogger() {
		// Default Kafka Setup 
		producer  = initializeProducer();
	}
	
	@Override
	public void log(String tweet) throws LogException {
		ProducerRecord<Long, String> record = new ProducerRecord<>(System.getProperty("KAFKA_TOPIC"), null, tweet);
		producer.send(record, new Callback() {

			@Override
			public void onCompletion(RecordMetadata metadata, Exception exception) {
		        if (exception == null) {
		            System.out.printf("Message with offset %d acknowledged by partition %d\n",
		                    metadata.offset(), metadata.partition());
		        } else {
		            System.out.println(exception.getMessage());
		        }				
			}
			
		});
	}

	private Producer<Long, String> initializeProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("KAFKA_SERVERS"));
        properties.put(ProducerConfig.ACKS_CONFIG, System.getProperty("KAFKA_ACKS_CONFIG"));
        properties.put(ProducerConfig.LINGER_MS_CONFIG, Integer.valueOf(System.getProperty("KAFKA_LINGER_MS_CONFIG")));
        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.valueOf(System.getProperty("KAFKA_RETRIES_CONFIG")));
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 35000);

        // @TODO: Can the serializers be configurable?
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<>(properties);
    }

}
