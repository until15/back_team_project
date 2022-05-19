package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.BimgCHGProjection;
import com.example.entity.CommunityCHG;
import com.example.entity.CommunityCHGProjection;
import com.example.entity.MemberCHG;
import com.example.entity.MemberCHGProjection;
import com.example.jwt.JwtUtil;
import com.example.repository.BimgRepository;
import com.example.repository.CommunityRepository;
import com.example.repository.MemberRepository;
import com.example.service.CommentService;
import com.example.service.CommuniryService;
import com.example.service.UserDetailsServiceImpl;

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
@RequestMapping(value = "/api/community")
public class CommunityRestController {

    @Autowired
    CommuniryService cService;

    @Autowired
    CommunityRepository cRepository;

    @Autowired
    CommentService coService;

    @Autowired
    BimgRepository bRepository;

    @Autowired
    UserDetailsServiceImpl uImpl;

    @Autowired
    MemberRepository mRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Value("${board.page.count}")
    int PAGECNT;

    // 게시글 등록
    // 127.0.0.1:9090/ROOT/api/community/insert
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> boardInsertPOST(@RequestBody CommunityCHG community,
            @RequestHeader(name = "token") String token) {
        System.out.println(community.toString());
        System.out.println(token);
        Map<String, Object> map = new HashMap<>();

        // 토큰에서 이메일 추출
        String userSubject = jwtUtil.extractUsername(token);
        System.out.println("토큰에 담긴 전보 : " + userSubject);

        // 추출된 결과값을 JSONObject 형태로 파싱
        JSONObject jsonObject = new JSONObject(userSubject);
        String memail = jsonObject.getString("username");

        System.out.println(memail);

        // 회원엔티티 객체 생성 및 이메일 추가
        MemberCHG member = new MemberCHG();
        member.setMemail(memail);
        // 게시판 엔티티에 추가
        community.setMemberchg(member);

        try {
            long ret = cService.boardInsertOne(community);
            if (ret > 0) {
                map.put("status", 200);
                map.put("result", ret);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 게시글 조회 및 페이지네이션
    // 127.0.0.1:9090/ROOT/api/community/selectlist
    @RequestMapping(value = "/selectlist", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> boardSelectListGET(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "btitle", defaultValue = "") String btitle) {
        Map<String, Object> map = new HashMap<>();
        try {

            Pageable pageable = PageRequest.of(page - 1, PAGECNT);
            List<CommunityCHGProjection> list = cService.selectBoardList(pageable, btitle);

            long total = cRepository.countByBtitleContaining(btitle);

            if (list != null) {
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

    // 게시글 1개 조회
    // 127.0.0.1:9090/ROOT/api/community/selectone?bno=1
    @RequestMapping(value = "/selectone", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> boardSelectListGET(@RequestParam(name = "bno") long bno) {
        Map<String, Object> map = new HashMap<>();
        try {

            // 게시판 번호 불러옴
            CommunityCHGProjection ret = cRepository.findByBno(bno);// .orElse(null);
            // CommunityCHG ret = cService.boardSelectOne(bno);

            // 저장되있는 이미지 목록 불러옴
            List<BimgCHGProjection> list = bRepository.findByCommunitychg_bnoOrderByBimgnoDesc(bno);
            MemberCHGProjection member = mRepository.findByMemail(ret.getMemail());
            map.put("useremail", member);
            CommunityCHGProjection prev = cRepository.findTopByBnoLessThanOrderByBnoDesc(bno);
            map.put("prev1", prev);
            CommunityCHGProjection next = cRepository.findTopByBnoGreaterThanOrderByBnoDesc(bno);
            map.put("next2", next);

            // 배열 형태로 반복문 돌림
            String[] imgs = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                imgs[i] = "/ROOT/api/bimg/selectimg?bimgno=" + list.get(i).getBimgno();
            }

            map.put("result", ret);
            map.put("imgurl", imgs);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 게시글 삭제
    // 127.0.0.1:9090/ROOT/api/community/delete?bno=1
    @RequestMapping(value = "/delete", method = { RequestMethod.DELETE }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> boardDELETE(@RequestParam(name = "bno") long bno,
            @RequestHeader(name = "token") String token) {
        System.out.println(bno);
        System.out.println(token);
        Map<String, Object> map = new HashMap<>();
        try {

            String userSubject = jwtUtil.extractUsername(token);
            System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String username = jsonObject.getString("username");

            CommunityCHG community = cRepository.findByMemberchg_memailAndBnoEqualsOrderByBnoDesc(username, bno);

            int ret = cService.boardDeleteOne(community.getBno());
            if (ret == 1) {
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 게시글 수정
    // 127.0.0.1:9090/ROOT/api/community/update?bno=1
    @RequestMapping(value = "/update", method = { RequestMethod.PUT }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> updatePUT(@RequestBody CommunityCHG community,
            @RequestHeader(name = "token") String token, @RequestParam(name = "bno") long bno) {
        Map<String, Object> map = new HashMap<>();
        try {

            String userSubject = jwtUtil.extractUsername(token);
            System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String username = jsonObject.getString("username");

            System.out.println(username);

            CommunityCHG community1 = cService.boardSelectPrivate(username, bno);
            community1.setBtitle(community.getBtitle());
            community1.setBcontent(community.getBcontent());

            cService.boardUpdateOne(community1);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 게시글 조회수 증가
    // 127.0.0.1:9090/ROOT/api/community/updatehit
    @RequestMapping(value = "/updatehit", method = { RequestMethod.PUT }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> updateHitPUT(@RequestParam(name = "bno") long bno) {
        System.out.println(bno);
        Map<String, Object> map = new HashMap<>();
        try {
            int ret = cService.boardUpdateHit(bno);
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

    // 게시글 조회수증가(이거 사용)
    // 127.0.0.1:9090/ROOT/api/community/updatehit1
    @RequestMapping(value = "/updatehit1", method = { RequestMethod.PUT }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> updateHit1PUT(@RequestParam(name = "bno") long bno) {
        System.out.println(bno);
        Map<String, Object> map = new HashMap<>();
        try {
            CommunityCHG community = cService.boardUpdateHit1(bno);
            map.put("srult", community);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

}
