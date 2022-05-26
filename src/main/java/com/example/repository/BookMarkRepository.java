package com.example.repository;

import java.util.List;

import com.example.entity.BookMarkCHG;
import com.example.entity.BookmarkProjection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookMarkRepository extends JpaRepository<BookMarkCHG, Long> {
    
    // 북마크 조회
    long countByBmknoContaining(long bmkno);

    // 북마크 목록 (페이지네이션)
    List<BookmarkProjection> findByMemberchg_memailOrderByBmknoDesc(String memail, Pageable page);

    // 북마크 중복 확인
    BookMarkCHG findByChallengechg_chgnoAndMemberchg_memail(Long no, String memail);
}
