package com.briup.run.web.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.bean.Memberspace;
import com.briup.run.common.exception.DataAccessException;
import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.service.IMemberService;

@Controller
@SessionAttributes(value = { "authCode", "member", "memberinfoByNum" })
public class MemberController {
	@Autowired
	private IMemberService memberService;

	@RequestMapping("/index")
	public String toLogin(HttpServletRequest request, HttpSession session, Model model) {
		try {
			Cookie[] cookies = request.getCookies();
			Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
			if (cookies != null && memberinfo != null) {
				for (Cookie c : cookies) {
					if (c.getName().equals("username") && c.getValue().equals(memberinfo.getNickname())) {
						return "member/activity";
					}
				}
			}
			List<Memberinfo> memberinfoByNum = memberService.findMemberinfoByNum(10);
			model.addAttribute("memberinfoByNum", memberinfoByNum);
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "login";
	}

	@RequestMapping("/register")
	public String toRegister() {
		return "register";
	}

	@RequestMapping("/doRegister")
	public String register(Memberinfo memberinfo, String authCode, @ModelAttribute("authCode") String authcode) {

		try {
			if (authCode.equals(authcode)) {
				memberService.registerMemberinfo(memberinfo);
				return "forward:/index";
			} else {
				throw new MemberServiceException("验证码错误!");
			}
		} catch (MemberServiceException e) {
			e.printStackTrace();
			return "register";
		}
	}

	@RequestMapping("/doLogin")
	public String login(String username, String password, String authCode, @ModelAttribute("authCode") String authcode,
			Model model, String autoLogin, HttpServletResponse response) {
		try {
			if (authcode.equals(authCode)) {
				if (autoLogin != null && Integer.parseInt(autoLogin) == 0) {
					Cookie c1 = new Cookie("username", username);
					Cookie c2 = new Cookie("password", password);

					c1.setMaxAge(60);
					c2.setMaxAge(60);

					response.addCookie(c1);
					response.addCookie(c2);
				}
				Memberinfo memberinfo = memberService.login(username, password);
				model.addAttribute("member", memberinfo);
				return "member/activity";
			} else {
				throw new MemberServiceException("验证码错误!");
			}
		} catch (MemberServiceException e) {
			e.printStackTrace();
			return "login";
		}
	}

	@RequestMapping("/logout")
	public String doLogout(HttpSession session, Model model) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		try {
			if (memberinfo != null) {
				memberService.logout(memberinfo.getNickname());
				memberinfo = new Memberinfo();
				model.addAttribute("member", memberinfo);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "forward:/index";
	}

	@RequestMapping("/findPassword")
	public String toFindPassword() {
		return "passwd_missing";
	}

	@RequestMapping("/doFindPassword")
	public String findPassword(String userName, String passwdQuestion, String passwdAnswer, Model model) {
		try {
			String newPasswd = memberService.getBackPasswd(userName, passwdQuestion, passwdAnswer);
			model.addAttribute("newPasswd", newPasswd);
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "newPasswd";
	}

	@RequestMapping("/space")
	public String toNoSpace(HttpSession session, Model model) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		try {
			if (memberinfo != null) {
				if (memberService.isMemberspace(memberinfo.getId())) {
					Memberspace memspace = memberService.findSpace(memberinfo.getId());
					model.addAttribute("memberspace", memspace);
					model.addAttribute("memberinfo", memberinfo);
					return "member/space";
				}
			}
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "member/noSpace";
	}

	@RequestMapping("/createSpace")
	public String toCreateSpace() {
		return "member/createSpace";
	}

	@RequestMapping("/doCreateSpaace")
	public String doCreateSpaace(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpSession session, Model model, Memberspace memberspace) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		try {
			if (memberinfo != null) {
				uploadFile(file, request, memberinfo, memberspace);
				memberspace.setMemberinfo(memberinfo);
				memberService.saveSpace(memberspace);
				model.addAttribute("memberspace", memberspace);
				model.addAttribute("memberinfo", memberinfo);
				return "member/space";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "login";
	}

	@RequestMapping("/doUpdateSpace")
	public String updateSpace(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session,
			Model model, Memberspace memberspace) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		try {
			Memberspace memspace = memberService.findSpace(memberinfo.getId());
			if (memspace != null) {
				uploadFile(file, request, memberinfo, memberspace);
				memberspace.setMemberinfo(memberinfo);
				memberspace.setId(memspace.getId());
				memberService.updateSpace(memberspace);
				model.addAttribute("memberspace", memberspace);
				model.addAttribute("memberinfo", memberinfo);
				return "member/space";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/toUpdateSpace")
	public String toUpdateSpace() {
		return "member/updateSpace";
	}

	@RequestMapping("/activity")
	public String toActivity() {
		return "member/activity";
	}

	@RequestMapping("/modify")
	public String toModify() {
		return "member/modify";
	}

	@RequestMapping("/doModify")
	public String doModify(Memberinfo memberinfo, HttpSession session, String oldPasswd, Model model) {
		Memberinfo member = (Memberinfo) session.getAttribute("member");
		memberinfo.setNickname(member.getNickname());
		memberinfo.setId(member.getId());
		try {
			Memberinfo memberinfo2 = memberService.saveOrUpDate(memberinfo, oldPasswd);
			model.addAttribute("memberspace", memberinfo2);
		} catch (MemberServiceException e) {
			e.printStackTrace();
			return "member/modify";
		}
		return "login";
	}

	@RequestMapping("/spaceAction")
	public String spaceAction(String name, Model model) {
		try {
			Memberinfo memberinfo = memberService.findMemberinfoByName(name);
			if (memberService.isMemberspace(memberinfo.getId())) {
				Memberspace memspace = memberService.findSpace(memberinfo.getId());
				model.addAttribute("memberspace", memspace);
				model.addAttribute("memberinfo", memberinfo);
				return "member/space";
			}
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "member/reminder";
	}

	@RequestMapping("/musicrun")
	public String toMusicrun() {
		return "other/musicrun";
	}

	@RequestMapping("/equip")
	public String toEquip() {
		return "other/equip";
	}

	@RequestMapping("/guide")
	public String toGuide() {
		return "other/guide";
	}

	@RequestMapping("/bbs")
	public String toBbs() {
		return "other/bbs";
	}

	private void uploadFile(MultipartFile file, HttpServletRequest request, Memberinfo memberinfo,
			Memberspace memberspace) {
		if (file != null) {
			if (!file.isEmpty()) {
				// 保存的文件路径
				// 需要的话可以给文件名上加时间戳
				String filePath = request.getServletContext().getRealPath("/") + memberinfo.getNickname() + "/"
						+ file.getOriginalFilename();
				File newFile = new File(filePath);
				// 文件所在目录不存在就创建
				if (!newFile.getParentFile().exists()) {
					newFile.getParentFile().mkdirs();
				}

				// 转存文件
				try {
					file.transferTo(newFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				memberspace.setIcon(memberinfo.getNickname() + "/" + file.getOriginalFilename());
			}
		}
	}

}
