package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.CommunityCHG;
import com.example.repository.CommunityRepository;
import com.example.service.CommuniryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
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

    // 127.0.0.1:9090/ROOT/api/community/insert
    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> boardInsertPOST(@RequestBody CommunityCHG community) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("status", 0);
            int ret = cService.boardInsertOne(community);
            if (ret == 1) {
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 127.0.0.1:9090/ROOT/api/community/selectlist
    @RequestMapping(value = "/selectlist", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = {
                    MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> boardSelectListGET(@RequestParam(name = "page", defaultValue = "") Pageable page,
            @RequestParam(name = "btitle") String btitle) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<CommunityCHG> list = cService.selectBoardList(page, btitle);
            System.out.println(list.toString());
            map.put("result", list);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }

        return map;
    }

    // 127.0.0.1:9090/ROOT/api/community/selectone?bno=1
    @RequestMapping(value = "/selectone", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> boardSelectListGET(@RequestParam(name = "bno") long bno) {
        Map<String, Object> map = new HashMap<>();
        try {
            CommunityCHG ret = cService.boardSelectOne(bno);

            map.put("result", ret);
            map.put("status", 200);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;

    }

    // 127.0.0.1:9090/ROOT/api/community/delete?bno=1
    @RequestMapping(value = "/delete", method = { RequestMethod.GET }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> deleteGET(@RequestParam(name = "bno") long bno) {
        Map<String, Object> map = new HashMap<>();
        try {

            int ret = cService.boarddeleteOne(bno);
            if (ret == 1) {
                map.put("status", 200);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;

    }

    // 127.0.0.1:9090/ROOT/api/community/update?bno=1
    @RequestMapping(value = "/update", method = { RequestMethod.PUT }, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> updatePUT(@RequestBody CommunityCHG community) {
        Map<String, Object> map = new HashMap<>();
        try {
            CommunityCHG community1 = cService.boardSelectOne(community.getBno());
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

}