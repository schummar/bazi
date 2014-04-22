package de.uni_augsburg.bazi.gui.mtable;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public class MTable<T> extends TableView<T>
{
	private Supplier<T> supplier;
	public MTable(Supplier<T> supplier)
	{
		this();
		this.supplier = supplier;
	}
	public MTable()
	{
		setEditable(true);
		getSelectionModel().setCellSelectionEnabled(true);
		addEventHandler(KeyEvent.KEY_PRESSED, this::pressed);
		addEventHandler(KeyEvent.KEY_TYPED, this::typed);
		addEventFilter(
			MouseEvent.MOUSE_DRAGGED, e -> {
				if (e.getTarget() instanceof Label)
					e.consume();
			}
		); // no column reordering
		getStylesheets().add("de/uni_augsburg/bazi/gui/mtable/m_table.css");
		setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
		setMinHeight(0);
	}


	public void addColumn(StringProperty name, Function<T, ObservableValue<String>> func)
	{
		TableColumn<T, String> col = new MTableColumn<>(name);
		col.setSortable(false);
		col.setMinWidth(100);
		col.setCellFactory(c -> new MTableCell<>());
		col.setCellValueFactory(p -> func.apply(p.getValue()));
		getColumns().add(col);
	}


	public int selectedRow()
	{
		return getSelectionModel().getSelectedCells().size() > 0
			? getSelectionModel().getSelectedCells().get(0).getRow() : -1;
	}

	public TableColumn<T, ?> selectedCol()
	{
		return getSelectionModel().getSelectedCells().size() > 0
			? getSelectionModel().getSelectedCells().get(0).getTableColumn()
			: (getColumns().size() > 0 ? getColumns().get(0) : null);
	}

	public void newRow(int row)
	{
		getItems().add(row, supplier.get());
		Platform.runLater(() -> getSelectionModel().select(row, selectedCol()));
	}

	public void selectNext()
	{
		if (selectedRow() == -1
			|| getSelectionModel().isSelected(getItems().size() - 1, getColumns().get(getColumns().size() - 1)))
			getSelectionModel().select(0, getColumns().get(0));
		else
			getSelectionModel().selectNext();
	}
	public void selectPrev()
	{
		if (selectedRow() == -1
			|| getSelectionModel().isSelected(getItems().size() - 1, getColumns().get(getColumns().size() - 1)))
			getSelectionModel().select(0, getColumns().get(0));
		else
			getSelectionModel().selectNext();
	}

	public void selectNextLine()
	{
		if (selectedRow() == getItems().size() - 1 || selectedRow() == -1)
			getSelectionModel().select(0, selectedCol());
		else
			getSelectionModel().selectBelowCell();
	}
	public void selectPrevLine()
	{
		if (selectedRow() == getItems().size() - 1 || selectedRow() == -1)
			getSelectionModel().select(0, selectedCol());
		else
			getSelectionModel().selectBelowCell();
	}

	public void editSelected()
	{
		edit(selectedRow(), selectedCol());
	}

	public void clearSelected()
	{
		typed = "";
		editSelected();
		edit(-1, null);
		typed = null;
	}

	public void deleteSelectedRow()
	{
		getItems().remove(selectedRow());
		if (selectedRow() < getItems().size() - 1) selectNextLine();
	}


	public void pressed(KeyEvent e)
	{
		switch (e.getCode())
		{
			case ENTER:
				if (e.isControlDown()) newRow(selectedRow() + 1);
				else editSelected();
				break;

			case TAB:
				selectNext();
				break;

			case DELETE:
				if (e.isControlDown()) deleteSelectedRow();
				else clearSelected();
				break;

			default:
				return;
		}
		e.consume();
	}


	public void typed(KeyEvent e)
	{
		if (e.getCharacter().trim().length() == 0) return;
		typed = e.getCharacter();
		editSelected();
		typed = null;
	}

	private String typed = null;
	public String typed() { return typed; }
}
