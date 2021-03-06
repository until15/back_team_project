package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.InquiryCHG;
import com.example.entity.InquiryCHGProjection;
import com.example.entity.IqcommentCHG;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.InquiryRepository;
import com.example.repository.IqcommentRepository;
import com.example.service.InquiryService;
import com.example.service.IqcommentService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/Iqcomment")
public class IqcommentRestController {

    @Autowired
    IqcommentService iService;

    @Autowired
    IqcommentRepository iRepository;

    @Autowired
    InquiryService inService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    InquiryRepository inRepository;

    // 댓글 등록
    // 127.0.0.1:9090/until15/api/Iqcomment/insert
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertPOST(@RequestBody IqcommentCHG iqcomment,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {

            // 토큰에서 이메일 추출
            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String memail = jsonObject.getString("username");

            // 회원엔티티 객체 생성 및 이메일 추가
            MemberCHG member = new MemberCHG();
            member.setMemail(memail);
            // 게시판 엔티티에 추가
            iqcomment.setMemberchg(member);

            int ret = iService.iqcommentInsert(iqcomment);
            if (ret == 1) {
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 댓글 목록에 보이기
    // 127.0.0.1:9090/until15/api/Iqcomment/selectone
    @RequestMapping(value = "/selectone", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertPOST(@RequestParam(name = "qno") long qno,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {

            // 토큰에서 이메일 추출
            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String memail = jsonObject.getString("username");
            // System.out.println(memail);

            InquiryCHG inquiry = inService.inquirySelectOne(qno);
            List<IqcommentCHG> list = iService.icommentSelectList(inquiry.getQno());
            if (list != null) {

                map.put("result", list);
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 댓글 삭제
    // 127.0.0.1:9090/until15/api/Iqcomment/delete?iqcmtno=1
    @RequestMapping(value = "/delete", method = { RequestMethod.DELETE }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> deleteOne(@RequestParam(name = "iqcmtno") long iqcmtno,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {

            String username = jwtUtil.extractUsername(token);
            // System.out.println(username);

            int ret = iService.deleteIqcomment(iqcmtno);
            if (ret == 1) {
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 127.0.0.1:9090/until15/api/Iqcomment/selectlist
    @RequestMapping(value = "/selectlist", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> selectListGET(
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {

            // 토큰에서 이메일 추출
            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String memail = jsonObject.getString("username");
            // System.out.println(memail);
            List<IqcommentCHG> list = iRepository.findAll();
            for (int i = 0; i < list.size(); i++) {

                if (list != null) {

                    map.put("result", list);
                    map.put("status", 200);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

}
