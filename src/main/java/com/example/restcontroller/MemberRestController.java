package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/member")
public class MemberRestController {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	JwtUtil jwtUtil;

	// 로그인
	// 127.0.0.1:9090/ROOT/api/member/login
	@RequestMapping(value = "/login", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> memberLoginPOST(
			@RequestBody MemberCHG member) {
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(member.toString());

			// Security 인증
			UserDetails user = userDetailsService.loadUserByUsername(member.getMemail());
			System.out.println("유저 아이디 : " + user);

			// 비밀번호 암호화
			BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
			System.out.println("비밀번호 암호화: " + bcpe.toString());

			// 토큰 발급
			System.out.println("토큰 발급: " + jwtUtil.generatorToken(member.getMemail()));

			// 유저의 암호와 입력한 암호가 일치하는 지 확인
			if (bcpe.matches(member.getMpw(), user.getPassword())) {

				// 토큰 발급
				String token = jwtUtil.generatorToken(member.getMemail());

				map.put("token", token);
				map.put("status", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return map;
	}

	// 127.0.0.1:9090/ROOT/api/member/mypage
	@RequestMapping(value = "/mypage", method = { RequestMethod.GET }, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> customerMypageGET(@RequestHeader(name = "token") String token) {
		System.out.println("MYPAGE:" + token);
		Map<String, Object> map = new HashMap<>();
		try {
			String username = jwtUtil.extractUsername(token);
			System.out.println(username);
			// 토큰이 있어야 실행됨.
			map.put("status", 200);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

}
