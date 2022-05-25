package com.example.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.JoinCHG;
import com.example.entity.JoinProjection;
import com.example.entity.JoinSelectOne;

@Repository
public interface JoinRepository extends JpaRepository<JoinCHG, Long>{

	// 참가 1개 조회
	JoinProjection findByJno(Long no);

	// 첼린지 번호와 아이디가 동시에 일치하는 항목 조회
	JoinCHG findByChallengechg_chgnoAndMemberchg_memail(Long no, String em);

	// 검색어가 포함된 항목 갯수
	long countByMemberchg_memailAndChallengechg_chgtitleContaining(String em, String title);
	
	// 참여했던 첼린지 전체 조회 (페이지네이션 : 첼린지 제목, 페이지별)
	List<JoinProjection> findByMemberchg_memailEqualsAndChallengechg_chgtitleContainingOrderByJnoDesc(String em, String title, Pageable page);
	
	// 진행 중인 첼린지 리스트 조회 (썸네일 포함)
	List<JoinProjection> findByMemberchg_memailAndChgstate(String em, int state);
	
	// 참여한 첼린지 1개 상세 조회
	JoinSelectOne findByMemberchg_memailAndJno(String em, long no);
	
	// 상태에 따른 첼린지 갯수
	long countByMemberchg_memailAndChgstate(String em, int state);
	
	// 진행 상태에 따른 첼린지 리스트 (페이지네이션)
	List<JoinProjection> findByMemberchg_memailAndChgstateOrderByJnoDesc(String em, int state, Pageable page);
	
	// 첼린지 번호에 해당하는 참가목록 조회
	List<JoinCHG> findByChallengechg_chgno(long no);
}
