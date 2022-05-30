package com.example.service;

import java.util.List;

import com.example.entity.CommentCHG;
import com.example.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository coRepository;

    @Override
    public int commentInsert(CommentCHG comment) {
        try {
            coRepository.save(comment);
            return 1;
        } catch (Exception e) {

        }
        return 0;
    }

    @Override
    public CommentCHG selectOneComment(long cmtno) {
        try {
            CommentCHG comment = coRepository.findById(cmtno).orElse(null);
            return comment;
        } catch (Exception e) {

        }
        return null;

    }

    @Override
    public List<CommentCHG> commentSelectList(Long bno) {
        try {
            List<CommentCHG> list = coRepository.findByCommunitychg_bnoOrderByCmtnoAsc(bno);

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int deleteComment(long cmtno) {
        try {

            coRepository.deleteById(cmtno);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public CommentCHG likeOne(long cmtno) {

        CommentCHG comment = coRepository.findById(cmtno).orElse(null);
        comment.setCmtno(comment.getCmtno());
        coRepository.save(comment);
        return comment;
    }

}
