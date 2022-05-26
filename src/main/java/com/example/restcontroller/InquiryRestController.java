package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.InquiryCHG;
import com.example.entity.InquiryCHGProjection;
import com.example.entity.InquiryimgCHGProjection;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.InquiryRepository;
import com.example.repository.InquiryimgRepository;
import com.example.repository.IqcommentRepository;
import com.example.service.InquiryService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/Inquiry")
public class InquiryRestController {

    @Autowired
    InquiryService iService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    InquiryRepository iRepository;

    @Autowired
    InquiryimgRepository imRepository;

    @Autowired
    IqcommentRepository iqRepository;

    @Value("${board.page.count}")
    int PAGECNT;

    // 문의등록
    // 127.0.0.1:9090/ROOT/api/Inquiry/insert
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertPOST(@RequestBody InquiryCHG inquiry,
            @RequestHeader(name = "token") String token) {

        Map<String, Object> map = new HashMap<>();
        try {

            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            JSONObject jsonObject = new JSONObject(userSubject);
            String username = jsonObject.getString("username");
            // System.out.println(username);

            // 회원엔티티 객체 생성 및 이메일 추가
            MemberCHG member = new MemberCHG();
            member.setMemail(username);
            // 게시판 엔티티에 추가
            inquiry.setMemberchg(member);

            long ret = iService.inquiryInsertOne(inquiry);
            if (ret > 0) {
                map.put("result", ret);
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 문의 게시판 리스트
    // 127.0.0.1:9090/ROOT/api/Inquiry/selectlist
    @RequestMapping(value = "/selectlist", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> inquirySelectListGET(@RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "qtitle", defaultValue = "") String qtitle,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {

            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            JSONObject jsonObject = new JSONObject(userSubject);
            String username = jsonObject.getString("username");

            Pageable pageable = PageRequest.of(page - 1, PAGECNT);
            List<InquiryCHGProjection> list = iService.selectListInquiry(username, username, pageable, qtitle);

            long total = iRepository.countByQtitleContaining(qtitle);

            if (list != null) {
                map.put("total", total);
                map.put("status", 200);
                map.put("result", list);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }

        return map;
    }

    // 문의 게시글 1개 조회
    // 127.0.0.1:9090/ROOT/api/Inquiry/selectone?qno=1
    @RequestMapping(value = "/selectone", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> inquirySelectListGET(@RequestParam(name = "qno") long qno,
            @RequestHeader(name = "token") String token) {

        Map<String, Object> map = new HashMap<>();
        try {

            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            JSONObject jsonObject = new JSONObject(userSubject);
            String username = jsonObject.getString("username");
            // System.out.println(username);
            InquiryCHGProjection inquiry = iRepository.findByQno(qno);
            // InquiryCHG ret = iService.inquirySelectOne(qno);

            List<InquiryimgCHGProjection> list = imRepository.findByInquirychg_qnoOrderByQimgnoDesc(qno);
            String[] imgs = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                imgs[i] = "/ROOT/api/Inquiryimg/selectimg?qimgno=" + list.get(i).getQimgno();
            }

            map.put("result", inquiry);
            map.put("imgurl", imgs);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 문의 게시글 삭제
    // 127.0.0.1:9090/ROOT/api/Inquiry/delete?qno=1
    @RequestMapping(value = "/delete", method = { RequestMethod.DELETE }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> inquiryDELETE(@RequestParam(name = "qno") long qno,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {

            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String username = jsonObject.getString("username");
            // System.out.println(username);

            int ret = iService.inquiryDeleteOne(qno);
            if (ret == 1) {
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 문의 게시글 수정
    // 127.0.0.1:9090/ROOT/api/Inquiry/update?qno=1
    @RequestMapping(value = "/update", method = { RequestMethod.PUT }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> inquiryupdatePUT(@RequestBody InquiryCHG inquiry,
            @RequestHeader(name = "token") String token, @RequestParam(name = "qno") long qno) {
        Map<String, Object> map = new HashMap<>();
        try {

            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String username = jsonObject.getString("username");
            // System.out.println(username);

            InquiryCHG inquiry1 = iService.inquirySelectOne(qno);
            inquiry1.setQtitle(inquiry.getQtitle());
            inquiry1.setQcontent(inquiry.getQcontent());

            iService.inquiryUpdateOne(inquiry1);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // com +
    // 127.0.0.1:9090/ROOT/api/Inquiry/updatecom?qno=
    @RequestMapping(value = "/updatecom", method = { RequestMethod.PUT }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> updateCom(@RequestParam(name = "qno") long qno) {
        Map<String, Object> map = new HashMap<>();
        try {

            int ret = iService.inquiryUpdateCom(qno);
            System.out.println(ret);
            if (ret == 1) {
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // com -
    // 127.0.0.1:9090/ROOT/api/Inquiry/updatetwo?qno=
    @RequestMapping(value = "/updatetwo", method = { RequestMethod.PUT }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> updateComTwo(@RequestParam(name = "qno") long qno) {
        Map<String, Object> map = new HashMap<>();
        try {

            int ret = iService.inquiryTwoeCom(qno);
            System.out.println(ret);
            if (ret == 1) {
                map.put("result", ret);
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

}
