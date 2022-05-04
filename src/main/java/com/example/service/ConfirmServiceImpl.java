package com.example.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.ConfirmCHG;
import com.example.repository.ConfirmRepository;

@Service
public class ConfirmServiceImpl implements ConfirmService {

	@Autowired ConfirmRepository cfRepository;
	
	// 인증 등록
	@Override
	public int ConfirmInsert(ConfirmCHG confirm) {
		try {
			cfRepository.save(confirm);
			
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// 회원과 오늘 날짜에 해당하는 데이터 조회
	@Override
	public ConfirmCHG todayConfirm(String email, Timestamp start, Timestamp end) {
		try {
			ConfirmCHG confirm = cfRepository.findByMemberchg_memailAndCcregdateBetween(email, start, end);
			return confirm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 인증 번호와 아이디가 일치하는 항목 1개 조회 (수정용)
	@Override
	public ConfirmCHG selectOneConfirm(long cfno, String email) {
		try {
			
			return cfRepository.findByCfnoAndMemberchg_memail(cfno, email);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 인증글 수정하기
	@Override
	public int updateOneConfirm(ConfirmCHG confirm) {
		try {
			cfRepository.save(confirm);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	
}
