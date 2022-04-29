package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.JoinCHG;
import com.example.entity.JoinProjection;
import com.example.entity.JoinSelectOne;

@Service
public interface JoinService {
	
	// 첼린지 1개 상세 조회
	public JoinSelectOne selectOneCHG(String em, long no);
	
    // 챌린지 참가
    public int challengeJoin(JoinCHG join);

    // 아이디와 첼린지 번호 동시에 일치하는 참가
    public JoinCHG duplicateJoin(Long no, String em);
    
    // 챌린지 포기
    public int challengeGiveUp(JoinCHG join);
	
    // 참여했던 첼린지 전체 조회
    public List<JoinProjection> joinedChallengeAllList(String memail);
	
	// 진행 중인 첼린지 조회
	public List<JoinProjection> joinChallengeList(String memail, int state);
	
	
}
