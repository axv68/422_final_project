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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


/*
 * Author: Vallath Nandakumar and EE 422C instructors
 * Date: April 20, 2020
 * This starter code is from the MultiThreadChat example from the lecture, and is on Canvas. 
 * It doesn't compile.
 */

public class AuctionClient extends Application { 
	// I/O streams 
	PrintWriter toServer = null; //************Was Printwriter
	//DataInputStream fromServer = null;
	BufferedReader fromServer = null;
	TextArea ta = new TextArea();
	String data = ""; 
	public String username = ""; 
	//public boolean userNameAccepted = false; 

	@Override
	public void start(Stage primaryStage) { 
	
		BorderPane textPane = new BorderPane(); 
		textPane.setPadding(new Insets(5, 5, 5, 5)); 
		textPane.setStyle("-fx-border-color: green");
		//textPane.setLeft(new Label("Enter a message: "));
		
		TextField tf = new TextField(); 
		tf.setAlignment(Pos.BOTTOM_RIGHT);
		tf.setPromptText("Enter your message");
		textPane.setCenter(tf);
		tf.setDisable(true); //don't allow textfield to work yet******
		
		//username field*****ADDED FIELD
		TextField userName = new TextField(); 
		userName.setAlignment(Pos.BASELINE_LEFT);
		userName.setPromptText("Enter a username");
		textPane.setLeft(userName);
		//***
		
		BorderPane mainPane = new BorderPane(); 
		ta = new TextArea(); //***************************8
		ta.setDisable(true); //don't allow textfield to work yet******
		mainPane.setCenter(new ScrollPane(ta));
		mainPane.setTop(textPane);

		// Create a scene and place it in the stage 
		Scene scene = new Scene(mainPane, 450, 200); 
		primaryStage.setTitle("Client"); // Set the stage title 
		primaryStage.setScene(scene); // Place the scene in the stage 
		primaryStage.show(); // Display the stage 

		tf.setOnAction(e -> { 
			try {
				
				String message = tf.getText().trim();
				
				toServer.println(message); 
				
				tf.setText("");
				
				toServer.flush(); 
				
			}
			catch (Exception ex){
				System.err.println(ex); 
			}
		}); 
		
		userName.setOnAction(e -> { 
			try {
				
				String username = userName.getText().trim();
				
				toServer.println(username); 
				
				tf.setText("");
				
				toServer.flush(); 
				
				userName.setDisable(true);
				ta.setDisable(false);
				tf.setDisable(false);
				
				primaryStage.setTitle(username);

//				String received = fromServer.readLine(); 	
//				ta.appendText("Message is " + received + " \n"); 
				
			}
			catch (Exception ex){
				System.err.println(ex); 
			}
		});  // etc.

		try { 
			// Create a socket to connect to the server 
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost", 8000); //connect to server at Port 5000
			
			BufferedReader rdr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// Create an input stream to receive data from the server 
			//fromServer = new DataInputStream(socket.getInputStream()); 
			fromServer = rdr; 

			// Create an output stream to send data to the server 
			toServer = new PrintWriter(socket.getOutputStream()); //*****Was Printwriter
			
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
					System.out.println("Message is " + message); 
					
					data = message; 
					System.out.println("Data is " + data); 
					Platform.runLater(new Runnable() { // Run from JavaFX GUI 
						@Override 
						public void run() { 
							ta.appendText("Message is " + data + "\n");
						} 
				}); 
					
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
