import java.io.IOException;
import java.util.Vector;

public class CiStatus
{

	final String jenkins_addr = "http://ci.jenkins-ci.org/view/Infrastructure/";
	final int update_delay = 20000;
	
	
	Vector<Build> builds;
	Scraper scraper;
	IconManager im;
	boolean should_notify = false;
	
	public static void main (String [] args) throws Exception 
	{
		CiStatus ci_status = new CiStatus();
		//TODO parse params
	}
	
	CiStatus() throws IOException, InterruptedException
	{
		should_notify = System.getProperty("os.name").equals("Linux");
		System.out.println("Should show notifications: " + should_notify);
		
		scraper = new Scraper();		
		builds = getBuilds();
		System.out.println("Found " + builds.size() + " builds.");
		im = new IconManager(builds);
		
		while(true)
		{
			update();
			Thread.sleep(update_delay);
		}
	}
	
	private Vector<String> getBuildDiff(Vector<Build> newBuilds) throws IOException
	{
		Vector<String> buildDiffs = new Vector<String>();	
		for(Build b : builds)
		{
			for (Build nb :newBuilds)
			{
				if (b.name.equals(nb.name) && b.condition != nb.condition)
				{
					String msg = nb.name + " has changed from " + b.status + " to " + nb.status;
					System.out.println(msg);
					buildDiffs.add(msg);
				}
			}
		}
		return buildDiffs;
	}
	
	private Vector<Build> getBuilds() throws IOException
	{
		System.out.println("fetching builds...");
		return scraper.getBuilds(jenkins_addr);
	}

	private void displayBuilds(Vector<Build> newBuilds)
	{
		im.updateIcons(newBuilds);
	}

	private void displayChanges(Vector<String> buildDiffs) throws InterruptedException, IOException {
		Runtime rt = Runtime.getRuntime();
		String notifications = "";
		for (String s :buildDiffs)
		{
			notifications += s + "\n";
		}
		if (buildDiffs.size() > 0)
		{
			Process p = rt.exec("notify-send Jenkins " + notifications);
			p.waitFor();
		}
	}
	
	private void update() throws IOException, InterruptedException
	{
		Vector<Build> newBuilds = getBuilds(); 
		scraper.printBuilds(builds);
		displayBuilds(newBuilds);
		
		if (should_notify)
		displayChanges(getBuildDiff(newBuilds));
	}
}