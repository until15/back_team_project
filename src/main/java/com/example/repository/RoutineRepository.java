package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.RoutineCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<RoutineCHG, Long> {

    // 루틴 조회
    List<RoutineCHG> findByMemberchg_memailEqualsOrderByRtnnoDesc(String memail);

    List<RoutineCHG> findByRtnnoIn(List<RoutineCHG> rtnnos);

    List<RoutineCHG> findByMemberchg_memailAndRtnseq(String memail, long rtnseq);

    @Transactional
    int deleteByMemberchg_memailAndRtnnoIn(String memail, Long[] rtnno);

}
