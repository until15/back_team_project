package com.example.service;

import java.util.List;

import com.example.entity.MemberCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    // 회원가입
    public int memberInsertOne(MemberCHG member);

    // 회원 정보 1명 확인
    public MemberCHG memberSelectOne(String memail);

    // 회원 목록 (관리자, 페이지 1 2 3)
    public List<MemberCHG> memberSelectList(Pageable page, String memail);

    // 회원 수 구하기
    public long memberCountSelect(String memail);

    // 회원 프로필 이미지 가져오기
    public MemberCHG memberProfileSelect(String memail);

    // 회원 프로필 이미지 수정하기
    public int memberProfileUpdate(MemberCHG member);

    // 회원 프로필 정보 변경
    public int memberUpdate(MemberCHG member);

    // 회원 암호 수정
    public long memberUpdatePw(String memail, String mpw);

    // 회원 정보(삭제용)
    public MemberCHG memberSelectMemail(String memail);

    // 회원 탈퇴 (삭제가 아닌 update)
    public int memberLeave(MemberCHG member);

}
