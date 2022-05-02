package com.example.service;

import java.util.List;

import com.example.entity.IqcommentCHG;
import com.example.repository.IqcommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IqcommentServiceImpl implements IqcommentService {

    @Autowired
    IqcommentRepository iqRepository;

    @Override
    public int iqcommentInsert(IqcommentCHG iqcomment) {
        try {
            iqRepository.save(iqcomment);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public IqcommentCHG selectOneIqcomment(long iqcmtno) {
        try {
            IqcommentCHG iqcomment = iqRepository.findById(iqcmtno).orElse(null);
            return iqcomment;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int deleteIqcomment(long iqcmtno) {
        try {
            iqRepository.deleteById(iqcmtno);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<IqcommentCHG> icommentSelectList(long qno) {
        try {

            List<IqcommentCHG> list = iqRepository.findByInquirychg_qnoOrderByIqcmtnoDesc(qno);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
