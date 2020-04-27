package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

/*
 * Author: Vallath Nandakumar and the EE 422C instructors.
 * Data: April 20, 2020
 * This starter code assumes that you are using an Observer Design Pattern and the appropriate Java library
 * classes.  Also using Message objects instead of Strings for socket communication.
 * See the starter code for the Chat Program on Canvas.  
 * This code does not compile.
 */
public class Server extends Observable {

    static Server server;
    ArrayList<ClientObserver> clientOutputStreams; 
    
    public ArrayList<AuctionItem> itemList = new ArrayList<AuctionItem>(); 

    
    public static void main (String [] args) {
        server = new Server();
        //server.populateItems();
        server.SetupNetworking(); 
    }


	private void SetupNetworking() {
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
    	
        //private  ObjectInputStream reader;
        //private  PrintWriter writer; // See Canvas. Extends ObjectOutputStream, implements Observer
        //Socket clientSocket;
       
        public ClientHandler(Socket clientSocket, PrintWriter writer) throws IOException {
			Socket sock = clientSocket; 
			try {
				Rdr = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
        }

        public void run() {
			String message; 
			try {
				while ((message = Rdr.readLine()) != null) {
					System.out.println("server read " + message); // lets server know that you recieved a message
					//parse(message);
					//notifyClients(message);
					setChanged(); 
					notifyObservers(message); 
					
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
        }
        
        private void parse(String message) { //will understand what the client sent a valid message but potential invalid bid 
        	
        	String[] words = message.split(" "); 
        	
        	//check for History request 
        	if (message.contentEquals("History")) {
        		
        	}
        	
//        	1.) checking the item 
//			2.) retrieve the items min price 
//				a.) compare the bid with the minprice
//					i.) if higher, then log bid for the client and notify all clients of new bid
//					ii.) O.W notify just that client with invalid bid message
			
        
        	
        }
    } // end of class ClientHandler
}
