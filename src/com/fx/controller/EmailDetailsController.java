package com.fx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.fx.model.EmailMessageBean;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

public class EmailDetailsController extends AbstractController implements Initializable {
	
	//private Singleton singleton;

    public EmailDetailsController(ModelAccess modelAccess) {
		super(modelAccess);
	}

	@FXML
    private WebView webView;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label SenderLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("EmailDetailsController initialized");
		
		EmailMessageBean SelectedMessage= getModelAccess().getSelectedMessage();
		
		subjectLabel.setText("Subject: " + SelectedMessage.getSubject());
		SenderLabel.setText("Subject: " + SelectedMessage.getSender());
		//webView.getEngine().loadContent(SelectedMessage.getContent());

	}

}
