package de.uni_augsburg.bazi.gui.mtable;

import de.uni_augsburg.bazi.gui.bind.NestedBinding;
import javafx.beans.binding.ListBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.function.BinaryOperator;
import java.util.function.Function;

public class MTableColumn<T, S> extends TableColumn<T, S>
{
	public MTableColumn(
		StringProperty name,
		Function<T, ObservableValue<S>> extractor,
		Function<S, String> toStringConverter,
		Function<String, S> fromStringConverter,
		BinaryOperator<S> aggregator)
	{
		setSortable(false);
		setMinWidth(100);
		setCellFactory(c -> new MTableCell<>(toStringConverter, fromStringConverter));
		setCellValueFactory(p -> extractor.apply(p.getValue()));


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

				return aggregated == null ? ""
					: String.format("(%s)", aggregated);
			}
		};

		setGraphic(new MTableHeader(name));
		textProperty().bind(b);
	}
}
