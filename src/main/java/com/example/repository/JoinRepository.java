package com.example.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.JoinCHG;
import com.example.entity.JoinProjection;

@Repository
public interface JoinRepository extends JpaRepository<JoinCHG, Long>{

	// 첼린지 1개 조회
	JoinProjection findByJno(Long no);

	// 유저가 첼린지에 중복 참여 했는지 확인
	JoinCHG findByChallengechg_chgnoAndMemberchg_memail(Long no, String em);
	
}
