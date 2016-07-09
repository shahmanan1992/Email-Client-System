package Content;

import java.util.HashMap;

public class Sample {
	int i=1;
	HashMap<String,String> received=new HashMap<String,String>();
	HashMap<String,String> sent=new HashMap<String,String>();
	public Sample()
	{
		received.put("Manan", "Shah");
		received.put("Jinali", "Gala");
		sent.put("Jyoti", "Shah");
		sent.put("Jagdish", "Shah");
	}
	public int getI()
	{
		return i;
	}
	
	public HashMap<String,String> getReceived()
	{
		return received;
	}
	
	public HashMap<String,String> getSent()
	{
		return sent;
	}
}
