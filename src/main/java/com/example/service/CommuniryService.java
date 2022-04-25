package com.example.service;

import java.util.List;

import com.example.entity.CommunityCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CommuniryService {

    public int boardInsertOne(CommunityCHG community);

    public List<CommunityCHG> selectBoardList(Pageable page, String btitle);
}
