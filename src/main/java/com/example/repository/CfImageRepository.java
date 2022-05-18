package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.CfImageCHG;

// 인증 이미지 저장소
@Repository
public interface CfImageRepository extends JpaRepository<CfImageCHG, Long> {

	public CfImageCHG findByCfimgno(long cfino);
	
	@Query(value = "SELECT CFI.CFIMGNO  FROM CF_IMAGECHG CFI WHERE CFI.CFNO=:no", nativeQuery = true)
	public List<Long> selectCFImageNo(@Param("no") long cfno);
	
	
}
