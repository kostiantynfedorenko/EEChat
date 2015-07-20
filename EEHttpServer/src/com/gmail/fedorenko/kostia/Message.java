package com.gmail.fedorenko.kostia;
//Server
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Message{

	public Date date = new Date();
	public String from;
	public String to;
	public String text;

	@Override
	public String toString() {
		return new StringBuilder().append("[").append(date.toString())
				.append(", From: ").append(from).append(", To: ").append(to)
				.append("] ").append(text).toString();
	}

	public int send(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setDoOutput(true);

		OutputStream os = con.getOutputStream();
		this.writeToStream(os);
		os.flush();
		os.close();

		return con.getResponseCode();
	}

	public void writeToStream(OutputStream out) throws IOException {
		Gson gson = new GsonBuilder().create();
		String s = gson.toJson(this);
//		System.out.println("To: " + s);
		byte[] packet = s.getBytes();

		DataOutputStream ds = new DataOutputStream(out);
		ds.writeInt(packet.length);
		ds.write(packet);
		ds.flush();
	}

	public static Message readFromStream(InputStream in) throws IOException,
			ClassNotFoundException {
		if (in.available() <= 0)
			return null;

		DataInputStream ds = new DataInputStream(in);
		int len = ds.readInt();
		byte[] packet = new byte[len];
		ds.read(packet);

		StringBuilder sb = new StringBuilder();
		for(byte b:packet)
			sb.append((char)b);
		
		String s=sb.toString();
		Gson gson = new GsonBuilder().create();
//		System.out.println("From: " + s);
		Message message = gson.fromJson(s, Message.class);
		
		return message;
	}
}
