package com.example.kafka.domain.entity;

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
}
