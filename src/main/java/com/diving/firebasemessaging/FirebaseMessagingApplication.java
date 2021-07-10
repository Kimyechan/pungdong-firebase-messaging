package com.diving.firebasemessaging;

import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

@SpringBootApplication
public class FirebaseMessagingApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:database.yml,"
            + "classpath:kafka.yml,"
            + "/home/ubuntu/config/project/pungdong/database.yml,"
            + "/home/ubuntu/config/project/pungdong/kafka.yml,"
            + "/home/ubuntu/config/project/pungdong/eureka.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(FirebaseMessagingApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
