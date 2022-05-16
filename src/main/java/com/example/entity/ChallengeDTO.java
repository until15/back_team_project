package com.example.entity;

import java.sql.Timestamp;

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
    
    private Long chgno;
    private Long chglike;
    private Long chglevel;
    private String chgtitle;
    private String chgintro;
    private String chgcontent;
    private Timestamp chgstart;
    private Timestamp chgend;
    private Timestamp recruitstart;
    private Timestamp recruitend;

    // 오류 : Could not locate appropriate constructor on class
    private int recstate = 1;
    private Long chgcnt = 1L;
    private Long chgfee;
    private byte[] chgimage;
	private Long chgisize;
	private String chgitype;
	private String chginame;
    
}
