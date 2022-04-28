package com.example.repository;


import java.util.List;

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
	
	// 참여했던 첼린지 전체 조회
	List<JoinProjection> findByMemberchg_memail(String em);
	
	// 내가 참여중인 첼린지 리스트
	List<JoinProjection> findByMemberchg_memailAndChgstate(String em, int state);
	
}
