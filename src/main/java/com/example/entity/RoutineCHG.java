package com.example.entity;

import java.util.Date;

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

  // 등록일
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
  @CreationTimestamp // CURRENT_DATE
  private Date rtnregdate;
  
  // 자세 테이블
  @ManyToOne
  @JoinColumn(name = "pno")
  private PoseCHG posechg;
}
