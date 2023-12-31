package com.fx.controller.service;

import javax.mail.internet.MimeBodyPart;

import com.fx.model.EmailMessageBean;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class SaveAttachmentsService extends Service<Void>{
	
	private String LOCATION_OF_DOWNLOADS = System.getProperty("user.home") + "/Downloads/";
	
	
	private EmailMessageBean messageToDownload;
	private ProgressBar progress;
	private Label label;
	
	
	

	public SaveAttachmentsService(ProgressBar progress, Label label) {
		this.progress = progress;
		this.label = label;
		
		this.setOnRunning(e->{showVisuals(true);});
		this.setOnSucceeded(e->{showVisuals(false);});
	}




	@Override
	protected Task<Void> createTask() {
		return new Task<Void>(){
			@Override
			protected Void call() throws Exception {				
				try {
					for(MimeBodyPart mbp: messageToDownload.getAttachmentsList()){
						updateProgress(messageToDownload.getAttachmentsList().indexOf(mbp),
								messageToDownload.getAttachmentsList().size());
						mbp.saveFile(LOCATION_OF_DOWNLOADS + mbp.getFileName());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
		};
	}

	public void setMessageToDownload(EmailMessageBean messageToDownload) {
		this.messageToDownload = messageToDownload;
	}

	private void showVisuals(boolean show){
		progress.setVisible(show);
		label.setVisible(show);
	}
}
