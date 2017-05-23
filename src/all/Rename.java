package all;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rename {
	public static final String LOG_DIR = "./";
	
	public static void log(String file, String log) {
		FileWriter fw;
		try {
			fw = new FileWriter(LOG_DIR+file, true);
			fw.write("\r\n"+new Date().toString()+"-----AAA:"+log);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void error(String file, String log) {
		FileWriter fw;
		try {
			fw = new FileWriter(LOG_DIR+file, true);
			fw.write("\r\n"+new Date().toString()+"-----EEE:"+log);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static void createDir(String d) {
		try {
			File dir = new File(d);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean validDate(String input) {
		String pat = "\\d{8}" ;
	    Pattern p = Pattern.compile(pat) ;
	    Matcher m = p.matcher(input) ;
	    return m.matches();
	}

	public static void printFlight(String server) {
		List<Flight> flight = FData.getInstance().initData(server);
		System.out.println("ID\tDEP\t\tDES\t\tDATE\t\tF/B/E");
		for(Flight f:flight) {
			System.out.println(f.getRecordID()+"\t"+f.getDeparture()+"\t"+f.getDestination()+"\t"+f.getDepartureDate()
			+"\t"+f.getBalanceFirstTickets()+"/"+f.getBalanceBusinessTickets()+"/"+f.getBalanceEconomyTickets());
		}
	}
}
