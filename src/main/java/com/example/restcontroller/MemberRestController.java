package com.example.restcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.entity.MemberCHG;
import com.example.entity.MemberCHGProjection;
import com.example.jwt.JwtUtil;
import com.example.repository.MemberRepository;
import com.example.service.MemberService;
import com.example.service.UserDetailsServiceImpl;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/member")
public class MemberRestController {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	MemberService mService;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	MemberRepository mRepository;

	@Autowired
	ResourceLoader rLoader;

	@Value("${board.page.count}")
	int PAGECNT;

	@Value("${default.image}")
	String DEFAULT_IMAGE;

	// ????????????
	// 127.0.0.1:9090/until15/api/member/join
	@RequestMapping(value = "/join", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> joinPOST(@ModelAttribute MemberCHG member, @AuthenticationPrincipal User user,
			@RequestParam(name = "mimage", required = false) MultipartFile file) {
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		Map<String, Object> map = new HashMap<>();
		try {
			member.setMpw(bcpe.encode(member.getMpw())); // ???????????? ??????
			member.setMrole(member.getMrole()); // ?????? ??????

			int ret = mService.memberInsertOne(member);
			if (ret == 1) {
				map.put("status", 200);
			}

			// ????????? ?????????
			if (file != null) {
				if (!file.isEmpty()) {
					member.setMprofile(file.getBytes()); // ????????? ?????????
					member.setMpname(file.getOriginalFilename()); // ????????? ??????
					member.setMpsize(file.getSize()); // ????????? ?????????
					member.setMptype(file.getContentType()); // ????????? ??????

					int ret1 = mService.memberInsertOne(member);
					if (ret1 == 1) {
						map.put("status", 202);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return map;
	}

	// ????????? (?????? ??????)
	// 127.0.0.1:9090/until15/api/member/login
	@RequestMapping(value = "/login", method = { RequestMethod.POST }, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> memberLoginPOST(
			@RequestBody MemberCHG member) {
		Map<String, Object> map = new HashMap<>();
		try {
			// System.out.println(member.toString()); // ???????????? ??? ??????

			// Security ??????
			UserDetails user = userDetailsService.loadUserByUsername(member.getMemail());
			// System.out.println("?????? ????????? : " + user);

			// ???????????? ?????????
			BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
			// System.out.println("???????????? ?????????: " + bcpe.toString());

			// ????????? ???????????? ?????? ??????
			MemberCHG member1 = mRepository.findById(member.getMemail()).orElse(null);
			// System.out.println("?????? ?????? : " + member1.toString());
			if (member1.getMstep() == 0) {

				// long ????????? mrank??? string ?????? ??????
				String rank = member1.getMrank().toString();

				// ?????? ??????
				// System.out.println("?????? ??????: " + jwtUtil.generatorToken(member.getMemail(),
				// member1.getMrole(), rank));

				// ????????? ????????? ????????? ????????? ???????????? ??? ??????
				if (bcpe.matches(member.getMpw(), user.getPassword())) {

					// ?????? ??????
					String token = jwtUtil.generatorToken(member.getMemail(), member1.getMrole(), rank);

					map.put("token", token);
					map.put("status", 200);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return map;
	}

	// 127.0.0.1:9090/until15/api/member/updatemember
	// ?????????????????? (??????, ??????, ????????????)
	@RequestMapping(value = "/updatemember", method = { RequestMethod.PUT }, consumes = {
			MediaType.ALL_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> updateMemberPUT(@RequestHeader(name = "token") String token,
			@RequestParam(name = "mimage", required = false) MultipartFile file, @ModelAttribute MemberCHG member)
			throws IOException {
		Map<String, Object> map = new HashMap<>();

		try {
			String userSubject = jwtUtil.extractUsername(token);
			System.out.println("????????? ?????? ?????? : " + userSubject);

			// ????????? ???????????? JSONObject ????????? ??????
			JSONObject jsonObject = new JSONObject(userSubject);
			String username = jsonObject.getString("username");

			System.out.println(username);
			MemberCHG member1 = mService.memberSelectOne(username);
			// ?????????
			member1.setMid(member.getMid());
			// // ??????
			member1.setMname(member.getMname());
			// // ?????????
			member1.setMphone(member.getMphone());
			// // ???
			member1.setMheight(member.getMheight());
			// // ?????????
			member1.setMweight(member.getMweight());

			// // ?????????
			if (file != null) {
				if (!file.isEmpty()) {
					member1.setMpname(file.getOriginalFilename());
					member1.setMprofile(file.getBytes());
					member1.setMpsize(file.getSize());
					member1.setMptype(file.getContentType());
				}
			}
			int ret = mService.memberUpdate(member1);
			if (ret == 1) {
				map.put("status", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return map;
	}

	// ???????????? (??????, ????????????, ????????????)
	// 127.0.0.1:9090/until15/api/member/updatepw
	@RequestMapping(value = "/updatepw", method = { RequestMethod.PUT }, consumes = {
			MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> customerupdatepwPUT(@RequestHeader(name = "token") String token,
			@RequestBody MemberCHG member) {
		System.out.println(member.toString());
		Map<String, Object> map = new HashMap<>();
		try {
			String userSubject = jwtUtil.extractUsername(token);
			System.out.println("????????? ?????? ?????? : " + userSubject);

			// ????????? ???????????? JSONObject ????????? ??????
			JSONObject jsonObject = new JSONObject(userSubject);
			String username = jsonObject.getString("username");

			System.out.println(username);
			UserDetails user = userDetailsService.loadUserByUsername(username);

			BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
			// ????????? ?????? ?????? ?????? ????????? ?????? ????????????
			if (bcpe.matches(member.getMpw(), user.getPassword())) {

				mService.memberUpdatePw(username, bcpe.encode(member.getMpw1()));
				map.put("status", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0); // ??????????????? ?????? ???
		}

		return map;
	}

	// ????????????(????????? ?????? ??????)
	// 127.0.0.1:9090/until15/api/member/deletemember
	@RequestMapping(value = "/deletemember", method = { RequestMethod.PUT }, consumes = {
			MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> deleteMemberOnePUT(
			@ModelAttribute MemberCHG member, @RequestHeader(name = "token") String token,
			@RequestParam(name = "mimage", required = false) MultipartFile file) throws IOException {
		System.out.println("=================================================" + file);
		Map<String, Object> map = new HashMap<>();
		try {

			String userSubject = jwtUtil.extractUsername(token);
			JSONObject jsonObject = new JSONObject(userSubject);
			String username = jsonObject.getString("username");

			MemberCHG member1 = mService.memberSelectOne(username);

			member1.setMstep(1);
			member1.setMid("????????? ?????????");
			member1.setMphone(null);
			member1.setMbirth(null);
			member1.setMweight(null);
			member1.setMheight(null);
			member1.setMgender(null);
			member1.setMrank(null);

			member1.setMpname(null);
			member1.setMprofile(null);
			member1.setMpsize(0L);
			member1.setMptype(null);

			UserDetails user = userDetailsService.loadUserByUsername(username);
			BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
			// ????????? ?????? ?????? ?????? ????????? ?????? ????????????
			if (bcpe.matches(member.getMpw(), user.getPassword())) {
				int ret = mService.memberLeave(member1);
				if (ret == 1) {
					map.put("status", 200);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return map;
	}

	// ?????? 1??? ??????
	// 127.0.0.1:9090/until15/api/member/selectmemberone
	@RequestMapping(value = "/selectmemberone", method = { RequestMethod.GET }, consumes = {
			MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> selectMemberOneGET(
			@RequestHeader(name = "token") String token) {
		System.out.println(token);
		Map<String, Object> map = new HashMap<>();
		try {

			String userSubject = jwtUtil.extractUsername(token);
			System.out.println("????????? ?????? ?????? : " + userSubject);

			// ????????? ???????????? JSONObject ????????? ??????
			JSONObject jsonObject = new JSONObject(userSubject);
			String username = jsonObject.getString("username");

			System.out.println(username);

			MemberCHG member1 = mService.memberSelectOne(username);
			String imgs = new String();
			imgs = "/until15/api/member/profile?memail=" + username;

			if (member1 != null) {
				map.put("result", member1);
				map.put("imgurl", imgs);
				map.put("status", 200);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return map;
	}

	// ?????? ????????? ?????? ??????
	// 127.0.0.1:9090/until15/api/member/profile
	@RequestMapping(value = "/profile", method = { RequestMethod.GET }, consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<byte[]> selectProfileGET(
			@RequestParam(name = "memail") String memail) throws IOException {
		try {

			MemberCHG member1 = mService.memberSelectOne(memail);

			if (member1.getMpsize() > 0) {
				HttpHeaders header = new HttpHeaders();
				if (member1.getMptype().equals("image/jpeg")) {
					header.setContentType(MediaType.IMAGE_JPEG);
				} else if (member1.getMptype().equals("image/png")) {
					header.setContentType(MediaType.IMAGE_PNG);
				} else if (member1.getMptype().equals("image/gif")) {
					header.setContentType(MediaType.IMAGE_GIF);
				}
				ResponseEntity<byte[]> response = new ResponseEntity<>(member1.getMprofile(), header, HttpStatus.OK);
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

	// ???????????????
	// 127.0.0.1:9090/until15/api/member/selectmemberlist
	@RequestMapping(value = "/selectmemberlist", method = { RequestMethod.GET }, consumes = {
			MediaType.ALL_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> boardSelectListGET(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "memail", defaultValue = "") String memail) {
		Map<String, Object> map = new HashMap<>();
		try {

			Pageable pageable = PageRequest.of(page - 1, PAGECNT);
			List<MemberCHG> list = mService.memberSelectList(pageable, memail);
			long total = mRepository.countByMemailContaining(memail);
			if (list != null) {
				map.put("total", total);
				map.put("result", list);
				map.put("status", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}

		return map;
	}

	// ????????????
	// 127.0.0.1:9090/until15/api/member/emailcheck
	@RequestMapping(value = "/emailcheck", method = { RequestMethod.GET }, consumes = {
			MediaType.ALL_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> emailCheckGET(@RequestParam(name = "memail") String memail) {
		Map<String, Object> map = new HashMap<>();
		try {
			MemberCHGProjection member1 = mRepository.findByMemail(memail);
			if (member1 != null) {
				map.put("result", member1);
				map.put("status", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}

		return map;
	}

	// ????????? ????????????
	// 127.0.0.1:9090/until15/api/member/checkmid
	@RequestMapping(value = "/checkmid", method = { RequestMethod.GET }, consumes = {
			MediaType.ALL_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> checkmidGET(@RequestParam(name = "mid") String mid) {
		Map<String, Object> map = new HashMap<>();
		try {

			MemberCHGProjection member = mRepository.findByMid(mid);
			if (member != null) {
				map.put("status", 200);
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}

		return map;
	}

	// ????????? ???????????? ????????????
	// 127.0.0.1:9090/until15/api/member/checkmidone
	@RequestMapping(value = "/checkmidone", method = { RequestMethod.GET }, consumes = {
			MediaType.ALL_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> checkMidOneGET(
			@RequestParam(name = "mid") String mid, @RequestHeader(name = "token") String token) {
		Map<String, Object> map = new HashMap<>();
		try {

			String userSubject = jwtUtil.extractUsername(token);
			// System.out.println("????????? ?????? ?????? : " + userSubject);

			// ????????? ???????????? JSONObject ????????? ??????
			JSONObject jsonObject = new JSONObject(userSubject);
			String username = jsonObject.getString("username");

			MemberCHGProjection member2 = mRepository.findByMemail(username);
			MemberCHGProjection member1 = mRepository.findByMid(mid);

			if (member2.getMemail() == member1.getMemail()) {
				System.out.println("=====================" + member2.getMemail());
				map.put("result1", username);
				map.put("result", member1.getMemail());
				map.put("status", 200);

			} else if (member2.getMemail() != member1.getMemail()) {
				map.put("result1", username);
				map.put("result", member1.getMemail());
				map.put("status", 1);
			} else {
				map.put("status", 2);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}

		return map;
	}

	// ????????? ??????
	// 127.0.0.1:9090/until15/api/member/findmemail?mname=&mbirth=
	@RequestMapping(value = "/findmemail", method = { RequestMethod.GET }, consumes = {
			MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> findMemailGET(@RequestParam(name = "mname") String mname,
			@RequestParam(name = "mbirth") String mbirth) {
		Map<String, Object> map = new HashMap<>();
		try {

			MemberCHGProjection member = mRepository.findByMnameAndMbirth(mname, mbirth);
			if (member != null) {
				map.put("result", member);
				map.put("status", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return map;

	}

	// ????????????
	// 127.0.0.1:9090/until15/api/member/updatepw3?memail=
	@RequestMapping(value = "/updatepw3", method = { RequestMethod.PUT }, consumes = {
			MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> updatepwPUT(@RequestParam(name = "memail") String memail) {
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		Map<String, Object> map = new HashMap<>();
		try {
			MemberCHG member1 = mRepository.findById(memail).orElse(null);
			if (member1 != null) {
				String uuid = UUID.randomUUID().toString().replace("-", "");
				uuid = uuid.substring(0, 6);

				member1.setMpw(uuid);
				String newpw = bcpe.encode(member1.getMpw());
				member1.setMpw(newpw);
				int ret = mService.memberUpdate(member1);
				if (ret == 1) {
					map.put("result", uuid);
					map.put("status", 200);

				}

			} else if (member1 == null) {
				map.put("status", 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0); // ??????????????? ?????? ???
		}

		return map;
	}

	// ???????????????
	// 127.0.0.1:9090/until15/api/member/selectmemberlist1
	@RequestMapping(value = "/selectmemberlist1", method = { RequestMethod.GET }, consumes = {
			MediaType.ALL_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> memberSelectListGET(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "memail", defaultValue = "") String memail, @RequestParam(name = "mid") String mid) {
		Map<String, Object> map = new HashMap<>();
		try {

			Pageable pageable = PageRequest.of(page - 1, PAGECNT);
			List<MemberCHGProjection> list = mRepository.findByMemailOrMidContainingOrderByMemailDesc(memail, mid,
					pageable);
			long total = mRepository.countByMidContaining(mid);
			if (list != null) {
				map.put("total", total);
				map.put("result", list);
				map.put("status", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}

		return map;
	}

}
