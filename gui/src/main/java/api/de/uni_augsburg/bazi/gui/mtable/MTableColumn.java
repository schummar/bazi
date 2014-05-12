package de.uni_augsburg.bazi.gui.mtable;

import de.uni_augsburg.bazi.gui.bind.NestedBinding;
import de.uni_augsburg.bazi.gui.view.EditableLabel;
import javafx.beans.binding.ListBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.function.BinaryOperator;
import java.util.function.Function;

public class MTableColumn<T, S> extends TableColumn<T, S>
{
	public MTableColumn(
		ObservableStringValue name,
		Function<T, ObservableValue<S>> extractor,
		Function<S, String> toStringConverter,
		Function<String, S> fromStringConverter,
		BinaryOperator<S> aggregator,
		Pos alignment
	)
	{
		setSortable(false);
		setMinWidth(100);
		setCellFactory(c -> new MTableCell<>(toStringConverter, fromStringConverter, alignment));
		setCellValueFactory(p -> extractor.apply(p.getValue()));


		EditableLabel tableHeader = new EditableLabel(name);
		tableHeader.setPrefWidth(1000);
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(tableHeader);
		if (aggregator != null) box.getChildren().add(createAggregation(extractor, aggregator));
		setGraphic(box);
	}

	private Label createAggregation(Function<T, ObservableValue<S>> extractor, BinaryOperator<S> aggregator)
	{
		ListBinding<ObservableValue<S>> attributes = NestedBinding.of(tableViewProperty())
			.listProperty(TableView::itemsProperty)
			.map(extractor);

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
					.reduce(aggregator)
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
