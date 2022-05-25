package com.example.repository;

import java.util.List;

import com.example.entity.PayCHG;
import com.example.entity.PayCHGProjection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepository extends JpaRepository<PayCHG, String> {

    // 유저 참가비 조회
    List<PayCHGProjection> findByJoinchg_Memberchg_memailContainingOrderByJoinchg_jnoDesc(String memail, Pageable page);

    // 유저 참가비 조회 + 참가 챌린지 번호
    PayCHGProjection findByJoinchg_Memberchg_memailAndJoinchg_jnoEquals(String memail, long jno);

    // 유저 환급용 참가비 조회
    PayCHG findByImpuidEquals(String uid);

    // 유저 환급용 달성률 조회
    PayCHGProjection findByJoinchg_jnoEquals(long jno);

    // 이메일이 포함된 전체 개수
    long countByJoinchg_Memberchg_memailContaining(String email);

}
