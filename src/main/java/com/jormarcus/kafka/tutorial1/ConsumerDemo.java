package com.jormarcus.kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemo {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getName());

        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "my-fourth-application";
        String topic = "first_topic";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // we use a deserializer b/c when consumer takes a string and serializes it to bytes and sends it Kafka,
        // Kafka sends the bytes to the consumer, who needs to take the bytes and create a string, this process
        // is known as deserialization
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // can be set with "earliest" (beginning of the topic), "latest" (only the new messages onwards),
        // or "none" (will throw an error if no offsets are saved, usually latest is used
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // subscribe consumer to our topic(s)
        // collections.singleton says makes it so we only subscribe to one topic
        consumer.subscribe(Collections.singleton(topic));

        // can subscribe to multiple topics by doing
        // consumer.subscribe(Arrays.asList("first_topic", "second_topic"));

        // poll for new data
        while(true) {
           ConsumerRecords<String, String> records =
                   consumer.poll(Duration.ofMillis(100)); //  need to use Duration from Kafka 2.0.0 onwards

            for(ConsumerRecord<String, String> record : records){
                logger.info("Key: " + record.key() + ", Value: " + record.value());
                logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
            }
        }
    }
}
