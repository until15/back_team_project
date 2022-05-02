package com.example.restcontroller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.SequenceGenerator;

import com.example.entity.PoseCHG;
import com.example.entity.RoutineCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.PoseRepository;
import com.example.service.RoutineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    //루틴 등록
    //127.0.0.1:9090/ROOT/api/routine/insertbatch.json
    // [{"rtnday":"월", "rtncnt" : 10, "rtnset" : 1, "rtnname" : "가나다", "posechg":{"pno":15}}, {"rtnday":"화", "rtncnt" : 10, "rtnset" : 1, "rtnname" : "가나다", "posechg":{"pno":15}}]
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
            for(int i=0; i<routine[i].getRtnname().length(); i++){
                RoutineCHG obj = new RoutineCHG();
                obj.setRtnday(routine[i].getRtnday());
                obj.setRtncnt(routine[i].getRtncnt());
                obj.setRtnset(routine[i].getRtnset());
                obj.setRtnname(routine[i].getRtnname());
                obj.setPosechg(routine[i].getPosechg());
                
                // System.out.println(obj.toString());
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

    
}
