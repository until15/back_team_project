package com.example.repository;

import java.util.List;

import com.example.entity.BimgCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BimgRepository extends JpaRepository<BimgCHG, Long> {

    List<BimgCHG> findByCommunitychg_bnoOrderByBimgnoDesc(long bno);

}
