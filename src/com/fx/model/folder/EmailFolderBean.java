package com.fx.model.folder;

import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.fx.model.EmailMessageBean;
import com.fx.view.ViewFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class EmailFolderBean<T> extends TreeItem<String> {
	
	private boolean topElement  = false;
	private int unreadMessageCount;
	private String name;
	@SuppressWarnings("unused")
	private String completeName;
	private ObservableList<com.fx.model.EmailMessageBean> data = FXCollections.observableArrayList();
	
	/**
	 * Constructor for top elements
	 * @param value
	 */
	
	public EmailFolderBean(String value){
		super(value, ViewFactory.defaultViewFactory.resolveIcon(value));
		this.name = value;
		this.completeName = value;
		data = null;
		topElement = true;
		this.setExpanded(true);
	}
	
	public EmailFolderBean(String value, String compleName){
		super(value, ViewFactory.defaultViewFactory.resolveIcon(value));
		this.name = value;
		this.completeName = compleName;
	}
	
	private void updateValue(){
		if(unreadMessageCount > 0){
			this.setValue((String)(name + "(" + unreadMessageCount + ")"));
		}else{
			this.setValue(name);
		}
	}
	
	public void incrementUnreadMessagesCount(int newMessages){
		unreadMessageCount = unreadMessageCount + newMessages;
		updateValue();
	}
	
	public void decrementUnreadMessagesCount(){
		unreadMessageCount--;
		updateValue();
	}
	
	public void addEmail(Message message) throws MessagingException {
		 addEmail(-1 , message);
	}
	
	public void addEmail(int position,Message message) throws MessagingException {
		boolean isRead = message.getFlags().contains(Flag.SEEN);
		EmailMessageBean emailMessageBean = new EmailMessageBean(message.getSubject(),
				message.getFrom()[0].toString(),
				message.getSize(), message.getSentDate(),
				message,
				isRead);
		if(position<0)
			data.add(emailMessageBean);
		else 
			//  0 = first msg
			data.add(position, emailMessageBean);
		if(!isRead){
			incrementUnreadMessagesCount(1);
		}
		
	}
	
	
	
	public boolean isTopElement(){
		return topElement;
	}
	
	public ObservableList<EmailMessageBean> getData(){
		return data;
	}
	
	
	
	
	
	
}
