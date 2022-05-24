package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.entity.RoutineCHG;
import com.example.entity.RoutineCHGProjection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<RoutineCHG, Long> {

    // 루틴 조회
    List<RoutineCHGProjection> findByMemberchg_memailEqualsOrderByRtnnoDesc(String memail, Pageable page);

    // 이메일이 포함된 전체개수
    long countByMemberchg_memailContaining(String email);
    
    // 루틴 개별 수정용 조회
    RoutineCHG findByMemberchg_memailAndRtnnoEquals(String memail, long rtnno);

    // 루틴 개별 조회
    RoutineCHGProjection findByRtnnoEquals(long rtnno);

    // 루틴 상세 조회
    List<RoutineCHG> findByMemberchg_memailAndRtnseq(String memail, long rtnseq);

    // 루틴 전체 삭제
    @Transactional
    int deleteByMemberchg_memailAndRtnnoIn(String memail, Long[] rtnno);

    // 루틴 개별 삭제
    @Transactional
    int deleteByMemberchg_memailAndRtnno(String memail, Long rtnno);

    // 루틴 삭제 seq
    @Transactional
    int deleteByMemberchg_memailAndRtnseq(String memail, Long rtnseq);

}
