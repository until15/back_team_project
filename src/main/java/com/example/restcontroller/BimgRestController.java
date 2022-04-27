package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import com.example.entity.BimgCHG;
import com.example.repository.BimgRepository;
import com.example.service.BimgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    // 127.0.0.1:9090/ROOT/api/bimg/insert
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertPSOT(@ModelAttribute BimgCHG bimg,
            @RequestParam(name = "file", required = false) MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        try {

            if (!file.isEmpty()) {
                bimg.setBimage(bimg.getBimage());
                bimg.setBimgname(bimg.getBimgname());
                bimg.setBimgsize(bimg.getBimgsize());
                bimg.setBimgtype(bimg.getBimgtype());
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
}
