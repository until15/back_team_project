package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.JoinOneView;

@Repository
public interface JoinOneRepository extends JpaRepository<JoinOneView, Long> {

	JoinOneView findByMemailAndJno(String em, long no);
	
	JoinOneView findByMemailAndChgno(String em, long no);

}
