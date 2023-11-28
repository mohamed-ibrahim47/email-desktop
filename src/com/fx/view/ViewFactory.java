package com.fx.view;

import java.io.IOException;

import com.fx.controller.AbstractController;
import com.fx.controller.ComposeMessageController;
import com.fx.controller.EmailDetailsController;
import com.fx.controller.MainController;
import com.fx.controller.ModelAccess;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ViewFactory {
	// TODO better solution
	public static ViewFactory defaultViewFactory = new ViewFactory();
	
	private ModelAccess modelAccess = new ModelAccess();
	private MainController mainController ;
	private EmailDetailsController emailDetailsController;
	
	
	public Scene getMainScene(){
		mainController = new MainController(modelAccess);
		return initialiazeScene("MainLayout.fxml", mainController);
		
	}
	
	public Scene getEmailDetailsScene(){
		
		emailDetailsController = new EmailDetailsController(modelAccess);
		return initialiazeScene("EmailDetailsLayout.fxml", emailDetailsController);
		
	}
	
	public Scene getComposeMessageScene(){
		AbstractController composeMessageScene = new ComposeMessageController(modelAccess);
		return initialiazeScene("composeMessageLayout.fxml", composeMessageScene);
		
	}
	
	
	public Node resolveIcon(String treeItemValue){
		String lowerCaseTreeItemValue = treeItemValue.toLowerCase();
		ImageView returnIcon;
			try {
				if(lowerCaseTreeItemValue.contains("inbox")){
					returnIcon= new ImageView(new Image(getClass().getResourceAsStream("images/inbox.png")));
				} else if(lowerCaseTreeItemValue.contains("sent")){
					returnIcon= new ImageView(new Image(getClass().getResourceAsStream("images/sent2.png")));
				} else if(lowerCaseTreeItemValue.contains("spam")){
					returnIcon= new ImageView(new Image(getClass().getResourceAsStream("images/spam.png")));
				} else if(lowerCaseTreeItemValue.contains("@")){
					returnIcon= new ImageView(new Image(getClass().getResourceAsStream("images/email.png")));
				} else{
					returnIcon= new ImageView(new Image(getClass().getResourceAsStream("images/folder.png")));
				}
			} catch (NullPointerException e) {
				System.out.println("Invalid image location!!!");
				e.printStackTrace();
				returnIcon = new ImageView();
			}
			
			returnIcon.setFitHeight(16);
			returnIcon.setFitWidth(16);

		return returnIcon;
	}

	
	private Scene initialiazeScene(String fxmlPath, AbstractController abstractController ) {
		/*
		 * method to initialiazeScene based on controller to make controllers point to the same ModelAcces (shared massage)
		 */
		FXMLLoader fxmlLoader;
		Parent parent = null;
		Scene scene;
		try {
			fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
			fxmlLoader.setController(abstractController);
			parent = fxmlLoader.load();
			
		} catch (IOException e) {
			return null;
		}
		scene = new Scene(parent);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
		
		return scene;
	}
}
