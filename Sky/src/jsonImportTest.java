import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class jsonImportTest {

	@Test
	public void test() throws FileNotFoundException, IOException, ParseException {
		JSONArray result = jsonImport.getJsonFile();
		if(result.equals(null) || result.isEmpty())
			fail();
	}
	
	@Test
	public void testgetBillTotal()
	{
		jsonImport.getBillTotal(2);
	}

	@SuppressWarnings("unchecked")
	private JSONObject callInfoJson()
	{
		JSONArray calls = new JSONArray();
		JSONObject called = new JSONObject();
		called.put("called", "07716393769");
		called.put("duration", "00:23:03");
		called.put("cost", new Double(2.15));
		
		calls.add(called);
		
		System.out.println(calls.size());
		
			JSONObject obj = new JSONObject();
			obj.put("total", new Double(2.0));
			obj.put("calls", calls);
			
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject statementJson()
	{
		JSONObject statement = new JSONObject();
		statement.put("generated", "2015-01-01");
		statement.put("due", "2015-01-01");
			JSONObject period = new JSONObject();
			period.put("from", "2015-01-01");
			period.put("to", "2015-01-01");
		statement.put("period", period);
		return statement;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject packageJson()
	{
		JSONObject packageJson = new JSONObject();
			JSONArray subscriptions = new JSONArray();
			JSONObject obj = new JSONObject();
			obj.put("type", "Telly");
			obj.put("name", "Info");
			obj.put("cost", new Double(50.00));
			
			subscriptions.add(obj);
		
			packageJson.put("subscriptions", subscriptions);
			packageJson.put("total", new Double(50.0));
			
			System.out.println(packageJson);
		return packageJson;
	}
	@SuppressWarnings("unchecked")
	private JSONObject skyStore()
	{
		JSONObject skyStore = new JSONObject();
		JSONArray rentals = new JSONArray();
		JSONObject r = new JSONObject();
		r.put("title", "Something");
		r.put("cost", new Double(25.00));
		JSONArray buyAndKeep = new JSONArray();
		JSONObject p = new JSONObject();
		p.put("title", "SomethingElse");
		p.put("cost", new Double(25.00));
		
		rentals.add(r);
		buyAndKeep.add(p);
		
		skyStore.put("rentals", r);
		skyStore.put("buyAndKeep", p);
		return skyStore;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray createJson()
	{
		JSONArray top = new JSONArray();
		top.add(statementJson());
		top.add(packageJson());
		top.add(callInfoJson());
		top.add(skyStore());
	     JSONObject total = new JSONObject();
	     total.put("total", new Double(75.0));
		
	     top.add(total);
	     return top;
	}
	
	@Test 
	public void testEndToEnd()
	{
		jsonImport.interigateBill(createJson());
	}
	
	@Test
	public void testgetCallInfoTotalValue() throws ParseException
	{
		try
		{
			jsonImport.getCallCharges(callInfoJson());
		}
		catch(Exception e)
		{
			System.out.println(e);
			fail();
		}
	}
	
	@Test
	public void testgetCallInfoTotalValueWithBadJson() throws ParseException
	{
		JSONObject json2 = (JSONObject)new JSONParser().parse("{\"total\":\"BlahNotAInt\"}");
		System.out.println(json2);
		try
		{
			jsonImport.getCallCharges(json2);
			fail();
		}
		catch(Exception e)
		{}
	}
}
