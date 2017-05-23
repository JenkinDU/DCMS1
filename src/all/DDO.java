package all;

public class DDO extends DCMS {
	public static final String SERVER_NAME = "DDO";
	public static final int PORT = 2021;
	public static final int UDP = 3021;
	
	public static void main(String[] args) {
		try {
			(new DDO()).run("DDO", SERVER_NAME, PORT, UDP);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
