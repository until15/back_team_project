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
@SequenceGenerator(name = "SEQ_IQCOMMENT", sequenceName = "SEQ_IQCOMMENT_IQCNTNO", allocationSize = 1, initialValue = 1)
public class IqcommentCHG {
    // 댓글번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_IQCOMMENT") // 시퀀스 적용
    private Long iqcmtno;
    // 댓글내용
    @Lob
    private String iqcontent;
    // 등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    @Column(name = "IQCMTGREGDATE")
    private Date iqcmtregdate;
    // 문의 테이블
    @ManyToOne
    @JoinColumn(name = "qno")
    private InquiryCHG inquirychg;
}