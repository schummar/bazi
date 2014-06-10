package de.uni_augsburg.bazi.gui.mtable;

import de.uni_augsburg.bazi.gui.view.EditableLabel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class MTableColumn<T, S> extends TableColumn<T, String>
{
	public MTableColumn(MTableColumnDefinition<T, S> definition)
	{
		setSortable(false);
		setMinWidth(100);
		setCellFactory(c -> new MTableCell(definition.alignment()));
		setCellValueFactory(p -> definition.attribute().apply(p.getValue()).asStringProperty());


		EditableLabel tableHeader = new EditableLabel(definition.title());
		tableHeader.setPrefWidth(1000);
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(tableHeader);
		if (definition.op() != null) box.getChildren().add(sum(definition));
		setGraphic(box);
	}

	private Label sum(MTableColumnDefinition<T, S> definition)
	{
	/*	ListBinding<ObservableValue<S>> attributes = NestedBinding.of(tableViewProperty())
			.listProperty(TableView::itemsProperty)
			.map(attribute.extractor());




		StringBinding b = new StringBinding()
		{
			{
				bind(attributes);
				attributes.forEach(this::bind);
				attributes.addListener(
					(ListChangeListener<ObservableValue<S>>) change -> {
						while (change.next())
						{
							change.getAddedSubList().forEach(this::bind);
							change.getRemoved().forEach(this::unbind);
						}
					}
				);
			}
			@Override protected String computeValue()
			{
				S aggregated = attributes.stream()
					.map(ObservableValue::getValue)
					.filter(o -> o != null)
					.reduce(attribute.addition())
					.orElse(null);

				return String.format("Σ=%s", aggregated != null ? aggregated : "");
			}
		};*/

		ObjectBinding<ObservableList<ObservableValue<S>>> values = Bindings.select(
			tableViewProperty(), "items"
		);
		values.addListener((observable, oldValue, newValue) -> System.out.println(newValue));


		Label label = new Label();
//		label.textProperty().bind(b);
		label.setMinWidth(Region.USE_PREF_SIZE);
		return label;
	}
}
