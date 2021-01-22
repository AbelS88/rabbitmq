package com.sesen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


public class Consumer {

	private static String QUEUE_NAME;
	private static String HOST;
	private static String ROUTE_FILE;
	private static int ticks;
	private static HashMap <String, Object> map = new HashMap <String, Object> ();
	private static List<String> listA  = new ArrayList<>();
	private static List<String> listB  = new ArrayList<>();
	private static List<String> listC  = new ArrayList<>();
	
    public static void main(String[] argv)  {
    	
    	Properties properties= new Properties();       
        try {
			properties.load(new FileInputStream(new File("../rabbitmq/src/main/java/com/sesen/configuracion.properties")));
		
			QUEUE_NAME=  properties.getProperty("QUEUE_NAME");
			HOST=  properties.getProperty("HOST");
			ROUTE_FILE=  properties.getProperty("ROUTE_FILE");
        
      
        	ConnectionFactory factory = new ConnectionFactory();
        	factory.setHost(HOST);
	        Connection connection = factory.newConnection();
	        Channel channel = connection.createChannel();
	    	channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	        	String message = new String(delivery.getBody(), "UTF-8");            	
	        	setMessage(message);
	        	System.out.println(" [x] Received '" + message + "'");
	        };
        
	        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    	
        } catch (IOException | TimeoutException e) {
    		e.printStackTrace();
    	}
     }
        
    
    public static void setMessage(String msg) throws IOException {    	
    	String id=getID(msg);
    	switch(id) {                	
    	  case "533_E:AAPL_TR" :
    		 listA.add("ID:"+id +" | SYMBOL.TICKER:E:AAPL | CURRENT.PRICE: "+getPRICE(msg));                		
    	     map.put("533_E:AAPL_TR",listA); 
    	     ticks=generateFiles(ticks,map);
        	 break;
    	  case "533_GOOGL_TR" :
     		 listB.add("ID:"+id +" | SYMBOL.TICKER:E:GOOG | CURRENT.PRICE: "+getPRICE(msg));
    	     map.put("533_GOOGL_TR",listB);    		    	    	
    	     ticks=generateFiles(ticks,map);
        	 break;
    	  case "533_E:MSFT_TR" :
     		 listC.add("ID:"+id +" | SYMBOL.TICKER:E:MSFT | CURRENT.PRICE: "+getPRICE(msg));
    	     map.put("533_E:MSFT_TR",listC);    		    	    	
    	     ticks=generateFiles(ticks,map);

    	}
  
    }
    public static String getID(String str) {
		 int position1=str.indexOf("ID", 0);
		 int position2=str.indexOf("TR", 0);
		 String id= str.substring(position1+3,position2+2);
		 return id;
	}
	public static String getPERMISSION(String str) {
		int position1=str.indexOf("PERMISSION", 0);
		 String permission= str.substring(position1+11,position1+14);
		 return permission;
	}
	public static String getPRICE(String str) {
		 int position1=str.indexOf("CURRENT.PRICE", 0);
		 int position2=str.indexOf("|ACTIVITY", 0);
		 if(position1!=-1) {
			 String ticker=str.substring(position1+14,position2);
			 int position3=ticker.indexOf("|TRADE", 0);
			 ticker=ticker.substring(0,position3);
			 return ticker;
		 }
		 else {
			 return "SIN PRECIO";
		 }
	}
	public static int generateFiles (int ticks,HashMap<String, Object> map) throws IOException {
		ticks++;
    	 if(ticks>=5) {	    	
	    			createFiles(map);	    			
	    			listA.clear();
	    			listB.clear();
	    			listC.clear();				
	    	} 
    	 return ticks;
	}
	public static  void createFiles(HashMap<String, Object> map) throws IOException {
		Iterator<?> it = map.entrySet().iterator();
		while (it.hasNext()) {
		    @SuppressWarnings("rawtypes")
			Map.Entry e = (Map.Entry)it.next();  		    
		    @SuppressWarnings("unchecked")
			List<String> list  = (List<String>) e.getValue();
		    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(ROUTE_FILE+e.getKey()+".1.txt", true));
		    for (int i=0;i<list.size();i++) {    		        
		        bufferedWriter.append(list.get(i));
		        bufferedWriter.newLine();

		      }
	        bufferedWriter.close();
		}
	}
    
}
		
	
	

