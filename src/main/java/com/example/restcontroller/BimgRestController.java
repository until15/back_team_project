package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.example.entity.BimgCHG;
import com.example.repository.BimgRepository;
import com.example.service.BimgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    BimgService bService;

    @Autowired
    ResourceLoader rLoader;

    @Value("${default.image}")
    String DEFAULT_IMAGE;

    // 127.0.0.1:9090/ROOT/api/bimg/insert
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertPSOT(@ModelAttribute BimgCHG bimg,
            @RequestParam(name = "file", required = false) MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        try {

            if (file != null) {
                if (!file.isEmpty()) {
                    bimg.setBimage(file.getBytes());
                    bimg.setBimgname(file.getOriginalFilename());
                    bimg.setBimgsize(file.getSize());
                    bimg.setBimgtype(file.getContentType());
                }
            }

            int ret = bService.insertBimg(bimg);
            if (ret == 1) {
                map.put("result", bimg);
                map.put(("status"), 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put(("status"), 0);
        }
        return map;
    }

    // 127.0.0.1:9090/ROOT/api/bimg/selectimg?bimgno=4
    @RequestMapping(value = "/selectimg", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<byte[]> selectvideoGET(
            @RequestParam(name = "bimgno") long bimgno) throws IOException {
        try {
            BimgCHG bimgCHG = bService.selectOneimage(bimgno);
            System.out.println(bimgCHG.getBimgtype());
            System.out.println(bimgCHG.getBimage().length);

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
}
