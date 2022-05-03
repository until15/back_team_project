package com.example.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name="RtnRunCHG")
@SequenceGenerator(name = "SEQ_RTNRUN", sequenceName = "SEQ_RTNRUN_RUNNO", allocationSize = 1, initialValue = 1)
public class RtnRunCHG {
  // 실행 번호
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RTNRUN") // 시퀀스 적용
  private Long runno;

  // 등록일
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
  @CreationTimestamp // CURRENT_DATE
  private Date runregdate;

  // 루틴 생성 테이블
  @ManyToOne
  @JoinColumn(name = "rtnno")
  private RoutineCHG routinechg;

//   // 챌린지 테이블
//   @OneToOne(mappedBy = "rtnrun")
//   private ChallengeCHG challengechg;
}
