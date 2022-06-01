package com.example.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.ToString;



@Service
public class PayServiceImpl implements PayService{

    @Value("${imp_key}")
    private String impKey;

    @Value("${imp_secret}")
    private String impSecret;


    // 결제 금액을 가져오기 위한 클래스 생성
    @Getter
    @ToString
    private class Response {
        private payAmount response;
    }

    @Getter
    @ToString
    private class payAmount {
        private int amount;
    }

    // 토큰 발급
    @Override
    public String getToken() throws IOException {
        try {
			URL url = new URL("https://api.iamport.kr/users/getToken");
            HttpsURLConnection httpcon = null;
 
			httpcon = (HttpsURLConnection) url.openConnection();
 
			httpcon.setRequestMethod("POST");
			httpcon.setRequestProperty("Content-type", "application/json");
            httpcon.setDoInput(true);
			httpcon.setDoOutput(true);

            // System.out.println("impKey===============" + impKey);
            // System.out.println("imp_secret===========" + impSecret);
			
            JsonObject json = new JsonObject();
			json.addProperty("imp_key", impKey);
			json.addProperty("imp_secret", impSecret);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(httpcon.getOutputStream()));
			
			bw.write(json.toString());
			bw.flush();
			bw.close();

            // System.out.println("PayServiceImpl 버퍼writer=============" + bw);
 
			BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "utf-8"));

            // System.out.println("PayServiceImpl 버퍼reader=============" + br);
 
			Gson gson = new Gson();
            
            // Json 문자열을 Object 클래스로 변환
			String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
			
			// System.out.println("PayServiceImpl response=========" + response);
 
			String token = gson.fromJson(response, Map.class).get("access_token").toString();
 
			br.close();
			httpcon.disconnect();

            // System.out.println("ServiceImpl token===============" + token);
 
			return token;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 결제 토큰을 사용하여 결제 금액 조회
    @Override
    public int paySelectOne(String imp_uid, String access_token) {
        try {
            URL url = new URL("https://api.iamport.kr/payments/" + imp_uid);

            HttpsURLConnection httpConn = null;
         
            httpConn = (HttpsURLConnection) url.openConnection();
         
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Authorization", access_token);
            httpConn.setDoOutput(true);
         
            BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
            
            Gson gson = new Gson();
            
            Response response = gson.fromJson(br.readLine(), Response.class);
            
            br.close();
            httpConn.disconnect();
            
            return response.getResponse().getAmount();
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 결제 취소 및 환불
    @Override
    public void payCancle(String access_token, String imp_uid, int amount, String reason) throws IOException{

        System.out.println("결제취소및환불====================");
        System.out.println(access_token);
        System.out.println(imp_uid);
        
        URL url = new URL("https://api.iamport.kr/payments/cancel");
        
        HttpsURLConnection httpConn = null;
        httpConn = (HttpsURLConnection) url.openConnection();

        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Accept", "application/json");
        httpConn.setRequestProperty("Content-type", "application/json");
        httpConn.setRequestProperty("Authorization", access_token);
        httpConn.setDoOutput(true);

        JsonObject json = new JsonObject();
        
		json.addProperty("imp_uid", imp_uid); // imp_uid를 환불 `unique key`로 입력
		json.addProperty("amount", amount); // 가맹점 클라이언트로부터 받은 환불금액
		// json.addProperty("checksum", amount); // [권장] 환불 가능 금액 입력
        json.addProperty("reason", reason); // 가맹점 클라이언트로부터 받은 환불사유

        System.out.println("serviceImpl======================="+reason);
        System.out.println("금액==================="+amount);
        System.out.println("imp_uid=========================" + imp_uid);
 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(httpConn.getOutputStream()));

		bw.write(json.toString());
		bw.flush();
		bw.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));

        String inputLine;
        while((inputLine=br.readLine())!=null){
            System.out.println("=====================RESPONSE"+inputLine);
        }

		br.close();
		httpConn.disconnect();
    }
    
}
