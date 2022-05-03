package com.example.repository;

import java.util.List;

import com.example.entity.LikeCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeCHG, Long> {
    

    // 좋아요 조회
    long countByLnoContaining(long lno);

    // 좋아요 데이터
    long countByChallengechg_Chgno(long chgno);

    // 중복 확인
    LikeCHG findByChallengechg_chgnoAndMemberchg_memail(Long no, String memail);

    // 좋아요 목록
    List<LikeCHG> findByMemberchg_memailOrderByLnoDesc(String memail, Pageable page);

   
}
