package assignment7;

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

public class Object_Server extends Observable{
	
	static Object_Server server;
    ArrayList<ClientObserver> clientOutputStreams;
    ArrayList<Thread> clients = new ArrayList<Thread>(); 
    
    public ArrayList<AuctionItem> itemList = new ArrayList<AuctionItem>(); 
    
    public HashMap<String, ArrayList<String>> database = new HashMap<String, ArrayList<String>>(); 
    
    public static void main (String [] args) {
        server = new Object_Server();
        //server.populateItems();
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
	
	/* Updating the clients with notifyClients function*/
//	private void notifyClients(String message) {
//		for (PrintWriter writer : clientOutputStreams) {
//			writer.println(message); 
//			writer.flush();
//		}
//	}
	
	 public void populateItems() {
 		// TODO Auto-generated method stub
     	//generate all the items to send to client o GUI
     	AuctionItem trumpet = new AuctionItem("Trumpet", 50000, 35); 
     	AuctionItem painting = new AuctionItem("Painting", 35000, 35); 
     	AuctionItem tiger = new AuctionItem("Tiger", 620000, 35); 
     	AuctionItem camera = new AuctionItem("Camera", 620000, 35); 

     	itemList.add(trumpet); 
     	itemList.add(painting); 
     	itemList.add(tiger); 
     	itemList.add(camera); 
 		
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
//        		if (database.containsKey(message.username) == false) {
//        			ArrayList<String> userRecord = new ArrayList<String>(); 
//        			database.put(message.username, userRecord); 
//        		}
        		setChanged(); 
        		notifyObservers(msg);
        	}
        	
        	else if (message.type == Message.message_type.bid) {
        		String msg = message.username + ": " + message.content; 
        		//database.get(message.username).add(message.content); 
        		//setChanged(); 
        		//notifyObservers(msg);  
        		
        		writer.flush();
        		writer.writeToMe("Server got your bid");
        		
        		
        	}
        	
        	else if(message.type == Message.message_type.history) {
        		for (int i = 0; i < database.get(message.username).size(); i++) {
        			setChanged(); 
            		notifyObservers(database.get(message.username).get(i));
        		}
        	}
        	
//        	1.) checking the item 
//			2.) retrieve the items min price 
//				a.) compare the bid with the minprice
//					i.) if higher, then log bid for the client and notify all clients of new bid
//					ii.) O.W notify just that client with invalid bid message
			
        
        	
        }
    } 
}
