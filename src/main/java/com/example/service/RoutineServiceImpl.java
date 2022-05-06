package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.example.entity.RoutineCHG;
import com.example.entity.RtnSeqCHG;
import com.example.repository.RoutineRepository;
import com.example.repository.RtnSeqRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoutineServiceImpl implements RoutineService{

    @Autowired EntityManagerFactory emf;
    @Autowired RoutineRepository rRepository;
    @Autowired RtnSeqRepository seqRepository;


    // 루틴 등록
    @Override
    public int RoutineInsertBatch(List<RoutineCHG> list) {
        EntityManager em = emf.createEntityManager();
        try {
            RtnSeqCHG seq = seqRepository.getById("RTN_SEQ");
            em.getTransaction().begin();
            // 루틴 한 번에 추가
            for(RoutineCHG routine : list){
                routine.setRtnseq(seq.getSeq());
                em.persist(routine);
            }
            em.getTransaction().commit();
            seq.setSeq(seq.getSeq() + 1);
            seqRepository.save(seq);
            return 1;
            
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return 0;
        }
    }

    // 루틴 수정
    @Override
    public int RoutineUpdateBatch(List<RoutineCHG> list) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // 루틴 한 번에 수정
            for(RoutineCHG routine : list){
                RoutineCHG oldRoutine = 
                em.find(RoutineCHG.class, routine.getRtnno());
                // oldRoutine.setRtnseq(routine.getRtnseq());
                oldRoutine.setRtnday(routine.getRtnday());
                oldRoutine.setRtncnt(routine.getRtncnt());
                oldRoutine.setRtnset(routine.getRtnset());
                oldRoutine.setPosechg(routine.getPosechg());
                em.persist(oldRoutine);
            }
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return 0;
        }
    }

    // 루틴 삭제
    @Override
    public int RoutineDelete(Long[] rtnno) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // 루틴 한 번에 삭제
			for( Long tmp : rtnno) {
				RoutineCHG oldRoutine 
					= em.find(RoutineCHG.class, tmp);
				em.remove(oldRoutine);
			}
			em.getTransaction().commit();
			return 1;
            
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return 0;
        }
    }

    // 루틴 조회
    @Override
    public List<RoutineCHG> RoutineSelectlist(String memail) {
        try {
           List<RoutineCHG> routine = rRepository.findByMemberchg_memailEqualsOrderByRtnnoDesc(memail);
           return routine;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 루틴 조회 (수정용)
    @Override
    public List<RoutineCHG> RoutineSelectPrivate(String memail, Long[] rtnno) {
        try {
            List<RoutineCHG> routine = rRepository.findByMemberchg_memailAndRtnnoEqualsOrderByRtnnoDesc(memail, rtnno);
            return routine;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
