package com.example.restcontroller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import com.example.entity.JoinCHG;
import com.example.entity.JoinProjection;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.JoinRepository;
import com.example.service.ConfirmService;


@RestController
@RequestMapping("/api/confirm")
public class ConfirmRestController {

	@Autowired JwtUtil jwtUtil;
	@Autowired JoinRepository jRepository;
	@Autowired ConfirmService cfService;
	
	// 인증하기
	// 127.0.0.1:9090/ROOT/api/confirm/insert.json?jno=
	// Headers -> token :
	// Body -> {"cfcomment":"테스트"}
	@RequestMapping(value="/insert.json", 
			method = {RequestMethod.POST},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	
	public Map<String, Object> insertConfirmPOST(
			@RequestParam(name = "jno") long jno,	// 참가번호
			@RequestHeader(name="token") String token,	// 토큰
			@RequestBody ConfirmCHG confirm){	// 인증글
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(jno);	// 참가번호
			System.out.println(token);	// 토큰
			System.out.println(confirm.toString());	// 인증글
			
			// 토큰에서 아이디 추출
			String username = jwtUtil.extractUsername(token);
//			System.out.println("유저이름 : " + username);
			
			// Confirm 엔티티에 회원정보를 담기 위해 아이디를 Member 엔티티에 넣어서 사용
			MemberCHG member = new MemberCHG();
			member.setMemail(username);
//			System.out.println("아이디가 엔티티에 담김 : " + member);
			
			// Confirm 엔티티의 Jno외래키에 참가번호를 담기 위해 Join 엔티티 사용 
			JoinCHG join = new JoinCHG();
			join.setJno(jno);
//			System.out.println("참가번호가 엔티티에 담김 : " + join);
			
			// 회원이 첼린지에 참가한 사람이지 확인
			// 참가 번호로 조회 후 참가한 아이디와 토큰의 아이디를 비교
			JoinProjection join1 = jRepository.findByJno(jno);
//			System.out.println("참가한 아이디 : " + join1.getMemberchgMemail());

			// Confirm 엔티티에 담기
			confirm.setMemberchg(member);
			confirm.setJoinchg(join);
//			System.out.println("인증 엔티티에 데이터 담김: " + confirm);
			
			// 인증은 하루에 한번으로 제한
			// 오늘 날짜에 해당하는 Confirm 엔티티 조회
			// 범위로 조회하기 -> 어제 00:00:00 부터 오늘 23:59:59 에 해당하는 날짜로 조회
			LocalDateTime starttime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0));
//			System.out.println("어제 00:00:00 날짜 : " + starttime);
			
			LocalDateTime endtime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
//			System.out.println("오늘 23:59:59 날짜" + endtime);
			
			// LocalDateTime -> Timestamp 타입을 변환 
			Timestamp starttstamp = Timestamp.valueOf(starttime);
			Timestamp endtstamp = Timestamp.valueOf(endtime);
//			System.out.println(starttstamp);
//			System.out.println(endtstamp);
			
			// 아이디와 오늘 날짜로 인증글 조회
			ConfirmCHG todayConfirm = cfService.todayConfirm(username, starttstamp, endtstamp);
			System.out.println("오늘 등록한 인증 조회 : " + todayConfirm.toString());
			
			// 첼린지 한 곳에서만 인증 가능하게됨 -> 인증글 조회 할 때 글번호 조건 추가해야됨
			
			// 유저가 등록한 인증글 중에 오늘날짜에 해당하는게 없을 경우에 인증 등록가능
//			if (todayConfirm == null) {
//				// 참가한 사람과 인증글 올릴 사람의 아이디가 일치하면 
//				if (join1.getMemberchgMemail().equals(username) ) {
//					
//					int ret = cfService.ConfirmInsert(confirm);
//					System.out.println(ret);
//					
//					if (ret == 1) {
//						map.put("status", 200);
//					}
//
//				}
//			}
//			else {
//				map.put("status", 0);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}
		return map;
	}
	
	
	// 인증 수정하기
	// 127.0.0.1:9090/ROOT/api/confirm/update.json?cfno=
	// Headers -> token:
	// Bodys -> {"cfcomment":"수정 테스트"}
	@RequestMapping(value="/update.json", 
			method = {RequestMethod.PUT},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> updateConfirmPUT(
			@RequestParam(name = "cfno") long cfno,
			@RequestHeader(name="token") String token,
			@RequestBody ConfirmCHG confirm){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(cfno);	// 인증번호
			System.out.println(token);	// 토큰
			System.out.println(confirm.toString());	// 수정할 인증글
			
			// 토큰에서 아이디 추출
			String username = jwtUtil.extractUsername(token);
			System.out.println("유저이름 : " + username);
			
			// 인증 번호와 토큰 아이디로 항목 1개 조회
			ConfirmCHG confirm1 = cfService.selectOneConfirm(cfno, username);
			System.out.println("1개 조회 : " + confirm1);
			
			
			// 인증 상태가 대기 중일 때만 수정 가능
			if (confirm1.getCfsuccess() == 0) {
				
				// 가져온 항목에 수정할 인증글 덮어씌우기
				confirm1.setCfcomment(confirm.getCfcomment());
				System.out.println(confirm1);
				
				// 수정한 항목 저장
				int ret = cfService.updateOneConfirm(confirm1);
				System.out.println(ret);
				
				if (ret == 1) {
					map.put("status", 200);				
				}
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
	
	
	// 인증 삭제하기
	// 127.0.0.1:9090/ROOT/api/confirm/delete.json?cfno=
	// Headers -> token:
	@RequestMapping(value="/delete.json", 
			method = {RequestMethod.DELETE},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> deleteConfirm(
			@RequestParam(name = "cfno") long cfno,
			@RequestHeader(name="token") String token){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(cfno);	// 인증 번호
			System.out.println(token);	// 토큰
			
			map.put("status", 0);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}
		return map;
	}
	
	
	// 인증 1개 조회
	
	
	
	// 인증 리스트
	
	
	
	// 인증 성공 여부
	
	
	
}
