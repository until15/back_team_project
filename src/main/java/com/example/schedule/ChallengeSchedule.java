package com.example.schedule;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
@EnableAsync
public class ChallengeSchedule {

	// 오늘 날짜에 시작하는 첼린지 상태 변경
	@Scheduled(cron = "0 0 16 * * *")
	@Async
	public void challengeStart() throws IOException {
		
		// rest 컨트롤러에서 만든 오늘날짜에 시작하는 첼린지 조회 URL 
		final String URL = "http://127.0.0.1:9090/ROOT/api/join/startchg";
		
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(URL).build();
		Response response = client.newCall(request).execute();
		String msg = response.body().string();	// String 타입으로 응답
		System.out.println(msg);
		
		// 스트링 타입의 메세지를 JSON형식으로 바꿔줌
		JSONObject jobj = new JSONObject(msg);
		System.out.println("상태 : "+ jobj.getLong("status"));

	}
	
	// 오늘 날짜에 종료하는 첼린지 상태 변경
	@Scheduled(cron = "0 0 16 * * *")
	@Async
	public void challengeEnd() throws IOException {
		
		// rest 컨트롤러에서 만든 오늘날짜에 시작하는 첼린지 조회 URL 
		final String URL = "http://127.0.0.1:9090/ROOT/api/join/endchg";
		
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(URL).build();
		Response response = client.newCall(request).execute();
		String msg = response.body().string();	// String 타입으로 응답
		System.out.println(msg);
		
		// 스트링 타입의 메세지를 JSON형식으로 바꿔줌
		JSONObject jobj = new JSONObject(msg);
		System.out.println("상태 : "+ jobj.getLong("status"));

	}
	
}
