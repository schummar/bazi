package de.uni_augsburg.bazi.gui.mtable;

import de.schummar.castable.CProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.function.Function;

public class MTableCell<T> extends TableCell<T, String>
{
	private TextField textField = null;

	public MTableCell(Pos alignment)
	{

		selectedProperty().addListener((ChangeListener<Boolean>) this::updateSelected);
		editingProperty().addListener(this::updateEditing);
		setAlignment(alignment);

	}
	private void updateSelected(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue)
	{
		if (newValue) getTable().setSelectedMCell(this);
	}
	private void updateEditing(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue)
	{
		if (newValue) getTable().setEditingMCell(this);
	}


	public MTable<T> getTable()
	{
		return (MTable<T>) getTableView();
	}

	public void overwrite(String item)
	{
		if (isEditing())
		{
			textField.setText(item);
			textField.end();
		}
		else
		{
			setItem(attribute.deserializer().apply(item));
			updateView();
		}
	}

	@Override public void startEdit()
	{
		if (!isEditable()
			|| !getTableView().isEditable()
			|| !getTableColumn().isEditable())
		{
			return;
		}
		super.startEdit();
		updateView();

		textField.requestFocus();
		textField.selectAll();
	}

	public void commitEdit()
	{
		try
		{
			commitEdit(attribute.deserializer().apply(textField.getText()));
		}
		catch (Exception e)
		{
			commitEdit(attribute.deserializer().apply(null));
		}
		updateView();
		getTableView().requestFocus();
	}


	@Override public void cancelEdit()
	{
		commitEdit();
	}

	@Override public void updateItem(S item, boolean empty)
	{
		super.updateItem(item, empty);
		updateView();
	}

	private void updateView()
	{
		if (isEditing())
		{
			if (textField == null)
			{
				textField = createTextField();
				setGraphic(textField);
			}
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			textField.setText(attribute.serializer().apply(getItem()));
		}
		else
		{
			setContentDisplay(ContentDisplay.TEXT_ONLY);
			setText(attribute.serializer().apply(getItem()));
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
