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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name="RoutineCHG")
@SequenceGenerator(name = "SEQ_ROUTINE", sequenceName = "SEQ_ROUTINE_RTNNO", allocationSize = 1, initialValue = 1)
public class RoutineCHG {
  // 루틴 번호
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ROUTINE") // 시퀀스 적용
  private Long rtnno;
  // 요일
  private String rtnday;
  // 운동 횟수
  private Long rtncnt;
  // 운동 세트
  private Long rtnset;
  // 루틴 명
  private String rtnname;
  // seq
  @Column(nullable = true)
  private Long rtnseq=0L;

  // 등록일
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
  @CreationTimestamp // CURRENT_DATE
  private Date rtnregdate;
  
  // 자세 테이블
  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "pno")
  private PoseCHG posechg;

  // 멤버 테이블
  @JsonBackReference(value="member")
  @ManyToOne
  @JoinColumn(name = "memail")
  private MemberCHG memberchg;

  // 루틴 실행 테이블 (cascade 설정)
  @OneToMany(mappedBy = "routinechg", cascade = CascadeType.REMOVE)
  @JsonBackReference(value="rtnrun")
  private List<RtnRunCHG> rtnrunchgList = new ArrayList<>();
  
}
