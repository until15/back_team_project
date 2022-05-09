package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.RtnRunCHG;
import com.example.entity.RtnRunNumDto;
import com.example.jwt.JwtUtil;
import com.example.repository.RoutineRepository;
import com.example.repository.RtnRunRepository;
import com.example.service.RtnRunService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
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
            List<RtnRunCHG> list = rrService.RtnRunSelectlist(runseq);
            for (int i = 0; i < run.length; i++) {
                RtnRunCHG obj = new RtnRunCHG();
                obj.setRunno(run[i].getRunno());
                obj.setRoutinechg(run[i].getRoutinechg());

                list.add(obj);
                System.out.println(list);
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
            // @RequestParam(name = "no") Long[] runno,
            @RequestBody RtnRunNumDto num) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);

            // List로 변환
            List<Integer> arr = num.getNum();
            System.out.println("=====================" + arr);

            // 루틴 실행 번호 추출
            List<RtnRunCHG> rtnRun = rrRepository.findByRunnoIn(arr);
            System.out.println(rtnRun);

            // List<RtnRunCHG> rtnRun = rrRepository.findRtnRunCHGsIn(runno);
            // System.out.println("=======================" + a);

            // if
            // (username.equals(rtnRun.get(0).getRoutinechg().getMemberchg().getMemail())) {
            // int ret = rrService.RtnRunDelete(runno);
            // if (ret == 1) {
            // map.put("status", 200);
            // }
            // }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

}
