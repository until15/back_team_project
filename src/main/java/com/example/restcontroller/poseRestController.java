package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import com.example.entity.PoseCHG;
import com.example.service.PoseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pose")
public class poseRestController {

    @Autowired PoseService pService;

    // 자세 등록
    // 127.0.0.1:9090/ROOT/api/pose/insert.json
    // {"pname":"aaa", "ppart" : "bbb", "pcontent" : "ccc", "plevel" : 1, "pstep" : 1, "memberchg":{"memail":"a@a.com"}}
    @RequestMapping(value="/insert.json", method = {RequestMethod.POST},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> poseInsertPOST(
        @RequestBody PoseCHG pose
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            int ret = pService.poseInsert(pose);
            if(ret == 1){
                map.put("status", 200);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 자세 수정
    // 127.0.0.1:9090/ROOT/api/pose/update.json
    // {"pname":"aaa2", "ppart" : "bbb2", "pcontent" : "ccc2", "plevel" : 1, "memberchg":{"memail":"a@a.com"}, "pno" : 1}
    @RequestMapping(value="/update.json", method = {RequestMethod.PUT},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> poseUpdatePOST(
        @RequestBody PoseCHG pose
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            PoseCHG pose1 = pService.poseSelectOne(pose.getPno());
            pose1.setPname(pose.getPname());
            pose1.setPpart(pose.getPpart());
            pose1.setPcontent(pose.getPcontent());
            pose1.setPlevel(pose.getPlevel());
            pose1.setMemberchg(pose.getMemberchg());
            
            int ret = pService.poseUpdate(pose1);
            if(ret == 1){
                map.put("status", 200);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 자세 조회
    @RequestMapping(value="/selectone.json", method = {RequestMethod.GET},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> selectoneGET(
        @RequestParam(name="pno") long pno
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            PoseCHG pose = pService.poseSelectOne(pno);
            if(pose != null){
                map.put("status", 200);
                map.put("result", pose);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }


    
}
