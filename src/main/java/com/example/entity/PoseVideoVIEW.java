package com.example.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "POSEVIDEO_VIEW")
public class PoseVideoVIEW {

    @Id
    private long vno;

    // 등록일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreationTimestamp // CURRENT_DATE
    private Date vregdate;

    // 영상이름
    public String vname;
    
    // 영상사이즈
    public Long vsize;
    
    // 영상타입
    public String vtype;
    
    // 영상
    @Lob
    private byte[] vvideo;

    public String meamil;

    public long pno;
    
}
