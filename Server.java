package assignment7;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.control.ComboBox;


public class Server extends Observable{
	
	static Server server;
    ArrayList<ClientObserver> clientOutputStreams;
    ArrayList<Thread> clients = new ArrayList<Thread>(); 
    
    public static ArrayList<AuctionItem> itemList = new ArrayList<AuctionItem>(); //item list
    public HashMap<String, ArrayList<String>> database = new HashMap<String, ArrayList<String>>(); //holds history of bids for users
    
    public ArrayList<Boolean> itemSold = new ArrayList<Boolean>(); 
    
    //timers for the items 
    static Timer t1;
	static Timer t2; 
	static Timer t3;
	static Timer t4; 
	static Timer t5; 
	static int t1interval;
	static int t2interval; 
	static int t3interval; 
	static int t4interval; 
	static int t5interval; 
	
    public static void main (String [] args) {
        server = new Server();
        server.populateItems();
        
        server.startTimers();
        
        server.SetupNetworking(); 
    }


	private void SetupNetworking(){
		clientOutputStreams = new ArrayList<ClientObserver>(); 
		
        try {
            ServerSocket ss = new ServerSocket(8000);
            while (true) {
                Socket clientSocket = ss.accept();
                ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
                Thread t = new Thread(new ClientHandler(clientSocket, writer));
                t.start();
                addObserver(writer);
                System.out.println("got a connection");
            }
        } catch (IOException e) {}
    }
	
	 public void populateItems() {
 		// TODO Auto-generated method stub
     	//generate all the items to send to client o GUI
		 
     	AuctionItem trumpet = new AuctionItem("Trumpet", 60, 35.0); 
     	AuctionItem painting = new AuctionItem("Painting", 75, 35.0); 
     	AuctionItem tiger = new AuctionItem("Tiger", 88, 35.0); 
     	AuctionItem camera = new AuctionItem("Camera", 110, 35.0); 
     	AuctionItem sword = new AuctionItem("Sword", 67, 35.0); 

     	itemList.add(trumpet); 
     	itemList.add(painting); 
     	itemList.add(tiger); 
     	itemList.add(camera); 
     	itemList.add(sword); 
     	
     	for (int i = 0; i < 5; i++) {
     		itemSold.add(false); 
     	}

     	//have to send items thru string
 	}
	
	 public void startTimers() {
		 	int delay = 1000;
		    int period = 1000;
		    t1 = new Timer();
		    t2 = new Timer(); 
		    t3 = new Timer(); 
		    t4 = new Timer(); 
		    t5 = new Timer(); 
		    t1interval = itemList.get(0).timer;
		    t2interval = itemList.get(1).timer;
		    t3interval = itemList.get(2).timer;
		    t4interval = itemList.get(3).timer;
		    t5interval = itemList.get(4).timer;

		    t1.schedule(new TimerTask() {
		        public void run() {
		        		//System.out.println("t1 time left: " + setInterval1());
		        	setInterval1(); 
		        }
		    }, delay, period); 
		    
		    t2.schedule(new TimerTask() {
		    	
		    	public void run() {
		            //System.out.println("t2 time left: " + setInterval2());
		    		setInterval2(); 
		        }
		    	
		    }, delay, period);
		    
		    t3.schedule(new TimerTask() {
		    	
		    	public void run() {
		            //System.out.println("t3 time left: " + setInterval3());
		    		setInterval3(); 
		        }
		    	
		    }, delay, period);
		    
		    t4.schedule(new TimerTask() {
		    	
		    	public void run() {
		            //System.out.println("t4 time left: " + setInterval4());
		    		setInterval4(); 
		        }
		    	
		    }, delay, period);	
		    
		    t5.schedule(new TimerTask() {
		    	
		    	public void run() {
		            //System.out.println("t5 time left: " + setInterval5());
		            setInterval5(); 
		        }
		    	
		    }, delay, period);
		    
	 }
	
	 public int setInterval1() {
		    if (t1interval == 1) {
		    	 t1.cancel();
		    	 if (!(itemList.get(0).buyer.contentEquals("Unknown"))){
		    		 setChanged(); 
			    	 notifyObservers(itemList.get(0).itemName + " is SOLD to " + itemList.get(0).buyer + "!");
		    	 }
		    	 else {
		    		 setChanged(); 
			    	 notifyObservers(itemList.get(0).itemName + " is UNSOLD!");
		    	 } 
		    	 itemSold.set(0, true); 
		    }
		    if (t1interval == 6) {
		    	setChanged(); 
		    	notifyObservers(itemList.get(0).itemName + " has 5 seconds left!"); 
		    }
		    if (t1interval == 31) {
		    	setChanged(); 
		    	notifyObservers(itemList.get(0).itemName + " has 30 seconds left!"); 
		    }
		    return --t1interval;
	 }
	 public int setInterval2() {
		    if (t2interval == 1) {
		    	 t2.cancel();
		    	 if (!(itemList.get(1).buyer.contentEquals("Unknown"))){
		    		 setChanged(); 
			    	 notifyObservers(itemList.get(1).itemName + " is SOLD to " + itemList.get(1).buyer + "!");
		    	 }
		    	 else {
		    		 setChanged(); 
			    	 notifyObservers(itemList.get(1).itemName + " is UNSOLD!");
		    	 }
		    	 itemSold.set(1, true); 
		    }
		    if (t2interval == 6) {
		    	setChanged(); 
		    	notifyObservers(itemList.get(1).itemName + " has 5 seconds left!"); 
		    }
		    if (t2interval == 30) {
		    	setChanged(); 
		    	notifyObservers(itemList.get(1).itemName + " has 30 seconds left!"); 
		    }
		    return --t2interval;
	 }
	 public int setInterval3() {
		    if (t3interval == 1) {
		    	 t3.cancel();
		    	 if (!(itemList.get(2).buyer.contentEquals("Unknown"))){
		    		 setChanged(); 
			    	 notifyObservers(itemList.get(2).itemName + " is SOLD to " + itemList.get(2).buyer + "!");
		    	 }
		    	 else {
		    		 setChanged(); 
			    	 notifyObservers(itemList.get(2).itemName + " is UNSOLD!");
		    	 }
		    	 itemSold.set(2, true); 

		    }
		    if (t3interval == 6) {
		    	setChanged(); 
		    	notifyObservers(itemList.get(2).itemName + " has 5 seconds left!"); 
		    }
		    if (t3interval == 31) {
		    	setChanged(); 
		    	notifyObservers(itemList.get(2).itemName +  " has 30 seconds left!"); 
		    }
		    return --t3interval; 
	 }
	 public int setInterval4() {
		    if (t4interval == 1) {
		    	 t4.cancel();
		    	 if (!(itemList.get(3).buyer.contentEquals("Unknown"))){
		    		 setChanged(); 
			    	 notifyObservers(itemList.get(3).itemName + " is SOLD to " + itemList.get(3).buyer + "!");
		    	 }
		    	 else {
		    		 setChanged(); 
			    	 notifyObservers(itemList.get(3).itemName + " is UNSOLD!");
		    	 }
		    }
		    if (t4interval == 6) {
		    	setChanged(); 
		    	notifyObservers(itemList.get(3).itemName + " has 5 seconds left!"); 
		    }
		    if (t4interval == 31) {
		    	setChanged(); 
		    	notifyObservers(itemList.get(3).itemName + " has 30 seconds left!"); 
		    }
	    	 itemSold.set(3, true); 
		    return --t4interval;
	 }
	 public int setInterval5() {
		    if (t5interval == 1) {
		    	 t5.cancel();
		    	 if (!(itemList.get(4).buyer.contentEquals("Unknown"))){
		    		 setChanged(); 
			    	 notifyObservers(itemList.get(4).itemName + " is SOLD to " + itemList.get(4).buyer + "!");
		    	 }
		    	 else {
		    		 setChanged(); 
			    	 notifyObservers(itemList.get(4).itemName + " is UNSOLD!");
		    	 }
		    	 itemSold.set(4, true); 
		    	 
		    }
		    if (t5interval == 6) {
		    	setChanged(); 
		    	notifyObservers(itemList.get(4).itemName + " has 5 seconds left!"); 
		    }
		    if (t5interval == 31) {
		    	setChanged(); 
		    	notifyObservers(itemList.get(4).itemName + " has 30 seconds left!"); 
		    }
		    return --t5interval;
	 }
	 
	 
    public class ClientHandler implements Runnable {
        private BufferedReader Rdr; 
    	
        private  ObjectInputStream reader;
        private  ClientObserver writer; // See Canvas. Extends ObjectOutputStream, implements Observer
        //Socket clientSocket;
       
        public ClientHandler(Socket clientSocket, ClientObserver writer) throws IOException {
			Socket sock = clientSocket; 
			this.writer = writer; 
			try {
				//Rdr = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				reader = new ObjectInputStream((sock.getInputStream())); 
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
        }

        public void run() {
        	Message message; 
			try {
				try {
					
					while ((message = (Message) reader.readObject()) != null) {
						System.out.println("server read " + message.username); // lets server know that you recieved a message
						parse(message);	
						
					}
					
				} catch (ClassNotFoundException | EOFException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					System.out.println("Client has left the Auction"); 
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
        }
        
        private void parse(Message message) { //will understand what the client sent a valid message but potential invalid bid 
        	
        	if (message.type == Message.message_type.username) {
        		String msg = message.username + " has joined the auction"; 
        		if (database.containsKey(message.username) == false) {
        			ArrayList<String> userRecord = new ArrayList<String>(); 
        			database.put(message.username, userRecord); 
        		}
        		setChanged(); 
        		notifyObservers(msg);
        	}
        	
        	else if (message.type == Message.message_type.bid) {
        		
        		database.get(message.username).add(message.content); 
        		boolean validBid = false; 
        		double minPrice = 0.0; 
        		String[] msg = message.content.split(" "); //splits the bid into the item and the bid amount
        		char[] bid = msg[1].toCharArray(); 
        		
        		int itemIndex = 0; 
        		for (int i = 0; i < itemList.size(); i++) {
        			if (msg[0].contentEquals(itemList.get(i).itemName)) {
            			minPrice = itemList.get(i).minPrice; //gets the minimum price of the item 
            			itemIndex = i; 
            		}
        		}
        		
        		ArrayList<Character> bidAmount = new ArrayList<Character>(); 
        		Double bidPrice = 0.0; 
        		
				for (int i = 0; i < bid.length; i++) { 
					bidAmount.add(bid[i]); 
				}
				if (bidAmount.get(0) == '$') {
					bidAmount.remove(0); 
					String num = ""; 
					for (int i = 0; i < bidAmount.size();i++) {
						num = num + bidAmount.get(i); 
					} 
					try {
						bidPrice = Double.parseDouble(num); 
					}
					catch(NumberFormatException ex) {
						writer.writeToMe("Invalid bid for " + msg[0]);
	            		writer.flush();
					}
				}
				if (bidPrice > minPrice) {
					if (itemSold.get(itemIndex) == false) {
						validBid = true; 
						itemList.get(itemIndex).minPrice = bidPrice; 
						itemList.get(itemIndex).buyer = message.username;
					}
					else {
						writer.writeToMe("Sorry but your bid can't be processed because time is up for " + itemList.get(itemIndex).itemName);
	            		writer.flush();
					}
					
				}
        		
        		if(validBid == true && itemSold.get(itemIndex) == false) {
        			setChanged(); 
            		notifyObservers("ALERT: New bid of " + msg[1]  + " for " + msg[0] + " by " + message.username);  
        		}
        		else if (validBid == false && itemSold.get(itemIndex) == false) {
        			writer.writeToMe("[Bid of " + msg[1] + " for " + msg[0] + " is at or below the new min price, which is " + "$" + itemList.get(itemIndex).minPrice + "]");
            		writer.flush();
        		}
        		
        		
        	}
        	
        	else if(message.type == Message.message_type.history) {
        		ArrayList<String> bids = database.get(message.username); 
        		if (bids.size() == 0) {
        			writer.writeToMe("<You have no bid history>"); 
        			writer.flush(); 
        		}
        		else {
        			writer.writeToMe("______________________");
        			writer.flush();
        			writer.writeToMe("**YOUR BID HISTORY**");
        			writer.flush();
        			for (int i = 0; i < bids.size(); i++) {

            			System.out.println(bids.get(i));
                		writer.writeToMe(bids.get(i));
                		writer.flush();
            		}
        			writer.writeToMe("______________________");
        			writer.flush();
        		}
        		
        	}
               	
        }
        
       
    } 
    
    
}
