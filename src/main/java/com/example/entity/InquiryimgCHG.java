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
@SequenceGenerator(name = "SEQ_INQUIRYIMG", sequenceName = "SEQ_INQUIRYIMG_QIMGNO", allocationSize = 1, initialValue = 1)
public class InquiryimgCHG {
    // 이미지코드
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INQUIRYIMG") // 시퀀스 적용
    private Long qimgno;
    // 문의 이미지
    @Lob
    private byte[] qimage;
    // 이미지 사이즈
    private Long qimgsize = 0L;
    // 이미지 타입
    private String qimgtype;
    // 이미지 이름
    private String qimgname;
    // 등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    @Column(name = "QIMGREGDATE")
    private Date qimgregdate;

    // 문의 테이블
    @ManyToOne
    @JoinColumn(name = "qno")
    private InquiryCHG inquirychg;
}