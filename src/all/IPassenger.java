package all;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPassenger  extends Remote{
	public static final String INTERFACE_NAME = "passenger";
	public Result book(String firstName, String lastName, String address, 
			String phone, String destination, String date, String ticketClass) throws RemoteException;
}
