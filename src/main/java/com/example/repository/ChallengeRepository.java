package com.example.repository;

import java.util.Date;
import java.util.List;

import com.example.entity.ChallengeCHG;
import com.example.entity.ChallengeProjection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<ChallengeCHG, Long> {
    
	// 첼린지 갯수 조회
    long countByChgnoContaining(long chgno);

    // 첼린지 검색 조회 (페이지네이션)
    // findBy 컬럼명 ContainingOrderBy컬럼명Desc
    // SELECT B.*, ROW_NUMBER() OVER (ORDER BY DESC) FROM BOARD10 B
    // WHERE BTITLE LIKE '%' || '검색어' || '%'
    List<ChallengeCHG> findByChgtitleContainingOrderByChgnoDesc(String challenge, Pageable page);
    // 오류 도와준 다희씨 고마워요 . . . 2022/04/28

    
    
    // 첼린지 인기 조회
    List<ChallengeCHG> findByChglikeOrderByChglikeDesc(long chglike, Pageable page);
    
    // 첼린지 신규 조회
    List<ChallengeCHG> findByChgregdateOrderByChgregdateDesc(Date chgregdate, Pageable page);

    
    // 생성자가 마지막으로 만든 첼린지 조회
    ChallengeProjection findTop1ByMemberchg_memailOrderByChgnoDesc(String em);
    
//    @Query(value = 
//			"SELECT MAX(CHGNO) FROM CHALLENGECHG WHERE MEMAIL=:em", 
//			nativeQuery = true)
//    long selectLastChallenge(@Param(value = "em") String email);
}
