package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "VideoCHG") // 테이블명
// 생성할 시퀀스
@SequenceGenerator(name = "SEQ_VIDEO", sequenceName = "SEQ_VIDEO_VNO", allocationSize = 1, initialValue = 1)
public class VideoCHG {

    // 영상번호
    @Id
    @Column(name = "VNO") // 컬럼명
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VIDEO") // 시퀀스 적용
    private Long vno;
    
    // 영상이름
    private String vname;
    
    // 영상사이즈
    private Long vsize;
    
    // 영상타입
    private String vtype;
    
    // 영상
    @Lob
    private byte[] vvideo;
    
    // 영상등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    @Column(name = "VREGDATE")
    private Date vregdate;

    // 자세 테이블
    @OneToOne
    @JoinColumn(name = "pno")
    private PoseCHG posechg;
}