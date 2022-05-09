package com.example.service;

import java.util.Collection;

import com.example.entity.MemberCHG;
import com.example.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{	// UserDetailsService 를 상속받음
    
    @Autowired MemberRepository mRepository;

    // 로그인에서 입력하는 정보 중에서 아이디를 받음
    // MemberRepository 를 이용해서 정보를 가져와서 UserDetails로 리턴
    // 아이디, 암호, 권한
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UserDetailsService : " + username);
        
        MemberCHG member = mRepository.findById(username).orElse(null);
        
        // 회원에 관한 정보를 전부 불러옴
//        System.out.println("로그인 후 아이디로 조회 : " + member.toString());

        // 권한정보를 문자열 배열로 만듦.
        String[] strRole = { member.getMrole() };
//        System.out.println("배열로 만든 권한 : " + strRole);


        //String 배열 권한을 Collection<Granted ... > 변환함
		Collection<GrantedAuthority> roles 
            = AuthorityUtils.createAuthorityList(strRole);
        
		System.out.println("배열을 collection으로 : " + roles);
		
		// 자동으로 세션에 데이터가 들어감
        // 목표: 아이디와 비밀번호, 권한을 리턴시킴
        // UserDetails => User
        User user = new User(member.getMemail(), member.getMpw(), roles);
        System.out.println("필요한 정보들을 user에 담음 : " + user);
        
        // SecurityConfig에서 사용
        return user;
    }

    
}
