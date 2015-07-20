package com.gmail.fedorenko.kostia;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		try {
			final Scanner scanner = new Scanner(System.in);

			System.out.println("Enter login: ");
			final String login = scanner.nextLine();
			
			Thread th = new Thread() {
				
				private int n;
				
				@Override
				public void run() {
					try {
						while ( ! isInterrupted()) {
							URL url = new URL("http://127.0.0.1:8888/get?from=" + n);
						   	HttpURLConnection http = (HttpURLConnection) url.openConnection();
						    try {
						    	InputStream is = http.getInputStream();
						    	Message m = null;    
						    	
						    	do {
						    		m = Message.readFromStream(is);
						    		if (m != null) {
						    			System.out.println(m.toString());
						    			n++;
						    		}
						    	} while (m != null);
						    } finally {
						    	http.disconnect();
						    }
						}
					} catch (Exception e) {
						return;
					}
				}
			};
			th.setDaemon(true);
			th.start();
			
			try {
				while (true) {
					String s = scanner.nextLine();
					if (s.isEmpty())
						break;
					
					int del = s.indexOf(':');
					String to = "";
					String text = s;
					
					if (del >= 0) {
						to = s.substring(0, del);
						text = s.substring(del + 1);
					}
					
					Message m = new Message();
					m.text = text;
					m.from = login;
					m.to = to;
					
					int res = m.send("http://127.0.0.1:8888/add");
					if (res != 200) {
						System.out.println("HTTP error: " + res);
						break;
					}
				}
			} finally {
				th.interrupt();
				scanner.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
