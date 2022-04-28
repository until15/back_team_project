package com.example.service;

import com.example.entity.BimgCHG;
import com.example.repository.BimgRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BimgServiceImpl implements BimgService {

    @Autowired
    BimgRepository bRepository;

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

}