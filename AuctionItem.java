package assignment7;

public class AuctionItem {
	
	public String itemName; 
	public int timer; 
	public int minPrice; 
	
	public AuctionItem(String name, Integer time, Integer price) { 
		this.itemName = name; 
		this.timer = time; 
		this.minPrice = price; 
	}
	
}
