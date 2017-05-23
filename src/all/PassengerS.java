package all;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PassengerS extends UnicastRemoteObject implements IPassenger {

	private String LOG_PATH = Rename.LOG_DIR+"LOG_";
	private String name;
	private String server;
	protected PassengerS(String name,String server) throws RemoteException {
		super();
		this.name = name;
		this.server = server;
		LOG_PATH=LOG_PATH+server+"/"+server+"_LOG.txt";
		Rename.printFlight(server);
	}

	@Override
	public Result book(String firstName, String lastName, String address, String phone, String destination,
			String date, String ticketClass) throws RemoteException {
		ArrayList<Flight> flight = (ArrayList<Flight>)FData.getInstance().initData(server);
		Result result = new Result();
		boolean r = false;
		String info = "success";
		Flight book = null;
		for(Flight f:flight) {
			if(f.getDeparture().equals(this.name)&&f.getDestination().equals(destination)&&f.getDepartureDate().equals(date)) {
				book = f;
				r = true;
				break;
			}
		}
		if(r) {
			if(book!=null&book.sellTicket(ticketClass)) {
				Ticket t = new Ticket(firstName, lastName, address, phone, destination, date, ticketClass, this.name);
				String index = Character.toUpperCase(lastName.charAt(0)) + "" ;
				TData.getInstance().add(server, t, index);
			} else {
				r = false;
				info = "Failed";
			}
		} else {
			info = "Failed";
		}
		result.setSuccess(r);
		result.setContent(info);
		return result;
	}
}
