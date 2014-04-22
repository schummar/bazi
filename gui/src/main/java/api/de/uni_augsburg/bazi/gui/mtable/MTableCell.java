package de.uni_augsburg.bazi.gui.mtable;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.function.Consumer;
import java.util.function.Function;

public class MTableCell<T> extends TableCell<T, String>
{
	private TextField textField = null;

	public MTableCell()
	{
		//getStyleClass().add("m-table-cell");
	}


	private void ifMTable(Consumer<MTable> consumer)
	{
		if (getTableView() instanceof MTable<?>)
			consumer.accept((MTable) getTableView());
	}

	private <S> S ifMTableGet(Function<MTable, S> function)
	{
		if (getTableView() instanceof MTable<?>)
			return function.apply((MTable) getTableView());
		return null;
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

		String typed = ifMTableGet(MTable::typed);
		textField.deselect();
		textField.requestFocus();
		if (typed != null)
		{
			textField.setText(typed);
			textField.end();
		}
		else
			textField.selectAll();
	}

	public void commitEdit()
	{
		commitEdit(textField.getText());
		updateView();
	}


	@Override public void cancelEdit()
	{
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
		return textField;
	}
	private void keyPressed(KeyEvent e)
	{
		switch (e.getCode())
		{
			case ENTER:
				commitEdit();
				if (e.isControlDown() || getTableRow().getIndex() == getTableView().getItems().size() - 1)
					ifMTable(t -> t.newRow(getTableRow().getIndex() + 1));
				else
					ifMTable(MTable::selectNextLine);
				break;

			case UP:
				if (e.isControlDown())
				{
					commitEdit();
					ifMTable(MTable::selectPrevLine);
				}
				break;

			case DOWN:
				if (e.isControlDown())
				{
					commitEdit();
					ifMTable(MTable::selectNextLine);
				}
				break;

			case LEFT:
				if (e.isControlDown())
				{
					commitEdit();
					ifMTable(MTable::selectPrev);
				}
				break;

			case RIGHT:
				if (e.isControlDown())
				{
					commitEdit();
					ifMTable(MTable::selectNext);
				}
				break;

			case ESCAPE:
				super.cancelEdit();
				updateView();
				break;

			case TAB:
				commitEdit();
				ifMTable(MTable::selectNext);

			default:
		}
	}
}
