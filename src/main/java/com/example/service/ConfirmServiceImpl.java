package com.example.service;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.entity.CfImageCHG;
import com.example.entity.ConfirmCHG;
import com.example.entity.ConfirmProjection;
import com.example.repository.CfImageRepository;
import com.example.repository.ConfirmRepository;

@Service
public class ConfirmServiceImpl implements ConfirmService {

	@Autowired ConfirmRepository cfRepository;
	
	@Autowired CfImageRepository cfiRepository;
	
	@Autowired EntityManagerFactory emf;
	
	// 인증 등록
	@Override
	public long ConfirmInsert(ConfirmCHG confirm) {
		try {
			ConfirmCHG confirm1 = cfRepository.save(confirm);
			
			return confirm1.getCfno();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// 회원과 오늘 날짜에 해당하는 데이터 조회
	@Override
	public ConfirmCHG todayConfirm(String email, long jno, Timestamp start, Timestamp end) {
		try {
			ConfirmCHG confirm = cfRepository.findByMemberchg_memailAndJoinchg_jnoAndCcregdateBetween(email, jno, start, end);
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

	// 인증글 삭제하기
	@Override
	public int deleteOneConfirm(long cfno) {
		try {
			cfRepository.deleteByCfno(cfno);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// 인증글 1개 조회 (필요한 항목만 Projection으로 조회)
	@Override
	public ConfirmProjection findOneConfirm(long cfno) {
		try {
			
			return cfRepository.findByCfno(cfno);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 인증글 전체 조회
	@Override
	public List<ConfirmProjection> selectListConfirm(String email, String text, Pageable page) {
		try {
			return cfRepository.findByMemberchg_memailAndCfcommentContainingOrderByCfnoDesc(email, text, page);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 첼린지 내 인증글 전체 조회
	@Override
	public List<ConfirmProjection> confirmFromChallenge(long chgno, Pageable page) {
		try {
			
			return cfRepository.findByJoinchg_challengechg_chgno(chgno, page);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 내가 쓴 인증글 첼린지 별로 조회
	@Override
	public List<ConfirmProjection> myConfirmFromChallenge(long no, String email, Pageable page) {
		try {
			return cfRepository.findByJoinchg_challengechg_chgnoAndMemberchg_memail(no, email, page);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 인증글 1개 조회 (성공 여부 판별용)
	@Override
	public ConfirmCHG selectSuccessOne(long no) {
		try {
			
			return cfRepository.findById(no).orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 성공 유무 별로 인증글 조회 (페이지네이션)
	@Override
	public List<ConfirmProjection> whetherSuccessCFM(long chgno, int no, Pageable page) {
		try {
			return cfRepository.findByJoinchg_challengechg_chgnoAndCfsuccess(chgno, no, page);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 인증하기 이미지 추가 (일괄)
	@Override
	public int ConfirmImage(List<CfImageCHG> list) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			
			for (CfImageCHG cfImg : list) {
                em.persist(cfImg);
            }
			
            em.getTransaction().commit();
			
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return 0;
		}
	}

	// 인증 이미지 조회
	@Override
	public CfImageCHG selectProveImage(long cfino) {
		try {
			
			return cfiRepository.findByCfimgno(cfino);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 인증 이미지번호 조회
	@Override
	public List<Long> selectCFImageNo(long cfno) {
		try {
			List<Long> cfimgno = cfiRepository.selectCFImageNo(cfno);
			return cfimgno;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
}
