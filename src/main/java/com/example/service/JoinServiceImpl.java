package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.entity.CHGImgView;
import com.example.entity.JoinCHG;
import com.example.entity.JoinProjection;
import com.example.entity.JoinSelectOne;
import com.example.repository.ChgImageRepository;
import com.example.repository.JoinRepository;

@Service
public class JoinServiceImpl implements JoinService{

	@Autowired JoinRepository jRepository;
	@Autowired ChgImageRepository ciRepository;
	@Autowired EntityManagerFactory emf;
	
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

	
	// 참여했던 모든 첼린지 조회 (아이디, 검색, 페이지네이션)
	@Override
	public List<JoinProjection> joinChallengeAllList(String memail, String title, Pageable page) {
		try {
			
			return jRepository.findByMemberchg_memailEqualsAndChallengechg_chgtitleContainingOrderByJnoDesc(memail, title, page);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	// 진행 상태 별 첼린지 조회 (아이디, 진행상태, 페이지네이션)
	@Override
	public List<JoinProjection> joinStateList(String memail, int state, Pageable page) {
		try {
			
			return jRepository.findByMemberchg_memailAndChgstateOrderByJnoDesc(memail, state, page);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 검색어가 포함된 항목의 갯수
	@Override
	public long selectCount(String email, String title) {
		try {
			return jRepository.countByMemberchg_memailAndChallengechg_chgtitleContaining(email, title);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// 진행 상태 별 참가 갯수
	@Override
	public long selectStateCount(String memail, int state) {
		try {
			return jRepository.countByMemberchg_memailAndChgstate(memail, state);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}


	// 이미지 한개 조회 ( 테스트 )
	@Override
	public CHGImgView selectOneImg(long chgno) {
		try {
			CHGImgView img = ciRepository.findById(chgno).orElse(null);
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	// 달성률 업데이트
	@Override
	public int challengeSuccessRate(JoinCHG join) {
		try {
			jRepository.save(join);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// 첼린지 시작/종료일에 맞춰 진행상태 변경
	@Override
	public int todayChallenge(List<JoinCHG> join) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			
			for (JoinCHG join1 : join) {
				
				JoinCHG oldjoin = em.find(JoinCHG.class, join1.getJno());
				System.out.println("변경된 진행 상태 : " + join1.getChgstate());
				oldjoin.setChgstate(join1.getChgstate());
                em.persist(oldjoin);
            }
			
			em.getTransaction().commit();
			return 1;
			
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return 0;
		}
	}

	
	
}
