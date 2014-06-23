package de.schummar.castable;

import java.util.Collection;

public class DataList<T extends Data> extends CList<T>
{
	protected final Class<T> type;
	public DataList(CastableList list, Converter<T> converter, Class<T> type)
	{
		super(list, converter);
		this.type = type;
	}

	public boolean setAllData(Collection<? extends Data> c)
	{
		boolean empty = isEmpty();
		clear();
		return addAllData(c) || !empty;
	}
	@Override public T set(int index, Data element)
	{
		return converter.unpack(list.set(index, converter.pack(element == null ? null : element.cast(type))));
	}
	@Override public boolean add(Data element)
	{
		return list.add(converter.pack(element == null ? null : element.cast(type)));
	}
	@Override public void add(int index, Data element)
	{
		list.add(index, converter.pack(element == null ? null : element.cast(type)));
	}
	public boolean addAllData(int index, Collection<? extends Data> c)
	{
		for (Data element : c) add(index++, element);
		return !c.isEmpty();
	}
	public boolean addAllData(Collection<? extends Data> c)
	{
		boolean changed = false;
		for (Data element : c) changed |= add(element);
		return changed;
	}
	@Override public T remove(int index)
	{
		return converter.unpack(list.remove(index));
	}
}
