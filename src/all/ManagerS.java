package all;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ManagerS extends UnicastRemoteObject implements IManager {

	private String LOG_PATH = Rename.LOG_DIR+"LOG_";
	private String server;
	private int PORT;
	
	protected ManagerS(String server, int udp) throws RemoteException {
		super();
		this.server = server;
		PORT = udp;
		LOG_PATH=LOG_PATH+server+"/"+server+"_LOG.txt";
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				init();
			}
		}).start();
	}
	private String fromOtherServers(String recordType, String ip, int port) {
		DatagramSocket aSocket = null;
		String receive = "";
		try {
			aSocket = new DatagramSocket();
			byte[] m = recordType.getBytes();
			InetAddress aHost = InetAddress.getByName(ip);
			DatagramPacket request = new DatagramPacket(m, m.length, aHost, port);
			aSocket.send(request);
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			receive = new String(reply.getData(), 0, reply.getLength()).trim();
		}catch (SocketException e){System.out.println("["+server+"]-"+"Socket: " + e.getMessage());
		Rename.error(LOG_PATH, "["+server+"]-"+"Socket: " + e.getMessage());
		}catch (IOException e){System.out.println("["+server+"]-"+"IO: " + e.getMessage());
		Rename.error(LOG_PATH, "["+server+"]-"+"IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
		return receive;
	}
	@Override
	public String getCount(String recordType) throws RemoteException {
		int count = getTypeCount(recordType);
		String value = "";
		if(MTL.SERVER_NAME.equals(server)) {
			value = server + " " +count+",";
			value +=fromOtherServers(recordType, "localhost", DDO.UDP);
			value +=",";
			value +=fromOtherServers(recordType, "localhost", LVL.UDP);
		} else if(DDO.SERVER_NAME.equals(server)) {
			value =fromOtherServers(recordType, "localhost", MTL.UDP);
			value += ("," + server + " " +count+",");
			value +=fromOtherServers(recordType, "localhost", LVL.UDP);
		} else if(LVL.SERVER_NAME.equals(server)) {
			value +=fromOtherServers(recordType, "localhost", MTL.UDP);
			value +=",";
			value +=fromOtherServers(recordType, "localhost", DDO.UDP);
			value += ("," + server + " " +count);
		}
		return value;
	}

	private int getTypeCount(String recordType) {
		HashMap<String,List<Ticket>> tickets = TData.getInstance().init(server);
		Iterator iter = tickets.entrySet().iterator();
		int count = 0;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			ArrayList<Ticket> value = (ArrayList<Ticket>)entry.getValue();
			for(Ticket f:value) {
				if(f!=null) {
					if (!recordType.equals(Flight.ALL_CLASS)) {
						if (recordType.equals(f.getTicketClass())) {
							count++;
						}
					} else {
						count++;
					 }
				}
			}
		}
		return count;
	}

	@Override
	public Result edit(int recordID, String fieldName, String newValue) throws RemoteException {
		ArrayList<Flight> flight = (ArrayList<Flight>)FData.getInstance().initData(server);
		Result result = new Result();
		boolean find = false;
		boolean r = false;
		for(Flight f:flight) {
			if(f.getRecordID() == recordID) {
				find = true;
				if(Flight.DEPARTURE.equals(fieldName)) {
					if(newValue!=null&&!newValue.equals(f.getDestination())) {
						f.setDeparture(newValue);
						r = true;
					} else {
					}
				} else if(Flight.DATE.equals(fieldName)) {
					f.setDepartureDate(newValue);
					r = true;
				} else if(Flight.DESTINATION.equals(fieldName)) {
					if(newValue!=null&&!newValue.equals(f.getDeparture())) {
						f.setDestination(newValue);
						r = true;
					} else {
					}
				} else if(Flight.F_SEATS.equals(fieldName)) {
					int old = f.getTotalFirstTickets()-f.getBalanceFirstTickets();//getRecordTypeCount(Flight.F_SEATS);
					if(Integer.valueOf(newValue) >= old) {
						f.setTotalFirstTickets(Integer.valueOf(newValue));
						r = true;
					} else {
					}
				} else if(Flight.B_SEATS.equals(fieldName)) {
					int old = f.getTotalBusinessTickets()-f.getBalanceBusinessTickets();//getRecordTypeCount(Flight.B_SEATS);
					if(Integer.valueOf(newValue) >= old) {
						f.setTotalBusinessTickets(Integer.valueOf(newValue));
						r = true;
					} else {
					}
				} else if(Flight.E_SEATS.equals(fieldName)) {
					int old = f.getTotalEconomyTickets()-f.getBalanceEconomyTickets();//getRecordTypeCount(Flight.E_SEATS);
					if(Integer.valueOf(newValue) >= old) {
						f.setTotalEconomyTickets(Integer.valueOf(newValue));
						r = true;
					} else {
					}
				}
				if(r) {
				}
				break;
			}
		}
		if(!find) {
			Flight f = new Flight();
			f.setRecordID(recordID);
			if(Flight.DEPARTURE.equals(fieldName)) {
				f.setDeparture(newValue);
			} else if(Flight.DATE.equals(fieldName)) {
				f.setDepartureDate(newValue);
			} else if(Flight.DESTINATION.equals(fieldName)) {
				f.setDestination(newValue);
			} else if(Flight.F_SEATS.equals(fieldName)) {
				f.setTotalFirstTickets(Integer.valueOf(newValue));
			} else if(Flight.B_SEATS.equals(fieldName)) {
				f.setTotalBusinessTickets(Integer.valueOf(newValue));
			} else if(Flight.E_SEATS.equals(fieldName)) {
				f.setTotalEconomyTickets(Integer.valueOf(newValue));
			}
			FData.getInstance().addNewFlight(server, f);
			r = true;
		}
		result.setSuccess(r);
//		result.setContent(info);
		
		return result;
	}
	private void init() {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(PORT);
			// create socket at agreed port
			byte[] buffer = new byte[1000];
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				String receive = new String(request.getData(), 0, request.getLength()).trim();
				int count = 0;
				if (Flight.FIRST.equals(receive)) {
					count = getTypeCount(Flight.FIRST);
				} else if (Flight.BUSINESS.equals(receive)) {
					count = getTypeCount(Flight.BUSINESS);
				} else if (Flight.ECONOMY.equals(receive)) {
					count = getTypeCount(Flight.ECONOMY);
				} else if (Flight.ALL_CLASS.equals(receive)) {
					count = getTypeCount(Flight.ALL_CLASS);
				}
				String re = server + " " + count;
				request.setData(re.getBytes());
				DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(),
						request.getPort());
				aSocket.send(reply);
			}
		}catch (SocketException e){System.out.println("["+server+"]-"+"Socket: " + e.getMessage());
		Rename.log(LOG_PATH, "["+server+"]-"+"Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("["+server+"]-"+"IO: " + e.getMessage());
		Rename.log(LOG_PATH, "["+server+"]-"+"IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}
	
	
	

}
