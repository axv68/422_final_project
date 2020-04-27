package assignment7;

import java.io.Serializable;

public class Message implements Serializable{
	
	String username = ""; 
	
	
	enum message_type{
		username, bid, history; 
	}
	
	message_type type; 
	
	Message(message_type topic, String name){
		type = topic; 
		username = name; 
		
		
	}
		

}

