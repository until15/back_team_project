package com.example.service;

import java.util.List;

import com.example.entity.ChallengeCHG;
import com.example.entity.ChallengeProjection;
import com.example.entity.ChallengeProjection2;
import com.example.entity.MemberCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ChallengeService {

    // 챌린지 등록
    public int insertChallengeOne(ChallengeCHG challenge);

    // 챌린지 1개 조회
    public ChallengeCHG challengeSelectOne(long chgno);

    // 챌린지 목록 (페이지 1 2 3)
    public List<ChallengeProjection2> challengeSelectList(Pageable page, String challenge);

    // 챌린지 작성자 별 조회
    public List<ChallengeProjection> memberSelectList(Pageable page, MemberCHG memberchg);

    // 챌린지 난이도별 조회
    public List<ChallengeProjection> levelSelectList(Pageable page, long chglevel);

    // 챌린지 인기별 조회
    public List<ChallengeProjection> likeSelectList(Pageable page, long chglike);

    // 챌린지 난이도 목록(리스트)
    public List<ChallengeProjection2> chgLevelSelectList(Pageable page, String challenge);

    // 챌린지 인기 목록(리스트)
    public List<ChallengeProjection2> chgLikeSelectList(Pageable page, String challenge);

    // 챌린지 수정
    public int challengeUpdateOne(ChallengeCHG challenge);

    // 챌린지 삭제
    public int deleteChallenge(long chgno);

    // 챌린지 시작
    public int challengeStart(ChallengeCHG challenge);

    // 챌린지 종료
    public int challengeEnd(ChallengeCHG challenge);

    // 모집 시작일
    public int recruitStart(ChallengeCHG challenge);

    // 모집 마감일
    public int recruitEnd(ChallengeCHG challenge);

    // 썸네일 이미지 가져오기
    public ChallengeCHG challengeImageSelect(long chgno);

    // 썸네일 이미지 수정하기
    public int challengeImageUpdate(ChallengeCHG challenge);

}
