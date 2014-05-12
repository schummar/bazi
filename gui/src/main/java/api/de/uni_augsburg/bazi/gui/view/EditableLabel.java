package de.uni_augsburg.bazi.gui.view;

import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableStringValue;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class EditableLabel extends Label
{
	private final WritableStringValue text;
	private boolean editing = false;
	private TextField textField;
	private ContextMenu contextMenu;

	public EditableLabel(ObservableStringValue text)
	{
		textProperty().bind(text);
		if (text instanceof WritableStringValue)
		{
			this.text = (WritableStringValue) text;
			setOnMouseClicked(this::clicked);
			getStyleClass().add("editable");
		}
		else this.text = null;
	}

	private void clicked(MouseEvent e)
	{
		if (!editing && e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2)
			startEditing();
		else if (!editing && e.getButton() == MouseButton.SECONDARY)
			showContextMenu(e);
	}

	private TextField createTextField()
	{
		TextField textField = new TextField();
		textField.getStyleClass().add("m-text-field");
		textField.focusedProperty().addListener(this::focusChanged);
		textField.setOnKeyPressed(this::keyPressed);
		return textField;
	}
	private void focusChanged(ObservableValue<? extends Boolean> value, Boolean oldValue, Boolean newValue)
	{
		if (editing && !newValue)
			commitEditing();
	}
	private void keyPressed(KeyEvent e)
	{
		switch (e.getCode())
		{
			case ENTER:
				commitEditing();
				break;
			case ESCAPE:
				cancelEditing();
				break;
		}
	}
	public void startEditing()
	{
		if (editing) return;

		editing = true;
		if (textField == null)
		{
			textField = createTextField();
			setGraphic(textField);
		}

		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		textField.setText(text.get());
		textField.selectAll();
		textField.requestFocus();
	}
	public void commitEditing()
	{
		if (!editing) return;

		text.setValue(textField.getText());
		cancelEditing();
	}
	public void cancelEditing()
	{
		if (!editing) return;

		editing = false;
		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}


	private ContextMenu createContextMenu()
	{
		ContextMenu contextMenu = new ContextMenu();
		MenuItem menuItem = new MenuItem("Edit label");
		menuItem.setOnAction(e -> startEditing());
		contextMenu.getItems().add(menuItem);
		return contextMenu;
	}
	private void showContextMenu(MouseEvent e)
	{
		if (contextMenu == null)
			contextMenu = createContextMenu();

		contextMenu.show(this, e.getScreenX(), e.getScreenY());
	}
}
