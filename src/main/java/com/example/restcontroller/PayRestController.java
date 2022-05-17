package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import com.example.entity.PayCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.PayRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pay")
public class PayRestController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    PayRepository pRepository;

    // 결제 정보 등록
    // 127.0.0.1:9090/ROOT/api/pay/insert.json
    // 미완성
    @RequestMapping(value = "/insert.json", method = { RequestMethod.POST }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> poseInsertPOST(
            @RequestHeader(name = "token") String token,
            @RequestBody PayCHG pay) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰에서 아이디 추출
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(username);
            String email = jsonObject.getString("username");
            System.out.println(email);

            PayCHG payone = pRepository.save(pay);

            map.put("result", payone);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

}
