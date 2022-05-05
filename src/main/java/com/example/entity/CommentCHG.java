package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@SequenceGenerator(name = "SEQ_COMMENT", sequenceName = "SEQ_COMMENT_CMTNO", allocationSize = 1, initialValue = 1)
public class CommentCHG {
    // 댓글 번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COMMENT") // 시퀀스 적용
    private Long cmtno;
    // 댓글 내용
    @Lob
    private String cmtcontent;
    // 좋아요 갯수 저장
    private Long cmtlike = 0L;
    // 등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    @Column(name = "CMTREGDATE")
    private Date cmtregdate;

    // 자유게시판 테이블
    @ManyToOne
    @JoinColumn(name = "bno")
    private CommunityCHG communitychg;

    @ManyToOne
    @JoinColumn(name = "memail")
    private MemberCHG memberchg;

    // 좋아요
    // @JsonBackReference
    // @OneToMany(mappedBy = "commentchg", cascade = CascadeType.REMOVE)
    // private List<CmtLikeCHG> cmtLikechglist = new ArrayList<>();

}
