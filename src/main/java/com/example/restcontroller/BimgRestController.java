package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.BimgCHG;
import com.example.entity.CommunityCHG;
import com.example.entity.MemberCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.BimgRepository;
import com.example.repository.CommunityRepository;
import com.example.service.BimgService;
import com.example.service.CommuniryService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/bimg")
public class BimgRestController {

    @Autowired
    BimgRepository bRepository;

    @Autowired
    CommunityRepository cRepository;

    @Autowired
    BimgService bService;

    @Autowired
    ResourceLoader rLoader;

    @Autowired
    CommuniryService cService;

    @Autowired
    JwtUtil jwtUtil;

    @Value("${default.image}")
    String DEFAULT_IMAGE;

    // 게시판 이미지 등록
    // 127.0.0.1:9090/until15/api/bimg/insert
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertPSOT(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestHeader(name = "token") String token, @RequestParam(name = "bno") long bno) {
        Map<String, Object> map = new HashMap<>();
        try {
            BimgCHG bimg = new BimgCHG();
            CommunityCHG community = new CommunityCHG();
            community.setBno(bno);
            bimg.setCommunitychg(community);

            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String username = jsonObject.getString("username");
            // System.out.println(username);
            MemberCHG member = new MemberCHG();
            member.setMemail(username);
            // 게시판 엔티티에 추가
            bimg.setMemberchg(member);

            map.put(("status"), 200);
            map.put("result", 0);

            if (file != null) {
                if (!file.isEmpty()) {
                    bimg.setBimage(file.getBytes());
                    bimg.setBimgname(file.getOriginalFilename());
                    bimg.setBimgsize(file.getSize());
                    bimg.setBimgtype(file.getContentType());

                    int ret = bService.insertBimg(bimg);
                    if (ret == 1) {
                        map.put("result", bimg);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put(("status"), 0);
        }
        return map;
    }

    // 게시판 이미지 일괄 등록
    // 127.0.0.1:9090/until15/api/bimg/insertbatch
    @RequestMapping(value = "/insertbatch", method = { RequestMethod.POST }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertbatchPSOT(
            @RequestParam(name = "file", required = false) MultipartFile[] file,
            @RequestHeader(name = "token") String token) {

        Map<String, Object> map = new HashMap<>();
        try {

            String userSubject = jwtUtil.extractUsername(token);
            JSONObject jsonObject = new JSONObject(userSubject);
            String username = jsonObject.getString("username");
            // System.out.println(username);

            List<BimgCHG> list = new ArrayList<>();
            for (int i = 0; i < file.length; i++) {

                BimgCHG bimg = new BimgCHG();

                if (file != null) {
                    if (!file[i].isEmpty()) {
                        bimg.setBimage(file[i].getBytes());
                        bimg.setBimgname(file[i].getOriginalFilename());
                        bimg.setBimgsize(file[i].getSize());
                        bimg.setBimgtype(file[i].getContentType());
                    }
                    MemberCHG member = new MemberCHG();
                    member.setMemail(username);
                    bimg.setMemberchg(member);
                }
                list.add(bimg);
            }

            int ret = bService.insertBatchBimg(list);
            if (ret == 1) {
                map.put(("status"), 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put(("status"), 0);
        }
        return map;
    }

    // 게시판 이미지 1개 조회
    // 127.0.0.1:9090/until15/api/bimg/selectimg?bimgno=4
    @RequestMapping(value = "/selectimg", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<byte[]> selectImageGET(
            @RequestParam(name = "bimgno") long bimgno) throws IOException {
        try {
            BimgCHG bimgCHG = bService.selectOneimage(bimgno);

            if (bimgCHG.getBimgsize() > 0) {
                HttpHeaders header = new HttpHeaders();
                if (bimgCHG.getBimgtype().equals("image/jpeg")) {
                    header.setContentType(MediaType.IMAGE_JPEG);
                } else if (bimgCHG.getBimgtype().equals("image/png")) {
                    header.setContentType(MediaType.IMAGE_PNG);
                } else if (bimgCHG.getBimgtype().equals("image/gif")) {
                    header.setContentType(MediaType.IMAGE_GIF);
                }
                ResponseEntity<byte[]> response = new ResponseEntity<>(bimgCHG.getBimage(), header, HttpStatus.OK);
                return response;
            } else {
                InputStream is = rLoader.getResource(DEFAULT_IMAGE).getInputStream();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                ResponseEntity<byte[]> response = new ResponseEntity<>(is.readAllBytes(), headers, HttpStatus.OK);
                return response;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 게시판 이미지 수정
    // 127.0.0.1:9090/until15/api/bimg/update
    @RequestMapping(value = "/update", method = { RequestMethod.PUT }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> imgUpdatePUT(@ModelAttribute BimgCHG bimg,
            @RequestParam(name = "mimage", required = false) MultipartFile file,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userSubject = jwtUtil.extractUsername(token);
            System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String username = jsonObject.getString("username");
            // System.out.println(username);

            BimgCHG bimg1 = bService.selectOneimage(bimg.getBimgno());
            bimg1.setBimage(file.getBytes());
            bimg1.setBimgname(file.getOriginalFilename());
            bimg1.setBimgsize(file.getSize());
            bimg1.setBimgtype(file.getContentType());
            int ret = bService.bimgUpdateOne(bimg1);
            if (ret == 1) {
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 게시판 이미지 삭제
    // 127.0.0.1:9090/until15/api/bimg/delete
    @RequestMapping(value = "/delete", method = { RequestMethod.DELETE }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> imgDELETE(@RequestParam(name = "bimgno") long bimgno,
            @RequestHeader(name = "token") String token) {

        Map<String, Object> map = new HashMap<>();
        try {

            String userSubject = jwtUtil.extractUsername(token);
            // System.out.println("토큰에 담긴 전보 : " + userSubject);

            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(userSubject);
            String username = jsonObject.getString("username");
            // System.out.println(username);
            BimgCHG bimg = bRepository.findByMemberchg_memailAndBimgnoOrderByBimgnoDesc(username, bimgno);

            // 게시판 번호 불러옴

            int ret = bService.deleteBimgOne(bimg.getBimgno());
            if (ret == 1) {
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 게시판 이미지 1개 조회
    // 127.0.0.1:9090/until15/api/bimg/selectimage1?bimgno=4
    @RequestMapping(value = "/selectimage1", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> selectImageOneGET(@RequestParam(name = "bimgno") long bimgno) {
        Map<String, Object> map = new HashMap<>();
        try {
            BimgCHG bimg = bService.selectOneimage(bimgno);
            if (bimg != null) {

                map.put("result", bimg);
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;

    }

    // 127.0.0.1:9090/until15/api/bimg/insert1
    @RequestMapping(value = "/insert1", method = { RequestMethod.POST }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insert1PSOT(
            @RequestParam(name = "file", required = false) MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        try {
            BimgCHG bimg = new BimgCHG();
            MemberCHG member = new MemberCHG();
            // 게시판 엔티티에 추가
            bimg.setMemberchg(member);

            map.put(("status"), 200);
            map.put("result", 0);

            if (file != null) {
                if (!file.isEmpty()) {
                    bimg.setBimage(file.getBytes());
                    bimg.setBimgname(file.getOriginalFilename());
                    bimg.setBimgsize(file.getSize());
                    bimg.setBimgtype(file.getContentType());

                    int ret = bService.insertBimg(bimg);
                    if (ret == 1) {
                        map.put("result", bimg);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put(("status"), 0);
        }
        return map;
    }

}
