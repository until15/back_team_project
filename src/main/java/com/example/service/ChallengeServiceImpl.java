package com.example.service;

import java.util.List;

import com.example.entity.ChallengeCHG;
import com.example.entity.ChallengeProjection;
import com.example.entity.ChallengeProjection2;
import com.example.entity.MemberCHG;
import com.example.repository.ChallengeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    @Autowired
    ChallengeRepository chgRepository;

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
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 챌린지 목록
    @Override
    public List<ChallengeProjection2> challengeSelectList(Pageable page, String challenge) {
        try {
            List<ChallengeProjection2> list = chgRepository.findByChgtitleContainingOrderByChgnoDesc(challenge, page);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 난이도별 조회
    @Override
    public List<ChallengeProjection> levelSelectList(Pageable page, long chglevel) {
        try {
            List<ChallengeProjection> list = chgRepository.findByChglevelOrderByChglevelDesc(chglevel, page);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 인기별 조회
    @Override
    public List<ChallengeProjection> likeSelectList(Pageable page, long chglike) {
        try {
            List<ChallengeProjection> list = chgRepository.findByChglikeOrderByChglikeDesc(chglike, page);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 챌린지 시작
    @Override
    public int challengeStart(ChallengeCHG challenge) {
        return 0;
    }

    // 챌린지 종료
    @Override
    public int challengeEnd(ChallengeCHG challenge) {
        return 0;
    }

    // 챌린지 모집 시작
    @Override
    public int recruitStart(ChallengeCHG challenge) {
        return 0;
    }

    // 챌린지 모집 마감
    @Override
    public int recruitEnd(ChallengeCHG challenge) {
        return 0;
    }

    // 썸네일 이미지 가져오기
    @Override
    public ChallengeCHG challengeImageSelect(long chgno) {
        try {
            ChallengeCHG challenge = chgRepository.findById(chgno).orElse(null);
            return challenge;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    // 썸네일 이미지 수정
    @Override
    public int challengeImageUpdate(ChallengeCHG challenge) {
        try {
            chgRepository.save(challenge);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 챌린지 난이도 목록(리스트)
    @Override
    public List<ChallengeProjection2> chgLevelSelectList(Pageable page, String challenge) {
        try {
            List<ChallengeProjection2> list = chgRepository.findByChgtitleContainingOrderByChglevelDesc(challenge,
                    page);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 챌린지 인기 목록(리스트)
    @Override
    public List<ChallengeProjection2> chgLikeSelectList(Pageable page, String challenge) {
        try {
            List<ChallengeProjection2> list = chgRepository.findByChgtitleContainingOrderByChglikeDesc(challenge, page);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 작성자 별 조회
    @Override
    public List<ChallengeProjection> memberSelectList(Pageable page, MemberCHG memberchg) {
        try {
            List<ChallengeProjection> list = chgRepository.findByMemberchgOrderByMemberchgDesc(memberchg, page);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
