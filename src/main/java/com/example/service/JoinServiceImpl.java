package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.JoinCHG;
import com.example.repository.JoinRepository;

@Service
public class JoinServiceImpl implements JoinService{

	@Autowired JoinRepository jRepository;
	
	// 첼린지 참가
	@Override
	public int challengeJoin(JoinCHG join) {
		try {
			jRepository.save(join);
			
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	
	// 첼린지 포기
	@Override
	public int challengeGiveUp(Long jno) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	// 현재 참여중인 첼린지 조회
	@Override
	public List<JoinCHG> joinChallengeList(String memail) {
		// TODO Auto-generated method stub
		return null;
	}

	
	// 참여했던 모든 첼린지 조회
	@Override
	public List<JoinCHG> joinedChallengeAllList(String memail) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
