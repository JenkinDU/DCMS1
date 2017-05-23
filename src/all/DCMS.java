package all;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DCMS {

	public DCMS() {
		super();
	}

	public void run(String name, String server, int port, int udp) throws Exception {
		String s = "["+server+"]-"+"Server is up and running!";
		System.out.println(s);
		
		Rename.createDir(Rename.LOG_DIR+"LOG_"+server+"/");
		FData.getInstance().initData(server);
		TData.getInstance().init(server);
		PassengerS passenger = new PassengerS(name, server);
		ManagerS manager = new ManagerS(server, udp);

		Registry r = LocateRegistry.createRegistry(port);
		r.rebind(IPassenger.INTERFACE_NAME, passenger);
		r.rebind(IManager.INTERFACE_NAME, manager);
		
		Rename.log(Rename.LOG_DIR+"LOG_"+server+"/"+server+"_LOG.txt", s);
	}
}