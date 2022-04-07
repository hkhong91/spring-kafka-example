package com.example.kafka.domain.repository;

import com.example.kafka.domain.entity.Original;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OriginalRepository extends JpaRepository<Original, Long> {
}
