package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
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
	// 127.0.0.1:9090/ROOT/api/join/insert?chgno=
	// Headers : token
	@RequestMapping(value="/insert", 
			method = {RequestMethod.POST},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> insertJoinPOST(
			@RequestHeader(name="token") String token,
			@RequestParam(name = "chgno") long chgno){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println("토큰 발급 : " + token);	// 토큰 발급
			System.out.println(chgno); 	// 넘어오는 join
			
			// 토큰에서 아이디 추출
			String username = jwtUtil.extractUsername(token);
			System.out.println("유저이름 : " + username);
			
			// member 엔티티에 아이디 담기
			MemberCHG email = new MemberCHG();
			email.setMemail(username);
			
			System.out.println("아이디 : " + email);
			
			// challenge 엔티티에 첼린지 번호 담기
			ChallengeCHG challenge = new ChallengeCHG();
			challenge.setChgno(chgno);
			
			System.out.println(challenge);
			
			// 참가 엔티티에 아이디와 첼린지 번호 담기
			JoinCHG join = new JoinCHG();
			join.setMemberchg(email);
			join.setChallengechg(challenge);
			
			System.out.println(join);	// join엔티티에 담겨있는 값 확인
			
			// 아이디와 첼린지 번호 동시에 일치하는 지 확인
			JoinCHG duplicate = jService.duplicateJoin(chgno, username);
			System.out.println(duplicate);
			
			if(duplicate == null) {
				int ret = jService.challengeJoin(join);
				if (ret == 1) {
					// 새로 참가하기는 200
					map.put("status", 200);
					
				}
			}else {
				// 이미 참여했으면 0
				map.put("status", 0);
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}
		
		return map;
		
	}
	
	
	// 포기하기 (chgstate 가 1이면 참여중 2면 포기)
	// 127.0.0.1:9090/ROOT/api/join/giveup?chgno=
	// Headers : token
	@RequestMapping(value="/giveup", 
			method = {RequestMethod.PUT},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> giveupCHG(
			@RequestHeader(name="token") String token,
			@RequestParam(name = "chgno") long chgno){
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println("토큰 발급 : " + token);	// 토큰 발급
			System.out.println(chgno); 	// 넘어오는 join
			
			// 토큰에서 아이디 추출
			String username = jwtUtil.extractUsername(token);
			System.out.println("유저이름 : " + username);

			JoinCHG join = jService.duplicateJoin(chgno, username);
			
			join.setChgstate(2);

			int ret = jService.challengeGiveUp(join);
			if (ret == 1) {
				
				map.put("status", 200);
			}
			else {
				map.put("status", 0);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}
		
		return map;
	}
	
	
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
	
	
	// 진행 중인 내 첼린지 전체 조회

	
	// 내가 참여한 첼린지 1개 조회
	
	
	// 내가 참여했던 첼린지 전체 조회
	// 127.0.0.1:9090/ROOT/api/join/selectlist
	@RequestMapping(value="/selectlist", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectlistGET(
			@RequestHeader(name="token") String token){
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(token);
			
			// 토큰에서 아이디 추출
			String username = jwtUtil.extractUsername(token);
			System.out.println("유저이름 : " + username);
			
			List<JoinProjection> list = jService.joinedChallengeAllList(username);
			System.out.println(list);
			
			map.put("status", 200);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		
		return map;
	}
	
	// 
	
	
	
}
