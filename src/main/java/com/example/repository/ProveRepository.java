package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.ProveCHGView;

@Repository
public interface ProveRepository extends JpaRepository<ProveCHGView, Long>{

	// 인증글 전체 조회 (페이지네이션)
	List<ProveCHGView> findByMemailContainingOrderByCfnoDesc(String em, Pageable page);
	
	// 검색어가 포함된 항목 갯수
	long countByMemailContaining(String em);
	
}
