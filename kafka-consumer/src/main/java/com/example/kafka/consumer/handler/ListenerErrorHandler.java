package com.example.kafka.consumer.handler;

import com.example.kafka.module.constant.KafkaHandlerName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;

@Configuration
@Slf4j
public class ListenerErrorHandler {

  @Bean(KafkaHandlerName.DEFAULT)
  public KafkaListenerErrorHandler defaultKafkaListenerErrorHandler() {
    return (message, exception) -> {
      log.error(exception.getMessage(), exception);
      return message;
    };
  }
}
