package com.example.service;

import java.util.List;

import com.example.entity.ChallengeCHG;
import com.example.repository.ChallengeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    @Autowired ChallengeRepository chgRepository;

    @Override
    public List<ChallengeCHG> MemberSelectList(Pageable page, String challenge) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ChallengeCHG RoutineSelectOne(Long chgno) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int challengeEnd(ChallengeCHG challenge) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int challengeStart(ChallengeCHG challenge) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int challengeUpdateOne(ChallengeCHG challenge) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteChallenge(ChallengeCHG challenge) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insertChallengeOne(ChallengeCHG challenge) {
        try {
            chgRepository.save(challenge);

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int recruitEnd(ChallengeCHG challenge) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int recruitStart(ChallengeCHG challenge) {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
