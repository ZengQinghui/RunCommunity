package com.briup.run.web.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
	private int count;

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		count++;
		HttpSession session = event.getSession();
		session.setAttribute("count", count);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		count--;
		HttpSession session = event.getSession();
		session.setAttribute("count", count);
	}

}
