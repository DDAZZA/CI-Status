import java.io.IOException;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Scraper {

	private final static int GREY = 0;
	private static final int YELLOW = -1;
	private final static int BLUE = 1;
	private static final int BLUE_ANIME = 2;
	private final static int RED = 3;
	private static final int RED_ANIME = 4;
	
	public static void printBuilds(Vector<Build> builds)
	{
		for(Build b: builds)
		{
			System.out.println("Name: " + b.name);
			System.out.println("Condition: " + b.condition);
			System.out.println("Status: " + b.status);
			System.out.println("");
		}
	}



	public Vector<Build> getBuilds(String jenkinsAddr) throws IOException
	{
		org.jsoup.nodes.Document doc = Jsoup.connect(jenkinsAddr).get();
		Elements imgsrc = doc.select(".dashboard #projectstatus tbody tr [data] [tooltip]");
		Elements buildNames = doc.select(".dashboard #projectstatus tbody tr td:gt(1):lt(3) a[href] ");
		Object[] buildnames = buildNames.toArray();
		Object[] imagesrcs = imgsrc.toArray();

		Vector<Build> builds = new Vector<Build>();
		for(int i=0;i<buildnames.length;i++)
		{
			builds.add(new Build(stripName(buildnames[i]), stripCondition(imagesrcs[i]), stripState(imagesrcs[i])) ); 
		}
		return builds;
	}

	private static String stripName(Object o)
	{
		String s = o.toString();
		int start = s.indexOf('>');
		int finish = s.indexOf('<',start+1);

		return s.substring(start+1, finish);
	}

	private static String stripState(Object o)
	{
		String s = o.toString();
		int start = s.indexOf('"');
		int finish = s.indexOf('"', start+1);

		return s.substring(start+1, finish);
	}

	private static int stripCondition (Object o)
	{
		String s = o.toString();
		if(s.contains("blue.png"))
		{
			return BLUE;
		}
		else if(s.contains("blue_anime.gif"))
		{
			return BLUE_ANIME;
		}
		else if(s.contains("red.png"))
		{
			return RED;
		}
		else if(s.contains("red_anime.gif"))
		{
			return RED_ANIME;
		}
		else if(s.contains("yellow.png"))
		{
			return YELLOW;
		}
		else if(s.contains("grey.png"))
		{
			return GREY;
		}
		return -1;
	}
}
