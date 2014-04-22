package de.uni_augsburg.bazi.gui.mtable;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class MTableHeader extends Label
{
	private final StringProperty text;
	private boolean editing = false;
	private TextField textField;

	public MTableHeader(StringProperty text)
	{
		this.text = text;
		textProperty().bind(text);
		setOnMouseClicked(this::clicked);
	}

	private void clicked(MouseEvent e)
	{
		if (!editing && e.getClickCount() == 2)
			startEditing();
	}

	private TextField createTextField()
	{
		TextField textField = new TextField();
		textField.getStyleClass().add("m-text-field");
		//textField.setAlignment(Pos.CENTER);
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
		if (textField == null) textField = createTextField();

		textProperty().unbind();
		textField.setText(text.getValue());
		setText("");
		setGraphic(textField);
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
		textProperty().bind(text);
		setGraphic(null);
	}
}
