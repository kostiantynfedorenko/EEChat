package com.gmail.fedorenko.kostia;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private MessageList msgList = MessageList.getInstance();
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException 
	{
		Message m = null;
		
		try {
			m = Message.readFromStream(req.getInputStream());
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		
		if (m == null) {
			resp.setStatus(400); // bad request
			return;
		} else
			msgList.add(m);
	}
}
