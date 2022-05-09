package com.example.repository;

import java.util.List;

import com.example.entity.RtnRunCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RtnRunRepository extends JpaRepository<RtnRunCHG, Long>{

    // 루틴 실행 조회
    List<RtnRunCHG> findByRunseqEqualsOrderByRunnoDesc(long runseq);

    // 루틴 삭제 조회
    List<RtnRunCHG> findByRunnoInOrderByRunnoDesc(List<Integer> runno);
    
}
