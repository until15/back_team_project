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

    @Override
    public CommunityCHG boardSelectOne(long bno) {
        try {
            CommunityCHG community = cRepository.findById(bno).orElse(null);

            return community;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public int boardUpdateOne(CommunityCHG community) {
        try {
            cRepository.save(community);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int boarddeleteOne(long bno) {
        try {
            cRepository.deleteById(bno);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
