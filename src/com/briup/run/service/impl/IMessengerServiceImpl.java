package com.briup.run.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.bean.Messagerecord;
import com.briup.run.common.exception.DataAccessException;
import com.briup.run.common.exception.MessengerServiceException;
import com.briup.run.dao.IMessengerDao;
import com.briup.run.service.IMessengerService;

@Service
public class IMessengerServiceImpl implements IMessengerService {
	@Autowired
	private IMessengerDao messengerDao;

	@Override
	public Integer findNewMessageNum(String nickname) throws MessengerServiceException {
		return null;
	}

	@Override
	public List<Memberinfo> findOneMemberinfo() throws MessengerServiceException {
		List<Memberinfo> memberinfo = null;
		try {
			memberinfo = messengerDao.findOneMemberinfo((int) (Math.random() * 20));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return memberinfo;
	}

	@Override
	public List<Memberinfo> findFriends(String age, String gender, String city) throws MessengerServiceException {
		List<Memberinfo> friends = null;
		try {
			friends = messengerDao.findFriends(age, gender, city);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return friends;
	}

	@Override
	public void saveMessage(Messagerecord message) throws MessengerServiceException {
		try {
			message.setSenderstatus(0L);
			message.setStatus("./images/icon09.gif");
			message.setReceiverstatus(0L);
			messengerDao.saveMessage(message);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Messagerecord> listSendMessage(String senderName) throws MessengerServiceException {
		List<Messagerecord> sendMessages = null;
		try {
			sendMessages = messengerDao.listSendMessage(senderName);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return sendMessages;
	}

	@Override
	public List<Messagerecord> listRecieveMessage(String recieveName) throws MessengerServiceException {
		List<Messagerecord> recieveMessages = null;
		try {
			recieveMessages = messengerDao.listRecieveMessage(recieveName);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return recieveMessages;
	}

	@Override
	public Messagerecord findMessage(Long id) throws MessengerServiceException {
		Messagerecord message = null;
		try {
			message = messengerDao.findMessage(id);
			message.setStatus("./images/icon10.gif");
			messengerDao.updateMessage(message);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return message;
	}

	@Override
	public void delRecieveMessage(Long id) throws MessengerServiceException {
		try {
			messengerDao.deleteRecieveMessage(id);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delSendMessage(Long id) throws MessengerServiceException {
		try {
			messengerDao.deleteSendMessage(id);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

}
