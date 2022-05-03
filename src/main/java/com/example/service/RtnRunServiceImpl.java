package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.example.entity.RtnRunCHG;
import com.example.repository.RtnRunRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RtnRunServiceImpl implements RtnRunService{

    @Autowired EntityManagerFactory emf;
    @Autowired RtnRunRepository rrRepository;

    // 루틴 실행 등록
    @Override
    public int RtnRunInsert(List<RtnRunCHG> runno) {
        EntityManager em = emf.createEntityManager();
        try {
            // 루틴 번호 한 번에 추가
			for( RtnRunCHG rtnrun : runno) {
				em.persist(rtnrun);
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
