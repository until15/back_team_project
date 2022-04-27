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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "PoseCHG") // 테이블명
// 생성할 시퀀스
@SequenceGenerator(name = "SEQ_POSE", sequenceName = "SEQ_POSE_PNO", allocationSize = 1, initialValue = 1)
public class PoseCHG {
	
    // 자세번호
    @Id
    @Column(name = "PNO") // 컬럼명
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POSE") // 시퀀스 적용
    private Long pno;
    
    // 자세이름
    private String pname;
    
    // 자세 부위
    private String ppart;
    
    // 자세내용
    @Lob
    private String pcontent;
    
    // 자세난이도
    private Long plevel;
    
    // 삭제 유무
    private int pstep;
    
    // 자세등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    @Column(name = "PREGDATE")
    private Date pregdate;
    
    // 자세 영상 테이블
    @OneToOne(mappedBy = "posechg", cascade = CascadeType.ALL)
    private VideoCHG videochg;
    
    // 회원테이블
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "memail")
    private MemberCHG memberchg;
    
    // // 루틴 생성 테이블
    // @OneToMany(mappedBy = "posechg")
    // private List<RoutineCHG> routinechgList = new ArrayList<>();
}