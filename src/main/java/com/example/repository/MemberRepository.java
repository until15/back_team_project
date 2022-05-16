package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.MemberCHG;
import com.example.entity.MemberCHGProjection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberCHG, String> {

    long countByMemailContaining(String memail);

    @Transactional
    @Query(value = "SELECT * FROM MEMBERCHG WHERE MEMAIL =:memail", nativeQuery = true)
    public MemberCHG selectOneMember(@Param(value = "memail") String memail);

    // findBy 컬럼명 ContainingOrderBy컬럼명Desc
    // SELECT B.*, ROW_NUMBER() OVER (ORDER BY DESC) FROM BOARD10 B
    // WHERE BTITLE LIKE '%' || '검색어' || '%'
    List<MemberCHG> findByMemailContainingOrderByMemailDesc(String Memail, Pageable page);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE MEMBERCHG SET MPW=:mpw WHERE MEMAIL=:memail", nativeQuery = true)
    public int updatePwMember(@Param(value = "memail") String memail, @Param(value = "mpw") String mpw);

    // memberprojection
    MemberCHGProjection findByMemail(String memail);

    MemberCHGProjection findByMid(String mid);

}
