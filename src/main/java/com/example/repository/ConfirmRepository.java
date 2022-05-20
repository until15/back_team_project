package com.example.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.ConfirmCHG;
import com.example.entity.ConfirmProjection;

@Repository
public interface ConfirmRepository extends JpaRepository<ConfirmCHG, Long>{
	
	// 어제 00:00:00 부터 오늘 23:59:59 사이의 데이터를 조회
	ConfirmCHG findByMemberchg_memailAndJoinchg_jnoAndCcregdateBetween(String em, long no, Timestamp start, Timestamp end);
	
	// 인증 번호와 아이디가 일치하는 항목 1개 조회 (수정용)
	ConfirmCHG findByCfnoAndMemberchg_memail(long no, String em);
	
	// 게시글 삭제하기
	@Transactional
	@Modifying(clearAutomatically = true)
	public int deleteByCfno(long no);
	
	// 인증글 1개 조회 (필요한 항목만 Projection에 담기)
	ConfirmProjection findByCfno(long no);
	
	// 내가 쓴 인증글 전체 조회
	List<ConfirmProjection> findByMemberchg_memailAndCfcommentContainingOrderByCfnoDesc(String email, String text, Pageable page);
	
	// 인증글 개수
	long countByJoinchg_challengechg_chgno(long no);
	
	// 첼린지 내 인증글 전체 조회 (페이지네이션)
	List<ConfirmProjection> findByJoinchg_challengechg_chgno(long no, Pageable page);
	
	// 내가 쓴 인증글 첼린지 별로 조회
	List<ConfirmProjection> findByJoinchg_challengechg_chgnoAndMemberchg_memail(long no, String email, Pageable page);
	
	// 성공 여부 별 인증글 개수
	
	// 성공 여부 별로 인증글 조회 (페이지네이션)
	List<ConfirmProjection> findByJoinchg_challengechg_chgnoAndCfsuccess(long chgno, int no, Pageable page);
}
