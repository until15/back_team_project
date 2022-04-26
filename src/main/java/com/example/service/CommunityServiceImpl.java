package com.example.service;

import java.util.List;

import com.example.entity.CommunityCHG;
import com.example.repository.CommunityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommunityServiceImpl implements CommuniryService {

    @Autowired
    CommunityRepository cRepository;

    @Override
    public int boardInsertOne(CommunityCHG community) {
        try {
            cRepository.save(community);

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<CommunityCHG> selectBoardList(Pageable page, String btitle) {
        try {
            List<CommunityCHG> list = cRepository.findByBtitleContainingOrderByBnoDesc(btitle, page);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
