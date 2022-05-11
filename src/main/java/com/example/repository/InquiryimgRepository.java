package com.example.repository;

import java.util.List;

import com.example.entity.InquiryCHGProjection;
import com.example.entity.InquiryimgCHG;
import com.example.entity.InquiryimgCHGProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryimgRepository extends JpaRepository<InquiryimgCHG, Long> {

    List<InquiryimgCHGProjection> findByInquirychg_qnoOrderByQimgnoDesc(long qno);

}
