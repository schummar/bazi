package de.uni_augsburg.bazi.gui.mtable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class MTableCell<T> extends TableCell<T, String>
{
	private TextField textField = null;

	public MTableCell()
	{
		ChangeListener<Boolean> c = this::updateSelected;
		selectedProperty().addListener((ChangeListener<Boolean>)this::updateSelected);
		editingProperty().addListener(this::updateEditing);

	}
	private void updateSelected(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue)
	{
		if (!oldValue && newValue) getTable().setSelectedMCell(this);
		if (oldValue && !newValue) getTable().setSelectedMCell(null);
	}
	private void updateEditing(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue)
	{
		if (!oldValue && newValue) getTable().setEditingMCell(this);
		if (oldValue && !newValue) getTable().setEditingMCell(null);
	}


	public MTable<T> getTable()
	{
		return (MTable<T>) getTableView();
	}

	public void overwrite(String item)
	{
		setItem(item);
		updateView();
		if (isEditing())
			textField.end();
	}

	@Override public void startEdit()
	{
		System.out.println("edit");
		if (!isEditable()
			|| !getTableView().isEditable()
			|| !getTableColumn().isEditable())
		{
			return;
		}
		super.startEdit();
		updateView();

		//getTable().setEditingMCell(this);
		textField.requestFocus();
		textField.selectAll();
	}

	public void commitEdit()
	{
		System.out.println("commit: " + textField.getText());
		commitEdit(textField.getText());
		updateView();
		getTableView().requestFocus();
	}


	@Override public void cancelEdit()
	{
		System.out.println("cancel");
		commitEdit();
	}

	@Override public void updateItem(String item, boolean empty)
	{
		super.updateItem(item, empty);
		updateView();
	}

	private void updateView()
	{
		if (isEditing())
		{
			if (textField == null) textField = createTextField();
			setText(null);
			setGraphic(textField);
			textField.setText(getItem());
		}
		else
		{
			setText(getItem());
			setGraphic(null);
		}
	}


	private TextField createTextField()
	{
		TextField textField = new TextField();
		textField.getStyleClass().add("m-text-field");
		textField.setOnKeyPressed(this::keyPressed);
		textField.focusedProperty().addListener(this::focusChanged);
		return textField;
	}
	private void keyPressed(KeyEvent e)
	{
		switch (e.getCode())
		{
			case ENTER:
				commitEdit();
				if (e.isControlDown())
					getTable().newRow(getTableRow().getIndex() + 1);
				else
					getTable().selectNextLine();
				break;

			case UP:
				if (!e.isControlDown()) return;
				commitEdit();
				getTable().selectPrevLine();
				break;

			case DOWN:
				if (!e.isControlDown()) return;
				commitEdit();
				getTable().selectNextLine();
				break;

			case LEFT:
				if (!e.isControlDown()) return;
				commitEdit();
				getTable().selectPrev();
				break;

			case RIGHT:
				if (!e.isControlDown()) return;
				commitEdit();
				getTable().selectNext();
				break;

			case ESCAPE:
				super.cancelEdit();
				updateView();
				break;

			case TAB:
				commitEdit();
				getTable().selectNext();
				break;

			default:
				return;
		}
		e.consume();
	}

	private void focusChanged(ObservableValue<? extends Boolean> v, Boolean o, Boolean n)
	{
		if (!n) cancelEdit();
	}
}
