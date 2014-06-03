package de.schummar.castable;

import javafx.beans.property.Property;

import java.util.List;

public interface Party extends Data
{
	@Attribute() Property<String> nameProperty();
	public default String name() { return nameProperty().getValue(); }
	public default void name(String name) { nameProperty().setValue(name); }

	@Attribute() Property<Double> voteProperty();
	public default Double vote() { return voteProperty().getValue(); }
	public default void vote(Double vote) { voteProperty().setValue(vote); }

	@Attribute() Property<Integer> seatProperty();
	public default Integer seat() { return seatProperty().getValue(); }
	public default void seat(Integer seat) { seatProperty().setValue(seat); }

	@Attribute() List<? extends Party> parties();
	@Attribute() List<String> tests();

	@Attribute() Party sub();
}


