package all;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FData {
	private static FData instance;
	private HashMap<String, List<Flight>> data;
	private int recordID = 0;

	private FData() {
		data = new HashMap<String, List<Flight>>();
	}

	public static synchronized FData getInstance() {
		if (instance == null) {
			instance = new FData();
		}
		return instance;
	}

	public synchronized List<Flight> initData(String name) {
		List<Flight> o = data.get(name);
		if (o == null) {
			data.put(name, addInitFlight(name));
		}
		return data.get(name);
	}
	private boolean isExit(int id) {
		Iterator iter = this.data.entrySet().iterator();
		int count = 0;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			ArrayList<Flight> value = (ArrayList<Flight>) entry.getValue();
			for (Flight f : value) {
				if (f != null) {
					if (id == f.getRecordID()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private ArrayList<Flight> addInitFlight(String name) {
		ArrayList<Flight> flight = new ArrayList<Flight>();
		
		if(MTL.SERVER_NAME.equals(name)) {
		} else if(DDO.SERVER_NAME.equals(name)) {
		} else if(LVL.SERVER_NAME.equals(name)) {
		}
		
		return flight;
	}
	public synchronized void addNewFlight(String name, Flight f) {
		List<Flight> list = data.get(name);
		if(list == null)
			list = new ArrayList<Flight>();
		if(f.getRecordID() <= 0 || isExit(f.getRecordID()))
			f.setRecordID(++recordID);
		list.add(f);
	}
	
}
