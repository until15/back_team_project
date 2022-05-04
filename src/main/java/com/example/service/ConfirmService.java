package com.example.service;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.example.entity.ConfirmCHG;

@Service
public interface ConfirmService {

	// 인증하기
	public int ConfirmInsert(ConfirmCHG confirm);
	
	// 오늘 날짜에 해당하는 인증글 조회
	public ConfirmCHG todayConfirm(String email, Timestamp start, Timestamp end);
	
	// 인증 번호와 아이디가 일치하는 항목 1개 조회 (수정용)
	public ConfirmCHG selectOneConfirm(long cfno, String email);
	
	// 인증글 수정
	public int updateOneConfirm(ConfirmCHG confirm);
	
}
