package com.example.service;

import com.example.entity.BimgCHG;

import org.springframework.stereotype.Service;

@Service
public interface BimgService {

    public int insertBimg(BimgCHG bimg);

    public BimgCHG selectOneimage(long bimgno);

}
