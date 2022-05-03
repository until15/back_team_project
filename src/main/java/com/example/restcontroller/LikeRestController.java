package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.ChallengeCHG;
import com.example.entity.LikeCHG;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.ChallengeRepository;
import com.example.repository.LikeRepository;
import com.example.service.ChallengeService;
import com.example.service.LikeService;

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
@RequestMapping("/api/like")
public class LikeRestController {

    @Autowired JwtUtil jwtUtil;

    @Autowired LikeService lService;

    @Autowired ChallengeService chgService;

    @Autowired LikeRepository lRepository;

    @Autowired ChallengeRepository cRepository;




    // 좋아요 추가
    // 127.0.0.1:9090/ROOT/api/like/insert
    // params => chgno:1
    // headers => token:...
    // body/json => { "memail":"이메일" }
    @RequestMapping(
        value    = "/insert", 
        method   = {RequestMethod.POST},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> insertBookmarkPOST(
        @RequestBody LikeCHG like,
        @RequestHeader(name = "token") String token, 
        @RequestParam(name = "chgno") long chgno ){  
            System.out.println("토큰 : " + token);
            System.out.println("챌린지 번호 : " + chgno);
        Map<String, Object> map = new HashMap<>();
        try {
            // 멤버 토큰
            String memail = jwtUtil.extractUsername(token);

            // 멤버 엔티티 
            MemberCHG member = new MemberCHG();
            member.setMemail(memail);

            // 챌린지 조회
            ChallengeCHG challenge = chgService.challengeSelectOne(chgno);
            System.out.println(challenge.toString());

            // 저장
            like.setChallengechg(challenge);
            like.setMemberchg(member);

            // 중복 확인
            LikeCHG duplicate = lService.duplicateInsert(chgno, memail);

            if(duplicate == null) {
                int ret = lService.insertLike(like);
                if(ret == 1) {
                    // chgno 불러오기
                    long chglike = lRepository.countByChallengechg_Chgno(chgno);
                    System.out.println(chglike);
                    challenge.setChglike(chglike);

                    // 저장
                    cRepository.save(challenge);
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

    // 좋아요 삭제(해제)
    // 127.0.0.1:9090/ROOT/api/like/delete
    // params => lno : 1, chgno : 1
    // headers => token:...
    @RequestMapping(
        value    = "/delete", 
        method   = {RequestMethod.DELETE},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> deleteChallengeDELETE(
        @RequestHeader(name = "token") String token, 
        @RequestParam(name = "chgno") long chgno,
        @RequestParam(name = "lno") long lno ){
            System.out.println("좋아요 번호 : "+ lno);
        Map<String, Object> map = new HashMap<>();
        try {
            // 멤버 토큰
            String memail = jwtUtil.extractUsername(token);

            // 멤버 엔티티 
            MemberCHG member = new MemberCHG();
            member.setMemail(memail);

            ChallengeCHG challenge = chgService.challengeSelectOne(chgno);
            System.out.println(challenge.toString());

            int ret = lService.deleteLike(lno);
            if(ret == 1) {
                long chglike = lRepository.countByChallengechg_Chgno(chgno);
                System.out.println(chglike);
                challenge.setChglike(chglike);

                // 저장
                cRepository.save(challenge);
                map.put("status", 200);
            } 
        }
        catch(Exception e) {
            e.printStackTrace();
			map.put("status", 0);
        }
        return map;
    }

    // 좋아요 조회
    // 127.0.0.1:9090/ROOT/api/like/selectone?lno=  
    // Params => key:lno, values:
    @RequestMapping(
        value    = "/selectone", 
        method   = {RequestMethod.GET},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> selectOneChallengeGET(
        @RequestHeader(name = "token") String token, 
        @RequestParam(name = "lno") long lno ){
        Map<String, Object> map = new HashMap<>();
        try {
            // 멤버 토큰
            String memail = jwtUtil.extractUsername(token);

            // 멤버 엔티티
            MemberCHG member = new MemberCHG();
            member.setMemail(memail);

            LikeCHG like = lService.likeSelectOne(lno);
            System.out.println("좋아요 번호 : " + lno);
            if(like != null){
                map.put("status", 200);
                map.put("result", like);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 좋아요 목록
    // 127.0.0.1:9090/ROOT/api/like/selectlist
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
            String memail = jwtUtil.extractUsername(token);
            System.out.println("토큰 : " + token);
            Pageable pageable = PageRequest.of(page - 1, 10);
            List<LikeCHG> list = lService.likeSelectList(pageable, memail);
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
