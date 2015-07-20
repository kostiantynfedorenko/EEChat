package com.gmail.fedorenko.kostia;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetListServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private MessageList msgList = MessageList.getInstance();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws IOException 
	{
		OutputStream os = resp.getOutputStream();
		String fromStr = req.getParameter("from");
		int from = Integer.parseInt(fromStr);
		Message m;
		
		List<Message> list = msgList.get();
		
		for (int i = from; i < list.size(); i++) {
			m = list.get(i);
			m.writeToStream(os);
		}
	}
}
