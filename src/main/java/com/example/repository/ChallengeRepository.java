package com.example.repository;

import java.util.Date;
import java.util.List;

import com.example.entity.ChallengeCHG;

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
    List<ChallengeCHG> findByChgnoContainingOrderByChgnoDesc(long chgno,
            Pageable page);


    
    
    // 첼린지 인기 조회
    List<ChallengeCHG> findByChglikeOrderByChglikeDesc(long chglike, Pageable page);
    
    // 첼린지 신규 조회
    List<ChallengeCHG> findByChgregdateOrderByChgregdateDesc(Date chgregdate, Pageable page);

    
}
