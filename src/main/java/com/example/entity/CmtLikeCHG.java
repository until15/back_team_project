package com.example.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@SequenceGenerator(name = "SEQ_CMTLIKE", sequenceName = "SEQ_CMTLIKE_CMTLIKENO", allocationSize = 1, initialValue = 1)
public class CmtLikeCHG {

    // 댓글 번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CMTLIKE") // 시퀀스 적용
    private Long cmtlikeno;
    // 댓글 내용

    // 등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    private Date cmtlikeregdate;

    @ManyToOne
    @JoinColumn(name = "memail")
    private MemberCHG memberchg;

    @ManyToOne
    @JoinColumn(name = "cmtno")
    private CommentCHG commentchg;
}
