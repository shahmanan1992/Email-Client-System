package Content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EmailClient {

	HashMap<String, String> users;
	static int mailServerCommunicationPort = 9701;
	static int mailServerQueuingCommunicationPort = 9901;
	String username, password;
	ArrayList<Mail> inbox;
	ArrayList<Mail> sentMails;

	public EmailClient() {
		inbox = new ArrayList<>();
		sentMails = new ArrayList<>();
		users = new HashMap<>();
		users.put("gilford.goa@glados.cs.rit.edu", "password1");
		users.put("gilford.goa@utah.cs.rit.edu", "password2");
		users.put("manan.shah@glados.cs.rit.edu", "password1");
		users.put("manan.shah@utah.cs.rit.edu", "password2");
	}

	public ArrayList<Mail> getInbox() {
		return inbox;
	}

	public void setInbox(ArrayList<Mail> inbox) {
		this.inbox = inbox;
	}

	public ArrayList<Mail> getSentMails() {
		return sentMails;
	}

	public void setSentMails(ArrayList<Mail> sentMails) {
		this.sentMails = sentMails;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean authenticateUser(String username, String password) {
		if (users.containsKey(username)) {
			if (users.get(username).equals(password)) {
				setUsername(username);
				setPassword(password);
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Mail>> refresh() {
		// ArrayList<Mail> inbox = new ArrayList<>();
		// ArrayList<Mail> sentMails = new ArrayList<>();
		inbox = new ArrayList<>();
		sentMails = new ArrayList<>();
		ArrayList<ArrayList<Mail>> mails = new ArrayList<>();
		Socket socket = null;
		ObjectInputStream inputStream;
		ObjectOutputStream outputStream;

		try {
			socket = new Socket(username.split("@")[1], mailServerCommunicationPort);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(username);
			inputStream = new ObjectInputStream(socket.getInputStream());

			ArrayList<Mail> inboxMail = (ArrayList<Mail>) inputStream.readObject();
			ArrayList<Mail> sentMail = (ArrayList<Mail>) inputStream.readObject();
			setInbox(inboxMail);
			setSentMails(sentMail);

			// inbox = (ArrayList<Mail>) inputStream.readObject();
			// sentMails = (ArrayList<Mail>) inputStream.readObject();

			mails.add(inbox);
			mails.add(sentMails);

			System.out.println("Mails received from mail server");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mails;
	}

	public boolean sendMail(Mail mail) {
		Socket socket = null;
		ObjectInputStream inputStream;
		ObjectOutputStream outputStream;

		try {
			socket = new Socket(username.split("@")[1], mailServerQueuingCommunicationPort);
			outputStream = new ObjectOutputStream(socket.getOutputStream());

			outputStream.writeObject(mail);
			inputStream = new ObjectInputStream(socket.getInputStream());
			socket.setSoTimeout(10000);
			System.out.println((String) inputStream.readObject());

		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

//	public void downloadAttachment(Mail mail) {
//		FileOutputStream fos;
//		try {
//			fos = new FileOutputStream("received file1." + mail.getAttachment().getType().split("/")[1]);
//			fos.write(mail.getAttachment().getAttachment());
//			fos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void downloadAttachment(String title,byte[] fileContent) {
//		FileOutputStream fos;
//		try {
//			fos = new FileOutputStream(title);
//			fos.write(fileContent);
//			fos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void viewMail(Mail mail) {
		byte[] data = mail.getMessage();
		System.out.println(new String(data));
		if (mail.isIfAttachment()) {
			System.out.println("attachent type:" + mail.getAttachment().getType());
		}
	}

	public Mail composeMail(String receiverAddress, byte[] message, boolean ifAttachment, String subject,
			String attachmentPath) {
		Mail mail = new Mail();
		try {
				Header header = new Header();
				header.setFrom(username);
				System.out.println(receiverAddress);
				header.setSendTo(receiverAddress);
				header.setSubject(subject);
				header.setDate(new Date() + "");
				mail.setHeader(header);
				mail.setMessage(message);

				if (ifAttachment) {

					mail.setIfAttachment(true);

					File file = new File(attachmentPath);
					Attachment attach = new Attachment();
					byte[] attachment = new byte[(int) file.length()];
					FileInputStream fileInputStream = new FileInputStream(file);
					fileInputStream.read(attachment);
					fileInputStream.close();

					Path path = Paths.get(attachmentPath);
					attach.setType(Files.probeContentType(path));
					attach.setAttachment(attachment);
					attach.setTitle(attachmentPath.substring(attachmentPath.lastIndexOf("\\")+1));
					System.out.println(attach.getTitle());
					mail.setAttachment(attach);
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mail;
	}

	/*
	 * public static void main(String[] args) { EmailClient client = new
	 * EmailClient(); if (Integer.parseInt(args[0]) == 1) {
	 * client.setUsername("gilford.goa@glados.cs.rit.edu"); Mail mail =
	 * client.composeMail("manan.shah1@utah.cs.rit.edu", new String("This mail"
	 * ).getBytes(), true, "test", "walk of life.mp3", new ArrayList<>());
	 * client.sendMail(mail); ArrayList<ArrayList<Mail>> mails =
	 * client.refresh(); System.out.println("Sent mails size : " +
	 * mails.get(1).size()); } else {
	 * client.setUsername("gilford.goa@glados.cs.rit.edu");
	 * ArrayList<ArrayList<Mail>> mails = client.refresh(); System.out.println(
	 * "inbox size : " + mails.get(0).size());
	 * client.viewMail(mails.get(0).get(0));
	 * //client.downloadAttachment(mails.get(0).get(0)); } }
	 */

}
