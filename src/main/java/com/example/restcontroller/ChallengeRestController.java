package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import com.example.entity.ChallengeCHG;
import com.example.service.ChallengeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeRestController {
    
    @Autowired ChallengeService chgService;

    // 챌린지 등록
    // 127.0.0.1:9090/ROOT/api/challenge/insert.json
    // {"chgtitle":"aaa", "chgintro" : "bbb", "chgcontent" : "ccc", "chgstart" : 1, "chgend" : 1, "recruitstart" : 1, "recruitend" : 1, "chfee" : 10000, "memberchg":{"memail":"admin"}}
    @RequestMapping(
        value="/insert.json", 
        method = {RequestMethod.POST},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> insertChallengePOST(
        @RequestBody ChallengeCHG chg
    ){  
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
}
