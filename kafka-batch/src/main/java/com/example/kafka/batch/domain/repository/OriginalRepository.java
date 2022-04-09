package com.example.kafka.batch.domain.repository;

import com.example.kafka.batch.domain.entity.Original;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OriginalRepository extends JpaRepository<Original, Long> {
}
