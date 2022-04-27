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


    // 챌린지 등록
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

    // 챌린지 수정
    @Override
    public int challengeUpdateOne(ChallengeCHG challenge) {
        try {
            chgRepository.save(challenge);
			return 1;
        }
        catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 챌린지 삭제
    @Override
    public int deleteChallenge(long chgno) {
		try {
			chgRepository.deleteById(chgno);
			return 1;
		} catch (Exception e) {
			return 0;
		}
    }

    // 챌린지 1개 조회
    @Override
    public ChallengeCHG challengeSelectOne(long chgno) {
        try {
            return chgRepository.findById(chgno).orElse(null);
        }       
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 챌린지 목록
    @Override
    public List<ChallengeCHG> challengeSelectList(Pageable page, String challenge) {
        try {
            List<ChallengeCHG> list = chgRepository.findByChgnoContainingOrderByChgnoDesc(challenge, page);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 챌린지 시작
    @Override
    public int challengeStart(ChallengeCHG challenge) {
        // TODO Auto-generated method stub
        return 0;
    }

    // 챌린지 종료
    @Override
    public int challengeEnd(ChallengeCHG challenge) {
        // TODO Auto-generated method stub
        return 0;
    }

    // 챌린지 모집 시작
    @Override
    public int recruitStart(ChallengeCHG challenge) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    // 챌린지 모집 마감
    @Override
    public int recruitEnd(ChallengeCHG challenge) {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
