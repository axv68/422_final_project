package final_exam;

import java.io.BufferedReader;
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


public class AuctionServer extends Observable{
	
	static AuctionServer server;
    ArrayList<ClientObserver> clientOutputStreams;
    ArrayList<Thread> clients = new ArrayList<Thread>(); 
    
    public static ArrayList<AuctionItem> itemList = new ArrayList<AuctionItem>(); //item list
    public HashMap<String, ArrayList<String>> database = new HashMap<String, ArrayList<String>>(); //holds history of bids for users
    
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
        server = new AuctionServer();
        server.populateItems();
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
                //clientOutputStreams.add(writer); 
                System.out.println("got a connection");
            }
        } catch (IOException e) {}
    }
	
	 public void populateItems() {
 		// TODO Auto-generated method stub
     	//generate all the items to send to client o GUI
		 
     	AuctionItem trumpet = new AuctionItem("Trumpet", 50, 35.0); 
     	AuctionItem painting = new AuctionItem("Painting", 35, 35.0); 
     	AuctionItem tiger = new AuctionItem("Tiger", 62, 35.0); 
     	AuctionItem camera = new AuctionItem("Camera", 62, 35.0); 
     	AuctionItem sword = new AuctionItem("Sword", 60, 35.0); 

     	itemList.add(trumpet); 
     	itemList.add(painting); 
     	itemList.add(tiger); 
     	itemList.add(camera); 
     	itemList.add(sword); 
   
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
		        		System.out.println("t1 time left: " + setInterval1());
		        }
		    }, delay, period); 
		    
		    t2.schedule(new TimerTask() {
		    	
		    	public void run() {
		            System.out.println("t2 time left: " + setInterval2());

		        }
		    	
		    }, delay, period);
		    
		    t3.schedule(new TimerTask() {
		    	
		    	public void run() {
		            System.out.println("t3 time left: " + setInterval3());

		        }
		    	
		    }, delay, period);
		    
		    t4.schedule(new TimerTask() {
		    	
		    	public void run() {
		            System.out.println("t4 time left: " + setInterval4());

		        }
		    	
		    }, delay, period);	
		    
		    t5.schedule(new TimerTask() {
		    	
		    	public void run() {
		            System.out.println("t5 time left: " + setInterval5());

		        }
		    	
		    }, delay, period);
		    
		    
	 }
	
	 private static final int setInterval1() {
		    if (t1interval == 1) {
		    	 t1.cancel();
			     System.out.println("Item 1 is Sold Out");
		    }
		    return --t1interval;
	 }
	 private static final int setInterval2() {
		    if (t2interval == 1) {
		    	 t2.cancel();
			     System.out.println("Item 2 is Sold Out");
		    }
		    return --t2interval;
	 }
	 private static final int setInterval3() {
		    if (t3interval == 1) {
		    	 t3.cancel();
			     System.out.println("Item 3 is Sold Out");
		    }
		    return --t3interval;
	 }
	 private static final int setInterval4() {
		    if (t4interval == 1) {
		    	 t4.cancel();
			     System.out.println("Item 4 is Sold Out");
		    }
		    return --t4interval;
	 }
	 private static final int setInterval5() {
		    if (t5interval == 1) {
		    	 t5.cancel();
			     System.out.println("Item 5 is Sold Out");
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
			//String message; 
        	Message message; 
			try {
				try {
					while ((message = (Message) reader.readObject()) != null) {
						System.out.println("server read " + message.username); // lets server know that you recieved a message
						parse(message);
//						setChanged(); 
//						notifyObservers(message.content); //need to change update method later
						//ObjectOutputStream.reset() whatever you set as objectoutputstream
						
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					validBid = true; 
					itemList.get(itemIndex).minPrice = bidPrice; 
					itemList.get(itemIndex).buyer = message.username; 
				}
        		
        		if(validBid == true) {
        			setChanged(); 
            		notifyObservers("ALERT: New bid of " + msg[1]  + " for " + msg[0] + " by " + message.username);  
        		}
        		else {
        			writer.writeToMe("[Bid of " + msg[1] + " for " + msg[0] + " is below the new min price, which is " + "$" + itemList.get(itemIndex).minPrice + "]");
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
