package com.fx.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Folder;

import com.fx.model.EmailAccountBean;
import com.fx.model.EmailMessageBean;
import com.fx.model.folder.EmailFolderBean;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ModelAccess {
	
	private Map<String, EmailAccountBean> emailAccounts = new HashMap<String, EmailAccountBean>();
	private ObservableList<String> emailAccountsNames = FXCollections.observableArrayList();
	
	public ObservableList<String> getEmailAccountNames(){
		return emailAccountsNames;
	}
	
	public EmailAccountBean getEmailAccountByName(String name){
		return emailAccounts.get(name);
	}
	
	public void addAccount(EmailAccountBean account){
		emailAccounts.put(account.getEmailAdress(), account);
		emailAccountsNames.add(account.getEmailAdress());
	}
	
	private EmailMessageBean selectedMessage;

	public EmailMessageBean getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(EmailMessageBean selectedMessage) {
		this.selectedMessage = selectedMessage;
	}
	
	EmailFolderBean<String> selectedFolder;

	public EmailFolderBean<String> getSelectedFolder() {
		return selectedFolder;
	}

	public void setSelectedFolder(EmailFolderBean<String> selectedFolder) {
		this.selectedFolder = selectedFolder;
	}
	
	// need folder to be accessed for the messages listener 
	private List<Folder> folders ;

	public List<Folder> getFolders() {
		return folders;
	}
	
	public void addFolder(Folder folder) {
		folders.add(folder);
	}
}
