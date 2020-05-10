/**  EE422C Final Project submission by
 *  Amit Verma
 *  axv68
 *  16320
 *   Spring 2020
 */

package final_exam;

public class AuctionItem {
	
	public String itemName; 
	public int timer; 
	public double minPrice; 
	public String buyer = "Unknown"; 
	
	public AuctionItem(String name, Integer time, Double price) { 
		this.itemName = name; 
		this.timer = time; 
		this.minPrice = price; 
	}
	
}
