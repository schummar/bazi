package de.uni_augsburg.bazi.gui.mtable;

import de.uni_augsburg.bazi.gui.bind.NestedBinding;
import de.uni_augsburg.bazi.gui.view.EditableLabel;
import javafx.beans.binding.ListBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class MTableColumn<T, S> extends TableColumn<T, S>
{
	public MTableColumn(
		ObservableObjectValue<String> name,
		MTableAttribute<T, S> attribute,
		Pos alignment
	)
	{
		setSortable(false);
		setMinWidth(100);
		setCellFactory(c -> new MTableCell<>(attribute, alignment));
		setCellValueFactory(p -> attribute.extractor().apply(p.getValue()));


		EditableLabel tableHeader = new EditableLabel(name);
		tableHeader.setPrefWidth(1000);
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(tableHeader);
		if (attribute.addition() != null) box.getChildren().add(createAggregation(attribute));
		setGraphic(box);
	}

	private Label createAggregation(MTableAttribute<T, S> attribute)
	{
		ListBinding<ObservableValue<S>> attributes = NestedBinding.of(tableViewProperty())
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
		};

		Label label = new Label();
		label.textProperty().bind(b);
		label.setMinWidth(Region.USE_PREF_SIZE);
		return label;
	}
}
