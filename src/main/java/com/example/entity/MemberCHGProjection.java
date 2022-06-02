package com.example.entity;

import java.time.LocalDate;
import java.util.Date;

public interface MemberCHGProjection {

    String getMemail();

    String getMpw();

    String getMid();

    String getMname();

    String getMbirth();

    Long getMgender();

    Long getMheight();

    Long getMweight();

    String getMphone();

    String getMrole();

    LocalDate getMregdate();

    Long getMrank();

    int getMstep();

}
