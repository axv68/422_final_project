/**  EE422C Final Project submission by
 *  Amit Verma
 *  axv68
 *  16320
 *   Spring 2020
 */

package final_exam;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends PrintWriter implements Observer{


	public ClientObserver(OutputStream outputStream) {
		super(outputStream);
	}

	@Override
	public void update(Observable o, Object arg) { //****need to update this based on the message broadcasted from the server
		this.println(arg);
		this.flush();
		
	}
	
	public void writeToMe(Object arg) {
		this.println(arg); 
		this.flush();
	}


}