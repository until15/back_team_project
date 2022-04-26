package com.example.service;

import java.util.List;

import com.example.entity.ChallengeCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ChallengeService {
    
    // 챌린지 등록
    public int insertChallengeOne(ChallengeCHG challenge);

    // 챌린지 1개 조회
    public ChallengeCHG RoutineSelectOne(Long chgno);

    // 챌린지 목록 (페이지 1 2 3)
    public List<ChallengeCHG> MemberSelectList(Pageable page, String challenge);

    // 챌린지 수정
    public int challengeUpdateOne(ChallengeCHG challenge);

    // 챌린지 삭제
    public int deleteChallenge(ChallengeCHG challenge);

    // 챌린지 시작
    public int challengeStart(ChallengeCHG challenge);

    // 챌린지 종료
    public int challengeEnd(ChallengeCHG challenge);

    // 모집 시작일 
    public int recruitStart(ChallengeCHG challenge);

    // 모집 마감일
    public int recruitEnd(ChallengeCHG challenge);


}
