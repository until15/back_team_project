package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "POSEVIDEO_VIEW")
public class PoseVideoVIEW {

    @Id
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

    private Long pno;
    
}
