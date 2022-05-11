package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.entity.MemberCHGProjection;
import com.example.entity.PoseCHG;
import com.example.entity.VideoCHG;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberRepository;
import com.example.repository.PoseRepository;
import com.example.repository.VideoRepository;
import com.example.service.PoseService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pose")
public class poseRestController {

    @Autowired ResourceLoader resLoader;
    @Autowired PoseService pService;
    @Autowired PoseRepository pRepository;
    @Autowired VideoRepository vRepository;
    @Autowired MemberRepository mRepository;
    
    @Autowired JwtUtil jwtUtil;

    @Value("${default.image}") String DEFAULT_IMAGE;
    @Value("${board.page.count}") int PAGECNT;

    // 자세 등록
    // 127.0.0.1:9090/ROOT/api/pose/insert.json
    // {"pname":"aaa", "ppart" : "bbb", "pcontent" : "ccc", "plevel" : 1, "pstep" : 1, "memberchg":{"memail":"a@a.com"}}
    @RequestMapping(value="/insert.json", method = {RequestMethod.POST},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> poseInsertPOST(
        @RequestHeader(name="token") String token,
        @RequestBody PoseCHG pose
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰에서 아이디 추출
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(username);
            String email = jsonObject.getString("username"); 
            System.out.println(email);

            int ret = pService.poseInsert(pose);
            if(ret == 1){
                map.put("status", 200);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 자세 수정
    // 127.0.0.1:9090/ROOT/api/pose/update.json
    // {"pname":"aaa2", "ppart" : "bbb2", "pcontent" : "ccc2", "plevel" : 1, "memberchg":{"memail":"a@a.com"}, "pno" : 1}
    @RequestMapping(value="/update.json", method = {RequestMethod.PUT},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> poseUpdatePUT(
        @RequestHeader(name="token") String token,
        @RequestBody PoseCHG pose
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(username);
            String email = jsonObject.getString("username"); 
            System.out.println(email);

            PoseCHG pose1 = pService.poseSelectPrivate(email, pose.getPno());
            pose1.setPname(pose.getPname());
            pose1.setPpart(pose.getPpart());
            pose1.setPcontent(pose.getPcontent());
            pose1.setPlevel(pose.getPlevel());
            
            int ret = pService.poseUpdate(pose1);
            if(ret == 1){
                map.put("status", 200);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 자세 조회
    // 127.0.0.1:9090/ROOT/api/pose/selectone.json?pno=
    @RequestMapping(value="/selectone.json", method = {RequestMethod.GET},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> selectoneGET(
        @RequestHeader(name="token", required = false) String token,
        @RequestParam(name="pno") long pno
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            // 로그인
            if(token != null){
                String username = jwtUtil.extractUsername(token);
                // 추출된 결과값을 JSONObject 형태로 파싱
                JSONObject jsonObject = new JSONObject(username);
                String email = jsonObject.getString("username");
                System.out.println(email);
    
                MemberCHGProjection member = mRepository.findByMemail(email);
                map.put("userEmail", member);
                PoseCHG pose = pService.poseSelectOne(pno);
                if(pose != null){
                    map.put("status", 200);
                    map.put("result", pose);
                    map.put("videoUrl", null);
                }
                VideoCHG videochg = vRepository.findByPosechg_pnoEquals(pno);
                String video = new String();
                video = "/ROOT/api/pose/video?no=" + videochg.getVno();
                if(video != null){
                    map.put("videoUrl", video);
                }

            }
            // 비회원
            else{
                PoseCHG pose = pService.poseSelectOne(pno);
                if(pose != null){
                    map.put("status", 200);
                    map.put("result", pose);
                    map.put("videoUrl", null);
                }
                VideoCHG videochg = vRepository.findByPosechg_pnoEquals(pno);
                String video = new String();
                video = "/ROOT/api/pose/video?no=" + videochg.getVno();
                if(video != null){
                    map.put("videoUrl", video);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 자세 목록 (검색어 + 페이지네이션)
    // 127.0.0.1:9090/ROOT/api/pose/selectlist.json?page=1&title=
    @RequestMapping(value="/selectlist.json", method = {RequestMethod.POST},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> selectlistGET(
        @RequestBody PoseCHG pose,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name="title", defaultValue = "") String title
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page-1, PAGECNT);
            List<PoseCHG> list = pService.poseSelectList(pose.getPstep(), pageable, title);
            if(!list.isEmpty()){
                map.put("status", 200);
                map.put("result", list);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 자세 삭제 (삭제가 아닌 수정 pstep)
    // 127.0.0.1:9090/ROOT/api/pose/delete.json
    // {"pno" : 1, "pstep" : 2}
    @RequestMapping(value="/delete.json", method = {RequestMethod.PUT},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> poseDeletePOST(
        @RequestHeader(name="token") String token,
        @RequestBody PoseCHG pose
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(username);
            String email = jsonObject.getString("username");
            PoseCHG pose1 = pService.poseSelectPrivate(email, pose.getPno());
            pose1.setPstep(pose.getPstep());
            
            int ret = pService.poseDelete(pose1);
            if(ret == 1){
                map.put("status", 200);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 자세 동영상 등록
    // 127.0.0.1:9090/ROOT/api/pose/insertvideo.json
    @RequestMapping(value="/insertvideo.json", method = {RequestMethod.POST},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> poseVideoInsertPOST(
        @RequestHeader(name="token") String token,
        @RequestParam(name="pvideo") MultipartFile file,
        @RequestParam(name="pno") long pno,
        @ModelAttribute VideoCHG video,
        @ModelAttribute PoseCHG pose
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            if(!file.isEmpty()){
                String username = jwtUtil.extractUsername(token);
                System.out.println(username);
                // 추출된 결과값을 JSONObject 형태로 파싱
                JSONObject jsonObject = new JSONObject(username);
                String email = jsonObject.getString("username"); 
                System.out.println(email);

                PoseCHG poseCHG = new PoseCHG();
                poseCHG.setPno(pno);
                video.setPosechg(poseCHG);
                video.setVtype(file.getContentType());
                video.setVname(file.getOriginalFilename());
                video.setVsize(file.getSize());
                video.setVvideo(file.getBytes());
            }
            long ret = pService.poseVideoInsert(video);
            if(ret == 1){
                map.put("status", 200);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 자세 동영상 조회
    // 127.0.0.1:9090/ROOT/api/pose/video?no=
    @RequestMapping(value="/video", method = {RequestMethod.GET},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<byte[]> selectvideoGET(
        @RequestParam(name="no") long vno
    )throws IOException{  
        System.out.println("자세 동영상 조회 번호 : " + vno);
        try {
            VideoCHG videoCHG = pService.poseVideoSelectOne(vno);
            System.out.println(videoCHG.getVtype());
            System.out.println(videoCHG.getVvideo().length);

            if(videoCHG.getVsize() > 0){
                HttpHeaders header = new HttpHeaders();
                if(videoCHG.getVtype().equals("video/mp4")){
                    header.setContentType(MediaType.parseMediaType("video/mp4"));
                }
                else if(videoCHG.getVtype().equals("video/ogg")){
                    header.setContentType(MediaType.parseMediaType("video/ogg"));
                }
                else if(videoCHG.getVtype().equals("video/webm")){
                    header.setContentType(MediaType.parseMediaType("video/webm"));
                }
                else if(videoCHG.getVtype().equals("image/jpeg")){
                    header.setContentType(MediaType.IMAGE_JPEG);
                }
                else if(videoCHG.getVtype().equals("image/png")){
                    header.setContentType(MediaType.IMAGE_PNG);
                }
                else if(videoCHG.getVtype().equals("image/gif")){
                    header.setContentType(MediaType.IMAGE_GIF);
                }
                ResponseEntity<byte[]> response = new ResponseEntity<>(videoCHG.getVvideo(), header, HttpStatus.OK);
                return response;
            }
            else{
                InputStream is = resLoader.getResource(DEFAULT_IMAGE).getInputStream();
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

    // 자세 동영상 수정
    // 127.0.0.1:9090/ROOT/api/pose/updatevideo.json
    @RequestMapping(value="/updatevideo.json", method = {RequestMethod.PUT},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> poseVideoUpdatePOST(
        @RequestHeader(name="token") String token,
        @RequestParam(name="pvideo") MultipartFile file,
        @RequestParam(name="vno") long vno,
        @RequestParam(name="pno") long pno
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            if(!file.isEmpty()){
                String username = jwtUtil.extractUsername(token);
                System.out.println(username);
                // 추출된 결과값을 JSONObject 형태로 파싱
                JSONObject jsonObject = new JSONObject(username);
                String email = jsonObject.getString("username"); 
                System.out.println(email);
                // pose 에서 pno 추출 
                PoseCHG pose = pRepository.getById(pno);
                // pno의 memail과 토큰에서 전달되는 meamil이 같은지 비교
                if(email.equals(pose.getMemberchg().getMemail())){
                    VideoCHG videoCHG = pService.poseVideoSelectOne(vno);
                    videoCHG.setVtype(file.getContentType());
                    videoCHG.setVname(file.getOriginalFilename());
                    videoCHG.setVsize(file.getSize());
                    videoCHG.setVvideo(file.getBytes());
    
                    long ret = pService.poseVideoUpdate(videoCHG);
                    if(ret == 1){
                        map.put("status", 200);
                    }

                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // 자세 동영상 삭제
    // 127.0.0.1:9090/ROOT/api/pose/deletevideo.json?no=8&pno=5
    @RequestMapping(value="/deletevideo.json", method = {RequestMethod.DELETE},
    consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> poseVideoDeleteDELETE(
        @RequestHeader(name="token") String token,
        @RequestParam(name="no") long vno,
        @RequestParam(name="pno") long pno
    ){  
        Map<String, Object> map = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            // 추출된 결과값을 JSONObject 형태로 파싱
            JSONObject jsonObject = new JSONObject(username);
            String email = jsonObject.getString("username"); 
            System.out.println(email);
            // pose 에서 pno 추출 
            PoseCHG pose = pRepository.getById(pno);
            // pno의 memail과 토큰에서 전달되는 meamil이 같은지 비교
            if(email.equals(pose.getMemberchg().getMemail())){
                int ret = pService.poseVideoDelete(vno);
                if(ret == 1){
                    map.put("status", 200);
                }
                System.out.println("리턴값리턴값리턴값" + ret);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

}
