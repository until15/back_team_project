package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.example.entity.BimgCHG;
import com.example.entity.BimgCHGProjection;
import com.example.repository.BimgRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BimgServiceImpl implements BimgService {

    @Autowired
    BimgRepository bRepository;
    @Autowired
    EntityManagerFactory emf;

    @Override
    public int insertBimg(BimgCHG bimg) {
        try {
            bRepository.save(bimg);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public BimgCHG selectOneimage(long bimgno) {
        try {
            BimgCHG bimg = bRepository.findById(bimgno).orElse(null);
            return bimg;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int bimgUpdateOne(BimgCHG bimg) {
        try {
            bRepository.save(bimg);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteBimgOne(long bimgno) {
        try {
            bRepository.deleteById(bimgno);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<BimgCHGProjection> selectimageList(long bimgno) {
        try {
            List<BimgCHGProjection> list = bRepository.findByBimgnoOrderByBimgnoDesc(bimgno);

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insertBatchBimg(List<BimgCHG> list) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            for (BimgCHG bimg : list) {
                em.persist(bimg);
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
