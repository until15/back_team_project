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
    List<PayCHGProjection> findByJoin_Memberchg_memailContainingOrderByJoin_jnoDesc(String memail, Pageable page);

}
