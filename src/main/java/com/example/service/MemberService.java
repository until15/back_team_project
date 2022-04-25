package com.example.service;

import java.util.List;

import com.example.entity.MemberCHG;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    // 회원가입
    public int MemberInsertOne(MemberCHG member);

    // 회원 정보 1명 확인
    public MemberCHG MemberSelectOne(String memail);

    // 회원 목록 (관리자, 페이지 1 2 3)
    public List<MemberCHG> MemberSelectList(Pageable page, String memail);

    // 회원 수 구하기
    public long MemberCountSelect(String memail);

    // 회원 프로필 이미지 가져오기
    public MemberCHG MemberProfileSelect(String memail);

    // 회원 프로필 이미지 수정하기
    public int MemberProfileUpdate(MemberCHG member);

    // 회원 프로필 정보 변경
    public int MemberUpdate(MemberCHG member);

    // 회원 암호 수정
    public long MemberUpdatePw(MemberCHG member);

    // 회원 탈퇴 (삭제가 아닌 update)
    public int MemberLeave(MemberCHG member);
    
}
