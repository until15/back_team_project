package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.ConfirmView;

@Repository
public interface CfmViewRepository extends JpaRepository<ConfirmView, Long> {

	// 인증글 개수
	long countByChgno(long no);
		
	// 첼린지 내 인증글 전체 조회 (페이지네이션)
	List<ConfirmView> findByChgnoOrderByCcregdateDesc(long no, Pageable page);
	
}
