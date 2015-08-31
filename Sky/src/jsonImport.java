import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class jsonImport {
	public static JSONArray callsMadeArray = new JSONArray();
	public static JSONArray subscriptionsArray = new JSONArray();
	public static JSONArray storeBoughtArray = new JSONArray();
	public static JSONArray storeRentArray = new JSONArray();
	public static JSONArray statementArray = new JSONArray();
	public static JSONArray periodArray = new JSONArray();
	
	public static double callsMadeTotal;
	public static double billTotal;
	
	public static void main() throws FileNotFoundException,
    IOException, ParseException
	{
		interigateBill(getJsonFile() );   
	}
	
	public static void interigateBill(JSONArray JsonBill)
	{
		for (Object b : JsonBill) 
	    {
	        JSONObject bill = (JSONObject) b;
	        
	        System.out.println(bill);
	        if((JSONObject) bill.get("package") != null)
	        getPackageInfo((JSONObject) bill.get("package") );
		    
	        if((JSONObject) bill.get("statement") != null)
	        	getStatementInfo((JSONObject) bill.get("statement"));
		    
	        if((JSONObject) bill.get("callCharges") != null)
	        	getCallCharges((JSONObject) bill.get("callCharges") );

	        if((JSONObject) bill.get("skyStore") != null)
	        	getSkyStoreInfo((JSONObject) bill.get("skyStore"));
	        
	        if((Double) bill.get("total") != null)
	        	getBillTotal((double) bill.get("total"));
            
	        System.out.println();
	    }
	}
	
	public static void getBillTotal(double total) 
	{
		billTotal = total;

	}

	public static void getPackageInfo(JSONObject userPackage) 
	{
		System.out.println("Here " + userPackage);
		getSubscriptionsInfo((JSONArray) userPackage.get("subscriptions"));
	}
	
	@SuppressWarnings("unchecked")
	public static void getSubscriptionsInfo(JSONArray subscriptions)
	{
		 for (Object object : subscriptions) 
        {
        	JSONObject field = (JSONObject) object;

            String name = (String) field.get("name");
            System.out.print("name::::" + name);
            
            String type = (String) field.get("type");
            System.out.print(", type::::" + type);
            
            double cost = (double) field.get("cost");
            System.out.println(", cost::::" + cost);
            
        	subscriptionsArray.add(field);
        }
	}

	@SuppressWarnings("unchecked")
	public static void getStatementInfo(JSONObject statement) 
	{
		 String generated = (String) statement.get("generated");
         System.out.print("generated::::" + generated);
         
         String due = (String) statement.get("due");
         System.out.println(", due::::" + due);
         
         statementArray.add(statement);
         getPeriodInfo(  (JSONObject) statement.get("period") );
	}
	
	@SuppressWarnings("unchecked")
	public static void getPeriodInfo(JSONObject period)
	{
		String from = (String) period.get("from");
        System.out.print("from::::" + from);
        
        String to = (String) period.get("to");
        System.out.println(", to::::" + to);
        
        periodArray.add(period);
	}
	
	public static void getCallCharges(JSONObject callCharges)
	{
		getCallsMadeInfo( (JSONArray) callCharges.get("calls") );
        
		getcallChargesTotal( (double) callCharges.get("total") );
	}
	
	public static void getcallChargesTotal(double total)
	{
		callsMadeTotal = total;
		System.out.println(", total::::" + total);
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray getCallsMadeInfo(JSONArray calls)
	{
		for (Object object : calls) 
        {
        	JSONObject field = (JSONObject) object;

        	Object called = (Object) field.get("called");
            System.out.println("called::::" + called);
            
            Object duration = (Object) field.get("duration");
            System.out.println("duration::::" + duration);
            
            double cost = (double) field.get("cost");
            System.out.println("cost::::" + cost);
            
            callsMadeArray.add(field);
        }
		
		return calls;
	}
	
	public static void getSkyStoreInfo(JSONObject store)
	{
		getRentInfo((JSONArray) store.get("rentals"));
		System.out.println(" ");
		getbuyAndKeepInfo((JSONArray) store.get("buyAndKeep"));  
	}
	
	@SuppressWarnings("unchecked")
	public static void getbuyAndKeepInfo(JSONArray bAK)
	{
		System.out.println("You have bought");
		 for(Object object : bAK)
	    {
	    	JSONObject field = (JSONObject) object;
	    	String title = (String)field.get("title");
	    	System.out.println("title::::" + title);
	    	
	    	double cost = (double) field.get("cost");
	    	System.out.println("cost::::" + cost);
	    	storeBoughtArray.add(field);
	    }
	}
	
	@SuppressWarnings("unchecked")
	public static void getRentInfo(JSONArray rent)
	{
		System.out.println("Rentals::::" + rent);
		for(Object object : rent)
	    {	
	    	JSONObject field = (JSONObject) object;
	    	String title = (String)field.get("title");
	    	System.out.println("title::::" + title);
	    	
	    	double cost = (double) field.get("cost");
	    	System.out.println("cost::::" + cost);
	    	storeRentArray.add(field);
	    }
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray getJsonFile() throws FileNotFoundException, IOException, ParseException
	{
		File file = new File("/Users/tchadwic/Documents/Workspace/bill.json");
		System.out.println(file);
		JSONParser parser = new JSONParser();
		Object obj  = parser.parse(new FileReader(
	            file));
		JSONArray array = new JSONArray();
		array.add(obj);
		
		return array;
	}
	
}
