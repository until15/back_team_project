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

    long countByMidContaining(String mid);

    // memberprojection
    MemberCHGProjection findByMemail(String memail);

    MemberCHG findByMemailOrderByMemailDesc(String memail);

    // Containing 입력값 포함된 것 전부
    MemberCHGProjection findByMid(String mid);

    MemberCHGProjection findByMemailAndMid(String memail, String mid);

    MemberCHGProjection findByMnameAndMbirth(String mname, String mbirth);

    MemberCHGProjection findByMpw(String mpw);

    @Query(value = "SELECT MID FROM MEMBERCHG WHERE MID NOT IN (=:mid)", nativeQuery = true)
    public MemberCHGProjection selectMidOne(@Param(value = "mid") String mid);

    @Query(value = "SELECT FROM MEMBERCHG WHERE MEMAIL =:memail", nativeQuery = true)
    public MemberCHG selectOneMember(@Param(value = "memail") String memail);

    // findBy 컬럼명 ContainingOrderBy컬럼명Desc
    // SELECT B.*, ROW_NUMBER() OVER (ORDER BY DESC) FROM BOARD10 B
    // WHERE BTITLE LIKE '%' || '검색어' || '%'
    List<MemberCHG> findByMemailContainingOrderByMemailDesc(String memail, Pageable page);

    List<MemberCHGProjection> findByMemailOrMidContainingOrderByMemailDesc(String memail, String mid, Pageable page);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE MEMBERCHG SET MPW=:mpw WHERE MEMAIL=:memail", nativeQuery = true)
    public int updatePw(@Param(value = "memail") String memail);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE MEMBERCHG SET MPW=:mpw WHERE MEMAIL=:memail", nativeQuery = true)
    public int updatePwMember(@Param(value = "memail") String memail, @Param(value = "mpw") String mpw);

}
