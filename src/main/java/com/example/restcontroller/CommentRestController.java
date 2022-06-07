package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.CommentCHG;
import com.example.entity.CommunityCHG;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.CommentRepository;
import com.example.repository.MemberRepository;
import com.example.service.CommentService;
import com.example.service.CommuniryService;

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
@RequestMapping(value = "/api/comment")
public class CommentRestController {

    @Autowired
    CommentService cService;

    @Autowired
    CommuniryService coService;

    @Autowired
    MemberRepository mRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CommentRepository cRepository;

    // 댓글 등록
    // 127.0.0.1:9090/until15/api/comment/insert
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertPOST(@RequestBody CommentCHG comment,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {

            // 토큰에서 이메일 추출
            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String memail = jsonObject.getString("username");

            System.out.println(memail);
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

    // 댓글 목록에 보이기
    // 127.0.0.1:9090/until15/api/comment/selectone
    @RequestMapping(value = "/selectone", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> selectOnePOST(@RequestParam(name = "bno") long bno) {
        Map<String, Object> map = new HashMap<>();
        try {

            // 게시판 번호 불러옴
            CommunityCHG community = coService.boardSelectOne(bno);

            // 게시판에 등록된 댓글 목록 불러옴
            List<CommentCHG> list = cService.commentSelectList(community.getBno());

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
    // 127.0.0.1:9090/until15/api/comment/delete?cmtno=1
    @RequestMapping(value = "/delete", method = { RequestMethod.DELETE }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> deleteOne(@RequestParam(name = "cmtno") long cmtno,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {

            // 토큰에서 이메일 추출
            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String memail = jsonObject.getString("username");

            CommentCHG comment = cRepository.findByMemberchg_memailAndCmtnoEqualsOrderByCmtnoDesc(memail, cmtno);
            // System.out.println(memail);

            int ret = cService.deleteComment(comment.getCmtno());
            if (ret == 1) {
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // // 댓글 좋아요
    // @RequestMapping(value = "/colike", method = { RequestMethod.PUT }, consumes =
    // { MediaType.ALL_VALUE }, produces = {
    // MediaType.APPLICATION_JSON_VALUE })
    // public Map<String, Object> likeUpPUT(@RequestParam(name = "cmtno") long
    // cmtno) {
    // Map<String, Object> map = new HashMap<>();
    // System.out.println(cmtno);
    // try {
    // CommentCHG comment = cService.likeOne(cmtno);
    // map.put("retult", comment);
    // map.put("status", 200);

    // } catch (Exception e) {
    // e.printStackTrace();
    // map.put("status", 0);
    // }
    // return map;
    // }

}
