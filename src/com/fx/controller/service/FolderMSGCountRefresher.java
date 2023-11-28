package com.fx.controller.service;

import java.util.List;

import javax.mail.Folder;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FolderMSGCountRefresher extends Service<Void> {

	/*
	 * this class just to get msg count for the FetchFolderService's method addMessageListener
	 * 
	 * */
	private List<Folder> folders;
	
	public FolderMSGCountRefresher(List<Folder> folders) {
		this.folders = folders;
	}


	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				for(;;) {
					try {
						Thread.sleep(10000);
						// this check to avoid ConcurrentModificationException(iterate on folder list while it's been modified by another service)
						if (FetchFoldersService.getInActiveServices()) {
							for (Folder folder : folders) {
								if (folder.getType() != Folder.HOLDS_FOLDERS && folder.isOpen()) {
									folder.getMessageCount();
								}
							} 
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		};
	}

}
