## Demo for Kafka Cross Cluster Failover

* This is a demonstration of cross cluster consumer failover using offset translation.
* This project has 2 classes for demonstrating two different types of Java consumers. 
  Democonsumer.java uses a normal Java Kafka consumer and prints out the contents of topic.
  DemoStreamConsumer.java uses a Stream Java Kafka Consumer and prints out the contents of the topic.

The most important thing to not is to use this line for normal consumers `props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, ConsumerTimestampsInterceptor.class.getName());` 
and for Streams consumers `props.put(StreamsConfig.mainConsumerPrefix(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG), ConsumerTimestampsInterceptor.class.getName());`

Streams consumers need that prefix because `group.id` is not used in Streams but `application.id` is used as consumer group.
