package com.example.service;

import java.util.List;

import com.example.entity.CmtLikeCHG;
import com.example.repository.CmtlikeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CmtLikeServiceImpl implements CmtLikeService {

    @Autowired
    CmtlikeRepository cRepository;

    @Override
    public int insertLike(CmtLikeCHG cmtlike) {
        try {
            cRepository.save(cmtlike);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public CmtLikeCHG duplicateInsert(Long no, String memail) {
        try {
            return cRepository.findByCommentchg_cmtnoAndMemberchg_memail(no, memail);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int deleteLike(long cmtlikeno) {
        try {
            cRepository.deleteById(cmtlikeno);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public CmtLikeCHG likeSelectOne(long cmtlikeno) {
        try {
            return cRepository.findById(cmtlikeno).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CmtLikeCHG> likeSelectList(Pageable page, String memail) {
        try {
            List<CmtLikeCHG> list = cRepository.findByMemberchg_memailOrderByCmtlikenoDesc(memail, page);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
