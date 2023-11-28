package com.fx.controller.service;

import javax.mail.Folder;
import javax.mail.Message;

import com.fx.model.folder.EmailFolderBean;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FetchMessagesOfFolderService extends Service<Void> {

	private EmailFolderBean<String> emailFolderBean;
	private Folder folder;
	
	
	public FetchMessagesOfFolderService(EmailFolderBean<String> emailFolderBean, Folder folder) {
		this.emailFolderBean = emailFolderBean;
		this.folder = folder;
	}


	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// avoid some cases
				if(folder.getType() != Folder.HOLDS_FOLDERS)
					folder.open(Folder.READ_WRITE);
				
				int folderSize =folder.getMessageCount();
				
				for(int i = folderSize; i > 0; i--){
					Message curMessage = folder.getMessage(i);
					emailFolderBean.addEmail(curMessage);
					
				}
				
				return null;
			}
		
		};
	}

}
