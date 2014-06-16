package de.uni_augsburg.bazi.gui.mtable;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;

import java.util.function.Supplier;

public class MTable<T> extends TableView<T>
{
	private Supplier<T> supplier = null;
	private ObjectProperty<MTableCell<T>> selectedMCell = new SimpleObjectProperty<>();
	private ObjectProperty<MTableCell<T>> editingMCell = new SimpleObjectProperty<>();

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
	public MTableCell<T> getSelectedMCell()
	{
		return selectedMCell.get();
	}
	public void setSelectedMCell(MTableCell<T> selectedMCell)
	{
		this.selectedMCell.set(selectedMCell);
	}
	public ObjectProperty<MTableCell<T>> selectedMCellProperty()
	{
		return selectedMCell;
	}
	public MTableCell<T> getEditingMCell()
	{
		return editingMCell.get();
	}
	public void setEditingMCell(MTableCell<T> editingMCell)
	{
		this.editingMCell.set(editingMCell);
	}
	public ObjectProperty<MTableCell<T>> editingMCellProperty()
	{
		return editingMCell;
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
		getColumns().forEach(
			c -> {
				@SuppressWarnings("unchecked")
				MTableColumn<T, ?> col = (MTableColumn<T, ?>) c;
				col.clear(item);
			}
		);
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
		editSelected();
		getEditingMCell().overwrite(e.getCharacter());
	}
}
