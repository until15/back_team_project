package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.BookMarkCHG;
import com.example.entity.BookmarkProjection;
import com.example.entity.ChallengeCHG;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.service.BookMarkService;
import com.example.service.ChallengeService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookmark")
public class BookMarkRestController {
    
    @Autowired JwtUtil jwtUtil;

    @Autowired BookMarkService bmkService;

    @Autowired ChallengeService chgService;

    // 북마크 추가
    // 127.0.0.1:9090/ROOT/api/bookmark/insert
    // params => chgno:1
    // headers => token:...
    // body/json => { "memail":"이메일" }
    @RequestMapping(
        value    = "/insert", 
        method   = {RequestMethod.POST},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> insertBookmarkPOST(
        @RequestBody BookMarkCHG bookmark,
        @RequestHeader(name = "token") String token, 
        @RequestParam(name = "chgno") long chgno ){  
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰에서 정보 추출
            String userSubject = jwtUtil.extractUsername(token);
            System.out.println("토큰에 담긴 정보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String email = jsonObject.getString("username");

            // 멤버 엔티티
            MemberCHG member = new MemberCHG();
            member.setMemail(email);

            // 챌린지 조회
            ChallengeCHG challenge = chgService.challengeSelectOne(chgno);
            
            // 저장
            bookmark.setChallengechg(challenge);
            bookmark.setMemberchg(member);

            // 중복 확인
            BookMarkCHG duplicate = bmkService.duplicateInsert(chgno, email);
            if(duplicate == null) {
                int ret = bmkService.insertBookMark(bookmark);
                if (ret == 1) {
                    map.put("status", 200);
                }  
            }
            else {
                map.put("status", 0);
            }       
        }
        catch(Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }



    // 북마크 삭제(해제)
    // 127.0.0.1:9090/ROOT/api/bookmark/delete
    // params => bmkno:1
    // headers => token:...
    @RequestMapping(
        value    = "/delete", 
        method   = {RequestMethod.DELETE},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> deleteChallengeDELETE(
        @RequestHeader(name = "token") String token, 
        @RequestParam(name = "bmkno") long bmkno ){
            System.out.println("북마크 번호 : "+bmkno);
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰에서 정보 추출
            String userSubject = jwtUtil.extractUsername(token);
            System.out.println("토큰에 담긴 정보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String email = jsonObject.getString("username");

            // 멤버 엔티티 
            MemberCHG member = new MemberCHG();
            member.setMemail(email);

            int ret = bmkService.deleteBookMark(bmkno);
            if(ret == 1) {
                map.put("status", 200);
            } 
        }
        catch(Exception e) {
            e.printStackTrace();
			map.put("status", 0);
        }
        return map;
    }


    // 북마크 조회(즐겨찾는 챌린지/1개)
    // 127.0.0.1:9090/ROOT/api/bookmark/selectone?bmkno=북마크번호
    // Params => key:chgno, values:챌린지번호
    @RequestMapping(
        value    = "/selectone", 
        method   = {RequestMethod.GET},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> selectOneChallengeGET(
        @RequestHeader(name = "token") String token, 
        @RequestParam(name = "bmkno") long bmkno ){
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰에서 정보 추출
            String userSubject = jwtUtil.extractUsername(token);
            System.out.println("토큰에 담긴 정보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String email = jsonObject.getString("username");

            // 멤버 엔티티
            MemberCHG member = new MemberCHG();
            member.setMemail(email);

            BookMarkCHG bookmark = bmkService.bookmarkSelectOne(bmkno);
            System.out.println("북마크 번호 : " + bmkno);
            if(bookmark != null){
                map.put("status", 200);
                map.put("result", bookmark);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }


    // 북마크 목록(즐켜찾는 챌린지/목록)
    // 127.0.0.1:9090/ROOT/api/bookmark/selectlist
    @RequestMapping(
        value    = "/selectlist", 
        method   = {RequestMethod.GET},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> selectlistGET(
        @RequestHeader(name = "token") String token,
        @RequestParam(name = "page", defaultValue = "1") int page){  
        System.out.println("페이지 : " + page);
        System.out.println("토큰 : " + token);
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰에서 정보 추출
            String userSubject = jwtUtil.extractUsername(token);
            System.out.println("토큰에 담긴 정보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String email = jsonObject.getString("username");

            Pageable pageable = PageRequest.of(page - 1, 10);
            List<BookmarkProjection> list = bmkService.bookmarkSelectList(pageable, email);
            if(list != null){
                map.put("status", 200);
                map.put("result", list);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }


}
