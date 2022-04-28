package com.example.service;

import java.util.List;

import com.example.entity.BookMarkCHG;
import com.example.repository.BookMarkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

public class BookMarkServiceImpl implements BookMarkService {
    
    @Autowired BookMarkRepository bmkRepository;


    // 북마크 추가 
    @Override   
    public int insertBookMark(BookMarkCHG bookmark) {
        try {
            bmkRepository.save(bookmark);
            return 1;
        }
        catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 북마크 삭제
    @Override
    public int deleteBookMark(long bmkno) {
        try {
            bmkRepository.deleteById(bmkno);
            return 1;
        }
        catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 북마크 조회
    @Override
    public BookMarkCHG bookmarkSelectOne(long bmkno) {
        // TODO Auto-generated method stub
        return null;
    }

    // 북마크 목록
    @Override
    public List<BookMarkCHG> bookmarkSelectList(Pageable page, String bookmark) {
        // TODO Auto-generated method stub
        return null;
    }

    
}
