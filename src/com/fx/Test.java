package com.fx;

import com.fx.model.EmailAccountBean;
import com.fx.model.EmailMessageBean;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Test {
	
	public static void main(String[] args) {
		final EmailAccountBean emailAccountBean = new EmailAccountBean("m49445069@gmail.com", "asfasdasdaAA123a");

		ObservableList<EmailMessageBean> data = FXCollections.observableArrayList();
		emailAccountBean.addEmailsToData(data);
	}

}
