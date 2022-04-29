package com.example.service;

import java.util.List;

import com.example.entity.BookMarkCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface BookMarkService {
    
    // 북마크 추가
    public int insertBookMark(BookMarkCHG bookmark);

    // 중복
    public BookMarkCHG duplicateInsert(Long no, String memail);

    // 북마크 삭제
    public int deleteBookMark(long bmkno);

    // 북마크 조회
    public BookMarkCHG bookmarkSelectOne(long bmkno);

    // 북마크 목록
    public List<BookMarkCHG> bookmarkSelectList(Pageable page, long bmkno);
}
