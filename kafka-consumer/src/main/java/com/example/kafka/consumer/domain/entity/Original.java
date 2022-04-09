package com.example.kafka.consumer.domain.entity;

import com.example.kafka.module.message.OriginalMessage;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Original {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private long number;

  public static Original of(OriginalMessage message) {
    return Original.builder()
        .number(message.getNumber())
        .build();
  }
}
