package com.example.service;

import java.util.List;

import com.example.entity.CommunityCHG;
import com.example.entity.CommunityCHGProjection;
import com.example.repository.CommunityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommunityServiceImpl implements CommuniryService {

    @Autowired
    CommunityRepository cRepository;

    @Override
    public long boardInsertOne(CommunityCHG community) {
        try {
            CommunityCHG community1 = cRepository.save(community);
            System.out.println(community1.toString());
            return community1.getBno();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<CommunityCHGProjection> selectBoardList(Pageable page, String btitle) {
        try {
            List<CommunityCHGProjection> list = cRepository.findByBtitleContainingOrderByBnoDesc(btitle, page);
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
    public int boardDeleteOne(long bno) {
        try {
            cRepository.deleteById(bno);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int boardUpdateHit(long bno) {
        try {
            cRepository.updateBoardHitOne(bno);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public CommunityCHG boardUpdateHit1(long bno) {
        try {
            CommunityCHG community = cRepository.findById(bno).orElse(null);
            community.setBhit(community.getBhit() + 1L);
            cRepository.save(community);
            return community;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CommunityCHG boardSelectPrivate(String memail, long bno) {
        try {
            CommunityCHG community = cRepository.findByMemberchg_memailAndBnoEqualsOrderByBnoDesc(memail, bno);
            return community;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
