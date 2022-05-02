package com.example.repository;

import java.util.List;

import com.example.entity.InquiryCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryCHG, Long> {

    long countByQtitleContaining(String qtitle);

    List<InquiryCHG> findByQtitleContainingOrderByQnoDesc(String qtitle, Pageable page);

}
