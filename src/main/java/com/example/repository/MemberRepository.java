package com.example.repository;

import java.util.List;

import com.example.entity.MemberCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberCHG, String> {

    long countByMemailContaining(String memail);

    // findBy 컬럼명 ContainingOrderBy컬럼명Desc
    // SELECT * FROM 테이블명
    // WHERE BTITLE LIKE '%' || '검색어' || '%' ORDER BY NO DESC
    List<MemberCHG> findByMemailContainingOrderByNoDesc(String memail);

    // findBy 컬럼명 ContainingOrderBy컬럼명Desc
    // SELECT B.*, ROW_NUMBER() OVER (ORDER BY DESC) FROM BOARD10 B
    // WHERE BTITLE LIKE '%' || '검색어' || '%'
    List<MemberCHG> findByMemailContainingOrderByNoDesc(String Memail, Pageable page);

}
