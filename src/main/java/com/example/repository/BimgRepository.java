package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.example.entity.BimgCHG;
import com.example.entity.BimgCHGProjection;

@Repository
public interface BimgRepository extends JpaRepository<BimgCHG, Long> {

    List<BimgCHGProjection> findByCommunitychg_bnoOrderByBimgnoDesc(long bno);

    List<BimgCHGProjection> findByBimgnoOrderByBimgnoDesc(long bimgno);

    BimgCHG findByMemberchg_memailAndBimgnoOrderByBimgnoDesc(String memail, long bimgno);

    @Modifying(clearAutomatically = true)
    int deleteByBimgnoOrderByCommunitychg_bno(long bimgno);

}