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
public class UserDetailsServiceImpl implements UserDetailsService{
    
    @Autowired MemberRepository mRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UserDetailsService : " + username);
        
        MemberCHG member = mRepository.findById(username).orElse(null);

        // 권한정보를 문자열 배열로 만듦.
        String[] strRole = { member.getMrole() };

        //String 배열 권한을 Collection<Granted ... > 변환함
		Collection<GrantedAuthority> roles 
            = AuthorityUtils.createAuthorityList(strRole);
        
        // UserDetails => User
        User user = new User(member.getMemail(), member.getMpw(), roles);
        return user;
    }

    
}
