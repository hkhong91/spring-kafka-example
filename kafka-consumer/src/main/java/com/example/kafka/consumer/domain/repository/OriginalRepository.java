package com.example.kafka.consumer.domain.repository;

import com.example.kafka.consumer.domain.entity.Original;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OriginalRepository extends JpaRepository<Original, Long> {
}
