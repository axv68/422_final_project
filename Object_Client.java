package assignment7;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.sun.glass.ui.Application.EventHandler;

import assignment7.Message.message_type;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/*
 * Author: Vallath Nandakumar and EE 422C instructors
 * Date: April 20, 2020
 * This starter code is from the MultiThreadChat example from the lecture, and is on Canvas. 
 * It doesn't compile.
 */

public class Object_Client extends Application { 
	// I/O streams 
	ObjectOutputStream toServer = null; //************ObjectOutputStream
	//DataInputStream fromServer = null;
	BufferedReader fromServer = null;
	TextArea ta = new TextArea();
	String data = ""; 
	public String username = ""; 
	//public boolean userNameAccepted = false; 
	static ComboBox<String> itemList; // = new ComboBox();

	@Override
	public void start(Stage primaryStage) { 
	
		BorderPane textPane = new BorderPane(); 
		textPane.setPadding(new Insets(5, 5, 5, 5)); 
		textPane.setStyle("-fx-border-color: red");
		
		
		TextField messageArea = new TextField(); 
		messageArea.setAlignment(Pos.BASELINE_LEFT);
		messageArea.setPromptText("Enter your bid");
		textPane.setCenter(messageArea);
		messageArea.setDisable(true); //don't allow textfield to work yet******
		
		//Button for the message field
		Button send = new Button(); 
		send.setText("Send Bid"); 
		send.setAlignment(Pos.BOTTOM_RIGHT);
		send.setDisable(true); //don't allow button to work yet******
		textPane.setRight(send);
		//******
		
		//Button for the history field
		Button history = new Button(); 
		history.setText("Get Bid History"); 
		history.setAlignment(Pos.BOTTOM_LEFT);
		history.setDisable(true); //don't allow button to work yet******
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
		//*****
		
		//Dropdown menu of items 
		itemList = new ComboBox(); 
		itemList.setPromptText("Select an item");
		itemList.setDisable(true); 
		itemList.getItems().addAll(
			            "Trumpet",
			            "Painting",
			            "Tiger",
			            "Camera"
		);   
		textPane.setBottom(itemList);
		
		//***********
		
		BorderPane mainPane = new BorderPane(); 
		ta = new TextArea(); //***************************8
		ta.setDisable(true); //don't allow textfield to work yet******
		mainPane.setCenter(new ScrollPane(ta));
		mainPane.setTop(textPane);
		
		mainPane.setRight(history);
		mainPane.setBottom(quit);
		//set item fields here
		

		// Create a scene and place it in the stage 
		Scene scene = new Scene(mainPane, 550, 300); 
		primaryStage.setTitle("Client"); // Set the stage title 
		primaryStage.setScene(scene); // Place the scene in the stage 
		primaryStage.show(); // Display the stage 
		
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
		
		messageArea.setOnMouseClicked(e -> { //FIX THIS*****
			try {
				messageArea.setText("$");
			}
			catch (Exception ex){
				System.err.println(ex); 
			}
		}); 
		
		
		messageArea.setOnAction(e -> { //need to rewrite
			try {
				String message = messageArea.getText().trim();
				String item = itemList.getValue(); 
				if (item != null) {
					String total = item + " " + message; 
					Message msg = new Message(message_type.bid, username, total);
					toServer.writeObject(msg);
					messageArea.setText("");
					toServer.flush(); 
				}
				else {
					ta.appendText("**You must select an item**" + "\n");
					messageArea.setText("");
				}
				
			}
			catch (Exception ex){
				System.err.println(ex); 
			}
		}); 
		
		send.setOnAction(e -> { 
			try {
				String message = messageArea.getText().trim();
				String item = itemList.getValue(); 
				if (item != null) {
					String total = item + " " + message; 
					Message msg = new Message(message_type.bid, username, total);
					toServer.writeObject(msg);
					messageArea.setText("");
					toServer.flush(); 
				}
				else {
					ta.appendText("**You must select an item**" + "\n");
					messageArea.setText("");
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
				messageArea.setDisable(false);
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
					System.out.println("Message is: " + message); 
					
					data = message; 
					ta.appendText(data + "\n"); 
//					Platform.runLater(new Runnable() { // Run from JavaFX GUI 
//						@Override 
//						public void run() { 
//							ta.appendText(data + "\n"); 
//						} 
//				}); 
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}