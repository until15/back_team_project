package com.example.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.ChallengeCHG;
import com.example.entity.ChallengeProjection;
import com.example.entity.ChallengeProjection2;
import com.example.entity.MemberCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<ChallengeCHG, Long> {

    // 첼린지 갯수 조회
    long countByChgtitleContaining(String chgtitle);

    // 첼린지 번호로 1개 조회
    ChallengeProjection findByChgno(long chgno);

    // 첼린지 검색 조회 (페이지네이션)
    // findBy 컬럼명 ContainingOrderBy컬럼명Desc
    // SELECT B.*, ROW_NUMBER() OVER (ORDER BY DESC) FROM BOARD10 B
    // WHERE BTITLE LIKE '%' || '검색어' || '%'
    List<ChallengeProjection2> findByChgtitleContainingOrderByChgnoDesc(String challenge, Pageable page);

    // 오류 도와준 다희씨 고마워요 . . . 2022/04/28

    // 작성자별 조회 갯수
    long countByMemberchg_memailAndChgtitleContaining(String email, String text);
    
    // 챌린지 작성자 별 조회
    List<ChallengeProjection> findByMemberchg_memailAndChgtitleContaining(String email, String text, Pageable page);

    // 첼린지 인기별 조회
    List<ChallengeProjection> findByChglikeOrderByChglikeDesc(long chglike, Pageable page);

    // 챌린지 난이도별 조회
    List<ChallengeProjection> findByChglevelOrderByChglevelDesc(long chglevel, Pageable page);

    // 챌린지 인기 목록(리스트)
    List<ChallengeProjection2> findByChgtitleContainingOrderByChglikeDesc(String challenge, Pageable page);

    // 챌린지 난이도 목록 (리스트)
    List<ChallengeProjection2> findByChgtitleContainingOrderByChglevelDesc(String challenge, Pageable page);

    // 첼린지 신규 조회
    List<ChallengeProjection> findByChgregdateOrderByChgregdateDesc(Date chgregdate, Pageable page);

    // 생성자가 마지막으로 만든 첼린지 조회
    ChallengeProjection findTop1ByMemberchg_memailOrderByChgnoDesc(String em);

    // @Query(value =
    // "SELECT MAX(CHGNO) FROM CHALLENGECHG WHERE MEMAIL=:em",
    // nativeQuery = true)
    // long selectLastChallenge(@Param(value = "em") String email);

    // 첼린지에 가입할 때마다 인원수 1씩 증가
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE CHALLENGECHG SET CHGCNT=CHGCNT+1 WHERE CHGNO=:no", nativeQuery = true)
    int increaseCnt(@Param(value = "no") long chgno);

    // 첼린지 기간 일 수
    @Query(value = "SELECT CHGDAYCNT FROM CHALLENGECHG WHERE CHGNO=:no", nativeQuery = true)
    Long challengeDayCount(@Param(value = "no") long chgno);
    
    // 오늘 날짜에 시작하는 첼린지 조회
 	public List<ChallengeCHG> findByChgstartBetween(Timestamp start, Timestamp end);

}
