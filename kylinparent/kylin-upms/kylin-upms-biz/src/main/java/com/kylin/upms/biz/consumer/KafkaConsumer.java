package com.kylin.upms.biz.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = {"test01"})
    public void consumer(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if(kafkaMessage.isPresent()){
            System.out.println(kafkaMessage.get()+"我已接收完毕");
        }
    }

    @KafkaListener(topics = {"method"})
    public void method(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if(kafkaMessage.isPresent()){
            System.out.println(kafkaMessage.get()+"我已接收完毕");
        }
    }

    @KafkaListener(topics = {"_id"})
    public void _id(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if(kafkaMessage.isPresent()){
            System.out.println(kafkaMessage.get()+"我已接收完毕id");
        }
    }

    @KafkaListener(topics = {"createTime"})
    public void createTime(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if(kafkaMessage.isPresent()){
            System.out.println(kafkaMessage.get()+"我已接收完毕createTime");
        }
    }

    @KafkaListener(topics = {"args"})
    public void args(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if(kafkaMessage.isPresent()){
            System.out.println(kafkaMessage.get()+"我已接收完毕args");
        }
    }

    @KafkaListener(topics = {"level"})
    public void level(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if(kafkaMessage.isPresent()){
            System.out.println(kafkaMessage.get()+"我已接收完毕level");
        }
    }

    @KafkaListener(topics = {"operationUnit"})
    public void operationUnit(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if(kafkaMessage.isPresent()){
            System.out.println(kafkaMessage.get()+"我已接收完毕operationUnit");
        }
    }

    @KafkaListener(topics = {"operationType"})
    public void operationType(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        System.out.println(kafkaMessage.get()+"我已接收完毕operationType");
        if(kafkaMessage.isPresent()){
            System.out.println(kafkaMessage.get()+"我已接收完毕operationType");
        }
    }
}
