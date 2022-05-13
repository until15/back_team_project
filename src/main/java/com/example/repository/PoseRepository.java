package com.example.repository;

import java.util.List;

import com.example.entity.MemberCHGProjection;
import com.example.entity.PoseCHG;
import com.example.entity.PoseCHGProjection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoseRepository extends JpaRepository<PoseCHG, Long> {

    // 검색어가 포함된 전체 개수
    long countByPstepEqualsAndPnameContaining(int step, String title);

    PoseCHGProjection findByPno(Long Pno);

    // 검색어 + 페이지네이션
    List<PoseCHG> findByPnameContainingOrderByPnoDesc(String title, Pageable page);

    // 검색어 + 페이지네이션 (step 1인 것만)
    List<PoseCHG> findByPstepEqualsAndPnameContainingOrderByPnoDesc(int step, String title, Pageable page);

    // 수정용 조회
    PoseCHG findByMemberchg_memailAndPnoEqualsOrderByPnoDesc(String memail, long pno);

}
