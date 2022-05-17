package com.example.entity;

//import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChallengeDTO {
    
    // 번호
    private Long chgno;

    // 좋아요
    private Long chglike;

    // 난이도
    private Long chglevel;

    // 제목
    private String chgtitle;

    // 소개
    private String chgintro;

    // 내용
    private String chgcontent;

    // 참여인원
    private Long chgcnt;

    // 참가비
    private Long chgfee;
    // private Timestamp chgstart;
    // private Timestamp chgend;
    // private Timestamp recruitstart;
    // private Timestamp recruitend;
    // private int recstate;
    
}
