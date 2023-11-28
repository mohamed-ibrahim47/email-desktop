package com.fx.model.table;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableRow;

public class BoldableRowFactory<T extends AbstractTableItem> extends TableRow<T> {
	
	
	// this used for binding with EmailMessageBean
	private SimpleBooleanProperty Bold = new SimpleBooleanProperty();
	
	private T currentItem = null;

	public BoldableRowFactory() {
		super();
		
		itemProperty().addListener((ObservableValue<? extends T > observable,T oldVal , T newVal ) -> {
			Bold.unbind();
			if(newVal != null  ) {
				Bold.bind(newVal.getReadProperty());
				currentItem=newVal;
			}
		});
		
		// call updateItem everytime isRead changes
		Bold.addListener((ObservableValue<? extends Boolean > observable,Boolean oldVal , Boolean newVal ) -> {
			if(currentItem != null && currentItem==getItem() ) {
				updateItem(getItem(), isEmpty());
			}
		});
	}
	
	@Override
	final protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			if (!item.isRead()) {
				setStyle("-fx-font-weight: bold");
			} else {
				setStyle("");
			}
		}
	}

}
