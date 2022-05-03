package com.example.repository;

import com.example.entity.RtnRunCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RtnRunRepository extends JpaRepository<RtnRunCHG, Long>{
    
}
