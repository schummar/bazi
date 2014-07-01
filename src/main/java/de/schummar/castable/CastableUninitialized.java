package de.schummar.castable;

import javafx.beans.InvalidationListener;

import java.util.ArrayList;
import java.util.List;

public class CastableUninitialized implements Castable
{
	private Castable value = new Castable()
	{
		private final List<InvalidationListener> deepListeners = new ArrayList<>()
			,
			listeners = new ArrayList<>();
		@Override public Castable copy()
		{
			return this;
		}
		@Override public void merge(Castable castable)
		{
			sameType(castable).merge(castable);
		}
		@Override public void overwrite(Castable castable)
		{
			sameType(castable).overwrite(castable);
		}
		@Override public void addDeepListener(InvalidationListener invalidationListener)
		{
			deepListeners.add(invalidationListener);
		}
		@Override public void removeDeepListener(InvalidationListener invalidationListener)
		{
			deepListeners.remove(invalidationListener);
		}
		@Override public void addListener(InvalidationListener listener)
		{
			listeners.add(listener);
		}
		@Override public void removeListener(InvalidationListener listener)
		{
			listeners.remove(listener);
		}
		@Override public boolean isCastableString()
		{
			return true;
		}
		@Override public CastableString asCastableString()
		{
			return init(new CastableString());
		}
		@Override public boolean isCastableList()
		{
			return true;
		}
		@Override public CastableList asCastableList()
		{
			return init(new CastableList());
		}
		@Override public boolean isCastableObject()
		{
			return true;
		}
		@Override public CastableObject asCastableObject()
		{
			return init(new CastableObject());
		}
		private <T extends Castable> T init(T t)
		{
			value = t;
			deepListeners.forEach(t::addDeepListener);
			listeners.forEach(t::addListener);
			return t;
		}
		private Castable sameType(Castable c)
		{
			if (c instanceof CastableObject) return asCastableObject();
			if (c instanceof CastableList) return asCastableList();
			if (c instanceof CastableString) return asCastableString();
			throw new CastableNotInitialized();
		}
		@Override public String toString()
		{
			return "null";
		}
	};


	@Override public Castable copy()
	{
		return value.copy();
	}
	@Override public void merge(Castable castable)
	{
		value.merge(castable);
	}
	@Override public void overwrite(Castable castable)
	{
		value.overwrite(castable);
	}
	@Override public void addDeepListener(InvalidationListener invalidationListener)
	{
		value.addDeepListener(invalidationListener);
	}
	@Override public void removeDeepListener(InvalidationListener invalidationListener)
	{
		value.removeDeepListener(invalidationListener);
	}
	@Override public void addListener(InvalidationListener listener)
	{
		value.addListener(listener);
	}
	@Override public void removeListener(InvalidationListener listener)
	{
		value.removeListener(listener);
	}
	@Override public boolean isCastableString()
	{
		return value.isCastableString();
	}
	@Override public CastableString asCastableString()
	{
		return value.asCastableString();
	}
	@Override public boolean isCastableList()
	{
		return value.isCastableList();
	}
	@Override public CastableList asCastableList()
	{
		return value.asCastableList();
	}
	@Override public boolean isCastableObject()
	{
		return value.isCastableObject();
	}
	@Override public CastableObject asCastableObject()
	{
		return value.asCastableObject();
	}
	@Override public String toString()
	{
		return value.toString();
	}
	public static class CastableNotInitialized extends RuntimeException {}

	private static final Castable NOT_INITIALIZED = null;
}
