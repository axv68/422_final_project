package final_exam;

import java.io.Serializable;

public class Message implements Serializable{
	
	public String username = "";
	public String content = ""; 
	
	
	enum message_type{
		username, bid, history; 
	}
	
	message_type type; 
	
	Message(message_type topic, String name, String msg){
		type = topic; 
		username = name; 
		content = msg; 
	}
		
	

}

