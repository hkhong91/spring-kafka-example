package com.example.kafka.batch.job;

import com.example.kafka.batch.domain.entity.Original;
import com.example.kafka.batch.domain.repository.OriginalRepository;
import com.example.kafka.module.constant.KafkaTopic;
import com.example.kafka.module.message.OriginalMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OriginalDlqJobConfig {

  private static final int CHUNK_SIZE = 10;

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final OriginalRepository originalRepository;
  private final KafkaProperties kafkaProperties;

  @Bean
  public Job originalDlqJob() {
    return jobBuilderFactory.get("originalDlqJob")
        .start(originalDlqStep())
        .incrementer(new RunIdIncrementer())
        .build();
  }

  @Bean
  public Step originalDlqStep() {
    return stepBuilderFactory.get("originalDlqStep")
        .<OriginalMessage, Original>chunk(CHUNK_SIZE)
        .reader(this.originalDlqReader())
        .processor(this.originalProcessor())
        .writer(this.originalWriter())
        .faultTolerant()
        .retryLimit(3)
        .retry(TimeoutException.class)
        .build();
  }

  public KafkaItemReader<String, OriginalMessage> originalDlqReader() {
    Properties properties = new Properties();
    properties.putAll(kafkaProperties.buildConsumerProperties());
    return new KafkaItemReaderBuilder<String, OriginalMessage>()
        .consumerProperties(properties)
        .partitions(0, 1, 2)
        .name("originalDlqReader")
        .saveState(true)
        .pollTimeout(Duration.ofSeconds(1))
        .topic(KafkaTopic.ORIGINAL_DEAD)
        .build();
  }

  public ItemProcessor<OriginalMessage, Original> originalProcessor() {
    return Original::of;
  }

  public RepositoryItemWriter<Original> originalWriter() {
    return new RepositoryItemWriterBuilder<Original>()
        .repository(this.originalRepository)
        .build();
  }
}
