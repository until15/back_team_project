package com.example.repository;

import com.example.entity.BookMarkCHG;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookMarkRepository extends JpaRepository<BookMarkCHG, Long> {
    
    // 북마크 조회
    long countByBmknoContaining(long bmkno);
}
