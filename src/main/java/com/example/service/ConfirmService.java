package com.example.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.entity.ConfirmCHG;
import com.example.entity.ConfirmProjection;

@Service
public interface ConfirmService {

	// 인증하기
	public int ConfirmInsert(ConfirmCHG confirm);
	
	// 오늘 날짜에 해당하는 인증글 조회
	public ConfirmCHG todayConfirm(String email, long jno, Timestamp start, Timestamp end);
	
	// 인증 번호와 아이디가 일치하는 항목 1개 조회 (수정용)
	public ConfirmCHG selectOneConfirm(long cfno, String email);
	
	// 인증글 수정
	public int updateOneConfirm(ConfirmCHG confirm);
	
	// 인증글 삭제
	public int deleteOneConfirm(long cfno);
	
	// 인증글 1개 조회 (필요한 항목만 Projection에 담기)
	public ConfirmProjection findOneConfirm(long cfno);
	
	// 인증글 전체 조회
	public List<ConfirmProjection> selectListConfirm(String email, String text, Pageable page);
	
	// 첼린지에 해당하는 인증글 전체 조회
	public List<ConfirmProjection> confirmFromChallenge(long chgno, Pageable page);
	
	// 내가 쓴 인증글 첼린지 별로 조회
	public List<ConfirmProjection> myConfirmFromChallenge(long no, String email, Pageable page);
	
}
