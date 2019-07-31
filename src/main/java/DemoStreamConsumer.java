import io.confluent.connect.replicator.offsets.ConsumerTimestampsCommitter;
import io.confluent.connect.replicator.offsets.ConsumerTimestampsInterceptor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class DemoStreamConsumer {
    public static void main(final String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ritchie-offset-stream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "<BOOTSTRAP-URL>");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        props.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"<API_KEY>\" password=\"<SECRET>\";");
        props.put("security.protocol","SASL_SSL");
        props.put("sasl.mechanism","PLAIN");
        props.put(StreamsConfig.mainConsumerPrefix(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG), ConsumerTimestampsInterceptor.class.getName());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream("<TOPIC_NAME>");
        textLines.mapValues( value -> {
            System.out.println(value);
            return value;
        });
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                streams.close();
            }
        }));
    }
}
