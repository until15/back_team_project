package com.example.service;

import java.util.List;

import com.example.entity.MemberCHG;
import com.example.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberRepository mRepository;

    // 회원 가입
    @Override
    public int MemberInsertOne(MemberCHG member) {
        try {
            mRepository.save(member);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 회원 1명 조회 (사용자 프로필 확인)
    @Override
    public MemberCHG MemberSelectOne(String memail) {
        try {
            MemberCHG member = mRepository.findById(memail).orElse(null);
            return member;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 회원 목록 (관리자용)
    @Override
    public List<MemberCHG> MemberSelectList(Pageable page, String memail) {
        try {
            List<MemberCHG> list = mRepository.findByMemailContainingOrderByMemailDesc(memail, page);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 회원 정보 수정
    @Override
    public int MemberUpdate(MemberCHG member) {
        try {
            mRepository.save(member);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 회원 암호 수정
    @Override
    public long MemberUpdatePw(MemberCHG member) {
        try {
            mRepository.save(member);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 회원 탈퇴 (update)
    @Override
    public int MemberLeave(MemberCHG member) {
        try {
            mRepository.save(member);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 회원 수 구하기
    @Override
    public long MemberCountSelect(String memail) {
        try {
            long count = mRepository.countByMemailContaining(memail);
            return count;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 회원 프로필 이미지 가져오기
    @Override
    public MemberCHG MemberProfileSelect(String memail) {
        try {
            MemberCHG member = mRepository.findById(memail).orElse(null);
            return member;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    // 회원 프로필 이미지 수정
    @Override
    public int MemberProfileUpdate(MemberCHG member) {
        try {
            mRepository.save(member);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
