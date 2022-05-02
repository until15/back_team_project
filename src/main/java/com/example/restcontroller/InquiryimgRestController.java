package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.example.entity.InquiryimgCHG;
import com.example.jwt.JwtUtil;
import com.example.service.InquiryimgService;

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
@RequestMapping(value = "api/Inquiryimg")
public class InquiryimgRestController {

    @Autowired
    InquiryimgService inService;

    @Autowired
    ResourceLoader rLoader;

    @Autowired
    JwtUtil jwtUtil;

    @Value("${default.image}")
    String DEFAULT_IMAGE;

    // 게시판 이미지 등록
    // 127.0.0.1:9090/ROOT/api/Inquiryimg/insert
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertPSOT(@ModelAttribute InquiryimgCHG inquiryimg,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);

            if (file != null) {
                if (!file.isEmpty()) {
                    inquiryimg.setQimage(file.getBytes());
                    inquiryimg.setQimgname(file.getOriginalFilename());
                    inquiryimg.setQimgsize(file.getSize());
                    inquiryimg.setQimgtype(file.getContentType());
                }
            }

            int ret = inService.insertQimg(inquiryimg);
            if (ret == 1) {
                map.put("result", inquiryimg);
                map.put(("status"), 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put(("status"), 0);
        }
        return map;
    }

    // 게시판 이미지 1개 조회
    // 127.0.0.1:9090/ROOT/api/Inquiryimg/selectimg?qimgno=4
    @RequestMapping(value = "/selectimg", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<byte[]> selectvideoGET(
            @RequestParam(name = "qimgno") long qimgno) throws IOException {
        try {
            InquiryimgCHG inquiryimg = inService.selectOneqimg(qimgno);
            System.out.println(inquiryimg.getQimgtype());
            System.out.println(inquiryimg.getQimage().length);

            if (inquiryimg.getQimgsize() > 0) {
                HttpHeaders header = new HttpHeaders();
                if (inquiryimg.getQimgtype().equals("image/jpeg")) {
                    header.setContentType(MediaType.IMAGE_JPEG);
                } else if (inquiryimg.getQimgtype().equals("image/png")) {
                    header.setContentType(MediaType.IMAGE_PNG);
                } else if (inquiryimg.getQimgtype().equals("image/gif")) {
                    header.setContentType(MediaType.IMAGE_GIF);
                }
                ResponseEntity<byte[]> response = new ResponseEntity<>(inquiryimg.getQimage(), header, HttpStatus.OK);
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
    // 127.0.0.1:9090/ROOT/api/Inquiryimg/update
    @RequestMapping(value = "/update", method = { RequestMethod.PUT }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> imgUpdatePUT(@ModelAttribute InquiryimgCHG inquiryimg,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);

            InquiryimgCHG inquiryimg1 = inService.selectOneqimg(inquiryimg.getQimgno());
            inquiryimg1.setQimage(file.getBytes());
            inquiryimg1.setQimgname(file.getOriginalFilename());
            inquiryimg1.setQimgsize(file.getSize());
            inquiryimg1.setQimgtype(file.getContentType());
            int ret = inService.qimgUpdateOne(inquiryimg1);
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
    // 127.0.0.1:9090/ROOT/api/Inquiryimg/delete?qimgno=7
    @RequestMapping(value = "/delete", method = { RequestMethod.DELETE }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> imgDELETE(@RequestParam(name = "qimgno") long qimgno,
            @RequestHeader(name = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {

            String username = jwtUtil.extractUsername(token);
            System.out.println(username);

            int ret = inService.deleteqimgOne(qimgno);
            if (ret == 1) {
                map.put("status", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

}
