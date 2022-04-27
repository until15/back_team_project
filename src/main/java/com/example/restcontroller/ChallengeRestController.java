package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.ChallengeCHG;
import com.example.service.ChallengeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeRestController {
    
    @Autowired ChallengeService chgService;

    // 챌린지 등록
    // 127.0.0.1:9090/ROOT/api/challenge/insert.json
    // {"chgtitle":"aaa", "chgintro" : "bbb", "chgcontent" : "ccc", "chgstart" : 1, "chgend" : 1, "recruitstart" : 1, "recruitend" : 1, "chfee" : 10000, "memberchg":{"memail":"admin"}}
    @RequestMapping(
        value    ="/insert.json", 
        method   = {RequestMethod.POST},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> insertChallengePOST(
        @RequestBody ChallengeCHG chg){  
        Map<String, Object> map = new HashMap<>();
        try {
            int ret = chgService.insertChallengeOne(chg);
            if(ret == 1){
                map.put("status", 200);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }


    // 챌린지 수정
    // 127.0.0.1:9090/ROOT/api/challenge/updateone.json
    // {"chgno" : 1, "chgtitle" : "aaa2", "chgintro" : "bbb2", "chgcontent" : "ccc2"}
    @RequestMapping(
        value    ="/updateone.json", 
        method   = {RequestMethod.PUT},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> updateChallengePUT(
        @RequestBody ChallengeCHG chg ){
        Map<String, Object> map = new HashMap<>();
        try {
            ChallengeCHG challenge = chgService.challengeSelectOne(chg.getChgno());
            challenge.setChgtitle(chg.getChgtitle());
            challenge.setChgintro(chg.getChgintro());
            challenge.setChgcontent(chg.getChgcontent());

            chgService.challengeUpdateOne(challenge);
            map.put("status", 200);
        }
        catch(Exception e) {
			e.printStackTrace();
			map.put("status", 0);
        }
        return map;
    }  

    // -----------------------------------------------미완성------------
    // 챌린지 삭제
    // 127.0.0.1:9090/ROOT/api/challenge/delete?chgno=1
    @RequestMapping(
        value    ="/delete.json", 
        method   = {RequestMethod.DELETE},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> deleteChallengeDELETE(
        @RequestParam("chgno") long chgno ){
            System.out.println(chgno);
        Map<String, Object> map = new HashMap<>();
        try {
            int ret = chgService.deleteChallenge(chgno);
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

    // 챌린지 1개 조회
    // 127.0.0.1:9090/ROOT/api/challenge/selectone.json?chgno=챌린지번호
    // Params => key:chgno, values:챌린지번호
    @RequestMapping(
        value    ="/selectone.json", 
        method   = {RequestMethod.GET},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> selectOneChallengeGET(
        @RequestParam("chgno") long chgno ){
        Map<String, Object> map = new HashMap<>();
        try {
            ChallengeCHG challenge = chgService.challengeSelectOne(chgno);
            if(challenge != null){
                map.put("status", 200);
                map.put("result", challenge);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // -----------------------------------------------미완성------------
    // 자세 목록 (검색어 + 페이지네이션) 
    // 127.0.0.1:9090/ROOT/api/pose/selectlist.json?page=1&title=&step=1
    @RequestMapping(
        value="/selectlist.json", 
        method = {RequestMethod.GET},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> selectlistGET(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name="challenge", defaultValue = "") String challenge ){  
        Map<String, Object> map = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page - 1, 10);
            List<ChallengeCHG> list = chgService.challengeSelectList(pageable   , challenge);
            if(list != null){
                map.put("status", 200);
                map.put("result", list);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }


    // 챌린지 시작
    
    // 챌린지 종료

    // 챌린지 모집 시작

    // 챌린지 모집 종료
    









}
