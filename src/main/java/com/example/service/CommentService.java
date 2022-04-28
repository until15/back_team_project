package com.example.service;

import java.util.List;

import com.example.entity.CommentCHG;

import org.springframework.stereotype.Service;

@Service
public interface CommentService {

    public int commentInsert(CommentCHG comment);

    public CommentCHG selectOneComment(long cmtno);

    public List<CommentCHG> commentSelectList(CommentCHG comment);

    public int deleteComment(long cmtno);

    public CommentCHG likeOne(long cmtno);

}
