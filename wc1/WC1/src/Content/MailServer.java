package Content;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MailServer {
	private int MailQueuerSocketPort = 9901;
	private int MailReceiverSocketPort = 9801; // equivalent to port 25
	private int MailClientCommunicationPort = 9701;
	HashMap<String, ArrayList<Mail>> mailbox;
	HashMap<String, ArrayList<Mail>> sentMails;
	ArrayList<Mail> mailQueue;

	public MailServer() {
		try {
			mailbox = new HashMap<>();
			sentMails = new HashMap<>();
			mailQueue = new ArrayList<>();
			mailbox.put("gilford.goa@" + InetAddress.getLocalHost().getHostName() + ".cs.rit.edu", new ArrayList<>());
			mailbox.put("manan.shah@" + InetAddress.getLocalHost().getHostName() + ".cs.rit.edu", new ArrayList<>());
			sentMails.put("gilford.goa@" + InetAddress.getLocalHost().getHostName() + ".cs.rit.edu", new ArrayList<>());
			sentMails.put("manan.shah@" + InetAddress.getLocalHost().getHostName() + ".cs.rit.edu", new ArrayList<>());
			new MailSender().start();
			new MailQueuer(MailQueuerSocketPort).start();
			new MailReceiver(MailReceiverSocketPort).start();
			new MailClientCommunication(MailClientCommunicationPort).start();
			System.out.println(InetAddress.getLocalHost().getHostName() + " mail server is running");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MailSender extends Thread {
		private Socket socket;
		private ObjectInputStream inputStream;
		private ObjectOutputStream outputStream;

		public MailSender() {

		}

		public void run() {

			while (true) {

				Iterator<Mail> it = mailQueue.iterator();

				while (it.hasNext()) {

					try {
						Mail mail = it.next();
						Header header = mail.getHeader();
						String domain = header.getSendTo().split("@")[1];

						System.out.println("Sending email to mail server " + domain);

						socket = new Socket(domain, MailReceiverSocketPort);

						outputStream = new ObjectOutputStream(socket.getOutputStream());

						outputStream.writeObject("Hello " + domain);

						socket.setSoTimeout(5000);

						inputStream = new ObjectInputStream(socket.getInputStream());

						System.out.println((String) inputStream.readObject());

						outputStream.writeObject("Mail from " + header.getFrom());

						System.out.println((String) inputStream.readObject());

						outputStream.writeObject(header.getSendTo());

						System.out.println((String) inputStream.readObject());

						String reply = (String) inputStream.readObject();

						if (Integer.parseInt(reply) == 0) {
							Mail errorMail = new Mail();
							String error = "Error in sending mail to " + header.getSendTo() + " User does not exist";
							errorMail.setMessage(error.getBytes());
							Header head = new Header();
							head.setFrom("Mail server");
							head.setSubject("email delivery failed");
							errorMail.setHeader(head);
							ArrayList<Mail> senderMails = mailbox.get(mail.getHeader().getFrom());
							senderMails.add(errorMail);
						} else {
							outputStream.writeObject(mail);
							System.out.println("Mail sent to mail server " + domain);
						}
						socket.close();

						it.remove();

					} catch (Exception e) {
						System.out.println("Receiving mail Server down or does not exist");

					}
				}

				try {
					sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	}

	class MailQueuer extends Thread {
		private ServerSocket mailQueuerSocket;
		private Socket socket;
		private ObjectInputStream inputStream;
		private ObjectOutputStream outputStream;

		public MailQueuer(int port) {
			try {
				mailQueuerSocket = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {

			while (true) {
				try {
					socket = mailQueuerSocket.accept();
					inputStream = new ObjectInputStream(socket.getInputStream());
					outputStream = new ObjectOutputStream(socket.getOutputStream());

					Mail mail = (Mail) inputStream.readObject();
					Header header = mail.getHeader();
					String domain = header.getSendTo().split("@")[1];

					if (domain.equals("glados.cs.rit.edu") || domain.equals("utah.cs.rit.edu")) {
						mailQueue.add(mail);
						System.out.println("Mail added to queue");
						outputStream.writeObject("mail queued succesfully");
					} else {
						System.out.println("Receiving mail server not recognized");
						outputStream.writeObject("Error in sending mail");
					}
					String mailSender = mail.getHeader().getFrom();
					if (sentMails.containsKey(mailSender)) {
						ArrayList<Mail> mails = sentMails.remove(mailSender);
						mails.add(mail);
						sentMails.put(mailSender, mails);
					}

					socket.close();

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}

		}
	}

	class MailReceiver extends Thread {
		private ServerSocket MailReceiverSocket;
		private Socket socket;
		private ObjectInputStream inputStream;
		private ObjectOutputStream outputStream;

		public MailReceiver(int port) {
			try {
				MailReceiverSocket = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {

			while (true) {
				try {
					socket = MailReceiverSocket.accept();
					inputStream = new ObjectInputStream(socket.getInputStream());
					outputStream = new ObjectOutputStream(socket.getOutputStream());

					System.out.println((String) inputStream.readObject());

					outputStream.writeObject("Hello " + socket.getRemoteSocketAddress().toString());

					System.out.println((String) inputStream.readObject());

					outputStream.writeObject("sender ok");

					String userName = (String) inputStream.readObject();

					System.out.println("Rcpt. to " + userName);

					if (mailbox.containsKey(userName)) {
						outputStream.writeObject("receipient ok");
						outputStream.writeObject("1");
						Mail mail = (Mail) inputStream.readObject();
						System.out
								.println("Mail received from mail server " + mail.getHeader().getFrom().split("@")[1]);
						mailbox.get(userName).add(mail);
						System.out.println("added to inbox of " + userName);
					} else {
						outputStream.writeObject("Unrecognized receipient");
						outputStream.writeObject("0");
					}

					socket.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	class MailClientCommunication extends Thread {
		private ServerSocket MailClientCommunicationSocket;
		private Socket socket;
		private ObjectInputStream inputStream;
		private ObjectOutputStream outputStream;

		public MailClientCommunication(int port) {
			try {
				MailClientCommunicationSocket = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {

			while (true) {
				try {
					socket = MailClientCommunicationSocket.accept();

					outputStream = new ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());

					String username = (String) inputStream.readObject();

					if (mailbox.containsKey(username)) {
						outputStream.writeObject(mailbox.get(username));
						outputStream.writeObject(sentMails.get(username));
						System.out.println("Mails sent to Email client");
					}
					socket.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	public static void main(String[] args) {
		MailServer server = new MailServer();
	}

}
