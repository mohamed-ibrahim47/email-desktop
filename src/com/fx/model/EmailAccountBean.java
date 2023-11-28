package com.fx.model;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import javafx.collections.ObservableList;

public class EmailAccountBean {
	
	private String emailAdress;
	private String password;
	private Properties properties;
	
	private Store store;
	private Session session;
	private int loginState = EmailConstants.LOGIN_STATE_NOT_READY;
	
	public String getEmailAdress() {
		return emailAdress;
	}

	public Properties getProperties() {
		return properties;
	}

	public Store getStore() {
		return store;
	}

	public Session getSession() {
		return session;
	}

	public int getLoginState() {
		return loginState;
	}
	
	public EmailAccountBean(String EmailAddress, String Password){
		this.emailAdress = EmailAddress;
		this.password = Password;
		
		properties = new Properties();
		//probs 4 gmail
		properties.put("mail.store.protocol", "imaps");
		properties.put("mail.transport.protocol", "smtps");
		properties.put("mail.smtps.host", "smtp.gmail.com");
		properties.put("mail.smtps.auth", "true");
		properties.put("incomingHost", "imap.gmail.com");
		properties.put("outgoingHost", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
		
		Authenticator auth = new Authenticator(){
			@Override
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(EmailAddress, password);
			}			
		};
		
		//Connection:
		session = Session.getInstance(properties, auth);
		try {
			this.store = session.getStore();
			store.connect(properties.getProperty("incomingHost"), emailAdress, password);
			System.out.println("EmailAccountBean constructed succesufully!!!");
			loginState = EmailConstants.LOGIN_STATE_SUCCEDED;
		} catch (Exception e) {
			e.printStackTrace();
			loginState = EmailConstants.LOGIN_STATE_FAILED_BY_CREDENTIALS;
		}
		
	}
	
	public void addEmailsToData(ObservableList<EmailMessageBean> data){
		try {
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			for(int i = folder.getMessageCount(); i > 0; i--){
				
				Message message = folder.getMessage(i);
				EmailMessageBean messageBean = new EmailMessageBean(message.getSubject(), 
						message.getFrom()[0].toString(),
						message.getSize(),  message.getSentDate(),
						message,
						message.getFlags().contains(Flag.SEEN));
				System.out.println("Got: " + messageBean);
				data.add(messageBean);
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPassword() {
		return password;
	}

	
	

}
