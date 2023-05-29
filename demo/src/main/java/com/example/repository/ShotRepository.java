package com.example.repository;

import com.example.models.Shot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShotRepository  extends JpaRepository<Shot,Long> {}
