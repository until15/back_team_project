package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.service.MemberService;
import com.example.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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

	@Autowired
	ResourceLoader rLoader;

	@Value("${default.image}")
	String DEFAULT_IMAGE;

	// 회원가입
	// 127.0.0.1:9090/ROOT/api/member/join
	@RequestMapping(value = "/join", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> joinPOST(@ModelAttribute MemberCHG member, @AuthenticationPrincipal User user,
			@RequestParam(name = "mimage") MultipartFile file) throws IOException {
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();

		member.setMpw(bcpe.encode(member.getMpw())); // 비밀번호 설정
		member.setMrole(member.getMrole()); // 권한 설정

		// 프로필 이미지
		member.setMprofile(file.getBytes()); // 프로필 이미지
		member.setMpname(file.getOriginalFilename()); // 이미지 이름
		member.setMpsize(file.getSize()); // 이미지 사이즈
		member.setMptype(file.getContentType()); // 이미지 타입

		Map<String, Object> map = new HashMap<>();
		try {
			int ret = mService.MemberInsertOne(member);
			if (ret == 1) {
				map.put("status", 200);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return map;
	}

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

	// 암호변경 (토큰, 현재암호, 변경암호)
	// 127.0.0.1:9090/ROOT/api/member/updatepw
	@RequestMapping(value = "/updatepw", method = { RequestMethod.PUT }, consumes = {
			MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> customerupdatepwPUT(@RequestHeader(name = "token") String token,
			@RequestBody MemberCHG member) {
		System.out.println(member.toString());
		Map<String, Object> map = new HashMap<>();
		try {
			String username = jwtUtil.extractUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(member.getMemail());
			BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
			// 함호화 되지 않는 것과 암호화 된것 비교하기
			if (bcpe.matches(member.getMpw(), user.getPassword())) {

				mService.MemberUpdatePw(username, bcpe.encode(member.getMpw1()));
				map.put("status", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0); // 정상적이지 않을 때
		}

		return map;
	}

	// 회원탈퇴(삭제가 아닌 수정)
	// 127.0.0.1:9090/ROOT/api/member/deletemember
	@RequestMapping(value = "deletemember", method = { RequestMethod.PUT }, consumes = {
			MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> selectMemberOneGET(@RequestHeader(name = "token") String token,
			@RequestBody MemberCHG member) {
		Map<String, Object> map = new HashMap<>();
		try {
			String username = jwtUtil.extractUsername(token);
			System.out.println(username);
			MemberCHG member1 = mService.MemberSelectOne(member.getMemail());
			member1.setMstep(member.getMstep());

			int ret = mService.MemberLeave(member1);
			if (ret == 1) {
				map.put("status", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return map;
	}

	// 회원 1명 조회
	// 127.0.0.1:9090/ROOT/api/member/selectmemberone
	@RequestMapping(value = "selectmemberone", method = { RequestMethod.GET }, consumes = {
			MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> selectMemberOneGET(@RequestBody MemberCHG member) throws IOException {
		Map<String, Object> map = new HashMap<>();
		try {
			MemberCHG member1 = mService.MemberSelectOne(member.getMemail());

			if (member1 != null) {
				map.put("result", member1);
				map.put("status", 200);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return map;
	}

	// 회원 프로필 사진 조회
	// 127.0.0.1:9090/ROOT/api/member/profile
	@RequestMapping(value = "/profile", method = { RequestMethod.GET }, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<byte[]> selectProfileGET(
			@RequestBody MemberCHG member) throws IOException {
		try {
			MemberCHG member1 = mService.MemberSelectOne(member.getMemail());

			if (member1.getMpsize() > 0) {
				HttpHeaders header = new HttpHeaders();
				if (member1.getMptype().equals("image/jpeg")) {
					header.setContentType(MediaType.IMAGE_JPEG);
				} else if (member1.getMptype().equals("image/png")) {
					header.setContentType(MediaType.IMAGE_PNG);
				} else if (member1.getMptype().equals("image/gif")) {
					header.setContentType(MediaType.IMAGE_GIF);
				}
				ResponseEntity<byte[]> response = new ResponseEntity<>(member1.getMprofile(), header, HttpStatus.OK);
				return response;
			} else {
				InputStream is = rLoader.getResource(DEFAULT_IMAGE).getInputStream();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.IMAGE_JPEG);
				ResponseEntity<byte[]> response = new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
				return response;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 회원리스트
	// 127.0.0.1:9090/ROOT/api/member/selectmemberlist
	@RequestMapping(value = "/selectmemberlist", method = { RequestMethod.GET }, consumes = {
			MediaType.ALL_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> boardSelectListGET(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "memeil", defaultValue = "") String memeil) {
		Map<String, Object> map = new HashMap<>();
		try {

			Pageable pageable = PageRequest.of(page - 1, 10);
			List<MemberCHG> list = mService.MemberSelectList(pageable, memeil);
			if (list != null) {
				map.put("status", 200);
				map.put("result", list);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}

		return map;
	}

}
