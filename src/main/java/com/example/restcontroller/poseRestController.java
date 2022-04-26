package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.PoseCHG;
import com.example.entity.VideoCHG;
import com.example.repository.PoseRepository;
import com.example.service.PoseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    @Autowired PoseRepository pRepository;

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
    // 127.0.0.1:9090/ROOT/api/pose/selectone.json?pno=
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


    // 자세 목록 (검색어 + 페이지네이션)
    // 127.0.0.1:9090/ROOT/api/pose/selectlist.json?page=1&title=&step=1
    @RequestMapping(value="/selectlist.json", method = {RequestMethod.GET},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> selectlistGET(
        @RequestParam(name="step546821#232", defaultValue = "1") int step,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name="title", defaultValue = "") String title
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page-1, 10);
            List<PoseCHG> list = pService.poseSelectList(step, pageable, title);
            long cnt = pService.poseCountSelect(title);
            if(list != null){
                map.put("status", 200);
                map.put("result", list);
                map.put("count", cnt);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 자세 삭제 (삭제가 아닌 수정 pstep)
    // 127.0.0.1:9090/ROOT/api/pose/delete.json
    // {"pno" : 1, "pstep" : 2}
    @RequestMapping(value="/delete.json", method = {RequestMethod.PUT},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> poseDeletePOST(
        @RequestBody PoseCHG pose
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            PoseCHG pose1 = pService.poseSelectOne(pose.getPno());
            pose1.setPstep(pose.getPstep());
            
            int ret = pService.poseDelete(pose1);
            if(ret == 1){
                map.put("status", 200);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 자세 동영상 등록 완성 X
    // 127.0.0.1:9090/ROOT/api/pose/insertvideo.json
    @RequestMapping(value="/insertvideo.json", method = {RequestMethod.POST},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, Object> poseVideoInsertPOST(
        @RequestBody VideoCHG video
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            int ret = pService.poseVideoInsert(video);
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
