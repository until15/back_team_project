package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.CommunityCHG;
import com.example.repository.CommunityRepository;
import com.example.service.CommuniryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/community")
public class CommunityController {

    @Autowired
    CommuniryService cService;

    @Autowired
    CommunityRepository cRepository;

    @GetMapping(value = "/insert")
    public String insertGET() {
        return "coinsert";
    }

    @GetMapping(value = "/board")
    public String boardGET(Model model, @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "btitle", defaultValue = "") String btitle) {

        PageRequest pageRequest = PageRequest.of(page - 1, 5);

        // 검색어, 페이지네이션
        // findAll == SELECT * FROM 테이블;
        List<CommunityCHG> list = cService.selectBoardList(pageRequest, btitle);
        model.addAttribute("list", list);

        // 10 => 1, 23 => 3, 45 => 5
        long total = cRepository.countByBtitleContaining(btitle);
        model.addAttribute("pages", (total - 1) / 5 + 1);
        return "board";
    }

    @RequestMapping(value = "/insert", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public Map<String, Object> boardInsertPOST(@RequestBody CommunityCHG community) {
        Map<String, Object> map = new HashMap<>();

        map.put("status", 0);

        int ret = cService.boardInsertOne(community);

        if (ret == 1) {

            map.put("status", 200);
        }
        return map;

    }
}
