package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.ConfirmCHG;
import com.example.jwt.JwtUtil;


@RestController
@RequestMapping("/api/confirm")
public class ConfirmRestController {

	@Autowired JwtUtil jwtUtil;
	
	// 인증하기
	// 127.0.0.1:9090/ROOT/api/confirm/insert.json
	@RequestMapping(value="/insert.json", 
			method = {RequestMethod.POST},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	
	public Map<String, Object> insertJoinPOST(
			@RequestParam(name = "jno") long jno,
			@RequestHeader(name="token") String token,
			@RequestBody ConfirmCHG confirm){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(jno);	// 참가번호
			System.out.println(token);	// 토큰
			System.out.println(confirm.toString());	// 인증글
			
			// 토큰에서 아이디 추출
			String username = jwtUtil.extractUsername(token);
			System.out.println("유저이름 : " + username);
			
			
			
			map.put("status", 200);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}
		
		return map;
	}
	
	
	// 인증 수정하기
	
	// 인증 삭제하기
	
	// 인증 1개 조회
	
	// 인증 리스트
	
	// 인증 성공 여부
	
}
