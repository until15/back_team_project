package com.example.repository;

import java.util.List;

import com.example.entity.CommunityCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<CommunityCHG, Long> {

    long countByBtitleContaining(String btitle);

    CommunityCHG findByBno(Long bno);

    // findBy 컬럼명 ContainingOrderBy컬럼명Desc
    // SELECT B.*, ROW_NUMBER() OVER (ORDER BY DESC) FROM BOARD10 B
    // WHERE BTITLE LIKE '%' || '검색어' || '%'
    List<CommunityCHG> findByBtitleContainingOrderByBnoDesc(String btitle, Pageable page);

}
