package com.example.service;

import java.util.List;

import com.example.entity.InquiryCHG;
import com.example.entity.InquiryCHGProjection;
import com.example.repository.InquiryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InquiryServiceImpl implements InquiryService {

    @Autowired
    InquiryRepository iRepository;

    @Override
    public long inquiryInsertOne(InquiryCHG inquiry) {
        try {
            InquiryCHG inquiry1 = iRepository.save(inquiry);
            return inquiry1.getQno();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<InquiryCHGProjection> selectInquiryList(Pageable page, String qtitle) {
        try {
            List<InquiryCHGProjection> list = iRepository.findByQtitleContainingOrderByQnoDesc(qtitle, page);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InquiryCHG inquirySelectOne(long qno) {
        try {
            InquiryCHG inquiry = iRepository.findById(qno).orElse(null);
            return inquiry;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int inquiryUpdateOne(InquiryCHG inquiry) {
        try {
            iRepository.save(inquiry);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int inquiryDeleteOne(long qno) {
        try {
            iRepository.deleteById(qno);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<InquiryCHGProjection> selectListInquiry(String memail, Pageable page, String btitle) {
        try {
            List<InquiryCHGProjection> list = iRepository.findByMemberchg_memailAndQtitleContainingOrderByQnoDesc(
                    memail, btitle,
                    page);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int inquiryUpdateCom(long qno) {
        try {
            iRepository.updateComOne(qno);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int inquiryTwoeCom(long qno) {
        try {
            iRepository.updateComTwo(qno);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

}
