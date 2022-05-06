package com.example.restcontroller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.ChallengeCHG;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.service.ChallengeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Value("${default.image}")
    String DEFAULT_IMAGE;

    // 챌린지 등록
    // 127.0.0.1:9090/ROOT/api/challenge/insert
    // headers => token:토큰
    // form-data : {"chgtitle":"aaa", "chgintro" : "bbb", "chgcontent" : "ccc",
    // "chgend" : yyyy-mm-dd 00:00:00, "recruitend" : yyyy-mm-dd 00:00:00, "chfee" : 10000 }
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertChallengePOST(
    		@ModelAttribute ChallengeCHG chg1,	// 이미지와 같이 넣을 땐 ModelAttribute 사용
            @RequestHeader(name = "token") String token,
            @RequestParam(name = "cimage") MultipartFile file
            ) throws IOException {

        System.out.println("토큰 : " + token);
        System.out.println("썸네일 : " + file);

        Map<String, Object> map = new HashMap<>();
        try {

            // 멤버 토큰
            String memail = jwtUtil.extractUsername(token);
            System.out.println(token.toString());

            // 멤버 엔티티
            MemberCHG member = new MemberCHG();
            member.setMemail(memail);

            ChallengeCHG chg = new ChallengeCHG();

            // 챌린지 생성일 = 모집 시작일
            // new Timestamp(System.currentTimeMillis()); => timeStamp to long
            chg.setRecruitstart(new Timestamp(System.currentTimeMillis()));
            
            // Tiemstamp 타입의 형식에 맞게 전달해야함 => yyyy-mm-dd 00:00:00
            chg.setRecruitend(chg1.getRecruitend()); // 모집 마감일 (임의 지정)
            chg.setChgstart(chg1.getRecruitend()); // 챌린지 시작일 = 모집 마감일
            chg.setChgend(chg1.getChgend()); 	// 챌린지 종료일 (임의 지정)
            
            chg.setMemberchg(member);	// 첼린지 생성자
            
            chg.setChgtitle(chg1.getChgtitle());	// 첼린지 제목
            chg.setChgintro(chg1.getChgintro()); 	// 첼린지 소개글
            chg.setChgcontent(chg1.getChgcontent());// 첼린지 내용
            chg.setChgfee(chg1.getChgfee()); 	// 첼린지 참가비
            
            System.out.println("첼린지에 추가할 항목 : " + chg.toString());

            // 썸네일 수정 필요.
            // 썸네일
            chg.setChgimage(file.getBytes());
            chg.setChginame(file.getOriginalFilename());
            chg.setChgisize(file.getSize());
            chg.setChgitype(file.getContentType());
                        
            int ret = chgService.insertChallengeOne(chg);
            System.out.println("DB에 추가됨 : " + ret);
            
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

    // 챌린지 수정
    // 127.0.0.1:9090/ROOT/api/challenge/updateone
    // {"chgno" : 1, "chgtitle" : "aaa2", "chgintro" : "bbb2", "chgcontent" :
    // "ccc2"}
    @RequestMapping(value = "/updateone", method = { RequestMethod.PUT }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> updateChallengePUT(
            @RequestBody ChallengeCHG chg,
            @RequestHeader(name = "token") String token,
            @RequestParam(name = "cimage") MultipartFile file) throws IOException {
        System.out.println("토큰 : " + token);
        Map<String, Object> map = new HashMap<>();
        try {
            // 멤버 토큰
            String memail = jwtUtil.extractUsername(token);
            System.out.println(token.toString());

            // 멤버 엔티티
            MemberCHG member = new MemberCHG();
            member.setMemail(memail);

            // 수정
            ChallengeCHG challenge = chgService.challengeSelectOne(chg.getChgno());
            challenge.setChgtitle(chg.getChgtitle());
            challenge.setChgintro(chg.getChgintro());
            challenge.setChgcontent(chg.getChgcontent());

            // 썸네일 변경
            chg.setChgimage(file.getBytes());
            chg.setChginame(file.getOriginalFilename());
            chg.setChgisize(file.getSize());
            chg.setChgitype(file.getContentType());
            
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
    // 127.0.0.1:9090/ROOT/api/challenge/delete?chgno=1
    @RequestMapping(value = "/delete", method = { RequestMethod.DELETE }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> deleteChallengeDELETE(
            @RequestParam("chgno") long chgno,
            @RequestHeader(name = "token") String token) {
        System.out.println("토큰 : " + token);
        System.out.println("챌린지 번호 : " + chgno);
        Map<String, Object> map = new HashMap<>();
        try {
            // 멤버 토큰
            String memail = jwtUtil.extractUsername(token);
            System.out.println(token.toString());

            // 멤버 엔티티
            MemberCHG member = new MemberCHG();
            member.setMemail(memail);

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
    // 127.0.0.1:9090/ROOT/api/challenge/selectone?chgno=챌린지번호
    // Params => key:chgno, values:챌린지번호
    @RequestMapping(value = "/selectone", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> selectOneChallengeGET(
            @RequestParam("chgno") long chgno) {
        Map<String, Object> map = new HashMap<>();
        try {
            ChallengeCHG challenge = chgService.challengeSelectOne(chgno);
            if (challenge != null) {
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
    // 127.0.0.1:9090/ROOT/api/challenge/selectlist?page=1&challenge
    @RequestMapping(value = "/selectlist", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> selectlistGET(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "challenge", defaultValue = "") String challenge) {
        Map<String, Object> map = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page - 1, 10);
            List<ChallengeCHG> list = chgService.challengeSelectList(pageable, challenge);
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

    // 챌린지 인기순 조회

}
