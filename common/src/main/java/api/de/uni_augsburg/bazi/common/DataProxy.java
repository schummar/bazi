package de.uni_augsburg.bazi.common;

import com.google.common.primitives.Primitives;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class DataProxy<I>
{
  private static final Map<Class<?>, DataProxy<?>> proxies = new HashMap<>();

  public static <I> DataProxy<I> of(Class<I> iClass)
  {
    @SuppressWarnings("unchecked")
    DataProxy<I> proxy = (DataProxy<I>) proxies.get(iClass);
    if (proxy == null) proxies.put(iClass, proxy = new DataProxy<>(iClass));
    return proxy;
  }


  private Class<I> iClass;
  private Class<?>[] superClasses = {};
  private String[] keys = {};
  private Class<?>[] types = {};

  public DataProxy(Class<I> iClass)
  {
    this.iClass = iClass;

    if (!iClass.isInterface()) throw new Exception(Error.IS_NO_INTERFACE);
  }

  public DataProxy<I> extend(Class<?>... superClasses)
  {
    for (Class<?> superClass : superClasses)
      if (!superClass.isAssignableFrom(iClass)) throw new Exception(Error.INVALID_SUPER_INTERFACE);

    this.superClasses = superClasses;
    return this;
  }

  public DataProxy<I> keys(String... keys)
  {
    Class<?>[] types = new Class[keys.length];
    for (int i = 0; i < keys.length; i++)
    {
      try
      {
        Method method = iClass.getMethod(keys[i]);
        if (Modifier.isStatic(method.getModifiers())
            || method.getParameterCount() > 0
            || method.getReturnType().isAssignableFrom(Void.class)
                )
          throw new Exception(Error.NO_FITTING_METHOD);

        types[i] = Primitives.wrap(method.getReturnType());

      } catch (NoSuchMethodException | SecurityException e)
      {
        throw new Exception(Error.NO_FITTING_METHOD);
      }
    }

    this.keys = keys;
    this.types = types;
    return this;
  }

  public I create(Object... values)
  {
    @SuppressWarnings("unchecked")
    I result = (I) Proxy.newProxyInstance(
            DataProxy.class.getClassLoader(),
            new Class<?>[]{iClass, Json.DefinesSerializer.class},
            new InvocationHandler(values)
    );
    return result;
  }

  private class InvocationHandler implements java.lang.reflect.InvocationHandler, Json.DefinesSerializer
  {
    private Object[] superInstances = new Object[superClasses.length];
    private Map<String, Object> data = new HashMap<>();

    private InvocationHandler(Object... values)
    {
      if (values.length < superClasses.length) throw new Exception(Error.MISSING_SUPER_INSTANCE);
      for (int i = 0; i < superInstances.length; i++)
      {
        if (!superClasses[i].isInstance(values[i])) throw new Exception(Error.INVALID_SUPER_INSTANCE);
        superInstances[i] = values[i];
      }

      for (int i = 0, j = superInstances.length; j < values.length; i++, j++)
      {
        if (keys.length <= i) throw new Exception(Error.TOO_MUCH_DATA);
        if (values[j] != null && !types[i].isInstance(values[j])) throw new Exception(Error.INVALID_DATA);
        data.put(keys[i], values[j]);
      }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
      if (method.getDeclaringClass().equals(Json.DefinesSerializer.class))
        return method.invoke(this, args);

      Object result = data.get(method.getName());
      if (result != null) return result;
      for (Object superInstance : superInstances)
        try
        {
          return method.invoke(superInstance);
        } catch (java.lang.Exception e) { }

      if (method.getName().equals("toString"))
        return toString();

      return null;
    }

    @Override
    public JsonElement serialize(JsonSerializationContext context)
    {
      JsonObject jo = new JsonObject();
      for (Object superInstance : superInstances)
      {

        JsonObject superJo = (JsonObject) context.serialize(superInstance);
        for (Map.Entry<String, JsonElement> entry : superJo.entrySet())
          jo.add(entry.getKey(), entry.getValue());
      }
      for (String key : keys)
        jo.add(key, context.serialize(data.get(key)));
      return jo;
    }

    @Override
    public String toString()
    {
      return Json.toJson(this);
    }
  }

  public static enum Error
  {
    IS_NO_INTERFACE, NO_FITTING_METHOD, INVALID_SUPER_INTERFACE, MISSING_SUPER_INSTANCE, INVALID_SUPER_INSTANCE, TOO_MUCH_DATA, INVALID_DATA
  }

  public static class Exception extends RuntimeException
  {
    private static final long serialVersionUID = 1L;
    private final Error error;

    public Exception(Error error)
    {
      this.error = error;
    }

    @Override
    public String getMessage()
    {
      return error.name();
    }

    public Error getError()
    {
      return error;
    }
  }
}
