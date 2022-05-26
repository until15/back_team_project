package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.JoinOneView;

@Repository
public interface JoinOneRepository extends JpaRepository<JoinOneView, Long> {

	// 참가자와 참가 번호로 조회
	JoinOneView findByMemailAndJno(String em, long no);
	
	
	// 참가자와 첼린지 번호로 조회
	JoinOneView findByMemailAndChgno(String em, long no);

	
	// 첼린지 생성자로 첼린지 조회
	JoinOneView findByChgnoAndMemail(Long chgno, String email);
}
