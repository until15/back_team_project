package com.example.service;

import java.util.List;

import com.example.entity.InquiryCHG;
import com.example.repository.InquiryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InquiryServiceImpl implements InquiryService {

    @Autowired
    InquiryRepository iRepository;

    @Override
    public int inquiryInsertOne(InquiryCHG inquiry) {
        try {
            iRepository.save(inquiry);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<InquiryCHG> selectInquiryList(Pageable page, String qtitle) {
        try {
            List<InquiryCHG> list = iRepository.findByQtitleContainingOrderByQnoDesc(qtitle, page);
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

}
