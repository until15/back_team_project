package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.RtnRunCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface RtnRunRepository extends JpaRepository<RtnRunCHG, Long> {

    // 루틴 실행 조회
    List<RtnRunCHG> findByRunseqEqualsOrderByRunnoDesc(long runseq);

    // 루틴 실행 번호 조회
    List<RtnRunCHG> findByRunnoEqualsOrderByRunnoDesc(Long[] runno);

    // 루틴 삭제 조회
    List<RtnRunCHG> findByRunnoIn(List<Integer> runno);

    // 루틴 삭제
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM RTNRUNCHG WHERE RUNSEQ IN :rseq", nativeQuery = true)
    public int deleteRtnrunGroup(@Param(value="rseq")Long rseq);

    // 루틴 삭제 조회
    @Query("select r from RtnRunCHG r where r.runno in :runno")
    List<RtnRunCHG> findRtnRunCHGsIn(Long[] runno);

}
