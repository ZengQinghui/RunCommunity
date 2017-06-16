package com.briup.run.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.briup.run.common.bean.Blackrecord;
import com.briup.run.common.bean.Friendrecord;
import com.briup.run.common.bean.Graderecord;
import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.bean.Memberspace;
import com.briup.run.common.bean.Pointaction;
import com.briup.run.common.bean.Pointrecord;
import com.briup.run.common.exception.DataAccessException;
import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.common.util.MD5;
import com.briup.run.common.util.RandomChar;
import com.briup.run.dao.IMemberDao;
import com.briup.run.service.IMemberService;

@Service
public class IMemberServiceImpl implements IMemberService {
	@Autowired
	private IMemberDao memberDao;

	@Override
	public void registerMemberinfo(Memberinfo memberinfo) throws MemberServiceException {
		try {
			Memberinfo mi = memberDao.findMemberinfoByName(memberinfo.getNickname());
			if (mi != null) {
				throw new MemberServiceException("用户已存在!");
			} else {
				Pointaction pointAction = memberDao.findPointactionByPointAction("REGISTER");
				Memberinfo recommender = memberDao.findMemberinfoByName(memberinfo.getRecommender());
				if (recommender != null) {
					Pointaction recPointAction = memberDao.findPointactionByPointAction("RECOMMEND");
					recommender.setPoint(recommender.getPoint() + recPointAction.getPoint());
					Pointrecord recPointrecord = new Pointrecord(recPointAction, recommender.getNickname(), new Date());
					memberDao.savePointrecord(recPointrecord);
					memberDao.updateMemberinfo(recommender);
				}
				Graderecord graderecord = memberDao.findMemberinfoLevel(pointAction.getPoint());
				Pointrecord pointrecord = new Pointrecord(pointAction, memberinfo.getNickname(), new Date());
				memberDao.savePointrecord(pointrecord);
				memberinfo.setPoint(pointAction.getPoint());
				memberinfo.setRegisterdate(new Date());
				memberinfo.setStatus(1L);
				memberinfo.setIsonline(0L);
				memberinfo.setGraderecord(graderecord);
				String md5ofPwd = MD5.getMD5Str(memberinfo.getPassword());
				memberinfo.setPassword(md5ofPwd);
				memberDao.saveMemberinfo(memberinfo);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Memberinfo findMemberinfoByName(String nickname) throws MemberServiceException {
		Memberinfo memberinfo = null;
		try {
			memberinfo = memberDao.findMemberinfoByName(nickname);
			Graderecord graderecord = memberDao.findMemberinfoLevel(memberinfo.getPoint());
			memberinfo.setGraderecord(graderecord);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return memberinfo;
	}

	@Override
	public Memberinfo login(String username, String passwd) throws MemberServiceException {
		Memberinfo memberinfo = null;
		// 1．查看登陆的用户名是否存在
		try {
			memberinfo = memberDao.findMemberinfoByName(username);
			if (memberinfo == null) {
				throw new MemberServiceException("");
			}
			// 2．查看登陆的用户是否现在已经是在线状态
			if (memberinfo.getIsonline() == 1) {
				throw new MemberServiceException("");
			}
			// 3．查看登陆的用户是否是注销状态
			if (memberinfo.getStatus() != 1) {
				throw new MemberServiceException("");
			}
			// 4．查看密码是否正确
			if (!memberinfo.getPassword().equals(MD5.getMD5Str(passwd))) {
				throw new MemberServiceException("");
			}
			// 5．判断这次登陆是否和上次登陆是否在同一天内，如果不是
			// 需要给这次登陆加登陆应该能获取的积分数，并需要保存获取积分的记录
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String nowString = format.format(new Date());
			Date latestdate = memberinfo.getLatestdate();
			if (memberinfo.getLatestdate() != null && !"".equals(memberinfo.getLatestdate())) {
				String lastString = format.format(memberinfo.getLatestdate());
				if (!nowString.equals(lastString)) {
					Pointaction pointAction = memberDao.findPointactionByPointAction("LOGIN");
					Long newPoint = memberinfo.getPoint() + pointAction.getPoint();
					memberinfo.setPoint(newPoint);
					Graderecord graderecord = memberDao.findMemberinfoLevel(newPoint);
					memberinfo.setGraderecord(graderecord);
					Pointrecord pointrecord = new Pointrecord(memberinfo.getNickname(), new Date());
					pointrecord.setPointaction(pointAction);
					memberDao.savePointrecord(pointrecord);
				}
			}

			Graderecord graderecord = memberDao.findMemberinfoLevel(memberinfo.getPoint());
			memberinfo.setGraderecord(graderecord);
			// 6．将会员的isonline设成在线状态，将latestDate设成当前时间，并需要把上次登陆的时间保存到memberinfo的另外一个实例变量中。还需要设定会员的当前级别
			memberinfo.setIsonline(1L);
			memberinfo.setLatestdate(new Date());

			memberDao.updateMemberinfo(memberinfo);
			memberinfo.setLatestdate(latestdate);

		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return memberinfo;
	}

	@Override
	public void logout(String nickname) throws MemberServiceException {
		try {
			Memberinfo memberinfo = memberDao.findMemberinfoByName(nickname);
			memberinfo.setIsonline(0L);
			memberDao.updateMemberinfo(memberinfo);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Memberinfo> findMemberinfoByNum(int number) throws MemberServiceException {
		List<Memberinfo> memberinfoByNum = null;
		try {
			memberinfoByNum = memberDao.findMemberinfoByNum(number);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return memberinfoByNum;
	}

	@Override
	public int findMemberinfoOnline() throws MemberServiceException {
		return 0;
	}

	@Override
	public Graderecord findMemberinfoLevel(Long point) throws MemberServiceException {
		Graderecord graderecord = null;
		try {
			graderecord = memberDao.findMemberinfoLevel(point);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return graderecord;
	}

	@Override
	public Memberinfo saveOrUpDate(Memberinfo memberinfo, String oldPasswd) throws MemberServiceException {
		Memberinfo memberinfo2 = null;
		try {
			Memberinfo memberinfoByName = memberDao.findMemberinfoByName(memberinfo.getNickname());
			if (memberinfoByName != null && memberinfoByName.getPassword().equals(MD5.getMD5Str(oldPasswd))) {
				memberinfo.setPassword(MD5.getMD5Str(memberinfo.getPassword()));
				memberDao.updateMemberinfo(memberinfo);
				memberinfo2 = memberDao.findMemberinfoByName(memberinfo.getNickname());
			} else {
				throw new MemberServiceException("旧密码输入错误!");
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return memberinfo2;
	}

	@Override
	public String getBackPasswd(String nickname, String pwdQuestion, String pwdAnswer) throws MemberServiceException {
		try {
			Memberinfo memberinfo = memberDao.findMemberinfoByName(nickname);
			if (memberinfo == null) {
				throw new MemberServiceException("用户不存在!");
			} else {
				if (memberinfo.getPasswordquestion().equals(pwdQuestion)
						&& memberinfo.getPasswordanswer().equals(pwdAnswer)) {
					String newPasswd = RandomChar.getChars(4, 6);
					memberinfo.setPassword(MD5.getMD5Str(newPasswd));
					memberDao.updateMemberinfo(memberinfo);
					return newPasswd;
				} else {
					throw new MemberServiceException("密保问题或答案输入错误!");
				}
			}

		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update(Memberinfo memberinfo) throws MemberServiceException {
	}

	@Override
	public void saveOrUpdate(String selfname, String friendname) throws MemberServiceException {
	}

	@Override
	public List<Memberinfo> listFriend(String selfname) throws MemberServiceException {
		List<Memberinfo> friends = null;
		try {
			friends = memberDao.listFriend(selfname);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return friends;
	}

	@Override
	public void moveToBlack(String selfname, String blackname) throws MemberServiceException {
		Blackrecord blackFriend = new Blackrecord(selfname, blackname);
		try {
			memberDao.saveBlackFriend(blackFriend);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Memberinfo> listBlack(String selfname) throws MemberServiceException {
		List<Memberinfo> listBlack = null;
		try {
			listBlack = memberDao.listBlack(selfname);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return listBlack;
	}

	@Override
	public Friendrecord findFriend(String selfName, String friendName) throws MemberServiceException {
		Friendrecord findfriend = null;
		try {
			findfriend = memberDao.findfriend(selfName, friendName);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return findfriend;
	}

	@Override
	public Boolean isMemberspace(Long id) throws MemberServiceException {
		try {
			Memberspace memberspace = memberDao.findSpace(id);
			if (memberspace != null) {
				return true;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void moveToFriend(String selfName, String name_searching) throws MemberServiceException {
		Friendrecord friend = new Friendrecord(selfName, name_searching);
		try {
			memberDao.saveFriend(friend);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteBlack(String selfName, String blackName) throws MemberServiceException {
		Blackrecord blackFriend = new Blackrecord(selfName, blackName);
		try {
			memberDao.deleteBlack(blackFriend);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteFriend(String selfName, String friendName) throws MemberServiceException {
		Friendrecord friend = new Friendrecord(selfName, friendName);
		try {
			memberDao.deleleFriend(friend);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delSpace(Long id) throws MemberServiceException {
	}

	@Override
	public Pointaction findPointactionByPointAction(String actionName) throws MemberServiceException {
		Pointaction pointAction = null;
		try {
			pointAction = memberDao.findPointactionByPointAction(actionName);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return pointAction;
	}

	@Override
	public void save(Pointrecord pointrecord) throws MemberServiceException {
	}

	@Override
	public Memberspace findSpace(Long id) throws MemberServiceException {
		Memberspace memberspace = null;
		try {
			memberspace = memberDao.findSpace(id);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return memberspace;
	}

	@Override
	public void saveSpace(Memberspace memberspace) throws MemberServiceException {
		try {
			memberDao.saveSpace(memberspace);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateSpace(Memberspace memberspace) throws MemberServiceException {
		try {
			memberDao.updateSpace(memberspace);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

}
