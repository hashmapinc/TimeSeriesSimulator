### start zookeeper
if you have installed zookeeper, start it, or
run the command:
``` sh
bin/zookeeper-server-start.sh config/zookeeper.properties
```

### start kafka with default configuration
``` sh
> bin/kafka-server-start.sh config/server.properties
```

### create a topic
``` sh
> bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 10 --topic test_topic
```

### Build the code
``` sh
> mvn clean install
```

### Run the Kafka Producer
``` sh
> mvn clean install
```

### Run the Console Consumer
``` sh
> ./kafka-console-consumer.sh --zookeeper localhost:2181 --topic test_topic
```

### The simulator
Run the KafkaProducer class with the arguments basicConfig.json {topic_name} {kafka broker}
``` sh
> 
```