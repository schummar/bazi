package de.uni_augsburg.bazi.gui.view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class NumberCheckbox extends CheckBox
{
	private static final int size = 10;

	private ObservableList<NumberCheckbox> selection = FXCollections.observableArrayList();
	private final Label label = new Label();

	public NumberCheckbox(String text, ObservableList<NumberCheckbox> selection)
	{
		super(text);
		getStylesheets().add(NumberCheckbox.class.getResource("number_checkbox.css").toString());
		label.getStyleClass().add("number_mark");

		Platform.runLater(
			() -> {
				StackPane mark = new StackPane();
				mark.setMinSize(size, size);
				mark.setMaxSize(size, size);
				mark.getChildren().add(label);
				StackPane box = (StackPane) lookup(".box");
				box.getChildren().setAll(mark);
			}
		);

		selectedProperty().addListener(selectedListener);
		selection.addListener(selectionListener);
	}

	public NumberCheckbox()
	{
		this("", FXCollections.observableArrayList());
	}


	private void setNumber(int i)
	{
		label.setText(Integer.toString(i));
	}

	private final ChangeListener<Boolean> selectedListener = (o, old, New) -> {
		if (!old && New) selection.add(this);
		else if (old && !New) selection.remove(this);
	};

	private final ListChangeListener<NumberCheckbox> selectionListener = change -> {
		int index = selection.indexOf(this);
		setSelected(index >= 0);
		setNumber(index + 1);
	};

	public ObservableList<NumberCheckbox> getSelection()
	{
		return selection;
	}
	public void setSelection(ObservableList<NumberCheckbox> selection)
	{
		this.selection.remove(selectionListener);
		this.selection = selection;
		this.selection.addListener(selectionListener);
	}
}
