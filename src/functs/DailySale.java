package functs;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import database.DBInitialize;
import livedata.cahierdata;

public class DailySale {

	public String empDailySale() {
		
			String dailysale="";
		try {
			new DBInitialize().dBInitializer();
			
			  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
			    Date date = new Date();  
			   
			
			
			String dsq = "SELECT SUM(`amount`) AS sale FROM `bill` WHERE `cashierid`='"+cahierdata.cashierrec.getId()+"' AND date Like '"+formatter.format(date)+"%'";
			new DBInitialize();
			ResultSet ds = DBInitialize.statement.executeQuery(dsq);
			if (ds.next()) {
				 dailysale=ds.getString("sale");
				//dailysale=String.format("%,d", sale);
			}
			
			}catch(Exception e) {
				
				System.out.println("Error in Daily Sale Calculation" );
			}
			
		
		
		return dailysale;
		
		
		
		
		
	}
	
	
}
