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

@Service
public class PayServiceImpl implements PayService{

    @Value("${imp_key}")
    private String impKey;

    @Value("${imp_secret}")
    private String impSecret;

    @Override
    public String getToken() throws IOException {
        try {
			URL url = new URL("https://api.iamport.kr/users/getToken");
            HttpsURLConnection httpcon = null;
 
			httpcon = (HttpsURLConnection) url.openConnection();
 
			httpcon.setRequestMethod("POST");
			httpcon.setRequestProperty("Content-type", "application/json");
			// httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setDoInput(true);
			httpcon.setDoOutput(true);
			
            JsonObject json = new JsonObject();
			json.addProperty("imp_key", impKey);
			json.addProperty("imp_secret", impSecret);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(httpcon.getOutputStream()));
			
			bw.write(json.toString());
			bw.flush();
			bw.close();
 
			BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "utf-8"));
 
			Gson gson = new Gson();
 
			String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
			
			System.out.println(response);
 
			String token = gson.fromJson(response, Map.class).get("access_token").toString();
 
			br.close();
			httpcon.disconnect();
 
			return token;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int paySelectOne(String imp_uid, String access_token) {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
