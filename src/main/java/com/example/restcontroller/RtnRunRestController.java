package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.MemberCHG;
import com.example.entity.RoutineCHG;
import com.example.entity.RtnRunCHG;
import com.example.entity.RtnRunNumDto;
import com.example.jwt.JwtUtil;
import com.example.repository.RoutineRepository;
import com.example.repository.RtnRunRepository;
import com.example.service.RtnRunService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rtnrun")
public class RtnRunRestController {

    @Autowired
    RtnRunService rrService;
    @Autowired
    RoutineRepository rRepository;
    @Autowired
    RtnRunRepository rrRepository;
    @Autowired
    JwtUtil jwtUtil;

    // 루틴 실행 등록
    // [{"routinechg":{"rtnno":23}}, {"routinechg":{"rtnno":24}}]
    // 127.0.0.1:9090/ROOT/api/rtnrun/insertbatch.json
    @RequestMapping(value = "/insertbatch.json", method = { RequestMethod.POST }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> RunInsertPOST(
            @RequestHeader(name = "token") String token,
            @RequestBody RtnRunCHG[] run) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰에서 아이디 추출
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(username);
            String email = jsonObject.getString("username");
           
           System.out.println(email);

            List<RtnRunCHG> list = new ArrayList<>();
            for (int i = 0; i < run.length; i++) {
                RtnRunCHG obj = new RtnRunCHG();
                obj.setRoutinechg(run[i].getRoutinechg());

                list.add(obj);
                System.out.println(list);
            }
            int ret = rrService.RtnRunInsert(list);
            if (ret == 1) {
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 루틴 실행 수정
    // 127.0.0.1:9090/ROOT/api/rtnrun/updatebatch.json
    // [{"runno" : 6, "routinechg":{"rtnno":19}}, {"runno" :
    // 7,"routinechg":{"rtnno":20}}]
    @RequestMapping(value = "/updatebatch.json", method = { RequestMethod.PUT }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> RoutineUpdatePUT(
            @RequestHeader(name = "token") String token,
            @RequestBody RtnRunCHG[] run,
            @RequestParam(name = "no") long runseq) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(username);
            String email = jsonObject.getString("username");
            System.out.println(email);

            List<RtnRunCHG> list = rrService.RtnRunSelectlist(runseq);
            // rtnno -> routine의 memail과 동일한 지 비교
            if(email.equals(list.get(0).getRoutinechg().getMemberchg().getMemail())){
                for (int i = 0; i < run.length; i++) {
                    RtnRunCHG obj = new RtnRunCHG();
                    obj.setRunno(run[i].getRunno());
                    obj.setRoutinechg(run[i].getRoutinechg());
    
                    list.add(obj);
                    // System.out.println(list);
                }                
            }
            int ret = rrService.RtnRunUpdate(list);
            if (ret == 1) {
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 루틴 실행 조회
    // 127.0.0.1:9090/ROOT/api/rtnrun/selectlist.json
    @RequestMapping(value = "/selectlist.json", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> RoutineselectlistGET(
            @RequestHeader(name = "token") String token,
            @RequestParam(name = "no") Long runseq) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            // 추출된 결과값을 JSONObject 형태로 파싱
           JSONObject jsonObject = new JSONObject(username);
           String email = jsonObject.getString("username");
           
           System.out.println(email);
            List<RtnRunCHG> list = rrService.RtnRunSelectlist(runseq);
            if (!list.isEmpty()) {
                map.put("status", 200);
                map.put("result", list);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 루틴 실행 삭제
    // 127.0.0.1:9090/ROOT/api/rtnrun/deletebatch.json?no=1,2
    @RequestMapping(value = "/deletebatch.json", method = { RequestMethod.DELETE }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> RoutineDeleteDELETE(
            @RequestHeader(name = "token") String token,
            @RequestParam(name="no") Long[] runno
            ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(username);
            String email = jsonObject.getString("username");
           
            System.out.println(email);

            // List<RtnRunCHG> list = rrRepository.findByRunnoEqualsOrderByRunnoDesc(runno);
            // System.out.println("===========" + runno);

            // if(email.equals(list.get(0).getRoutinechg().getMemberchg().getMemail())) {
                int ret = rrService.RtnRunDelete(runno);
                if (ret == 1) {
                    map.put("status", 200);
                }
            // }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

}
