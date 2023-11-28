package com.fx;


import com.fx.view.ViewFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimpleApp extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		//ViewFactory vFactory = new ViewFactory();
		ViewFactory vFactory = ViewFactory.defaultViewFactory;
		Scene scene = vFactory.getMainScene();
		
		//Pane parent =FXMLLoader.load(getClass().getResource("MainLayout.fxml"));	
		//Scene scene = new Scene(parent);
		//scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
		stage.setScene(scene);
		stage.show();
	}

}
