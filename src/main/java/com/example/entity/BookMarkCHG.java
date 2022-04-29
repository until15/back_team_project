package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "BookMarkCHG")
@SequenceGenerator(
    name = "SEQ_BOOKMARK", 
    sequenceName = "SEQ_BOOKMARK_BMKNO", 
    allocationSize = 1, initialValue = 1)
public class BookMarkCHG {
    
    // 북마크 번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOOKMARK")
    private Long bmkno;

    // 즐겨찾기 여부
    private int bmkstate = 1;

    // 북마크 추가일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    @Column(name = "BMKREGDATE")
    private Date bmkregdate;

    // ---------------외래키---------------

    // 챌린지 테이블
    @ManyToOne
    @JoinColumn(name = "chgno")
    private ChallengeCHG challengechg;
    
    // 회원테이블
    @ManyToOne
    @JoinColumn(name = "memail")
    private MemberCHG memberchg;
}