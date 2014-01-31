package de.uni_augsburg.bazi.cl;

import com.google.common.collect.ImmutableList;
import de.uni_augsburg.bazi.common.Json;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropInput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class BaziFile
{
	public static BaziFile load(Path path)
	{
		try
		{
			String content = new String(Files.readAllBytes(path));
			return load(content);
		}
		catch (IOException e) {}

		return null;
	}

	public static BaziFile load(String content)
	{
		return Json.fromJson(content, BaziFile.class);
	}


	private List<Method> methods = new ArrayList<>();
	public List<String> seats = new ArrayList<>();
	public List<? extends Party> parties = new ArrayList<>();

	public BaziFile() { }

	public BaziFile(List<Method> methods, List<String> seats, List<? extends Party> parties)
	{
		this.methods = methods;
		this.seats = seats;
		this.parties = parties;
	}

	public List<String> seats()
	{
		return seats;
	}

	public List<? extends Party> parties()
	{
		return ImmutableList.copyOf(parties);
	}


	public static class Party implements MonopropInput.Party
	{
		public String name = "";
		public Rational votes = BMath.ZERO;
		public Int min = BMath.ZERO, max = BMath.INF, dir = BMath.ZERO;
		public List<Party> apparentment = new ArrayList<>();

		public Party() { }

		public Party(String name, Rational votes, Int min, Int max, Int dir, List<Party> apparentment)
		{
			this.name = name;
			this.votes = votes;
			this.min = min;
			this.max = max;
			this.dir = dir;
			this.apparentment = apparentment;
		}

		public String name()
		{
			return name;
		}

		public Rational votes()
		{
			return votes;
		}

		public Int min()
		{
			return min;
		}

		public Int max()
		{
			return max;
		}

		public Int dir()
		{
			return dir;
		}

		public List<? extends MonopropInput.Party> apparentment()
		{
			return null;
		}
	}
}
