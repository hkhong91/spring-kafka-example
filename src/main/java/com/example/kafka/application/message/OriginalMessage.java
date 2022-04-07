package com.example.kafka.application.message;

import com.example.kafka.domain.entity.Original;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OriginalMessage {

  private long number;

  public Original toEntity() {
    return Original.builder()
        .number(this.number)
        .build();
  }
}
