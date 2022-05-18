package com.example.repository;

import java.util.List;

import com.example.entity.InquiryCHG;
import com.example.entity.InquiryCHGProjection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryCHG, Long> {

    long countByQtitleContaining(String qtitle);

    // 고객용 주문내역
    List<InquiryCHG> findByMemberchg_memailAndQtitleContainingOrderByQnoDesc(String memail, String qtitle,
            Pageable page);

    InquiryCHGProjection findByQno(long qno);

    List<InquiryCHG> findByQtitleContainingOrderByQnoDesc(String qtitle, Pageable page);

}
