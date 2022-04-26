package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.JoinCHG;
import com.example.service.JoinService;

@RestController
@RequestMapping("/api/join")
public class JoinRestController {

	@Autowired JoinService jService;
	
	// 참가하기
	// 127.0.0.1:9090/ROOT/api/join/insert
	// {"chgstate":1, "challengechg":{"chgno":1}, "memberchg":{"memail":"bb"} }
	@RequestMapping(value="/insert", 
			method = {RequestMethod.POST},	// POST로 받음
			consumes = {MediaType.ALL_VALUE},	// 모든 타입을 다 받음
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public Map<String, Object> insertJoinPOST(
			@RequestBody JoinCHG join){
		
		Map<String, Object> map = new HashMap<>();
		try {
			System.out.println(join.toString()); 	// 넘어오는 join
			
			int ret = jService.challengeJoin(join);
			
			if(ret == 1) {	
				map.put("status", 200);	
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
		}
		
		return map;
		
	}
	
	// 포기하기
	
	
	// 참여 중인 첼린지 조회
	
	
	// 참여했던 첼린지 전체 조회
	
	
	
}
