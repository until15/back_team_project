package com.example.repository;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.ConfirmCHG;

@Repository
public interface ConfirmRepository extends JpaRepository<ConfirmCHG, Long>{
	
	// 어제 00:00:00 부터 오늘 23:59:59 사이의 데이터를 조회
	ConfirmCHG findByMemberchg_memailAndCcregdateBetween(String em, Timestamp start, Timestamp end);
	
	// 인증 번호와 아이디가 일치하는 항목 1개 조회 (수정용)
	ConfirmCHG findByCfnoAndMemberchg_memail(long no, String em);
	
}
