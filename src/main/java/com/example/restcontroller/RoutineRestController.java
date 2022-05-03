package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.MemberCHG;
import com.example.entity.RoutineCHG;
import com.example.jwt.JwtUtil;
import com.example.service.RoutineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routine")
public class RoutineRestController {

    @Autowired JwtUtil jwtUtil;
    @Autowired RoutineService rService;

    @Value("${board.page.count}") int PAGECNT;

    //루틴 등록
    //127.0.0.1:9090/ROOT/api/routine/insertbatch.json
    //[{"rtnday":"테스트", "rtncnt" : 10, "rtnset" : 1, "rtnname" : "가나다", "posechg":{"pno":15}, "memberchg":{"memail":""}}, {"rtnday":"테스트", "rtncnt" : 10, "rtnset" : 1, "rtnname" : "가나다", "posechg":{"pno":15}, "memberchg":{"memail":""}}]
    @RequestMapping(value="/insertbatch.json", method = {RequestMethod.POST},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> RoutineInsertPOST(
        @RequestHeader(name="token") String token,
        @RequestBody RoutineCHG[] routine
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            List<RoutineCHG> list = new ArrayList<>();
            for(int i=0; i<routine.length; i++){
                RoutineCHG obj = new RoutineCHG();
                obj.setRtnday(routine[i].getRtnday());
                obj.setRtncnt(routine[i].getRtncnt());
                obj.setRtnset(routine[i].getRtnset());
                obj.setRtnname(routine[i].getRtnname());

                MemberCHG member = new MemberCHG();
                member.setMemail(username);
                obj.setMemberchg(member);
                obj.setPosechg(routine[i].getPosechg());
                
                list.add(obj);
                System.out.println(list);
            }
            int ret = rService.RoutineInsertBatch(list);
            if(ret == 1){
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 루틴 수정
    //127.0.0.1:9090/ROOT/api/routine/updatebatch.json
    @RequestMapping(value="/updatebatch.json", method = {RequestMethod.PUT},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> RoutineUpdatePUT(
        @RequestHeader(name="token") String token,
        @RequestBody RoutineCHG[] routine
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            List<RoutineCHG> list = rService.RoutineSelectlist(username);
            for(int i=0; i<routine.length; i++){
                RoutineCHG obj = new RoutineCHG();
                obj.setRtnno(routine[i].getRtnno());
                obj.setRtnday(routine[i].getRtnday());
                obj.setRtncnt(routine[i].getRtncnt());
                obj.setRtnset(routine[i].getRtnset());
                obj.setRtnname(routine[i].getRtnname());
                obj.setPosechg(routine[i].getPosechg());
                
                list.add(obj);
                System.out.println(list);
            }
            int ret = rService.RoutineUpdateBatch(list);
            if(ret == 1){
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 루틴 조회 
    //127.0.0.1:9090/ROOT/api/routine/selectlist.json
    @RequestMapping(value="/selectlist.json", method = {RequestMethod.GET},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> RoutineselectlistGET(
        @RequestHeader(name="token") String token
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            List<RoutineCHG> list = rService.RoutineSelectlist(username);
            if(!list.isEmpty()){
                map.put("status", 200);
                map.put("result", list);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 루틴 삭제
    //127.0.0.1:9090/ROOT/api/routine/deletebatch.json?no=1,2
    @RequestMapping(value="/deletebatch.json", method = {RequestMethod.DELETE},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> RoutineDeleteDELETE(
        @RequestHeader(name="token") String token,
        @RequestParam(name="no") Long[] rtnno
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            int ret = rService.RoutineDelete(rtnno);
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
