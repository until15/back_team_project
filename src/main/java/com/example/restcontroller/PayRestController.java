package com.example.restcontroller;

import java.io.IOException;

import com.example.entity.PayCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.PayRepository;
import com.example.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pay")
public class PayRestController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    PayService pService;

    @Autowired
    PayRepository pRepository;


    // 결제 정보 검증 및 등록
    // 127.0.0.1:9090/ROOT/api/pay/insert.json
    @PostMapping(value = "/insert.json")
    public ResponseEntity<String> paySelectonPost
    (@RequestBody PayCHG pay) throws IOException {

        // 토큰 가져오기
        String token = pService.getToken();
        System.out.println("토큰 ========== " + token);

        // 결제된 금액
        int amount = pService.paySelectOne(pay.getImpuid(), token);
        System.out.println("결제금액=======" + amount);
        System.out.println("결제되어야하는금액=========" + pay.getPprice());

        try {
            // 결제 되어야 하는 금액과 결제된 금액이 동일할 경우에만 정상 결제 및 DB 등록
            if(pay.getPprice() == amount){
                pRepository.save(pay);
                return new ResponseEntity<>("결제 완료", HttpStatus.OK);
            }
            else{
                pService.payCancle(pay.getImpuid(), token, amount, "결제 실패");
                return new ResponseEntity<String>("결제 취소", HttpStatus.BAD_REQUEST);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            pService.payCancle(pay.getImpuid(), token, amount, "결제 오류");
            return new ResponseEntity<String>("결제 오류", HttpStatus.BAD_REQUEST);
        }
    }

}
