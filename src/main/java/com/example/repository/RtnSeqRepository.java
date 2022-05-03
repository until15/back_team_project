package com.example.repository;

import com.example.entity.RtnSeqCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RtnSeqRepository extends JpaRepository<RtnSeqCHG, String>{

    // INSERT INTO RTN_SEQCHG VALUES('RTN_SEQ', CURRENT_DATE , 1);
    
}
