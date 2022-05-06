package com.example.repository;

import java.util.List;

import com.example.entity.CmtLikeCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmtlikeRepository extends JpaRepository<CmtLikeCHG, Long> {

    // 좋아요 조회
    long countByCmtlikenoContaining(long cmtlikeno);

    // 좋아요 데이터
    long countByCommentchg_cmtno(long cmtno);

    // 중복 확인
    CmtLikeCHG findByCommentchg_cmtnoAndMemberchg_memail(Long no, String memail);

    // 좋아요 목록
    List<CmtLikeCHG> findByMemberchg_memailOrderByCmtlikenoDesc(String memail, Pageable page);

}
