package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.JoinCHG;
import com.example.entity.JoinProjection;
import com.example.entity.JoinSelectOne;
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

	
	// 아이디와 첼린지 번호 동시에 일치하는 참가
	@Override
	public JoinCHG duplicateJoin(Long no, String em) {
		try {
			
			return jRepository.findByChallengechg_chgnoAndMemberchg_memail(no, em);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	// 첼린지 1개 조회
	@Override
	public JoinSelectOne selectOneCHG(String em, long no) {
		try {
			
			return jRepository.findByMemberchg_memailAndJno(em, no);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	// 첼린지 포기
	@Override
	public int challengeGiveUp(JoinCHG join) {
		try {
			jRepository.save(join);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	
	// 현재 진행 중인 첼린지 조회
	@Override
	public List<JoinProjection> joinChallengeList(String memail, int state) {
		try {
			
			return jRepository.findByMemberchg_memailAndChgstate(memail, state);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	// 참여했던 모든 첼린지 조회
	@Override
	public List<JoinProjection> joinedChallengeAllList(String memail) {
		try {
			
			return jRepository.findByMemberchg_memail(memail);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
}
