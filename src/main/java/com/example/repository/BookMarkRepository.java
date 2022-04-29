package com.example.repository;

import java.util.List;

import com.example.entity.BookMarkCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookMarkRepository extends JpaRepository<BookMarkCHG, Long> {
    
    // 북마크 조회
    long countByBmknoContaining(long bmkno);

    // 북마크 목록 (페이지네이션)-----------------임시 : Containing => Like--------------
    List<BookMarkCHG> findByBmknoLikeOrderByBmknoDesc(long bmkno, Pageable page);

    // 북마크 중복 확인
    BookMarkCHG findByChallengechg_chgnoAndMemberchg_memail(Long no, String memail);
}
