package all;

public class MTL extends DCMS {
	public static final String SERVER_NAME = "MTL";
	public static final int PORT = 2020;
	public static final int UDP = 3020;
	
	public static void main(String[] args) {
		try {
			(new MTL()).run("Montreal", SERVER_NAME, PORT, UDP);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
