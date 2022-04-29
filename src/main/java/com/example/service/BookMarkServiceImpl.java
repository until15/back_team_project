package com.example.service;

import java.util.List;

import com.example.entity.BookMarkCHG;
import com.example.repository.BookMarkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
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
        try {
            return bmkRepository.findById(bmkno).orElse(null);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 북마크 목록
    @Override
    public List<BookMarkCHG> bookmarkSelectList(Pageable page, long bmkno) {
        try {
            List<BookMarkCHG> list = bmkRepository.findByBmknoLikeOrderByBmknoDesc(bmkno, page);
            return list;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 중복
    @Override
    public BookMarkCHG duplicateInsert(Long no, String memail) {
        try {
            return bmkRepository.findByChallengechg_chgnoAndMemberchg_memail(no, memail);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
}
