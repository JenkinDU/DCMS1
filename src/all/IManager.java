package all;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IManager extends Remote {
	public static final String INTERFACE_NAME = "manager";
	public String getCount(String recordType) throws RemoteException;
	public Result edit(int recordID, String fieldName, String newValue) throws RemoteException;
}
