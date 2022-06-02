package com.example.restcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.PayCHG;
import com.example.entity.PayCHGProjection;
import com.example.jwt.JwtUtil;
import com.example.repository.PayRepository;
import com.example.service.PayService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @Value("${board.page.count}")
    int PAGECNT;


    // 결제 정보 검증 및 등록
    // 127.0.0.1:9090/ROOT/api/pay/insert.json
    @PostMapping(value = "/insert.json")
    public ResponseEntity<String> paySelectonPost
    (@RequestBody PayCHG pay) throws IOException {

        // 토큰 가져오기
        String token = pService.getToken();
        System.out.println("토큰 ========== " + token);

        // 결제된 금액
        int amount = pService.paySelectOne(pay.getImp_uid(), token);
        System.out.println("결제금액=======" + amount);
        System.out.println("결제되어야하는금액=========" + pay.getAmount());

        try {
            // 결제 되어야 하는 금액과 결제된 금액이 동일할 경우에만 정상 결제 및 DB 등록
            if(pay.getAmount() == amount){
                pRepository.save(pay);
                return new ResponseEntity<>("결제 완료", HttpStatus.OK);
            }
            else{
                pService.payCancle(pay.getImp_uid(), amount, "결제 실패");
                return new ResponseEntity<String>("결제 취소", HttpStatus.BAD_REQUEST);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            pService.payCancle(pay.getImp_uid(), amount, "결제 오류");
            return new ResponseEntity<String>("결제 오류", HttpStatus.BAD_REQUEST);
        }
    }

    // 유저 참가비 조회
    //127.0.0.1:9090/ROOT/api/pay/selectlist.json
    @RequestMapping(value = "/selectlist.json", method = { RequestMethod.GET }, consumes = {
        MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
public Map<String, Object> PayselectlistGET(
        @RequestHeader(name = "token") String token,
        @RequestParam(name = "page", defaultValue = "1") int page) {
    Map<String, Object> map = new HashMap<>();
    try {
        String username = jwtUtil.extractUsername(token);
        System.out.println("token : " + username);
        // 추출된 결과값을 JSONObject 형태로 파싱
        JSONObject jsonObject = new JSONObject(username);
        String email = jsonObject.getString("username");
        System.out.println(email);

        Pageable pageable = PageRequest.of(page-1, PAGECNT);
        List<PayCHGProjection> list = pRepository.findByJoinchg_Memberchg_memailContainingOrderByJoinchg_jnoDesc(email, pageable);

        // 전체 개수
        long total = pRepository.countByJoinchg_Memberchg_memailContaining(email);

        if (!list.isEmpty()) {
            map.put("status", 200);
            map.put("result", list);
            map.put("total", total);
        }

    } catch (Exception e) {
        e.printStackTrace();
        map.put("status", 0);
    }
    return map;
}

    // 환급 페이지에서 조회할 것
    // 127.0.0.1:9090/ROOT/api/pay/selectone.json
    @RequestMapping(value = "/selectone.json", method = { RequestMethod.GET }, consumes = {
        MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
public Map<String, Object> PayselectoneGET(
        @RequestHeader(name = "token") String token,
        @RequestParam(name = "no") long jno) {
    Map<String, Object> map = new HashMap<>();
    try {
        String username = jwtUtil.extractUsername(token);
        System.out.println("token : " + username);
        // 추출된 결과값을 JSONObject 형태로 파싱
        JSONObject jsonObject = new JSONObject(username);
        String email = jsonObject.getString("username");
        System.out.println(email);

        PayCHGProjection payProjection = pRepository.findByJoinchg_Memberchg_memailAndJoinchg_jnoEquals(email, jno);

        map.put("status", 200);
        map.put("result", payProjection);
        

    } catch (Exception e) {
        e.printStackTrace();
        map.put("status", 0);
    }
    return map;
}

    // 달성률에 따른 환급
    // 127.0.0.1:9090/ROOT/api/pay/refund.json
    @PostMapping(value = "/refund.json")
    public ResponseEntity<String> payRefoundPost
    (@RequestBody PayCHG pay) throws IOException {
        // 유저 참가비, 환불된 금액 가져오기
        PayCHG pay1 = pRepository.SelectOneImp(pay.getImp_uid());
        System.out.println("유저참가비=============" + pay1.getAmount());
        System.out.println("환불된 금액============" + pay1.getChecksum());

        // 유저 달성률 가져오기
        PayCHGProjection payProjection = pRepository.findByJoinchg_jnoEquals(pay.getJoinchg().getJno());
        
        // 환불 가능 금액
        int payprice = pay1.getAmount() - pay1.getChecksum();

        // 달성률 int로 변환(반올림)
        float payf = payProjection.getChgrate();
        int payint = Math.round(payf);

        // 달성률에 따른 환급금
        int payrefund = (int) (payprice*(payint*0.01));
        // 80% 이상 달성시 100% 환불
        if(payint >= 80 ){
            payrefund = payprice;
        }
        System.out.println("환급금====================" + payrefund);
        try {
            // 달성률이 양수
            if(payProjection.getChgrate() > 0){
                pService.payCancle(pay.getImp_uid(), payrefund, pay.getReason());
                PayCHG payput = pRepository.SelectOneImp(pay.getImp_uid());
                payput.setChecksum(payrefund);
                payput.setReason(pay.getReason());
                pRepository.save(payput);
                return new ResponseEntity<String>("환급 완료", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<String>("환급 실패", HttpStatus.BAD_REQUEST);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("환급 오류", HttpStatus.BAD_REQUEST);
        }
    }

}
