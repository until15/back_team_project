package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import com.example.entity.CommentCHG;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/comment")
public class CommentRestController {

    @Autowired
    CommentService cService;

    @Autowired
    JwtUtil jwtUtil;

    // 127.0.0.1:9090/ROOT/api/comment/insert
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertPOST(@RequestBody CommentCHG comment,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {

            // 토큰에서 이메일 추출
            String memail = jwtUtil.extractUsername(token);

            // 회원엔티티 객체 생성 및 이메일 추가
            MemberCHG member = new MemberCHG();
            member.setMemail(memail);
            // 게시판 엔티티에 추가
            comment.setMemberchg(member);

            int ret = cService.commentInsert(comment);
            if (ret == 1) {
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 127.0.0.1:9090/ROOT/api/comment/select?cmtno=3
    @RequestMapping(value = "/select", method = { RequestMethod.GET }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertPOST(@RequestParam(name = "cmtno") long cmtno) {
        Map<String, Object> map = new HashMap<>();
        try {
            CommentCHG comment = cService.selectOneComment(cmtno);
            map.put("result", comment);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 127.0.0.1:9090/ROOT/api/comment/delete?cmtno=1
    @RequestMapping(value = "/delete", method = { RequestMethod.DELETE }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> deleteOne(@RequestParam(name = "cmtno") long cmtno) {
        Map<String, Object> map = new HashMap<>();
        try {
            int ret = cService.deleteComment(cmtno);
            if (ret == 1) {
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    @RequestMapping(value = "/colike", method = { RequestMethod.PUT }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> likeUpPUT(@RequestParam(name = "cmtno") long cmtno) {
        Map<String, Object> map = new HashMap<>();
        System.out.println(cmtno);
        try {
            CommentCHG comment = cService.likeOne(cmtno);
            map.put("retult", comment);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;

    }

}
