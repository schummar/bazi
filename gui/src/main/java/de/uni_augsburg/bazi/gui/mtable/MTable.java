package de.uni_augsburg.bazi.gui.mtable;

import javafx.application.Platform;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;

import java.util.function.Supplier;

public class MTable<T> extends TableView<T>
{
	private Supplier<T> supplier = null;
	private String startedTyping = null;

	public MTable()
	{
		setEditable(true);
		getSelectionModel().setCellSelectionEnabled(true);
		setOnKeyPressed(this::keyPressed);
		setOnKeyTyped(this::keyTyped);
		getStylesheets().add("de/uni_augsburg/bazi/gui/mtable/m_table.css");
		setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
		setMinHeight(0);
	}
	public Supplier<T> getSupplier()
	{
		return supplier;
	}
	public void setSupplier(Supplier<T> supplier)
	{
		this.supplier = supplier;
	}
	public String startedTyping()
	{
		return startedTyping;
	}


	public <S> void addColumn(MTableColumnDefinition<T, S> definition)
	{
		getColumns().add(definition.createColumn());
	}


	public int selectedRow()
	{
		return getSelectionModel().getSelectedCells().size() > 0
			? getSelectionModel().getSelectedCells().get(0).getRow() : -1;
	}
	public TableColumn<T, ?> selectedCol()
	{
		return getSelectionModel().getSelectedCells().size() > 0
			? getColumns().get(getSelectionModel().getSelectedCells().get(0).getColumn())
			: (getColumns().size() > 0 ? getColumns().get(0) : null);
	}
	public void newRow(int row)
	{
		T item = supplier.get();
		getItems().add(row, item);
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
			|| getSelectionModel().isSelected(0, getColumns().get(0)))
			getSelectionModel().select(0, getColumns().get(0));
		else
			getSelectionModel().selectPrevious();
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
		T item = getSelectionModel().getSelectedItem();
		@SuppressWarnings("unchecked")
		MTableColumn<T, ?> col = (MTableColumn<T, ?>) selectedCol();
		col.clear(item);
	}
	public void deleteSelectedRow()
	{
		getItems().remove(selectedRow());
		if (selectedRow() < getItems().size() - 1) selectNextLine();
	}
	public void keyPressed(KeyEvent e)
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
	public void keyTyped(KeyEvent e)
	{
		if (e.getCharacter().trim().length() == 0) return;
		startedTyping = e.getCharacter();
		editSelected();
		startedTyping = null;
	}
}
