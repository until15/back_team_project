package com.example.restcontroller;

import java.util.HashMap;
import java.util.Map;

import com.example.entity.CommentCHG;
import com.example.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/comment")
public class CommentRestController {

    @Autowired
    CommentService cService;

    // 127.0.0.1:9090/ROOT/api/comment/insert
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> insertPOST(@RequestBody CommentCHG comment) {
        Map<String, Object> map = new HashMap<>();
        try {

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

}