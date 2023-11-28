package com.fx.controller.service;

import com.fx.controller.ModelAccess;
import com.fx.model.EmailAccountBean;
import com.fx.model.EmailConstants;
import com.fx.model.folder.EmailFolderBean;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CreateAndRegisterEmailAcctService extends Service<Integer>{

	private String EmailAddress ,Pass;
	private EmailFolderBean<String> folderRoot;
	private ModelAccess modelAccess ;

	
	public CreateAndRegisterEmailAcctService(String emailAddress, String pass, EmailFolderBean<String> folderRoot,
			ModelAccess model) {
		EmailAddress = emailAddress;
		Pass = pass;
		this.folderRoot = folderRoot;
		this.modelAccess = model;
	}


	@Override
	protected Task<Integer> createTask() {
		
		
		return new Task<Integer>() {

			@Override
			protected Integer call() throws Exception {
				EmailAccountBean emailAccountBean = new EmailAccountBean(EmailAddress, Pass);
				if(emailAccountBean.getLoginState() == EmailConstants.LOGIN_STATE_SUCCEDED) {
					modelAccess.addAccount(emailAccountBean);
					EmailFolderBean<String> emailFolderBean = new EmailFolderBean<String>(EmailAddress);
					folderRoot.getChildren().add(emailFolderBean);
					// initiate folder service
					FetchFoldersService fetchFoldersService = new FetchFoldersService(emailAccountBean, emailFolderBean,modelAccess);
					fetchFoldersService.start();
				}
				
				return emailAccountBean.getLoginState();
			}
		};
	}

}
