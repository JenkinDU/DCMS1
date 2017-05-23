package all;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TData {
	private static TData instance;
	private HashMap<String, HashMap<String, List<Ticket>>> data;
	private int recordID = 0;
	
	private TData() {
		data = new HashMap<String, HashMap<String, List<Ticket>>>();
	}

	public static synchronized TData getInstance() {
		if (instance == null) {
			instance = new TData();
		}
		return instance;
	}
	public synchronized void add(String name, Ticket t, String index) {
		HashMap<String, List<Ticket>> o = data.get(name);
		ArrayList<Ticket> list = (ArrayList<Ticket>) o.get(index);
		if(list == null)
			list = new ArrayList<Ticket>();
		t.setRecordID(++recordID);
		list.add(t);
		o.put(index, list);
	}
	public synchronized HashMap<String, List<Ticket>> init(String name) {
		HashMap<String, List<Ticket>> o = data.get(name);
		if (o == null) {
			o = new HashMap<String, List<Ticket>>();
			data.put(name, o);
		}
		return data.get(name);
	}
	


}
