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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@SequenceGenerator(name = "SEQ_INQUIRY", sequenceName = "SEQ_INQUIRY_QNO", allocationSize = 1, initialValue = 1)
public class InquiryCHG {
    // 문의번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INQUIRY") // 시퀀스 적용
    private Long qno;
    // 제목
    private String qtitle;
    // 내용
    @Lob
    private String qcontent;

    // 등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    @Column(name = "QREGDATE")
    private Date qregdate;

    // 회원테이블
    @ManyToOne
    @JoinColumn(name = "memail")
    private MemberCHG memberchg;

    // 문의 사진 테이블
    @JsonManagedReference
    @OneToMany(mappedBy = "inquirychg", cascade = CascadeType.REMOVE)
    private List<InquiryimgCHG> inquiryimgchgList = new ArrayList<>();

    // 문의 댓글 테이블
    @JsonManagedReference
    @OneToMany(mappedBy = "inquirychg", cascade = CascadeType.REMOVE)
    private List<IqcommentCHG> iqcommentchgList = new ArrayList<>();
}