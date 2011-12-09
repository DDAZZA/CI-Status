public class Build
{
	public String name;
	public int condition;
	public String status;

	Build(String n, int con, String state)
	{
		name = n;
		condition = con;
		status = state;
	}

	public String getCondition(){return null;}
}
