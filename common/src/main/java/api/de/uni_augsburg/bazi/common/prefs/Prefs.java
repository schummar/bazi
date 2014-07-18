package de.uni_augsburg.bazi.common.prefs;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Locale;
import java.util.prefs.Preferences;

public class Prefs
{
	private static final String LOCALE = "locale";
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage(Prefs.class);

	private static Multimap<String, Listener<?>> listeners = ArrayListMultimap.create();

	private static <T> void onChange(String key, T old, T New)
	{
		new ArrayList<>(listeners.get(key)).forEach(
			l -> {
				@SuppressWarnings("unchecked")
				Listener<T> lt = (Listener<T>) l;
				lt.changed(old, New);
			}
		);
	}

	public static Locale locale()
	{
		String l = PREFERENCES.get(LOCALE, null);
		return l != null ? Locale.forLanguageTag(l) : Locale.getDefault();
	}
	public static void setLocale(Locale locale)
	{
		Locale old = locale();
		PREFERENCES.put(LOCALE, locale.toLanguageTag());
		onChange(LOCALE, old, locale());
	}
	public static synchronized void addOnLocaleChange(Listener<Locale> listener)
	{
		listeners.put(LOCALE, listener);
	}
	public static synchronized void removeOnLocaleChange(Listener<Locale> listener)
	{
		listeners.remove(LOCALE, listener);
	}


	public interface Listener<T>
	{
		void changed(T old, T New);
	}
}
