package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
@Table(name = "PayCHG")
public class PayCHG {

    // 고유 ID
    @Id
    private String impuid;

    // 결제 ID
    private String merchantuid;

    // 결제 금액
    private int pprice;

    // 환불된 총 금액(환불 가능 금액 계산시 사용)
    private int cancelprice;

    // 환급 이유
    @Column(nullable = true)
    private String reason;

    // 참여 중인 챌린지 번호 FK
    @ManyToOne
    @JoinColumn(name = "jno")
    public JoinCHG joinchg;

    // 결제 수단
    private String pmethod;

    // 결제일
    @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    @Column(name = "PAYREGDATE")
    private Date pregdate;

}
