package com.example.repository;

import java.util.List;

import com.example.entity.PoseCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoseRepository extends JpaRepository<PoseCHG, Long>{

    // 검색어가 포함된 전체 개수
    long countByPnameContaining(String title);

    // 검색어 + 페이지네이션
    List<PoseCHG> findByPnameContainingOrderByPnoDesc(String title, Pageable page);
    
}
