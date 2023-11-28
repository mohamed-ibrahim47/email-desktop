package com.fx.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import com.fx.model.table.AbstractTableItem;
import com.fx.model.table.FormattableInteger;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmailMessageBean  extends AbstractTableItem{

	private SimpleStringProperty subject;
	private SimpleStringProperty sender;
	private SimpleObjectProperty<FormattableInteger> size;
	private Message msg;
	private SimpleObjectProperty<Date> date;

	
	//Attachments hanling:
	private List<MimeBodyPart> attachmentsList = new ArrayList<MimeBodyPart>();
	private StringBuffer attachmentsNames = new StringBuffer();
	
	public EmailMessageBean(String sub , String send , Integer sze,Date date,Message message,boolean read) {
		super(read);
		subject=new SimpleStringProperty(sub);
		sender = new SimpleStringProperty(send);
		size = new SimpleObjectProperty<FormattableInteger>( new FormattableInteger(sze));
		msg = message;
		this.date = new SimpleObjectProperty<Date>(date);

	}
	
	public Message getMsg() {
		return msg;
	}

	public String getSender(){
		return sender.get();
	}
	public String getSubject(){
		return subject.get();
	}

	public Date getDate(){
		return date.get();
	}
	public FormattableInteger getSize(){
		return size.get();
	}
	
	public List<MimeBodyPart> getAttachmentsList() {
		return attachmentsList;
	}

	public String getAttachmentsNames() {
		return attachmentsNames.toString();
	}
	
	public void addAttachment(MimeBodyPart mbp){
		attachmentsList.add(mbp);
		try {
			attachmentsNames.append(mbp.getFileName() + "; ");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean hasAttachments(){
		return attachmentsList.size() > 0;
	}
	
	
	public void clearAttachments(){
		attachmentsList.clear();
		attachmentsNames.setLength(0);
	}

	
	
	@Override
	public String toString() {
		return "EmailMessageBean "
				+ "sender=" + sender.get() + 
				", subject=" + subject.get() +
				", size=" + size.get() ;
	}
}
