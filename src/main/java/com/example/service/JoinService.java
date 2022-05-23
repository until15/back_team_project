package com.example.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.entity.CHGImgView;
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
    
    // 검색어가 포함된 항목의 객수
 	public long selectCount(String email, String title);
    
    // 참여했던 첼린지 전체 조회 (페이지네이션)
    public List<JoinProjection> joinChallengeAllList(String memail, String title, Pageable page);
	
	// 진행 중인 첼린지 조회
	public List<JoinProjection> joinChallengeList(String memail, int state);
	
	// 진행 상태 별 첼린지 갯수
	public long selectStateCount(String memail, int state);
	
	// 진행 상태 별 첼린지 조회 (페이지네이션)
	public List<JoinProjection> joinStateList(String memail, int state, Pageable page);
	
	// 이미지 한개 조회 ( 테스트 )
	public CHGImgView selectOneImg(long chgno);
	
	// 달성률 업데이트
	public int challengeSuccessRate(JoinCHG join);

}
