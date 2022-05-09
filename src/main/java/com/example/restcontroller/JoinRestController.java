package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.ChallengeCHG;
import com.example.entity.JoinCHG;
import com.example.entity.JoinProjection;
import com.example.entity.JoinSelectOne;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.ChallengeRepository;
import com.example.service.JoinService;

@RestController
@RequestMapping("/api/join")
public class JoinRestController {

	@Autowired JoinService jService;
	
	@Autowired ChallengeRepository chgRepository;
	
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
					// 참가할 때마다 첼린지 인원수 1씩 증가
					int ret1 = chgRepository.increaseCnt(chgno);
					System.out.println(ret1);
					
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

	
	
	// 진행 상태에 따른 조회 리스트
	// 127.0.0.1:9090/ROOT/api/join/joinstate?chgstate=&page=
	@RequestMapping(value="/joinstate", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> joinStateListGET(
			@RequestHeader(name="token") String token,
			@RequestParam(name ="chgstate", defaultValue = "") int state,
			@RequestParam(name="page", defaultValue ="1") int page){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(state);
			System.out.println(token);
			System.out.println(page);
			
			// 페이지네이션
			PageRequest pageRequest = PageRequest.of(page-1, 5);
			System.out.println(pageRequest);
			
			// 토큰에서 아이디 추출
			String username = jwtUtil.extractUsername(token);
			System.out.println("유저이름 : " + username);
			
			// 아이디, 진행 상태, 페이지네이션으로 조회
			List<JoinProjection> list = jService.joinStateList(username, state, pageRequest);
			System.out.println(list);
			
			if(!list.isEmpty()) {
				map.put("result", list);
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
	
	
	// 내가 참여한 첼린지 1개 상세 조회
	// 127.0.0.1:9090/ROOT/api/join/selectone?jno=8
	@RequestMapping(value="/selectone", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectOneGET(
			@RequestParam(name = "jno") long jno,
			@RequestHeader(name="token") String token){
		Map<String, Object> map = new HashMap<>();
		try {
			
			System.out.println(jno);
			System.out.println(token);
			
			// 토큰에서 아이디 추출
			String username = jwtUtil.extractUsername(token);
			System.out.println("유저이름 : " + username);
			
			// 아이디와 번호가 동시에 일치하는 것 조회
			JoinSelectOne join = jService.selectOneCHG(username, jno);
			System.out.println(join);
			
			if(join != null) {
				map.put("result", join);
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
	
	
	// 진행 중인 내 첼린지 전체 조회
	// 127.0.0.1:9090/ROOT/api/join/inglist
	// Headers => token :
	@RequestMapping(value="/inglist", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectIngListGET(
			@RequestHeader(name="token") String token){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(token);
			
			// 토큰에서 아이디 추출
			String username = jwtUtil.extractUsername(token);
			System.out.println("유저이름 : " + username);
			
			// 참가상태 변수 : 3 => 진행중
			int state = 1;

			// 아이디와 참가 변수를 전달해서 진행중 인 첼린지만 조회
			List<JoinProjection> list = jService.joinChallengeList(username, state);
			System.out.println(list);
			
			map.put("result", list);
			map.put("status", 200);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		
		return map; 
	}
	
	
	// 내가 참여했던 첼린지 전체 조회 (페이지네이션)
	// 127.0.0.1:9090/ROOT/api/join/selectlist
	// Headers => token : 
	@RequestMapping(value="/selectlist", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectlistGET(
			@RequestHeader(name="token") String token,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "title", defaultValue = "") String title){
		
		Map<String, Object> map = new HashMap<>();
		try {
//			System.out.println(token);
			System.out.println("페이지 확인" + page);
			System.out.println("제목으로 검색 : " + title);
			
			// 토큰에서 아이디 추출
			String username = jwtUtil.extractUsername(token);
//			System.out.println("유저이름 : " + username);
			
			// 페이지네이션(시작페이지(0부터), 갯수)
			PageRequest pageRequest = PageRequest.of(page-1, 5);
			System.out.println("페이지네이션 : " + pageRequest);
			
			// 사용자가 참여한 첼린지 검색+페이지네이션 조회 
			List<JoinProjection> list =jService.joinChallengeAllList(username, title, pageRequest);
			
			System.out.println(list);
			
			if(!list.isEmpty()) {
				map.put("result", list);
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
	
}
