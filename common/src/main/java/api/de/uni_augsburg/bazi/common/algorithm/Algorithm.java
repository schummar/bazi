package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Data;
import de.uni_augsburg.bazi.common.PluginManager;

import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public interface Algorithm<I extends Data, O extends Data>
{
	public static Algorithm<?, ?> create(String name, Data params, PluginManager manager)
	{
		@SuppressWarnings("unchecked")
		Algorithm<?, ?> algorithm = manager.create(
			AlgorithmPlugin.class, p -> p.createAlgoritm(name, params.cast(p.getParamsInterface()))
		);
		return algorithm;
	}


	public Class<I> getInputInterface();
	public List<Class<? extends Data>> getAllInputInterfaces();
	public O apply(I in);
	public default O applyCast(Data in) { return apply(in.cast(getInputInterface())); }
}
