package com.example.repository;

import java.util.List;

import com.example.entity.RoutineCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends JpaRepository<RoutineCHG, Long>{

    List<RoutineCHG> findByRtnnameContainingOrderByRtnnoDesc(String[] rtnname);
    
}
