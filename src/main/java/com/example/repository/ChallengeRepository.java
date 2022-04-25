package com.example.repository;

import java.util.List;

import com.example.entity.ChallengeCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<ChallengeCHG, Long> {
    
    long countByChgnoContaining(long chgno);

    // findBy 컬럼명 ContainingOrderBy컬럼명Desc
    // SELECT B.*, ROW_NUMBER() OVER (ORDER BY DESC) FROM BOARD10 B
    // WHERE BTITLE LIKE '%' || '검색어' || '%'
    List<ChallengeCHG> findByChgnoContainingOrderByChgnoDesc(long chgno,
            Pageable page);

}
