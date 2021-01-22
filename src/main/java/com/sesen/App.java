package com.sesen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

/**
 * Hello world!
 *
 */

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.Keymap;

public class App {

	public static void main(String[] args) {
		try {
			
			HashMap <String, Object> map = new HashMap <String, Object> ();
			List<String> listA  = new ArrayList<>();
			List<String> listB  = new ArrayList<>();
			List<String> listC  = new ArrayList<>();
			String str,id;
			
			RandomAccessFile file = new RandomAccessFile("/Users/abel/Documents/messaging-app/src/source/US_RT_RAW_20160912_01 copia 2.txt", "r");			

			while ((str = file.readLine()) != null) {
			    															 
                if (!getPERMISSION(str).equals("922")) {
                	id=getID(str);
                	switch(id) {                	
                	  case "533_E:AAPL_TR" :
                		 listA.add("ID:"+id +" | SYMBOL.TICKER:E:AAPL | CURRENT.PRICE: "+getPRICE(str));                		
                	      break; 
                	  case "533_E:GOOG_TR" :
                 		 listB.add("ID:"+id +" | SYMBOL.TICKER:E:GOOG | CURRENT.PRICE: "+getPRICE(str));
                	     break; 
                	  case "533_E:MSFT_TR" :
                 		 listC.add("ID:"+id +" | SYMBOL.TICKER:E:MSFT | CURRENT.PRICE: "+getPRICE(str));
                	     break;  
                	}
                	
                	System.out.println(str);

                	System.out.println(getPRICE(str));
                	//System.out.println(permission);

                }
				
			}
			listA.add("ID:533_E:AAPL_TR | SYMBOL.TICKER:E:AAPL | CURRENT.PRICE: SIN PRECIO");
			listA.add("ID:533_E:AAPL_TR | SYMBOL.TICKER:E:AAPL | CURRENT.PRICE: SIN PRECIO");
			listA.add("ID:533_E:AAPL_TR | SYMBOL.TICKER:E:AAPL | CURRENT.PRICE: SIN PRECIO");

			map.put("533_E:AAPL_TR",listA);
			map.put("533_E:GOOG_TR",listB);
			map.put("533_E:MSFT_TR",listC);
        	System.out.println(map);

        	Iterator it = map.entrySet().iterator();
    		while (it.hasNext()) {
    		    Map.Entry e = (Map.Entry)it.next();  		    
    		    System.out.println(e.getKey() + " " + e.getValue());
    		   
    		    List<String> list  = (List<String>) e.getValue();
//    		    Path file1 = Paths.get("/Users/abel/Documents/messaging-app/src/source/"+e.getKey()+".txt");
//    		    Files.write(file1, list, StandardCharsets.UTF_8);
//    		    File fout = new File("/Users/abel/Documents/messaging-app/src/source/"+e.getKey()+".1.txt");
//    			FileOutputStream fos = new FileOutputStream(fout);
//    		 
//    			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
//    		 
    			// FileWriter fileWriter = new FileWriter("/Users/abel/Documents/messaging-app/src/source/"+e.getKey()+".1.txt");
    		    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/Users/abel/Documents/messaging-app/src/source/"+e.getKey()+".1.txt", true));

    		    for (int i=0;i<list.size();i++) {    		        
    		        System.out.println(list.get(i));
    		        bufferedWriter.append(list.get(i));
    		        bufferedWriter.newLine();

    		      }
		        bufferedWriter.close();
    		}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
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
			 return ticker;
		 }
		 else {
			 return "SIN PRECIO";
 
		 }
	}
	
}
