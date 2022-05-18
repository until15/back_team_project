package com.example.restcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.entity.PayCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.PayRepository;
import com.example.service.PayService;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.IamportClient;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/pay")
public class PayRestController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    PayService pService;

    @Autowired
    PayRepository pRepository;

    // 인증 토큰
    // public String getToken(HttpServletRequest request,
    //                 HttpServletResponse response,
    //                 JSONObject json,
    //                 String requestURL) throws Exception{
    // String _token = "";
    // try{
    //     String requestString = "";
    //     URL url = new URL("https://api.iamport.kr/users/getToken");
    //     HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
    //     connection.setDoOutput(true); 				
    //     connection.setInstanceFollowRedirects(false);  
    //     connection.setRequestMethod("POST");
    //     connection.setRequestProperty("Content-Type", "application/json");

    //     OutputStream os= connection.getOutputStream();
    //     os.write(json.toString().getBytes());
    //     connection.connect();

    //     StringBuilder sb = new StringBuilder();

    //     if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

    //         BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
    //         String line = null;  
    //         while ((line = br.readLine()) != null) {  
    //             sb.append(line + "\n");
    //         }
    //         br.close();
    //         requestString = sb.toString();
    //     }

    //     os.flush();
    //     connection.disconnect();

    //     JSONParser jsonParser = new JSONParser();
    //     JSONObject jsonObj = (JSONObject) jsonParser.parse(requestString);

    //     if((Long)jsonObj.get("code")  == 0){
    //         JSONObject getToken = (JSONObject) jsonObj.get("response");
    //         System.out.println("getToken =>"+ getToken.get("access_token") );
    //         _token = (String)getToken.get("access_token");
    //     }

    // }catch(Exception e){
    //     e.printStackTrace();
    //     _token = "";
    // }
    // return _token;
    // }

    // 인증 토큰 발급 받기 
    // 127.0.0.1:9090/ROOT/api/pay/paytoken.json
    // @PostMapping(value = "/paytoken.json")
    // public void payTokenPost(@RequestParam(name = "imp_uid")String impuid){
    //     RestTemplate restTemplate = new RestTemplate();

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_JSON);
        
    //     String imp_key 		=	URLEncoder.encode('rest API KEY', "UTF-8");
    //     String imp_secret	=	URLEncoder.encode('rest Secret KEY', "UTF-8");
    //     JSONObject json = new JSONObject();
    //     json.put("imp_key", imp_key);
    //     json.put("imp_secret", imp_secret);

    //     try {
    //         HttpEntity<JsonObject> entity = new HttpEntity<>(json, headers);
    //         ResponseEntity<JsonObject> token = restTemplate.postForEntity("https://api.iamport.kr/users/getToken", entity, JsonObject.class);
            
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    // }

    // 인증 토큰
    // 127.0.0.1:9090/ROOT/api/pay/paytoken.json
    @RequestMapping(value = "/paytoken.json", method = { RequestMethod.POST }, consumes = {
        MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
        public Map<String, Object> payTokenPOST(
        @RequestParam(name="imp_uid")String imp_uid) {
        Map<String, Object> map = new HashMap<>();
        try {
            System.out.println(imp_uid);

            
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }


    // 결제 정보 검증 및 등록
    // 127.0.0.1:9090/ROOT/api/pay/insert.json
    // 미완성
    @PostMapping(value = "/insert.json")
    public ResponseEntity<String> paySelectonPost
    (@RequestBody PayCHG pay) throws IOException {

        pay.getImpuid();
        pay.getMerchantuid();

        System.out.println(pay.getImpuid());
        System.out.println(pay.getMerchantuid());

        String token = pService.getToken();
        System.out.println("토큰 : " + token);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    // @RequestMapping(value = "/insert.json", method = { RequestMethod.POST }, consumes = {
    //         MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    // public Map<String, Object> poseInsertPOST(
    //         ) {
    //     Map<String, Object> map = new HashMap<>();
    //     try {
    //         String token = pService.getToken();
    //         System.out.println("토큰 : " + token);
            
    //         // PayCHG payone = pRepository.save(pay);

    //         // map.put("result", payone);
    //         // map.put("status", 200);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         map.put("status", 0);
    //     }
    //     return map;
    // }

}
