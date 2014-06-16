package de.uni_augsburg.bazi.gui.mtable;

import de.uni_augsburg.bazi.gui.bind.BindingHelper;
import de.uni_augsburg.bazi.gui.bind.ReductionBinding;
import de.uni_augsburg.bazi.gui.view.EditableLabel;
import javafx.beans.binding.Binding;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.fxmisc.easybind.EasyBind;

public class MTableColumn<T, S> extends TableColumn<T, String>
{
	private final MTableColumnDefinition<T, S> definition;
	private ObservableList<ObservableValue<S>> attributes = null;

	public MTableColumn(MTableColumnDefinition<T, S> definition)
	{
		this.definition = definition;

		setSortable(false);
		setMinWidth(100);
		setCellFactory(c -> new MTableCell<>(definition.alignment()));
		setCellValueFactory(p -> definition.stringAttribute().apply(p.getValue()));

		EditableLabel tableHeader = new EditableLabel(definition.title());
		tableHeader.setPrefWidth(1000);
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(tableHeader);
		if (definition.op() != null) box.getChildren().add(createAggregation());
		setGraphic(box);
	}

	public void clear(T item)
	{
		System.out.println(item + ", " + definition.def());
		definition.attribute().apply(item).setValue(definition.def());
	}

	public ObservableList<ObservableValue<S>> attributes()
	{
		if (attributes == null)
		{
			Binding<ObservableList<T>> itemsBinding = EasyBind
				.select(tableViewProperty())
				.selectObject(TableView::itemsProperty);
			attributes = EasyBind.map(BindingHelper.listBinding(itemsBinding), definition.attribute());
		}
		return attributes;
	}

	private Label createAggregation()
	{
		Binding<S> sumBinding = new ReductionBinding<>(attributes(), definition.op(), definition.invOp());
		Binding<String> stringBinding = EasyBind.map(sumBinding, sum -> String.format("Σ=%s", sum));

		Label label = new Label();
		label.textProperty().bind(stringBinding);
		label.setMinWidth(Region.USE_PREF_SIZE);
		return label;
	}
}
