package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.JoinCHG;

@Repository
public interface JoinRepository extends JpaRepository<JoinCHG, Long>{

	
	
}
