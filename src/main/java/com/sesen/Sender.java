package com.sesen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {
	private static String QUEUE_NAME;
	private static String HOST;
	private static String INPUT_FILE;
	private static String ROUTE_FILE;
	private static String IGNORE_PERMISSION;
	private static RandomAccessFile FILE;

	public static void main(String[] args) {
		Properties properties= new Properties();       
        try {
			
        	properties.load(new FileInputStream(new File("../rabbitmq/src/main/java/com/sesen/configuracion.properties")));
        	
        	HOST=  properties.getProperty("HOST");
        	QUEUE_NAME=  properties.getProperty("QUEUE_NAME");
            ROUTE_FILE=  properties.getProperty("ROUTE_FILE");
            INPUT_FILE=properties.getProperty("INPUT_FILE");
            IGNORE_PERMISSION=properties.getProperty("IGNORE_PERMISSION");
            
            ConnectionFactory factory= new ConnectionFactory();
        	factory.setHost(HOST);
			Connection connection = factory.newConnection();
			Channel channel1 = connection.createChannel();	
		    channel1.queueDeclare(QUEUE_NAME, false, false, false, null);
		    FILE = new RandomAccessFile(ROUTE_FILE+INPUT_FILE, "r");			
			String str;

			while ((str = FILE.readLine()) != null) {
			    															 
                if (!getPERMISSION(str).equals(IGNORE_PERMISSION)) {
          		    channel1.basicPublish("", QUEUE_NAME, false, null, str.getBytes());
          		    System.out.println("Enviado " +str);
          		    
                }
                }
			FILE.close();
			channel1.close();
			connection.close();
			
	} catch (IOException | TimeoutException e) {
		e.printStackTrace();
	}

	}
	public static String getPERMISSION(String str) {
		int position1=str.indexOf("PERMISSION", 0);
		 String permission= str.substring(position1+11,position1+14);
		 return permission;
	}
}
		
	
	

