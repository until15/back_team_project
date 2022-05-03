package com.example.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.RtnRunCHG;
import com.example.jwt.JwtUtil;
import com.example.service.RtnRunService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rtnrun")
public class RtnRunRestController {

    @Autowired RtnRunService rrService;
    @Autowired JwtUtil jwtUtil;

    //루틴 실행 등록
    //127.0.0.1:9090/ROOT/api/rtnrun/insertbatch.json
    //[{"rtnday":"테스트", "rtncnt" : 10, "rtnset" : 1, "rtnname" : "가나다", "posechg":{"pno":15}, "memberchg":{"memail":""}}, {"rtnday":"테스트", "rtncnt" : 10, "rtnset" : 1, "rtnname" : "가나다", "posechg":{"pno":15}, "memberchg":{"memail":""}}]
    @RequestMapping(value="/insertbatch.json", method = {RequestMethod.POST},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> RoutineInsertPOST(
        @RequestHeader(name="token") String token,
        @RequestBody RtnRunCHG[] runno
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            List<RtnRunCHG> list = new ArrayList<>();
            for(int i=0; i<runno.length; i++){
                RtnRunCHG run = new RtnRunCHG();
                run.setRoutinechg(runno[i].getRoutinechg());
                
                list.add(run);
                System.out.println(list);
            }
            int ret = rrService.RtnRunInsert(list);
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
