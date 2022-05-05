package com.example.repository;

import com.example.entity.RtnRunSeqCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RtnRunSeqRepository extends JpaRepository<RtnRunSeqCHG, String>{

    // INSERT INTO RTN_RUN_SEQCHG VALUES('RUN_SEQ', CURRENT_DATE , 1);
    
}
