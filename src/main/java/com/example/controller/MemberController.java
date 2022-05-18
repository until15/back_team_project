package com.example.controller;


import java.io.IOException;

import com.example.entity.MemberCHG;

import com.example.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/member")
public class MemberController {
    
    @Autowired MemberService mService;
    
    // 로그인
    // 127.0.0.1:9090/ROOT/member/login
    @GetMapping(value = "/login")
    public String loginGET() {
        return "login";
    }

    // 회원가입
    // 127.0.0.1:9090/ROOT/member/join
    @GetMapping(value = "/join")
    public String joinGET() {
        return "join";
    }

    // 회원가입 동작, 프로필 사진
    // 127.0.0.1:9090/ROOT/member/joinaction
    @PostMapping(value = "/joinaction")
    public String joinPOST( 
        @ModelAttribute MemberCHG member,
        @AuthenticationPrincipal User user,                // userDetailsService 
        @RequestParam(name = "mimage") MultipartFile file  // 이미지
    ) throws IOException {
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        System.out.println(bcpe.toString()); // 확인

        member.setMpw( bcpe.encode( member.getMpw() ) );  // 비밀번호 설정
        member.setMrole(member.getMrole());               // 권한 설정

        // 프로필 이미지
        System.out.println(file.getOriginalFilename());
        member.setMprofile(file.getBytes());          // 프로필 이미지
        member.setMpname(file.getOriginalFilename()); // 이미지 이름
        member.setMpsize(file.getSize());             // 이미지 사이즈
        member.setMptype(file.getContentType());      // 이미지 타입

        mService.memberInsertOne(member);
        
        return "redirect:/member/login";
    }    


}
