import java.io.IOException;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Scraper {

	final static int BLUE = 1;
	final static int RED = 2;
	final static int GREY = 3;
	
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
		if(s.contains("blue"))
		{
			return BLUE;
		}
		else if(s.contains("red"))
		{
			return RED;
		}
		else if(s.contains("grey"))
		{
			return GREY;
		}
		return -1;
	}
}
