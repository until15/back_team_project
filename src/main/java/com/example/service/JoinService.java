package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.JoinCHG;
import com.example.entity.JoinProjection;

@Service
public interface JoinService {
	
	// 첼린지 1개 조회
	public JoinProjection selectOneCHG(Long jno);
	
    // 챌린지 참가
    public int challengeJoin(JoinCHG join);

    // 중복 참여 확인
    public JoinCHG duplicateJoin(Long no, String em);
    
    // 챌린지 포기
    public int challengeGiveUp(Long jno);
	
	// 참여중인 첼린지 조회
	public List<JoinCHG> joinChallengeList(String memail);
	
	// 참여했던 첼린지 전체 조회
	public List<JoinCHG> joinedChallengeAllList(String memail);
}
