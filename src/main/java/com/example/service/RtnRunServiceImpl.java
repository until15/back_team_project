package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.example.entity.RtnRunCHG;
import com.example.entity.RtnRunSeqCHG;
import com.example.repository.RtnRunRepository;
import com.example.repository.RtnRunSeqRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RtnRunServiceImpl implements RtnRunService{

    @Autowired EntityManagerFactory emf;
    @Autowired RtnRunRepository rrRepository;
    @Autowired RtnRunSeqRepository rrsRepository;

    // 루틴 실행 등록
    @Override
    public int RtnRunInsert(List<RtnRunCHG> rtnno) {
        try {
            EntityManager em = emf.createEntityManager();
            try {
                RtnRunSeqCHG seq = rrsRepository.getById("RUN_SEQ");
                em.getTransaction().begin();
                // 한 번에 추가
                for(RtnRunCHG run : rtnno){
                    run.setRunseq(seq.getSeq());
                    em.persist(run);
                }
                em.getTransaction().commit();
                seq.setSeq(seq.getSeq() + 1);
                rrsRepository.save(seq);
                return 1;
                
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
                return 0;
            }
        } catch (Exception e) {
            
        }
        return 0;
    }

    // 루틴 실행 수정
    @Override
    public int RtnRunUpdate(List<RtnRunCHG> runno) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // 한 번에 수정
            for(RtnRunCHG run : runno){
                RtnRunCHG oldRun = 
                em.find(RtnRunCHG.class, run.getRunno());
                oldRun.setRoutinechg(run.getRoutinechg());
                em.persist(oldRun);
            }
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return 0;
        }
    }

    // 루틴 실행 삭제
    @Override
    public int RtnRunDelete(Long[] runno) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // 루틴 한 번에 삭제
			for( Long tmp : runno) {
				RtnRunCHG oldRun
					= em.find(RtnRunCHG.class, tmp);
				em.remove(oldRun);
			}
			em.getTransaction().commit();
			return 1;
            
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return 0;
        }
    }

    // 루틴 실행 조회
    @Override
    public List<RtnRunCHG> RtnRunSelectlist(long runseq) {
        try {
            List<RtnRunCHG> list = rrRepository.findByRunseqEqualsOrderByRunnoDesc(runseq);
            return list;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
