package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.CfImageCHG;
import com.example.entity.ChallengeCHG;
import com.example.entity.ChallengeProjection;
import com.example.entity.ConfirmCHG;
import com.example.entity.ConfirmProjection;
import com.example.entity.ConfirmView;
import com.example.entity.JoinCHG;
import com.example.entity.JoinProjection;
import com.example.entity.MemberCHG;
import com.example.entity.ProveCHGView;
import com.example.jwt.JwtUtil;
import com.example.repository.CfmViewRepository;
import com.example.repository.ChallengeRepository;
import com.example.repository.ConfirmRepository;
import com.example.repository.JoinRepository;
import com.example.repository.ProveRepository;
import com.example.service.ConfirmService;
import com.example.service.JoinService;

// 인증
@RestController
@RequestMapping("/api/confirm")
public class ConfirmRestController {

	@Autowired JwtUtil jwtUtil;	// 토큰 
	@Autowired JoinRepository jRepository;	// 참가 저장소
	@Autowired ConfirmService cfService;	// 인증 서비스
	@Autowired ChallengeRepository chgRepository;	// 첼린지 저장소
	@Autowired ProveRepository pRepository;	// 인증 저장소
	@Autowired ResourceLoader rLoader;
	@Autowired ConfirmRepository cfRepository;
	@Autowired CfmViewRepository cfmVRepository;
	@Autowired JoinService jService;
	
	// 디폴트 이미지
	@Value("${default.image}")
    String DEFAULT_IMAGE;
	
	
	// 달성률 증가
	// 127.0.0.1:9090/ROOT/api/confirm/successrate.json?chgno=&jno=
	@RequestMapping(value="/successrate.json", 
			method = {RequestMethod.PUT},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> successRatePOST(
			@RequestParam(name = "chgno") long chgno,
			@RequestParam(name = "jno") long jno){
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(chgno);
			// 첼린지 번호에 해당하는 일 수 조회
//			Long count = chgRepository.challengeDayCount(chgno);
//			System.out.println(count);
			
			ChallengeCHG chg = chgRepository.findById(chgno).orElse(null);
//			System.out.println(chg.getChgstart());
//			System.out.println(chg.getChgend());
			
			Date start = new Date(chg.getChgstart().getTime());
			Date end = new Date(chg.getChgend().getTime());
//			System.out.println("시작일 : " + start);
			
			long diffSec = (end.getTime() - start.getTime()) / 1000;
//			System.out.println("차이 : " + diffSec);
			
//			System.out.println("시작일 : " + start);
//			System.out.println("종료일" + end);
			
			long diffDays = diffSec / (24*60*60);
//			System.out.println("차이 일 수 : " + diffDays);
			
			// 참가 번호에 해당하는 인증횟수 조회
			long count = pRepository.countByJnoAndCfsuccess(jno, 1);
//			System.out.println("성공 인증 갯수 : " + count);	
//			
//			System.out.println("계산값 : "+ (float)count/diffDays*100);
			
			// 달성률 계산
			float successRate = (float)count/diffDays*100;
			// 달성률 소수 두번째 자리까지
//			System.out.println("달성률 : " + Math.round(successRate*100)/100.0f);
			
			JoinCHG join = jRepository.findById(jno).orElse(null);
			join.setChgrate(Math.round(successRate*100)/100.0f);
			
			int ret = jService.challengeSuccessRate(join);
			
			if (ret == 1) {
				
				map.put("status", 200);

			} else {
				map.put("status", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}
		return map;
	}
	
	
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
//			System.out.println(jno);	// 참가번호
//			System.out.println(token);	// 토큰
//			System.out.println(confirm.toString());	// 인증글
			
			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
			System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
	        JSONObject jsonObject = new JSONObject(userSubject);
	        String email = jsonObject.getString("username");
	        
//	        System.out.println(email);
			
			// Confirm 엔티티에 회원정보를 담기 위해 아이디를 Member 엔티티에 넣어서 사용
			MemberCHG member = new MemberCHG();
			member.setMemail(email);
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
			LocalDateTime starttime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
			System.out.println("어제 00:00:00 날짜 : " + starttime);
			
			LocalDateTime endtime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
			System.out.println("오늘 23:59:59 날짜" + endtime);
			
			// LocalDateTime -> Timestamp 타입을 변환 
			Timestamp starttstamp = Timestamp.valueOf(starttime);
			Timestamp endtstamp = Timestamp.valueOf(endtime);
//			System.out.println(starttstamp);
//			System.out.println(endtstamp);
			
			// 아이디와 오늘 날짜로 인증글 조회
			ConfirmCHG todayConfirm = cfService.todayConfirm(email, jno, starttstamp, endtstamp);
//			System.out.println("오늘 등록한 인증 조회 : " + todayConfirm.toString());

			// 유저가 등록한 인증글 중에 오늘날짜에 해당하는게 없을 경우에 인증 등록가능
			if (todayConfirm == null) {
				// 참가한 사람과 인증글 올릴 사람의 아이디가 일치하면 
				if (join1.getMemberchgMemail().equals(email) ) {
					
					long ret = cfService.ConfirmInsert(confirm);
//					System.out.println(ret);
					
					if (ret > 0) {
						map.put("result", ret);
						map.put("status", 200);
					}

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
			
			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
			System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
	        JSONObject jsonObject = new JSONObject(userSubject);
	        String email = jsonObject.getString("username");
	        
	        System.out.println(email);
			
			// 인증 번호와 토큰 아이디로 항목 1개 조회
			ConfirmCHG confirm1 = cfService.selectOneConfirm(cfno, email);
			System.out.println("1개 조회 : " + confirm1);
			
			
			// 인증 상태가 대기 중일 때만 수정 가능
			if (confirm1 != null) {				
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
			
			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
			System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
	        JSONObject jsonObject = new JSONObject(userSubject);
	        String email = jsonObject.getString("username");
	        
	        System.out.println(email);
			
			// 인증 번호와 토큰 아이디로 항목 1개 조회
			ConfirmCHG confirm1 = cfService.selectOneConfirm(cfno, email);
			System.out.println("1개 조회 : " + confirm1);
			
			// 조회한 값이 있을 때
			if (confirm1 != null) {
				// 성공 유무가 판별나기 전에만 삭제 가능
				if(confirm1.getCfsuccess() == 0) {
					
					// DB에서 완전 삭제
					int ret = cfService.deleteOneConfirm(cfno);
					System.out.println(ret);
					
					if (ret == 1) {
						map.put("status", 200);						
					}
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
	
	
	// 인증글 전체 조회( 페이지네이션 )
	// 127.0.0.1:9090/ROOT/api/confirm/provelist.json?page=&email=
	@RequestMapping(value="/provelist.json", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> proveListGET(
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "email", defaultValue = "") String email){
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(page);
			System.out.println(email);
			
			// 페이지네이션(시작페이지(0부터), 갯수)
			PageRequest pageRequest = PageRequest.of(page-1, 5);
			System.out.println("페이지네이션 : " + pageRequest);
			
			List<ProveCHGView> list =pRepository.findByMemailContainingOrderByCfnoDesc(email, pageRequest);
			
			long total = pRepository.countByMemailContaining(email);
			System.out.println(total);
			
			
			if (!list.isEmpty()) {
				map.put("pages", (total-1)/5+1);
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
	
	
	// 인증 1개 조회
	// 127.0.0.1:9090/ROOT/api/confirm/selectone.json?cfno=
	// Headers -> token:
	@RequestMapping(value="/selectone.json", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectOneConfirm(
			@RequestParam(name = "cfno") long cfno){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(cfno);	// 인증 번호
			
			// 인증 번호로 항목 1개 조회
			// Projection 으로 필요한 항목만 조회
			ConfirmProjection cfProjection = cfService.findOneConfirm(cfno);
			System.out.println(cfProjection);
			
			
			// 조회할 값이 있을 때 결과 반환
			if(cfProjection != null) {
				map.put("result", cfProjection);
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
	
	// 첼린지 내에서 인증 리스트 전체 조회
	// 127.0.0.1:9090/ROOT/api/confirm/chgcfmlist.json?chgno=&page=
	@RequestMapping(value="/chgcfmlist.json", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectChgConfirmList(
			@RequestParam(name = "chgno") long chgno,
			@RequestParam(name = "page", defaultValue = "1") int page){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(chgno); 	// 첼린지 번호
			System.out.println(page); 	// 페이지네이션
			
			// 페이지네이션(시작페이지(0부터), 갯수)
			PageRequest pageRequest = PageRequest.of(page-1, 5);
			System.out.println("페이지네이션 : " + pageRequest);
			
//			long total = cfRepository.countByJoinchg_challengechg_chgno(chgno);
			
			// 첼린지 번호에 해당하는 인증글 전체 조회
//			List<ConfirmProjection> cfmFromChg = cfService.confirmFromChallenge(chgno ,pageRequest);
//			System.out.println(cfmFromChg);
			
			// 게시글 전체 갯수
			long total = cfmVRepository.countByChgno(chgno);
			
			// 첼린지 번호에 해당하는 인증글 전체 조회
			List<ConfirmView> list = cfmVRepository.findByChgnoOrderByCcregdateDesc(chgno, pageRequest);
			
			// 조회한 값이 있을 때 반환 
			if(!list.isEmpty()) {
				map.put("result", list);
				map.put("pages", (total-1)/5+1);
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
	
	
	// 내가 인증한 리스트 전체 조회 (페이지네이션)
	// 127.0.0.1:9090/ROOT/api/confirm/myselectlist.json?page=?&text=?
	// Headers -> token :
	@RequestMapping(value="/myselectlist.json", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectlistConfirm(
			@RequestHeader(name = "token") String token,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "text", defaultValue = "") String text){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(token);	// 토큰
			System.out.println(page); 	// 페이지네이션
			System.out.println(text); 	// 검색어
			
			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
			System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
	        JSONObject jsonObject = new JSONObject(userSubject);
	        String email = jsonObject.getString("username");
	        
	        System.out.println(email);
			
			// 페이지네이션(시작페이지(0부터), 갯수)
			PageRequest pageRequest = PageRequest.of(page-1, 5);
			System.out.println("페이지네이션 : " + pageRequest);
			
			// 검색 + 페이지네이션으로 아이디에 해당하는 인증 리스트 조회하기
			List<ConfirmProjection> list = cfService.selectListConfirm(email, text, pageRequest);
			System.out.println(list);
			
			// 결과값이 있을 때 반환
			if (!list.isEmpty()) {				
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
	
	
	// 내가 인증한 리스트 첼린지별 조회 (페이지네이션)
	// 127.0.0.1:9090/ROOT/api/confirm/mycfmlist.json?chgno=&page=
	// Headers -> token :
	@RequestMapping(value="/mycfmlist.json", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectMyConfirmLIST(
			@RequestHeader(name = "token") String token,
			@RequestParam(name = "chgno") long chgno,
			@RequestParam(name = "page", defaultValue = "1") int page){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(token);	// 토큰
			System.out.println(chgno); 	// 첼린지 번호
			System.out.println(page); 	// 페이지

			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
			System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
	        JSONObject jsonObject = new JSONObject(userSubject);
	        String email = jsonObject.getString("username");
	        
	        System.out.println(email);
			
			// 페이지네이션(시작페이지(0부터), 갯수)
			PageRequest pageRequest = PageRequest.of(page-1, 5);
			System.out.println("페이지네이션 : " + pageRequest);
			
			// 첼린지 번호와 토큰의 아이디로 인증 리스트 조회
			List<ConfirmProjection> list = cfService.myConfirmFromChallenge(chgno, email, pageRequest);
			System.out.println(list);
			
			// 결과 값이 있을 때 list 반환
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
	
	
	// 생성자가 인증 성공 여부 판별하기
	// 127.0.0.1:9090/ROOT/api/confirm/whethercfm.json?cfno=
	// Body -> {"cfsuccess" : "1"}
	@RequestMapping(value="/whethercfm.json", 
			method = {RequestMethod.PUT},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> whethersuccessPUT(
			@RequestParam(name = "cfno") long cfno,
			@RequestBody ConfirmCHG confirm){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(cfno); 	// 인증글 번호
			System.out.println("성공 여부 : " + confirm);	// 성공 여부
			
			// 토큰에서 정보 추출
//			String userSubject = jwtUtil.extractUsername(token);
//			System.out.println("토큰에 담긴 전보 : " + userSubject);
	
			// 추출된 결과값을 JSONObject 형태로 파싱
//	        JSONObject jsonObject = new JSONObject(userSubject);
//	        String email = jsonObject.getString("username");	// 로그인 한 아이디
//	        
//	        System.out.println("토큰에서 아이디 추출 : " + email);
			
	        // 번호로 해당 첼린지 조회
//			ChallengeProjection chg = chgRepository.findByChgno(chgno);
//			System.out.println("생성자 아이디 : " + chg.getMemberchgMemail());	// 첼린지 생성자
//			
//			// 생성자와 회원이 동일 할 때
//			if (chg.getMemberchgMemail().equals(email)) {
				
				// 인증 번호로 조회
				ConfirmCHG cfm = cfService.selectSuccessOne(cfno);
//				System.out.println("인증글 조회 : " + cfm);
				
				cfm.setCfsuccess(confirm.getCfsuccess()); 	// 성공 여부
				
				int ret = cfService.updateOneConfirm(cfm);
				if (ret == 1) {	
					map.put("status", 200);
				}
//			}
			else {	
				map.put("status", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}
		return map;
	}
	
	
	// 성공 유무 별로 조회하기 (첼린지 생성자 권한)
	// 127.0.0.1:9090/ROOT/api/confirm/selectsuccess.json?chgno=&cfsuccess=&page=
	// Headers => token :
	@RequestMapping(value="/selectsuccess.json", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectSuccessGET(
			@RequestParam(name = "chgno") long chgno,
			@RequestHeader(name = "token") String token,
			@RequestParam(name = "cfsuccess") int cfsuccess,
			@RequestParam(name = "page", defaultValue = "1") int page){	// 페이지네이션
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(chgno);	// 첼린지 번호
			System.out.println(token);	// 토큰
			System.out.println(cfsuccess); 	// 성공 유무
			System.out.println(page); 	// 페이지
			
			// 페이지네이션(시작페이지(0부터), 갯수)
			PageRequest pageRequest = PageRequest.of(page-1, 5);
			System.out.println("페이지네이션 : " + pageRequest);
			
			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
//			System.out.println("토큰에 담긴 전보 : " + userSubject);
	
			// 추출된 결과값을 JSONObject 형태로 파싱
	        JSONObject jsonObject = new JSONObject(userSubject);
	        String email = jsonObject.getString("username");	// 로그인 한 아이디
	        
	        System.out.println("토큰에서 아이디 추출 : " + email);
			
	        // 번호로 해당 첼린지 조회
 			ChallengeProjection chg = chgRepository.findByChgno(chgno);
	        
	        // 생성자와 회원이 동일 할 때
			if (chg.getMemberchgMemail().equals(email)) {
				List<ConfirmProjection> list = cfService.whetherSuccessCFM(chgno, cfsuccess, pageRequest);
				if (!list.isEmpty()) {
					map.put("result", list);
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
	
	
	// 인증 이미지 일괄 추가하기
	// 127.0.0.1:9090/ROOT/api/confirm/cfimage.insert?cfno=
	@RequestMapping(value="/cfimage.insert", 
			method = {RequestMethod.POST},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> CFImagePOST(
			@RequestParam(name = "cfno") long cfno,
			@RequestParam(name = "file", required = false) MultipartFile[] file) throws IOException{
		Map<String, Object> map = new HashMap<>();
		try {
			// System.out.println("인증번호 : " + cfno);	// 인증 번호
			// System.out.println("이미지 파일 : " + file);	// 이미지 데이터
			
			// 이미지가 여러개 들어가기 위한 List
			List<CfImageCHG> list = new ArrayList<>();
			
			// 반복문을 사용해서 엔티티에 이미지 데이터 저장
			for( int i=0;i<file.length;i++) {
				
				CfImageCHG cfImg = new CfImageCHG();
				
				if (!file[i].isEmpty()) {
					cfImg.setCfimage(file[i].getBytes());
					cfImg.setCfimgname(file[i].getOriginalFilename());
					cfImg.setCfimgtype(file[i].getContentType());
					cfImg.setCfimgsize(file[i].getSize());
					cfImg.setCfno(cfno);
				}
				
				list.add(cfImg);
			}
//			System.out.println("리스트에 담긴 데이터 : " + list);
			
			// 이미지 디비에 넣기
			int ret = cfService.ConfirmImage(list);
			// System.out.println("이미지 추가 성공적: " + ret);
			if (ret == 1) {
				map.put("status", 200);				
			} else {
				map.put("status", 0);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}
		return map;
	}
	
	// 인증 이미지 조회
	// 127.0.0.1:9090/ROOT/api/confirm/cfimages.json?cfimgno=
	@RequestMapping(value="/cfimages.json", 
			method = {RequestMethod.GET},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<byte[]> confirmImgGET(
			@RequestParam(name = "cfimgno") long cfino){
		try {
    		// 이미지를 한개 조회
    		CfImageCHG cfImage = cfService.selectProveImage(cfino);
    		
    		System.out.println("이미지 조회 : " + cfImage.getCfimgsize());
    		// 썸네일 이미지가 있을 때
    		if (cfImage.getCfimgsize() > 0) {
    			HttpHeaders header = new HttpHeaders();
                if (cfImage.getCfimgtype().equals("image/jpeg")) {
                    header.setContentType(MediaType.IMAGE_JPEG);
                } else if (cfImage.getCfimgtype().equals("image/png")) {
                    header.setContentType(MediaType.IMAGE_PNG);
                } else if (cfImage.getCfimgtype().equals("image/gif")) {
                    header.setContentType(MediaType.IMAGE_GIF);
                }
                ResponseEntity<byte[]> response = new ResponseEntity<>(cfImage.getCfimage(), header, HttpStatus.OK);
                return response;
                
			} else {	// 썸네일 이미지가 없을 때
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
	
	// 인증이미지 URL (인증 번호로 인증 이미지 번호 조회)
	// 127.0.0.1:9090/ROOT/api/confirm/selectimages?cfno=
	@RequestMapping(value = "/selectimages",
			method = {RequestMethod.GET}, 
			consumes = {MediaType.ALL_VALUE}, 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> selectImageGET(
			@RequestParam(name = "cfno") long cfno){
		Map<String, Object> map = new HashMap<>();
		try {
//			System.out.println("cfno : " + cfno);
			
			List<Long> list = cfService.selectCFImageNo(cfno);
//			System.out.println("리스트 : " + list.get(1));
			
			// 이미지 URL
			String[] imgs = new String[list.size()];
			for (int i=0;i<list.size();i++) {
				
				if(list.get(i) == null) {
					imgs[i] = "";
				}
				else {
					imgs[i] = "/ROOT/api/confirm/cfimages.json?cfimgno=" + list.get(i);					
				}
			}
			System.out.println("이미지 url : " + imgs.length);
			
			
//			map.put("result", list);
			map.put("status", 200);
			map.put("images", imgs);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}
		return map;
	}
	
	
}
