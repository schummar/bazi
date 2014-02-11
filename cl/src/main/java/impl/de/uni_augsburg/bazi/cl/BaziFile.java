package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.biprop.BipropInput;
import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Int;
import de.uni_augsburg.bazi.math.Rational;
import de.uni_augsburg.bazi.monoprop.MonopropInput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class BaziFile implements BipropInput
{
	public static BaziFile load(Path path, Format format)
	{
		try
		{
			String content = new String(Files.readAllBytes(path));
			return load(content, format);
		}
		catch (IOException e) {}

		return null;
	}

	public static BaziFile load(String content, Format format)
	{
		return format.deserialize(content, BaziFile.class);
	}

	public Algorithm algorithm = null;
	public List<BasicMethod> methods = new ArrayList<>();

	public List<Interval> seats = new ArrayList<>();
	public List<Party> parties = new ArrayList<>();
	public List<District> districts = new ArrayList<>();

	@Override public List<? extends District> districts() { return districts; }


	public static class Party implements MonopropInput.Party
	{
		public String name = "";
		public Rational votes = BMath.ZERO;
		public Int min = BMath.ZERO, max = BMath.INF, dir = BMath.ZERO;
		public List<Party> apparentment = new ArrayList<>();

		@Override public String name() { return name; }
		@Override public Rational votes() { return votes; }
		@Override public Int min() { return min; }
		@Override public Int max() { return max; }
		@Override public Int dir() { return dir; }
		@Override public List<? extends MonopropInput.Party> parties() { return apparentment; }
	}


	public static class District implements BipropInput.District
	{
		public String name = "";
		public Int seats = BMath.ZERO;
		public List<BaziFile.Party> parties = new ArrayList<>();

		@Override public String name() { return name; }
		@Override public Int seats() { return seats; }
		@Override public Collection<? extends Party> parties() { return parties; }
	}
}
