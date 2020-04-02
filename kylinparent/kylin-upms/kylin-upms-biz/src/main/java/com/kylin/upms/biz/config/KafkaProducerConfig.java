package com.kylin.upms.biz.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import java.util.LinkedHashMap;
import java.util.Map;
@Configuration
 @EnableKafka
 public class KafkaProducerConfig {
     @Value("${kafka.brokers}")
     private String brokers;
     public Map producerConfigs() {
         Map props = new LinkedHashMap<>();
         props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
         props.put(ProducerConfig.RETRIES_CONFIG, 0);
         props.put(ProducerConfig.BATCH_SIZE_CONFIG, 4096);
         props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
         props.put(ProducerConfig.BATCH_SIZE_CONFIG, 40960);
         props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
         props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
         return props;
     }
     public ProducerFactory producerFactory() {
         return new DefaultKafkaProducerFactory<>(producerConfigs());
     }
     @Bean
     public KafkaTemplate kafkaTemplate() {
         return new KafkaTemplate<>(producerFactory());
     }
 }