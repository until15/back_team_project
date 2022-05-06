package com.example.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "COMMUNITYCHG")
@SequenceGenerator(name = "SEQ_COMMUNITY", sequenceName = "SEQ_COMMUNITY_BNO", allocationSize = 1, initialValue = 1)
public class CommunityCHG {
    // 글번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COMMUNITY") // 시퀀스 적용
    private Long bno;
    // 글제목
    private String btitle;
    // 글내용
    @Lob
    private String bcontent;
    // 조회수
    private Long bhit = 1L;

    // 등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    @Column(name = "BREGDATE")
    private Date bregdate;

    // 회원테이블
    @ManyToOne
    @JoinColumn(name = "memail")
    private MemberCHG memberchg;

    // 자유게시판 이미지 테이블
    // @JsonBackReference
    // @OneToMany(mappedBy = "communitychg", cascade = CascadeType.REMOVE)
    // private List<BimgCHG> bimgchgList = new ArrayList<>();

    // 댓글 테이블
    // @JsonBackReference
    // @OneToMany(mappedBy = "communitychg", cascade = CascadeType.REMOVE)
    // private List<CommentCHG> commentchgList = new ArrayList<>();

    @Transient
    private String[] imageurl;
}
