package de.uni_augsburg.bazi.cl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import de.uni_augsburg.bazi.common.Json;

import java.lang.reflect.Type;

@Json.Deserialize(Method.Deserializer.class)
public class Method
{
	public static class Deserializer implements JsonDeserializer<Method>
	{
		@Override
		public Method deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			return null;
		}
	}
}
