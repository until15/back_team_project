package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.InquiryCHG;
import com.example.entity.InquiryCHGProjection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryCHG, Long> {

    long countByQtitleContaining(String qtitle);

    // 고객용 주문내역
    List<InquiryCHG> findByMemberchg_memailAndQtitleContainingOrderByQnoDesc(String memail, String qtitle,
            Pageable page);

    InquiryCHGProjection findByQno(long qno);

    List<InquiryCHGProjection> findByQnoOrderByQno(long qno);

    List<InquiryCHG> findByQtitleContainingOrderByQnoDesc(String qtitle, Pageable page);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE INQUIRYCHG SET COM=COM+1 WHERE QNO =:qno", nativeQuery = true)
    public int updateComOne(@Param(value = "qno") long qno);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE INQUIRYCHG SET COM=COM-1 WHERE QNO =:qno", nativeQuery = true)
    public int updateComTwo(@Param(value = "qno") long qno);

}
