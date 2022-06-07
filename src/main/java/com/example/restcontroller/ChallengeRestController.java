package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.example.entity.CHGImgView;
import com.example.entity.ChallengeCHG;
import com.example.entity.ChallengeCHGView;
import com.example.entity.ChallengeProjection;
import com.example.entity.ChallengeProjection2;
import com.example.entity.JoinCHG;
import com.example.entity.MemberCHG;
import com.example.entity.RoutineCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.ChallengeRepository;
import com.example.repository.ChgImageRepository;
import com.example.repository.ChgViewRepository;
import com.example.repository.MemberRepository;
import com.example.repository.RoutineRepository;
import com.example.repository.RtnRunRepository;
import com.example.service.ChallengeService;
import com.example.service.JoinService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeRestController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ChallengeService chgService;

    @Autowired
    ChallengeRepository chgRepository;

    @Autowired
    ChgViewRepository cvRepository;

    @Autowired
    RoutineRepository rtnRepository;

    @Autowired
    RtnRunRepository rtnRunRepository;

    @Autowired
    JoinService jService;

    @Autowired
    MemberRepository mRepository;

    @Autowired
    ChgImageRepository chgIRepository;

    @Autowired
    ResourceLoader rLoader;

    @Value("${default.image}")
    String DEFAULT_IMAGE;

    // 생성자가 마지막으로 만든 첼린지 조회 테스트
    // 127.0.0.1:9090/until15/api/challenge/testone?email='cc'
    @RequestMapping(value = "/testone", method = { RequestMethod.GET }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> testGET(
            @RequestParam(name = "email") String em) {
        Map<String, Object> map = new HashMap<>();
        try {
            ChallengeProjection chg = chgRepository.findTop1ByMemberchg_memailOrderByChgnoDesc(em);
            // System.out.println(chg.toString());

            map.put("ressult", chg);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // 챌린지 등록
    // 127.0.0.1:9090/until15/api/challenge/insert
    // headers => token:토큰
    // form-data : "chgtitle":"aaa", "chgintro" : "bbb", "chgcontent" : "ccc",
    // "chglevel" : 1,
    // "chgend1" : yyyy-mm-dd 00:00:00, "recruitend1" : yyyy-mm-dd 00:00:00, "chfee"
    // :
    // 10000
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertChallengePOST(
            @ModelAttribute ChallengeCHG chg1, // 이미지와 같이 넣을 땐 ModelAttribute 사용
            @RequestHeader(name = "token") String token,
            @RequestParam(name = "chgroutine") Long rtnno,
            @RequestParam(name = "cimage", required = false) MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();

        try {
            // 토큰에서 정보 추출
            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 정보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String email = jsonObject.getString("username");

            // 멤버 엔티티
            MemberCHG member = new MemberCHG();
            member.setMemail(email);

            // 멤버 레벨
            MemberCHG member1 = mRepository.findById(email).orElse(null);
            // System.out.println(member1.getMrank());

            // 챌린지 엔티티
            ChallengeCHG chg = new ChallengeCHG();

            // 루틴 번호 가져오기
            RoutineCHG rtn = rtnRepository.findById(rtnno).orElse(null);

            // 챌린지 생성일 = 모집 시작일
            // new Timestamp(System.currentTimeMillis()); => timeStamp to long
            chg.setRecruitstart(new Timestamp(System.currentTimeMillis()));

            // string to timestamp 아래와 같은 포멧으로 기입.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

            // 모집 마감일 (임의 지정)
            Date chgrend = formatter.parse(chg1.getRecruitend1());
            Timestamp ts1 = new Timestamp(chgrend.getTime());
            chg.setRecruitend(ts1);

            // 시작일 = 모집 마감일
            chg.setChgstart(ts1);

            // 종료일 (임의 지정)
            Date chgend = formatter.parse(chg1.getChgend1());
            Timestamp ts2 = new Timestamp(chgend.getTime());
            chg.setChgend(ts2);

            // 챌린지 시작일 종료일 차이
            String date = chg1.getChgend1(); // 날짜2
            System.out.println("날짜2 : " + date);

            Date start = new Date(ts1.getTime()); // timeStamp
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(date); // string
            long diffSec = (start.getTime() - end.getTime()) / 1000; // 초 차이
            long diffDays = diffSec / (24 * 60 * 60); // 일자수 차이
            // System.out.println(diffSec + "초 차이");
            // System.out.println(diffDays + "일 차이");

            chg.setChgtitle(chg1.getChgtitle()); // 제목
            chg.setChgintro(chg1.getChgintro()); // 소개글
            chg.setChgcontent(chg1.getChgcontent()); // 내용
            chg.setChgfee(chg1.getChgfee()); // 참가비
            chg.setChglevel(member1.getMrank());
            // chg.setChglevel(chg1.getChglevel()); // 챌린지 레벨
            chg.setMemberchg(member); // 첼린지 생성자
            chg.setChgroutine(rtn.getRtnno()); // 루틴 번호

            // 썸네일
            chg.setChgimage(file.getBytes());
            chg.setChginame(file.getOriginalFilename());
            chg.setChgisize(file.getSize());
            chg.setChgitype(file.getContentType());

            int ret = chgService.insertChallengeOne(chg);
            // System.out.println("DB에 추가됨 : " + ret);

            if (ret == 1) {
                // 첼린지 생성 시 생성자 참가
                JoinCHG join = new JoinCHG();
                join.setMemberchg(member); // 참가하는 아이디(생성자)

                // 생성자가 마지막으로 만든 첼린지 조회
                ChallengeProjection chgpro = chgRepository.findTop1ByMemberchg_memailOrderByChgnoDesc(email);
                ChallengeCHG chg3 = new ChallengeCHG();
                chg3.setChgno(chgpro.getChgno());

                join.setChallengechg(chg3); // 참가하는 첼린지 번호

                // 생성자 첼린지에 참여하기
                int ret1 = jService.challengeJoin(join);
                if (ret1 == 1) {

                    map.put("status", 200);
                }
            } else {
                map.put("status", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // 챌린지 수정
    // 127.0.0.1:9090/until15/api/challenge/updateone
    // {"chgno" : 1, "chgtitle" : "aaa2", "chgintro" : "bbb2", "chgcontent" :
    // "ccc2"}
    @RequestMapping(value = "/updateone", method = { RequestMethod.PUT }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> updateChallengePUT(
            @ModelAttribute ChallengeCHG chg,
            @RequestHeader(name = "token") String token,
            @RequestParam(name = "cimage") MultipartFile file) throws IOException {
        // System.out.println("토큰 : " + token);
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰에서 정보 추출
            String userSubject = jwtUtil.extractUsername(token);
            System.out.println("토큰에 담긴 정보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String email = jsonObject.getString("username");

            // System.out.println(email);

            // 멤버 엔티티
            MemberCHG member = new MemberCHG();
            member.setMemail(email);

            // 수정
            ChallengeCHG challenge = chgService.challengeSelectOne(chg.getChgno());
            challenge.setChgtitle(chg.getChgtitle());
            challenge.setChgintro(chg.getChgintro());
            challenge.setChgcontent(chg.getChgcontent());

            // 썸네일 변경

            challenge.setChgimage(file.getBytes());
            challenge.setChginame(file.getOriginalFilename());
            challenge.setChgisize(file.getSize());
            challenge.setChgitype(file.getContentType());

            // 저장
            chgService.challengeUpdateOne(challenge);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 챌린지 삭제
    // 127.0.0.1:9090/until15/api/challenge/delete?chgno=1
    @RequestMapping(value = "/delete", method = { RequestMethod.DELETE }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> deleteChallengeDELETE(
            @RequestParam("chgno") long chgno,
            @RequestHeader(name = "token") String token) {
        // System.out.println("토큰 : " + token);
        // System.out.println("챌린지 번호 : " + chgno);
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰에서 정보 추출
            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String email = jsonObject.getString("username");

            // System.out.println(email);

            // 멤버 엔티티
            MemberCHG member = new MemberCHG();
            member.setMemail(email);

            // 삭제 => 저장
            int ret = chgService.deleteChallenge(chgno);
            if (ret == 1) {
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 챌린지 1개 조회
    // 127.0.0.1:9090/until15/api/challenge/selectone?chgno=챌린지번호
    // Params => key:chgno, values:챌린지번호
    @RequestMapping(value = "/selectone", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> selectOneChallengeGET(
            @RequestParam("chgno") long chgno) {
        Map<String, Object> map = new HashMap<>();
        try {
            ChallengeCHG challenge = chgService.challengeSelectOne(chgno);

            String thumbnail = "/until15/api/join/thumbnail?chgno=" + chgno;

            if (challenge != null) {
                map.put("image", thumbnail);
                map.put("status", 200);
                map.put("result", challenge);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 챌린지 목록 (검색어 + 페이지네이션)
    // 127.0.0.1:9090/until15/api/challenge/selectlist?page=1&challenge
    @RequestMapping(value = "/selectlist", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> selectlistGET(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "challenge", defaultValue = "") String challenge) {
        Map<String, Object> map = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page - 1, 9);
            List<ChallengeProjection2> list = chgService.challengeSelectList(pageable, challenge);
            long total = chgRepository.countByChgtitleContaining(challenge);

            List<Map<String, Object>> list1 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map1 = new HashMap<>();
                ChallengeProjection2 obj = list.get(i);
                map1.put("obj", obj);
                map1.put("img", "/until15/api/challenge/thumbnail?chgno=" + obj.getChgno());
                list1.add(map1);
            }

            if (list != null) {
                map.put("total", total);
                map.put("status", 200);
                map.put("result", list);
                map.put("result1", list1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 챌린지 인기별 조회
    // 127.0.0.1:9090/until15/api/challenge/selectlistlike
    @RequestMapping(value = "/selectlistlike", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> selectlistLikeGET(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "like", defaultValue = "1") long chglike,
            @RequestParam(name = "challenge", defaultValue = "") String challenge) {
        Map<String, Object> map = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page - 1, 10);

            List<ChallengeProjection> list = chgService.likeSelectList(pageable, chglike);

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

    // 챌린지 인기 목록(페이지네이션)
    // 127.0.0.1:9090/until15/api/challenge/likeselectlist
    @RequestMapping(value = "/likeselectlist", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> likeSelectlistGET(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "like", defaultValue = "") String challenge) {
        Map<String, Object> map = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page - 1, 10);
            List<ChallengeProjection2> list = chgService.chgLikeSelectList(pageable, challenge);

            long total = chgRepository.countByChgtitleContaining(challenge);

            if (list != null) {
                map.put("total", total);
                map.put("status", 200);
                map.put("result", list);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 챌린지 난이도별 조회
    // 127.0.0.1:9090/until15/api/challenge/selectlistlevel
    @RequestMapping(value = "/selectlistlevel", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> selectlistLevelGET(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "level", defaultValue = "1") long chglevel,
            @RequestParam(name = "challenge", defaultValue = "") String challenge) {
        Map<String, Object> map = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page - 1, 10);

            List<ChallengeProjection> list = chgService.levelSelectList(pageable, chglevel);

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

    // 챌린지 난이도 목록(페이지네이션)
    // 127.0.0.1:9090/until15/api/challenge/levelselectlist
    @RequestMapping(value = "/levelselectlist", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> levelSelectlistGET(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "level", defaultValue = "") String challenge) {
        Map<String, Object> map = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page - 1, 10);
            List<ChallengeProjection2> list = chgService.chgLevelSelectList(pageable, challenge);

            long total = chgRepository.countByChgtitleContaining(challenge);

            if (list != null) {
                map.put("total", total);
                map.put("status", 200);
                map.put("result", list);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    ////////////////////////////////////////////////////////////// ↓ 메 인 화 면 ↓
    ////////////////////////////////////////////////////////////// //////////////////////////////////////////////////////////////

    // 챌린지 인기 목록 내림차순 (9개표시)
    // 127.0.0.1:9090/until15/api/challenge/selectlikelist
    @RequestMapping(value = "/selectlikelist", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> selectLikeListGET(
            @RequestParam(name = "like", defaultValue = "") String challenge) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<ChallengeCHGView> list = cvRepository.selectLikeCHG(challenge);
            // URL화 시킨 이미지를 배열에 담기
            // String[] imgs = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {

                ChallengeCHGView obj = list.get(i);
                // System.out.println("테스트 번호 : "+obj.getChgno());
                obj.setImgurl("/until15/api/challenge/thumbnail?chgno=" + obj.getChgno());

                // imgs[i] = "/until15/api/challenge/thumbnail?chgno=" + list.get(i).getChgno();
            }
            // System.out.println("이미지 url : " + imgs.toString());

            // map.put("images", imgs);
            map.put("result", list);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 챌린지 난이도 목록 내림차순 (9개표시)
    // 127.0.0.1:9090/until15/api/challenge/selectlevellist
    @RequestMapping(value = "/selectlevellist", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> selectLevelListGET(
            @RequestParam(name = "level", defaultValue = "") String challenge) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<ChallengeCHGView> list = cvRepository.selectLevelCHG(challenge);
            for (int i = 0; i < list.size(); i++) {
                ChallengeCHGView obj = list.get(i);
                // System.out.println("테스트 번호 : " + obj.getChgno());
                obj.setImgurl("/until15/api/challenge/thumbnail?chgno=" + obj.getChgno());
            }
            map.put("result", list);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 썸네일 조회
    // 127.0.0.1:9090/until15/api/challenge/thumbnail?chgno=
    @RequestMapping(value = "/thumbnail", method = { RequestMethod.GET }, // POST로 받음
            consumes = { MediaType.ALL_VALUE }, // 모든 타입을 다 받음
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<byte[]> selectImageGET(
            @RequestParam(name = "chgno") long chgno) {
        // System.out.println("챌린지 번호" + chgno);
        try {
            // 이미지를 한개 조회
            CHGImgView chgImage = chgIRepository.findByChgno(chgno);

            // System.out.println("이미지 조회 : " + chgImage.getChgisize());
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

    // 마감된 챌린지 제외 조회

}
