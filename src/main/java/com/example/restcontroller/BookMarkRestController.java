package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import com.example.entity.BookMarkCHG;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookmark")
public class BookMarkRestController {
    

    // 북마크 추가
    // 127.0.0.1:9090/ROOT/api/bookmark/insert
    @RequestMapping(
        value    ="/insert", 
        method   = {RequestMethod.POST},
        consumes = {MediaType.ALL_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> insertBookmarkPOST(
        @RequestBody BookMarkCHG bookmark){  
        Map<String, Object> map = new HashMap<>();
        try {

        }
        catch(Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

}
