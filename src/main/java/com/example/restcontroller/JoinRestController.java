package com.example.restcontroller;

import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.CHGImgView;
import com.example.entity.ChallengeCHG;
import com.example.entity.ChallengeProjection;
import com.example.entity.JoinCHG;
import com.example.entity.JoinCHGView;
import com.example.entity.JoinOneView;
import com.example.entity.JoinProjection;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.ChallengeRepository;
import com.example.repository.ChgImageRepository;
import com.example.repository.ChgViewRepository;
import com.example.repository.JoinOneRepository;
import com.example.repository.JoinRepository;
import com.example.repository.JoinningRepository;
import com.example.service.JoinService;

@RestController
@RequestMapping("/api/join")
public class JoinRestController {

	@Autowired
	JoinService jService;
	
	@Autowired JoinRepository jRepository;

	@Autowired
	ChallengeRepository chgRepository;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	ResourceLoader rLoader;

	@Autowired
	JoinningRepository jingRepository;

	@Autowired
	ChgImageRepository chgIRepository;
	
	@Autowired
	JoinOneRepository joRepository;
	
	@Autowired
	ChgViewRepository cvRepository;

	@Value("${default.image}")
	String DEFAULT_IMAGE;

	
	// 첼린지 시작 : chgstate 1 => 3
	// 127.0.0.1:9090/ROOT/api/join/startchg
	@RequestMapping(value = "/startchg", 
			method = { RequestMethod.GET }, // POST로 받음
			consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> startChallengeGET() {
		Map<String, Object> map = new HashMap<>();
		try {
			
			LocalDateTime starttime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
//			System.out.println("오늘 00:00:00 날짜 : " + starttime);
			
			LocalDateTime endtime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
//			System.out.println("오늘 23:59:59 날짜 : " + endtime);
			
			// LocalDateTime -> Timestamp 타입을 변환 
			Timestamp starttstamp = Timestamp.valueOf(starttime);
			Timestamp endtstamp = Timestamp.valueOf(endtime);
//			System.out.println(starttstamp);
//			System.out.println(endtstamp);
			
			List<Long> start = cvRepository.selectTodayCHG(starttstamp, endtstamp);
//			System.out.println("시작시점 첼린지 : "+ start);
			List<JoinCHG> join = new ArrayList<JoinCHG>();
			for(int i=0;i<start.size();i++) {
//				System.out.println(start.get(i));
				List<JoinCHG> list = jRepository.findByChallengechg_chgno(start.get(i));
				for(int j=0;j<list.size();j++) {
					if(list.get(j).getChgstate() == 1) {
						join.add(list.get(j));						
					}
				}
			}
			for(int h=0;h<join.size();h++) {
				join.get(h).setChgstate(3);
			}
//			jRepository.saveAll(join);
			jService.todayChallenge(join);
			
			map.put("status", 200);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}

		return map;
	}
	
	
	// 첼린지 종료 : (참가상태 변경)chgstate 3 => 4
	// 127.0.0.1:9090/ROOT/api/join/endchg
	@RequestMapping(value = "/endchg", 
			method = { RequestMethod.GET }, // POST로 받음
			consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> endChallengeGET(){
		Map<String, Object> map = new HashMap<>();
		try {
			
			LocalDateTime starttime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
//			System.out.println("오늘 00:00:00 날짜 : " + starttime);
			
			LocalDateTime endtime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
//			System.out.println("오늘 23:59:59 날짜 : " + endtime);
			
			// LocalDateTime -> Timestamp 타입을 변환 
			Timestamp starttstamp = Timestamp.valueOf(starttime);
			Timestamp endtstamp = Timestamp.valueOf(endtime);
//			System.out.println(starttstamp);
//			System.out.println(endtstamp);
			
			List<Long> end = cvRepository.selectEndCHG(starttstamp, endtstamp);
			System.out.println("종료시점 첼린지 : "+ end);
			List<JoinCHG> join = new ArrayList<JoinCHG>();
			for(int i=0;i<end.size();i++) {
				System.out.println(end.get(i));
				List<JoinCHG> list = jRepository.findByChallengechg_chgno(end.get(i));
				for(int j=0;j<list.size();j++) {
					if(list.get(j).getChgstate() != 2) {
						join.add(list.get(j));						
					}
				}
			}
			for(int h=0;h<join.size();h++) {
				join.get(h).setChgstate(4);
			}
//			jRepository.saveAll(join);
			jService.todayChallenge(join);
			
			map.put("status", 200);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}

		return map;
	}
	
	
	// 참가하기
	// 127.0.0.1:9090/ROOT/api/join/insert?chgno=
	// Headers : token
	@RequestMapping(value = "/insert", 
			method = { RequestMethod.POST }, // POST로 받음
			consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> insertJoinPOST(
			@RequestHeader(name = "token") String token,
			@RequestParam(name = "chgno") long chgno) {

		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println("토큰 발급 : " + token); // 토큰 발급
			System.out.println(chgno); // 넘어오는 join

			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
			System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 변환
			JSONObject jsonObject = new JSONObject(userSubject);
			// JSONObject 형태를 파싱해서 사용
			String email = jsonObject.getString("username"); // 회원 아이디
			long rank = jsonObject.getLong("userrank"); // 회원 등급

			System.out.println(email);
			System.out.println(rank);

			// member 엔티티에 아이디 담기
			MemberCHG memail = new MemberCHG();
			memail.setMemail(email);

			// System.out.println("아이디 : " + memail);

			// 등급을 조회하기 위해 첼린지 번호로 조회
			ChallengeProjection chg = chgRepository.findByChgno(chgno);
			// System.out.println("첼린지 번호로 난이도 조회 : " + chg.getChglevel().toString());

			// challenge 엔티티에 첼린지 번호 담기
			ChallengeCHG challenge = new ChallengeCHG();
			challenge.setChgno(chgno);

			// System.out.println(challenge);

			// 참가 엔티티에 아이디와 첼린지 번호 담기
			JoinCHG join = new JoinCHG();
			join.setMemberchg(memail);
			join.setChallengechg(challenge);

			// System.out.println(join); // join엔티티에 담겨있는 값 확인

			// 아이디와 첼린지 번호 동시에 일치하는 지 확인
			JoinCHG duplicate = jService.duplicateJoin(chgno, email);
			// System.out.println(duplicate);
			

			// 기존에 가입한 첼린지가 아닐 때 참가 가능
			if (duplicate == null) {
				// 회원 등급과 난이도가 일치할 때 참가 가능
				if (rank >= chg.getChglevel()) {

					int ret = jService.challengeJoin(join);
					if (ret == 1) {
						
						JoinOneView join1 = joRepository.findByMemailAndChgno(email, chgno);
						System.out.println(join1.getJno());
						
						// 참가할 때마다 첼린지 인원수 1씩 증가
						int ret1 = chgRepository.increaseCnt(chgno);
						System.out.println(ret1);

						// 새로 참가하기는 200
						map.put("result", join1.getJno());
						map.put("status", 200);
					}
				} else {
					map.put("error", "애송아 레벨이 낮다");
				}

			} else {
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
	@RequestMapping(value = "/giveup", method = { RequestMethod.PUT }, // POST로 받음
			consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> giveupCHG(
			@RequestHeader(name = "token") String token,
			@RequestParam(name = "chgno") long chgno) {

		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println("토큰 발급 : " + token); // 토큰 발급
			System.out.println(chgno); // 넘어오는 join

			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
			System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
			JSONObject jsonObject = new JSONObject(userSubject);
			String email = jsonObject.getString("username");

			System.out.println(email);

			JoinCHG join = jService.duplicateJoin(chgno, email);

			join.setChgstate(2);

			int ret = jService.challengeGiveUp(join);
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

	// 진행 상태에 따른 조회 리스트
	// 127.0.0.1:9090/ROOT/api/join/joinstate?chgstate=&page=
	@RequestMapping(value = "/joinstate", method = { RequestMethod.GET }, // POST로 받음
			consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> joinStateListGET(
			@RequestHeader(name = "token") String token,
			@RequestParam(name = "chgstate", defaultValue = "1") int state,
			@RequestParam(name = "page", defaultValue = "1") int page) {

		Map<String, Object> map = new HashMap<>();
		try {
			// System.out.println(state);
			// System.out.println(token);
			// System.out.println(page);

			// 페이지네이션
			PageRequest pageRequest = PageRequest.of(page - 1, 5);
			// System.out.println(pageRequest);

			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
			// System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
			JSONObject jsonObject = new JSONObject(userSubject);
			String email = jsonObject.getString("username");

			// System.out.println(email);

			// 아이디, 진행 상태, 페이지네이션으로 조회
			List<JoinProjection> list = jService.joinStateList(email, state, pageRequest);
			// System.out.println(list);

			long total = jService.selectStateCount(email, state);
			// System.out.println(total);

			if (!list.isEmpty()) {
				map.put("pages", (total - 1) / 5 + 1);
				map.put("result", list);
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
	
	
	// 내가 만든 첼린지 리스트 조회 (페이지네이션)
	// 127.0.0.1:9090/ROOT/api/join/cidselectlist?page=&text=
	@RequestMapping(value = "/cidselectlist", 
			method = { RequestMethod.GET }, // POST로 받음
			consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> cIdSelectListGET(
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "text", defaultValue = "") String text,
			@RequestHeader(name = "token") String token ){
		Map<String, Object> map = new HashMap<>();
		try {
//			System.out.println("페이지 : " + page);
//			System.out.println("토큰 : " + token);
			
			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
//			System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
			JSONObject jsonObject = new JSONObject(userSubject);
			String email = jsonObject.getString("username");

//			System.out.println(email);
			
			// 페이지네이션(시작페이지(0부터), 갯수)
			PageRequest pageRequest = PageRequest.of(page - 1, 10);
//			System.out.println("페이지네이션 : " + pageRequest);
			
			List<ChallengeProjection> list = chgRepository.findByMemberchg_memailAndChgtitleContaining(email, text, pageRequest);
			
			long total = chgRepository.countByMemberchg_memailAndChgtitleContaining(email, text);
//			System.out.println("항목 갯수 : " + total);
			if (!list.equals(null)) {
				map.put("pages", (total-1)/10+1);
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
	
	
	// 내가 생성한 첼린지 상세 조회
	// 127.0.0.1:9090/ROOT/api/join/cidselectone?chgno=81
	@RequestMapping(value = "/cidselectone", 
			method = { RequestMethod.GET }, // POST로 받음
			consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> cIdSelectOneGET(
			@RequestParam(name = "chgno") long chgno,
			@RequestHeader(name = "token") String token) {
		Map<String, Object> map = new HashMap<>();
		try {
//			System.out.println("첼린지 번호 : " + chgno);
//			System.out.println("토큰 : " + token);
			
			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
//			System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
			JSONObject jsonObject = new JSONObject(userSubject);
			String email = jsonObject.getString("username");

//			System.out.println(email);
			
			JoinOneView join = joRepository.findByChgnoAndMemail(chgno, email);
//			System.out.println("상세 조회 : " + join);
			
			String thumbnail = "/ROOT/api/join/thumbnail?chgno=" + chgno;
			
			if (!join.equals(null)) {
				map.put("image", thumbnail);
				map.put("result", join);
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

	
	// 내가 참여한 첼린지 1개 상세 조회
	// 127.0.0.1:9090/ROOT/api/join/selectone?jno=8
	@RequestMapping(value = "/selectone", method = { RequestMethod.GET }, // POST로 받음
			consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> selectOneGET(
			@RequestParam(name = "jno") long jno,
			@RequestHeader(name = "token") String token) {
		Map<String, Object> map = new HashMap<>();
		try {

			System.out.println(jno);
			System.out.println(token);

			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
			System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
			JSONObject jsonObject = new JSONObject(userSubject);
			String email = jsonObject.getString("username");

			System.out.println(email);

			// 아이디와 번호가 동시에 일치하는 것 조회
//			JoinSelectOne join = jService.selectOneCHG(email, jno);
//			System.out.println(join);

			JoinOneView join = joRepository.findByMemailAndJno(email, jno);

			String thumbnail = "/ROOT/api/join/thumbnail?chgno="+join.getChgno();

			System.out.println(join.getJregdate());

			if (!join.equals(null)) {
				map.put("image", thumbnail);
				map.put("result", join);
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

	// 진행 중인 내 첼린지 전체 조회
	// 127.0.0.1:9090/ROOT/api/join/inglist
	// Headers => token :
	@RequestMapping(value = "/inglist", method = { RequestMethod.GET }, // POST로 받음
			consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> selectIngListGET(
			@RequestHeader(name = "token") String token) {

		Map<String, Object> map = new HashMap<>();
		try {
			// System.out.println(token);

			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
			// System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
			JSONObject jsonObject = new JSONObject(userSubject);
			String email = jsonObject.getString("username");

			System.out.println(email);

			// 참가상태 변수 : 1 => 진행중
			int state = 1;

			// 아이디와 참가 변수를 전달해서 진행 중인 첼린지만 조회
			// List<JoinProjection> list = jService.joinChallengeList(email, state);
			// System.out.println(list);

			// 진행 중인 첼린지 썸네일 포함 9개만 조회
			List<JoinCHGView> list = jingRepository.selectJoinningCHG(email, state);

			// URL화 시킨 이미지를 배열에 담기
			String[] imgs = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				imgs[i] = "/ROOT/api/join/thumbnail?chgno=" + list.get(i).getChgno();
				list.get(i).setJimgurl(imgs[i]);
			}
			System.out.println("이미지 url : " + imgs.toString());

			map.put("images", imgs);
			map.put("result", list);
			map.put("status", 200);

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}

		return map;
	}



	// 썸네일 조회
	// 127.0.0.1:9090/ROOT/api/join/thumbnail?chgno=
	@RequestMapping(value = "/thumbnail", method = { RequestMethod.GET }, // POST로 받음
			consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<byte[]> selectImageGET(
			@RequestParam(name = "chgno") long chgno) {
		try {
			// 이미지를 한개 조회
			CHGImgView chgImage = chgIRepository.findByChgno(chgno);

			System.out.println("이미지 조회 : " + chgImage.getChgisize());
			// 썸네일 이미지가 있을 때
			if (chgImage.getChgisize() > 0) {
				HttpHeaders header = new HttpHeaders();
				if (chgImage.getChgitype().equals("image/jpeg")) {
					header.setContentType(MediaType.IMAGE_JPEG);
				} else if (chgImage.getChgitype().equals("image/png")) {
					header.setContentType(MediaType.IMAGE_PNG);
				} else if (chgImage.getChgitype().equals("image/gif")) {
					header.setContentType(MediaType.IMAGE_GIF);
				}
				ResponseEntity<byte[]> response = new ResponseEntity<>(chgImage.getChgimage(), header, HttpStatus.OK);
				return response;

			} else { // 썸네일 이미지가 없을 때
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

	// 내가 참여했던 첼린지 전체 조회 (페이지네이션)
	// 127.0.0.1:9090/ROOT/api/join/selectlist?page=&title=
	// Headers => token :
	@RequestMapping(value = "/selectlist", method = { RequestMethod.GET }, // POST로 받음
			consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> selectlistGET(
			@RequestHeader(name = "token") String token,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "title", defaultValue = "") String title) {

		Map<String, Object> map = new HashMap<>();
		try {
			// System.out.println(token);
			// System.out.println("페이지 확인" + page);
			// System.out.println("제목으로 검색 : " + title);

			// 토큰에서 정보 추출
			String userSubject = jwtUtil.extractUsername(token);
			// System.out.println("토큰에 담긴 전보 : " + userSubject);

			// 추출된 결과값을 JSONObject 형태로 파싱
			JSONObject jsonObject = new JSONObject(userSubject);
			String email = jsonObject.getString("username");

			// System.out.println(email);

			// 페이지네이션(시작페이지(0부터), 갯수)
			PageRequest pageRequest = PageRequest.of(page - 1, 5);
			// System.out.println("페이지네이션 : " + pageRequest);

			// 사용자가 참여한 첼린지 검색+페이지네이션 조회
			List<JoinProjection> list = jService.joinChallengeAllList(email, title, pageRequest);
			// System.out.println(list);

			// 검색어가 포함된 항목의 갯수
			// 페이지네이션 계산
			long total = jService.selectCount(email, title);
			// System.out.println(total);
			// System.out.println((total-1)/5+1);

			// 7 -> 2
			// 10 -> 2
			// 8 -> 2
			// 3 -> 1
			// 11 -> 3

			if (!list.isEmpty()) {
				map.put("pages", (total - 1) / 5 + 1);
				map.put("result", list);
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

}
