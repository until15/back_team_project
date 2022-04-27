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

import com.example.entity.ChallengeCHG;
import com.example.entity.JoinCHG;
import com.example.entity.JoinProjection;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.service.JoinService;

@RestController
@RequestMapping("/api/join")
public class JoinRestController {

	@Autowired JoinService jService;
	
	@Autowired JwtUtil jwtUtil;
	
	// 참가하기
	// 127.0.0.1:9090/ROOT/api/join/insert
	// {"chgstate":1, "challengechg":{"chgno":1} }
	// Headers : token
	@RequestMapping(value="/insert", 
			method = {RequestMethod.POST},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> insertJoinPOST(
			@RequestBody JoinCHG join,
			@RequestHeader(name="token") String token){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println("토큰 발급 : " + token);	// 토큰 발급
			System.out.println(join.toString()); 	// 넘어오는 join
			
			// 토큰에서 아이디 추출
			String username = jwtUtil.extractUsername(token);
			System.out.println("유저이름 : " + username);
			
			// member 엔티티에 아이디 담기
			MemberCHG email = new MemberCHG();
			email.setMemail(username);
			
			System.out.println("아이디 : " + email);
			
			// 참가 엔티티에 아이디 담기
			join.setMemberchg(email);
			
			ChallengeCHG challenge = new ChallengeCHG();
			challenge.setChgno(null);
			
			jService.duplicateJoin(null, username);
			
			int ret = jService.challengeJoin(join);
			

			map.put("status", 200);
					
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		
		return map;
		
	}
	
	
	// 테스트용
	// 127.0.0.1:9090/ROOT/api/join/test
	// {"chgstate":1, "chgno":1 }
	@RequestMapping(value="/test", 
			method = {RequestMethod.POST},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> insertJoinPOST(
			@RequestBody Map<String, Object> join,
			@RequestHeader(name="token") String token) {
		Map<String, Object> map = new HashMap<>();
		try {
			
			System.out.println(join);
			
			JoinCHG join1 = new JoinCHG();
			join1.setChgstate(((Integer)join.get("chgstate")));
			
			System.out.println(join1);
			
			ChallengeCHG challenge = new ChallengeCHG();
			challenge.setChgno(((Integer)join.get("chgno")).longValue());
			System.out.println(challenge);
			
			join1.setChallengechg(challenge);
			
			String username = jwtUtil.extractUsername(token);
			System.out.println(username);
			
			MemberCHG member = new MemberCHG();
			member.setMemail(username);
			System.out.println(member);
			
			join1.setMemberchg(member);
			
			System.out.println(join1.toString());
			
			
			JoinCHG test1 = jService.duplicateJoin(challenge.getChgno(), username);
			System.out.println(test1.toString());
			
			if(test1 != null) {
				map.put("status", 200);
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		
		return map;
	}
	
	
	// 포기하기
	
	
	// 참여 번호로 참가한 첼린지 1개 조회(테스트용)
	// 127.0.0.1:9090/ROOT/api/join/selectone?jno=9
	@RequestMapping(value="/selectone", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectjoinGET(
			@RequestParam(name = "jno") long jno){
		Map<String, Object> map = new HashMap<>();
		try {
			
			System.out.println(jno);
			
			JoinProjection join = jService.selectOneCHG(jno);
			System.out.println(join.toString());
			
			map.put("result", join);
			map.put("status", 200);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		
		return map;
	}
	
	// 현재 참여중인 첼린지 조회
	
	
	// 참여했던 첼린지 전체 조회
	
	
	
}
