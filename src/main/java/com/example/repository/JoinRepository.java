package com.example.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.JoinCHG;
import com.example.entity.JoinProjection;

@Repository
public interface JoinRepository extends JpaRepository<JoinCHG, Long>{

	// 첼린지 번호와 멤버아이디가 기존에 있는지 조회
	JoinProjection findByJno(Long no);
	
}
