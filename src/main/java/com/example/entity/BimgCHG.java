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

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@SequenceGenerator(name = "SEQ_Bimage", sequenceName = "SEQ_Bimage_BIMGNO", allocationSize = 1, initialValue = 1)
public class BimgCHG {
    // 이미지 번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_Bimage") // 시퀀스 적용
    private Long bimgno;
    // 이미지
    @Lob
    @Column(nullable = true)
    private byte[] bimage;
    // 이미지 사이즈
    private Long bimgsize = 0L;
    // 이미지 타입
    private String bimgtype;
    // 이미지 이름
    private String bimgname;

    // 등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    @Column(name = "BIMGREGDATE")
    private Date bimgregdate;

    // 자유게시판 테이블
    @ManyToOne
    @JoinColumn(name = "bno")
    private CommunityCHG communitychg;
}