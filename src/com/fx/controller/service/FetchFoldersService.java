package com.fx.controller.service;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

import com.fx.controller.ModelAccess;
import com.fx.model.EmailAccountBean;
import com.fx.model.folder.EmailFolderBean;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FetchFoldersService extends Service<Void> {
	private EmailAccountBean email;
	private EmailFolderBean<String> folderRoot;
	private ModelAccess modelAccess ;
	
	private static int NUMBER_OF_ACTIVE_FETCHFOLDERSERVICES;
	
	public FetchFoldersService(EmailAccountBean email, EmailFolderBean<String> folderRoot , ModelAccess model) {
		this.email = email;
		this.folderRoot = folderRoot;
		this.modelAccess = model;
		
		this.setOnSucceeded(x -> {
			NUMBER_OF_ACTIVE_FETCHFOLDERSERVICES--;
		});
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				NUMBER_OF_ACTIVE_FETCHFOLDERSERVICES++;
				if(email != null) {
					// get folder from store 
					Folder[] folders = email.getStore().getDefaultFolder().list();
					for (Folder folder : folders) {
						// folder needed in modelAccess to make the MessageCountListener works(when gets messages count)
						modelAccess.addFolder(folder);
						
						//
						EmailFolderBean<String> item = new EmailFolderBean<>(folder.getName(),folder.getFullName());
						folderRoot.getChildren().add(item);
						folderRoot.setExpanded(true);
						
						//
						addMessageListener(item,folder);
						
						//
						FetchMessagesOfFolderService messagesOfFolderService = new FetchMessagesOfFolderService(item, folder);
						messagesOfFolderService.start();
						
						// iterate over folder to get sub folder
						Folder[] subfolders = folder.list();
						for (Folder subfolder : subfolders) {
							//
							modelAccess.addFolder(subfolder);
							
							//
							EmailFolderBean<String> subItem = new EmailFolderBean<>(subfolder.getName(),subfolder.getFullName());
							item.getChildren().add(subItem);
							item.setExpanded(true);
							
							//
							addMessageListener(subItem,subfolder);
							
							//
							FetchMessagesOfFolderService messagesOfSubFolderService = new FetchMessagesOfFolderService(subItem, subfolder);
							messagesOfSubFolderService.start();
						}
					}
				}
				return null;
			}
		};
		
	}
	
	private void addMessageListener(EmailFolderBean<String> folderBean,Folder folder) {
		folder.addMessageCountListener(new MessageCountAdapter() {
			@Override
			public void messagesAdded(MessageCountEvent event) {
				// MessageCountEvent contains any received msgs
				for (int i = 0; i < event.getMessages().length; i++) {
					try {
						// get last msg
						Message curMsg = folder.getMessage(folder.getMessageCount() - i);
						folderBean.addEmail(0,curMsg);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public static boolean getInActiveServices () {
		return NUMBER_OF_ACTIVE_FETCHFOLDERSERVICES == 0;
	}
}
