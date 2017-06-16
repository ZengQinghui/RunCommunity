package com.briup.run.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.briup.run.common.bean.Friendrecord;
import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.bean.Messagerecord;
import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.common.exception.MessengerServiceException;
import com.briup.run.service.IMemberService;
import com.briup.run.service.IMessengerService;

@Controller
@SessionAttributes(value = { "authCode", "member", "memberinfoByNum", "Memberinfoes" })
public class MessengerController {
	@Autowired
	private IMessengerService messengerService;
	@Autowired
	private IMemberService memberService;

	@RequestMapping("/buddyList")
	public String toBuddyList(HttpSession session, Model model) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		String selfName = memberinfo.getNickname();
		try {
			List<Memberinfo> friends = memberService.listFriend(selfName);
			model.addAttribute("Memberinfoes", friends);
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "messenger/buddyList";
	}

	@RequestMapping("/memberList")
	public String toMemberList() {
		return "messenger/memberList";
	}

	@RequestMapping("/matchFriend")
	public String toMatchFriend() {
		return "messenger/matchFriend";
	}

	@RequestMapping("randomMatchFriend")
	public String doRandomMatchFriend(HttpSession session, Model model) {
		try {
			List<Memberinfo> friend = messengerService.findOneMemberinfo();
			Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
			for (int i = 0; i < friend.size(); i++) {
				if (memberinfo.equals(friend.get(i))) {
					friend.remove(i);
				}
			}
			model.addAttribute("friends", friend);
		} catch (MessengerServiceException e) {
			e.printStackTrace();
		}
		return "messenger/matchFriend";
	}

	@RequestMapping("/findFriends")
	public String findFriends(String age, String gender, String provinceCity, Model model, HttpSession session) {
		try {
			Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
			List<Memberinfo> friends = messengerService.findFriends(age, gender, provinceCity);
			for (int i = 0; i < friends.size(); i++) {
				if (memberinfo.equals(friends.get(i))) {
					friends.remove(i);
				}
			}
			model.addAttribute("friends", friends);
		} catch (MessengerServiceException e) {
			e.printStackTrace();
		}
		return "messenger/matchFriend";
	}

	@RequestMapping("/addFriend")
	public String doAddFriend(String friendName, HttpSession session, Model model) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		String selfName = memberinfo.getNickname();
		try {
			Friendrecord findFriend = memberService.findFriend(selfName, friendName);
			if (findFriend == null) {
				memberService.moveToFriend(selfName, friendName);
				return "forward:/buddyList";
			}
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "forward:/buddyList";
	}

	@RequestMapping("/deleteFriend")
	public String doDeleteFriend(HttpSession session, String nickName) {
		String[] nickNames = nickName.split(",");
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		String selfName = memberinfo.getNickname();
		try {
			for (String name : nickNames) {
				memberService.deleteFriend(selfName, name);
			}
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "forward:/buddyList";
	}

	@RequestMapping("/blackList")
	public String toBlackList(HttpSession session, Model model) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		String selfName = memberinfo.getNickname();
		try {
			List<Memberinfo> listBlack = memberService.listBlack(selfName);
			model.addAttribute("blackList", listBlack);
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "messenger/blackList";
	}

	@RequestMapping("/moveToBlackList")
	public String doMoveToBlackList(HttpSession session, String blackName) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		String selfName = memberinfo.getNickname();
		try {
			memberService.deleteFriend(selfName, blackName);
			memberService.moveToBlack(selfName, blackName);
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "forward:/buddyList";
	}

	@RequestMapping("/moveToFriend")
	public String doMoveToFriend(HttpSession session, String blackName) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		String selfName = memberinfo.getNickname();
		try {
			memberService.deleteBlack(selfName, blackName);
			memberService.moveToFriend(selfName, blackName);
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "forward:/blackList";
	}

	@RequestMapping("/deleteBlack")
	public String doDeleteBlack(HttpSession session, String nickName) {
		String[] nickNames = nickName.split(",");
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		String selfName = memberinfo.getNickname();
		try {
			for (String name : nickNames) {
				memberService.deleteBlack(selfName, name);
			}
		} catch (MemberServiceException e) {
			e.printStackTrace();
		}
		return "forward:/blackList";
	}

	@RequestMapping("/sendInfo")
	public String doSendInfo() {
		return "messenger/sendInfo";
	}

	@RequestMapping("/outbox")
	public String toOutBox(HttpSession session, Model model) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		String sender = memberinfo.getNickname();
		try {
			List<Messagerecord> sendMessages = messengerService.listSendMessage(sender);
			model.addAttribute("sendMessages", sendMessages);
		} catch (MessengerServiceException e) {
			e.printStackTrace();
		}
		return "messenger/outbox";
	}

	@RequestMapping("/sendMessage")
	public String doSendMessage(HttpSession session, String receiver, String title, String content, Model model) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		String sender = memberinfo.getNickname();
		try {
			Messagerecord message = new Messagerecord(sender, receiver, new Date(), title, content);
			messengerService.saveMessage(message);
		} catch (MessengerServiceException e) {
			e.printStackTrace();
		}
		return "forward:/outbox";
	}

	@RequestMapping("/view")
	public String doView(String ID, Model model) {
		try {
			Messagerecord message = messengerService.findMessage(Long.parseLong(ID));
			model.addAttribute("message", message);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (MessengerServiceException e) {
			e.printStackTrace();
		}
		return "messenger/view";
	}

	@RequestMapping("/inbox")
	public String toInBox(HttpSession session, Model model) {
		Memberinfo memberinfo = (Memberinfo) session.getAttribute("member");
		String recieveName = memberinfo.getNickname();
		try {
			List<Messagerecord> recieveMessages = messengerService.listRecieveMessage(recieveName);
			model.addAttribute("recieveMessages", recieveMessages);
		} catch (MessengerServiceException e) {
			e.printStackTrace();
		}
		return "messenger/inbox";
	}

	@RequestMapping("/delRecieveMessage")
	public String doDelRecieveMessage(String ID) {
		String[] ids = ID.split(",");
		try {
			for (String id : ids) {
				messengerService.delRecieveMessage(Long.parseLong(id));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (MessengerServiceException e) {
			e.printStackTrace();
		}
		return "forward:/inbox";
	}

	@RequestMapping("/delSendMessage")
	public String doDelSendMessage(String ID) {
		String[] ids = ID.split(",");
		try {
			for (String id : ids) {
				messengerService.delSendMessage(Long.parseLong(id));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (MessengerServiceException e) {
			e.printStackTrace();
		}
		return "forward:/outbox";
	}

}
