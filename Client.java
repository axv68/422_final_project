/**  EE422C Final Project submission by
 *  Amit Verma
 *  axv68
 *  16320
 *   Spring 2020
 */


package final_exam;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import com.sun.glass.ui.Application.EventHandler;

import final_exam.Message.message_type;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Client extends Application { 
	// I/O streams 
	ObjectOutputStream toServer = null; //
	BufferedReader fromServer = null;
	TextArea ta = new TextArea();
	String data = ""; 
	public String username = ""; 
	static ComboBox<String> itemList; // = new ComboBox();
	
	static ObservableList<String> names = FXCollections.observableArrayList(
	          "Trumpet", "Painting", "Tiger", "Camera", "Sword");

	
	
	@Override
	public void start(Stage primaryStage) { 

		//TODO - mainPane set top to horizonatal box
	
		BorderPane textPane = new BorderPane(); 
		textPane.setPadding(new Insets(5, 5, 5, 5)); 
		textPane.setStyle("-fx-border-color: black; -fx-background-color: papayawhip;");
		//textPane.setStyle("-fx-background-color: papayawhip"); 
		
		
		TextField bidField = new TextField(); 
		bidField.setAlignment(Pos.BASELINE_LEFT);
		bidField.setPromptText("Enter your bid");
		bidField.setStyle("-fx-border-color: green");
		textPane.setCenter(bidField);
		bidField.setDisable(true); //don't allow textfield to work yet******
		
		//Button for the message field
		Button send = new Button(); 
		send.setText("Send Bid"); 
		send.setAlignment(Pos.BOTTOM_RIGHT);
		send.setDisable(true); //don't allow button to work yet*****
		send.setStyle("-fx-background-color: AQUAMARINE; -fx-border-color: black;");
		textPane.setRight(send);
		//******
		
		//Button for the history field
		Button history = new Button(); 
		history.setText("Get Bid History"); 
		history.setAlignment(Pos.BOTTOM_LEFT);
		history.setDisable(true); //don't allow button to work yet******
		history.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
		
		//******
		
		//username field
		TextField userName = new TextField(); 
		userName.setAlignment(Pos.BASELINE_LEFT);
		userName.setPromptText("Enter a username");
		textPane.setLeft(userName);
		//***
		
		//Quit Button 
		Button quit = new Button(); 
		quit.setText("Leave Auction"); 
		quit.setAlignment(Pos.BOTTOM_LEFT);
		quit.setStyle("-fx-background-color: CORAL; -fx-border-color: black;");
		
		//*****
		
		//Dropdown menu of items 
		itemList = new ComboBox(); 
		itemList.setPromptText("Select an item");
		itemList.setDisable(true); 
		itemList.getItems().addAll(
			            "Trumpet",
			            "Painting",
			            "Tiger",
			            "Camera",
			            "Sword"
		);   
		textPane.setBottom(itemList);
		itemList.setStyle("-fx-background-color: thistle; -fx-border-color: black;");
		

		
		//***********
		
		BorderPane mainPane = new BorderPane(); 
		ta = new TextArea(); //***************************8
		ta.setStyle("-fx-control-inner-background: LIGHTCYAN");
		ta.setEditable(false);
		ta.setDisable(true); //don't allow textfield to work yet******
		mainPane.setCenter(new ScrollPane(ta));
		mainPane.setTop(textPane);
		
		mainPane.setRight(history);
		mainPane.setBottom(quit);
		
		//Showing Timers with Items
		ListView<String> listView = new ListView<String>(names); 
		listView.setStyle("-fx-control-inner-background: beige");
		mainPane.setLeft(listView); 
		
		mainPane.setStyle("-fx-background-color: MISTYROSE");
		
		//set item fields here
		

		// Create a scene and place it in the stage 
		Scene scene = new Scene(mainPane, 750, 300); 
		primaryStage.setTitle("Client"); // Set the stage title 
		primaryStage.setScene(scene); // Place the scene in the stage 
		primaryStage.show(); // Display the stage 
		
		itemList.setOnMouseEntered(e -> {
			itemList.setStyle("-fx-background-color: azure; -fx-border-color: black;");
		}); 
		
		itemList.setOnMouseExited(e -> {
			itemList.setStyle("-fx-background-color: thistle; -fx-border-color: black;");
		});
		
		history.setOnMouseEntered(e -> {
			history.setStyle("-fx-background-color: azure; -fx-border-color: black;");
		}); 
		
		history.setOnMouseExited(e -> {
			history.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
		});
		
		history.setOnAction(e -> { 
			try {
				Message msg = new Message(message_type.history, username, "");
				toServer.writeObject(msg);
				toServer.flush(); 
			}
			catch (Exception ex){
				System.err.println(ex); 
			}
		});
		
		quit.setOnAction(e -> { 
			System.exit(0); 
		});
		
		quit.setOnMouseEntered(e -> {
			quit.setStyle("-fx-background-color: azure; -fx-border-color: black;");
		}); 
		
		quit.setOnMouseExited(e -> {
			quit.setStyle("-fx-background-color: coral; -fx-border-color: black;");
		});
		
		bidField.setOnMouseClicked(e -> { 
			try {
				bidField.setText("$");
			}
			catch (Exception ex){
				System.err.println(ex); 
			}
		}); 
		
		
		bidField.setOnAction(e -> { //need to rewrite
			try {
				
				
				String message = bidField.getText().trim();
				String item = itemList.getValue(); 
				int flag = 1; 
				if (item != null) {
					char[] bid = message.toCharArray(); 
					ArrayList<Character> bidAmount = new ArrayList<Character>(); 
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
							Double bidPrice = Double.parseDouble(num); 
						}
						catch(NumberFormatException ex) {
							ta.appendText("--Please Enter A Valid Bid--" + "\n");
							bidField.setText(""); 
							flag = 0; 
						}
					}
					
					if (flag != 0) {
						String total = item + " " + message; 
						Message msg = new Message(message_type.bid, username, total);
						//synchronized(sendLock) {
							toServer.writeObject(msg);
							bidField.setText("");
							toServer.flush(); 
						//}
					}
				}
				else {
					ta.appendText("**You Must Select An Item**" + "\n");
					bidField.setText("");
				}
				
			}
			catch (Exception ex){
				System.err.println(ex); 
			}
		}); 
		
		send.setOnMouseEntered(e -> {
			send.setStyle("-fx-background-color: azure; -fx-border-color: black;");
		}); 
		
		send.setOnMouseExited(e -> {
			send.setStyle("-fx-background-color: AQUAMARINE; -fx-border-color: black;");
		});
		
		send.setOnAction(e -> { 
			try {
				String message = bidField.getText().trim();
				String item = itemList.getValue(); 
				int flag = 1; 
				if (item != null) {
					char[] bid = message.toCharArray(); 
					ArrayList<Character> mine = new ArrayList<Character>(); 
					for (int i = 0; i < bid.length; i++) {
						mine.add(bid[i]); 
					}
					if (mine.get(0) == '$') {
						mine.remove(0); 
						String num = ""; 
						for (int i = 0; i < mine.size();i++) {
							num = num + mine.get(i); 
						}
						try {
							Double bidPrice = Double.parseDouble(num); 
						}
						catch(NumberFormatException ex) {
							ta.appendText("--Please Enter A Valid Bid--" + "\n");
							bidField.setText(""); 
							flag = 0; 
						}
					}
					
					if (flag != 0) {
						String total = item + " " + message; 
						Message msg = new Message(message_type.bid, username, total);
						toServer.writeObject(msg);
						bidField.setText("");
						toServer.flush(); 
					}
				}
				else {
					ta.appendText("**You Must Select An Item**" + "\n");
					bidField.setText("");
				}
				
			}
			catch (Exception ex){
				System.err.println(ex); 
			}
		}); 
		
		userName.setOnAction(e -> { //setting username field
			try {
				
				username = userName.getText().trim();
				Message msg = new Message(message_type.username, username, ""); 
				toServer.writeObject(msg);
				toServer.flush(); 
				
				userName.setDisable(true);
				ta.setDisable(false);
				bidField.setDisable(false);
				send.setDisable(false);
				history.setDisable(false); 
				itemList.setDisable(false);
				primaryStage.setTitle("Auction Client - " + username);
			}
			catch (Exception ex){
				System.err.println(ex); 
			}
		});  

		try { 
			// Create a socket to connect to the server 
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost", 8000); //connect to server at Port 5000
			BufferedReader rdr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Create an input stream to receive data from the server 
			fromServer = rdr; 
			// Create an output stream to send data to the server 
			toServer = new ObjectOutputStream(socket.getOutputStream()); //*****ObjectOutputStream
			Thread readerThread = new Thread(new IncomingReader());
			readerThread.start();
			
		} 
		catch (IOException ex) { 
			ta.appendText(ex.toString() + '\n');
		}
	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	class IncomingReader implements Runnable {
		public void run() {
			String message = null;
			try {
				while ((message = fromServer.readLine()) != null) {
						
					URL resource = getClass().getResource("dishR.wav");
					AudioClip ding = new AudioClip(resource.toString());
					ding.play();
					
					System.out.println("Message is: " + message); 
						
					data = message; 
					ta.appendText(data + "\n"); 

				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}


}