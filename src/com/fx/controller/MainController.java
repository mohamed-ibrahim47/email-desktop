package com.fx.controller;

import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;

import com.fx.controller.service.CreateAndRegisterEmailAcctService;
import com.fx.controller.service.FolderMSGCountRefresher;
import com.fx.controller.service.MessageRendererService;
import com.fx.controller.service.SaveAttachmentsService;
import com.fx.model.EmailMessageBean;
import com.fx.model.folder.EmailFolderBean;
import com.fx.model.table.BoldableRowFactory;
import com.fx.model.table.FormattableInteger;
import com.fx.view.ViewFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MainController extends AbstractController implements Initializable {

	public MainController(ModelAccess modelAccess) {
		super(modelAccess);
	}


	@FXML
	private TreeView<String> emailFoldersTreeView;
	//root of tree
   // private TreeItem<String> root  = new TreeItem<String>();
    //private SampleData sampleData = new SampleData();
    private MenuItem showDetails = new MenuItem("show details");
   // private Singleton singleton;

	
	@FXML
	private TableView<EmailMessageBean> emailTableView;

	@FXML
	private TableColumn<EmailMessageBean, String> subjectCol;

	@FXML
	private TableColumn<EmailMessageBean, String> senderCol;

	@FXML
	private TableColumn<EmailMessageBean, FormattableInteger> sizeCol;

	@FXML
	private WebView messageRenderer;

	@FXML
	private Button Button1,downAttachBtn;
	
    @FXML
    private Label downAttachLabel;
    
    @FXML
    private ProgressBar downAttachProgress;
    
    private SaveAttachmentsService saveAttachmentsService;
    private MessageRendererService messageRendererService;

    @FXML
    private TableColumn<EmailMessageBean, Date> dateCol;
	@FXML
	void Button1Action(ActionEvent event) {
		
//    	Service<Void> service = new Service<Void>(){
//
//			@Override
//			protected Task createTask() {
//				return new Task<Void>() {
//
//					@Override
//					protected Void call() throws Exception {
//						final EmailAccountBean emailAccountBean = new EmailAccountBean("m49445069@gmail.com",
//								"asfasdasdaAA123a");
//
//						ObservableList<EmailMessageBean> data = getModelAccess().getSelectedFolder().getData();
//						emailAccountBean.addEmailsToData(data);
//						return null;
//
//					}
//
//				};
//			}
//		};
//		
//		service.start();
		
		Scene scene = ViewFactory.defaultViewFactory.getComposeMessageScene();
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		System.out.println("button1 clicked!!");

	}
    @FXML
	void changeReadAction() {
		EmailMessageBean message = getModelAccess().getSelectedMessage();
		if (message != null) {
			boolean value = message.isRead();
			message.setRead(!value);
			EmailFolderBean<String> selectedFolder = getModelAccess().getSelectedFolder();
			if (selectedFolder != null) {
				if (value) {
					selectedFolder.incrementUnreadMessagesCount(1);
				} else {
					selectedFolder.decrementUnreadMessagesCount();
				}
			}
		}
	}

    
    @FXML
    void downAttachBtnAction(ActionEvent event) {
    	EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
    	if(message != null  && message.hasAttachments()){
    		saveAttachmentsService.setMessageToDownload(message);
    		saveAttachmentsService.restart();
    	}
    }
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//attachments
		downAttachProgress.setVisible(false);
		downAttachLabel.setVisible(false);
		saveAttachmentsService = new SaveAttachmentsService(downAttachProgress, downAttachLabel);
		downAttachProgress.progressProperty().bind(saveAttachmentsService.progressProperty());

		
		//rendering
		 messageRendererService = new MessageRendererService(messageRenderer.getEngine());
		//
		FolderMSGCountRefresher folderMSGCountRefresher = new FolderMSGCountRefresher(getModelAccess().getFolders());
		folderMSGCountRefresher.start();
		
		//ViewFactory vFactory = new ViewFactory();
		ViewFactory vFactory = ViewFactory.defaultViewFactory;

		// table
		emailTableView.setRowFactory(x -> new BoldableRowFactory<>());
		subjectCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("subject"));
		senderCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("sender"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, FormattableInteger>("size"));
		dateCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, Date>("date"));
		
		// bug
		sizeCol.setComparator(new FormattableInteger(0));
		
		//emailTableView.setItems(Data);
		// set items in email table view based on selected tree item
		emailFoldersTreeView.setOnMouseClicked(x ->{
			EmailFolderBean<String> item = (EmailFolderBean<String>) emailFoldersTreeView.getSelectionModel().getSelectedItem();
			if(item != null && !item.isTopElement()) {
				emailTableView.setItems(item.getData());
				getModelAccess().setSelectedFolder(item);
				//clear messages
				getModelAccess().setSelectedMessage(null);

			}
			
		});
		
		// render massage based on select email in emailTableView
		emailTableView.setOnMouseClicked(x ->{
			EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
			if(message != null) {
				// message

				//messageRenderer.getEngine().loadContent(message.getContent());
				getModelAccess().setSelectedMessage(message);
				
				//
				messageRendererService.setMessageToRender(message);
				messageRendererService.restart();
				//Platform.runLater(messageRendererService); // run on application thread (performance issue for large msgs)
			}
			
		});
		
		// menu on email table view
		emailTableView.setContextMenu(new ContextMenu(showDetails));

		showDetails.setOnAction(e->{
			
//			Pane pane = new Pane();
//			try {
//				pane = FXMLLoader.load(getClass().getResource("EmailDetailsLayout.fxml"));
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}	
			//Scene scene = new Scene(pane);
			//scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
			
			Scene scene = vFactory.getEmailDetailsScene();
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		});
		
		
		
		//TreeView
		EmailFolderBean<String> root = new EmailFolderBean<>("");
		emailFoldersTreeView.setRoot(root);
		emailFoldersTreeView.setShowRoot(false);
		
		CreateAndRegisterEmailAcctService createAndRegisterEmailAcctService = 
				new CreateAndRegisterEmailAcctService("m49445069@gmail.com","asfasdasdaAA123a", root,getModelAccess());
		createAndRegisterEmailAcctService.start();
		
		
		
		/*
		 // just exp
	  	EmailFolderBean<String> exp = new EmailFolderBean<>("example@yahoo.com");
		root.getChildren().add(exp);
		EmailFolderBean<String> Inbox = new EmailFolderBean<>("Inbox", "CompleteInbox");
		EmailFolderBean<String> Sent = new EmailFolderBean<>("Sent", "CompleteSent");
			Sent.getChildren().add(new EmailFolderBean<>("Subfolder1", "SubFolder1Complete"));
			Sent.getChildren().add(new EmailFolderBean<>("Subfolder2", "SubFolder1Complete2"));
		EmailFolderBean<String> Spam = new EmailFolderBean<>("Spam", "CompleteSpam");
		
		exp.getChildren().addAll(Inbox, Sent, Spam);
		
		Inbox.getData().addAll(SampleData.Inbox);
		Sent.getData().addAll(SampleData.Sent);
		Spam.getData().addAll(SampleData.Spam);
		*/
		
		/*emailFoldersTreeView.setRoot(root);
		root.setValue("example@yahoo.com");
		root.setGraphic(vFactory.resolveIcon(root.getValue()));

		TreeItem<String> Inbox = new TreeItem<String>("Inbox", vFactory.resolveIcon("Inbox"));
		TreeItem<String> Sent = new TreeItem<String>("Sent", vFactory.resolveIcon("Sent"));
		TreeItem<String> Subitem1 = new TreeItem<String>("Subitem1", vFactory.resolveIcon("Subitem1"));
		TreeItem<String> Subitem2 = new TreeItem<String>("Subitem2", vFactory.resolveIcon("Subitem2"));
		Sent.getChildren().addAll(Subitem1, Subitem2);
		TreeItem<String> Spam = new TreeItem<String>("Spam", vFactory.resolveIcon("Spam"));
		TreeItem<String> Trash = new TreeItem<String>("Trash", vFactory.resolveIcon("Trash"));

		root.getChildren().addAll(Inbox, Sent, Spam, Trash);
		root.setExpanded(true);
		*/
	}

	
}
