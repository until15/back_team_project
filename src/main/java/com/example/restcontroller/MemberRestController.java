package com.example.restcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.service.MemberService;
import com.example.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/member")
public class MemberRestController {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	MemberService mService;

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

	// 127.0.0.1:9090/ROOT/api/member/updatemember
	// 회원정보수정 (토큰, 이름, 전화번호)
	@RequestMapping(value = "/updatemember", method = { RequestMethod.PUT }, consumes = {
			MediaType.ALL_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> customerupdateMemberPUT(@RequestHeader(name = "token") String token,
			@ModelAttribute MemberCHG member, @RequestParam(name = "mimage") MultipartFile file) throws IOException {
		System.out.println(member);
		Map<String, Object> map = new HashMap<>();
		try {
			String username = jwtUtil.extractUsername(token);
			System.out.println(username);
			MemberCHG member1 = mService.MemberSelectOne(member.getMemail());
			// 닉네임
			member1.setMid(member.getMid());
			// // 이름
			member1.setMname(member.getMname());
			// // 연락처
			member1.setMphone(member.getMphone());
			// // 키
			member1.setMheight(member.getMheight());
			// // 몸무게
			member1.setMweight(member.getMweight());

			// // 이미지
			member1.setMpname(file.getOriginalFilename());
			member1.setMprofile(file.getBytes());
			member1.setMpsize(file.getSize());
			member1.setMptype(file.getContentType());

			int ret = mService.MemberUpdate(member1);
			if (ret == 1) {
				map.put("status", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return map;
	}

	// 127.0.0.1:9090/ROOT/api/customer/updatepw
	// 암호변경 (토큰, 현재암호, 변경암호)
	// @RequestMapping(value = "/updatepw", method = { RequestMethod.PUT }, consumes
	// = {MediaType.ALL_VALUE }, produces = {MediaType.APPLICATION_JSON_VALUE })
	// public Map<String, Object> customerupdatepwPUT(@RequestHeader(name = "token")
	// String token,
	// @RequestBody MemberCHG member) {
	// Map<String, Object> map = new HashMap<>();
	// try {
	// String username = jwtUtil.extractUsername(token);
	// UserDetails user = userDetailsService.loadUserByUsername(member.getMemail());
	// BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
	// // 함호화 되지 않는 것과 암호화 된것 비교하기
	// if (bcpe.matches(member.getMpw(), user.getPassword()))
	// {mService.MemberUpdatePw(username, bcpe.encode(member.getMpw1()))

	// map.put("status", 200);

	// } catch (Exception e) {
	// e.printStackTrace();
	// map.put("status", 0); // 정상적이지 않을 때
	// }

	// return map;
	// }

}
