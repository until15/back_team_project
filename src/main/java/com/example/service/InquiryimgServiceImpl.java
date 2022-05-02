package com.example.service;

import com.example.entity.InquiryimgCHG;
import com.example.repository.InquiryimgRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiryimgServiceImpl implements InquiryimgService {

    @Autowired
    InquiryimgRepository imRepository;

    @Override
    public int insertQimg(InquiryimgCHG inquiryimg) {
        try {
            imRepository.save(inquiryimg);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public InquiryimgCHG selectOneqimg(long qimgno) {
        try {
            InquiryimgCHG inquiryimg = imRepository.findById(qimgno).orElse(null);
            return inquiryimg;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int qimgUpdateOne(InquiryimgCHG inquiryimg) {
        try {
            imRepository.save(inquiryimg);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteqimgOne(long qimgno) {
        try {
            imRepository.deleteById(qimgno);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
