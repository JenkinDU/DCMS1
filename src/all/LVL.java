package all;

public class LVL extends DCMS {
	public static final String SERVER_NAME = "LVL";
	public static final int PORT = 2022;
	public static final int UDP = 3022;
	
	public static void main(String[] args) {
		try {
			(new LVL()).run("LVL", SERVER_NAME, PORT, UDP);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
