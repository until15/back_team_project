package com.example.repository;

import com.example.entity.VideoCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<VideoCHG, Long>{
    
}
