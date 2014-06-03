package de.uni_augsburg.bazi.common.algorithm;

import de.schummar.castable.Attribute;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Real;
import javafx.beans.property.Property;

import java.util.List;

/** A minimal vector input interface. */
public interface VectorData extends Data
{
	/**
	 * The name of the apportionment.
	 * @return the name of the apportionment.
	 */
	@Attribute Property<String> nameProperty();
	default String name() { return nameProperty().getValue(); }
	default void name(String v) { nameProperty().setValue(v); }

	/**
	 * The number of seats to be apportioned.
	 * @return the number of seats to be distributed.
	 */
	@Attribute Property<Int> seatsProperty();
	default Int seats() { return seatsProperty().getValue(); }
	default void seats(Int v) { seatsProperty().setValue(v); }

	/**
	 * The list of parties the seats shall be apportioned among.
	 * @return the list of parties the seats shall be apportioned among.
	 */
	@Attribute List<? extends Party> parties();


	/** An input party. */
	public interface Party extends Data
	{
		/**
		 * The name of the party.
		 * @return the name of the party.
		 */
		@Attribute Property<String> namePropery();
		default String name() { return namePropery().getValue(); }
		default void name(String v) { namePropery().setValue(v); }

		/**
		 * The votes of the party. Can be any non-negative real number.
		 * @return the votes of the party.
		 */
		@Attribute Property<Real> votesPropery();
		default Real votes() { return votesPropery().getValue(); }
		default void votes(Real v) { votesPropery().setValue(v); }

		/**
		 * The minimum number of seats the party shall receive.
		 * @return the minimum number of seats the party shall receive.
		 */
		@Attribute Property<Int> minPropery();
		default Int min() { return minPropery().getValue(); }
		default void min(Int v) { minPropery().setValue(v); }

		/**
		 * The maximum number of seats the party shall receive.
		 * @return the maximum number of seats the party shall receive.
		 */
		@Attribute(def = "oo") Property<Int> maxPropery();
		default Int max() { return maxPropery().getValue(); }
		default void max(Int v) { maxPropery().setValue(v); }

		@Attribute Property<Int> seatsPropery();
		default Int seats() { return seatsPropery().getValue(); }
		default void seats(Int v) { seatsPropery().setValue(v); }

		@Attribute Property<Uniqueness> uniquenessProperty();
		default Uniqueness uniqueness() { return uniquenessProperty().getValue(); }
		default void uniqueness(Uniqueness v) { uniquenessProperty().setValue(v); }

		@Attribute Property<Boolean> conditionUsedProperty();
		default Boolean conditionUsed() { return conditionUsedProperty().getValue(); }
		default void conditionUsed(Boolean v) { conditionUsedProperty().setValue(v); }
	}
}
