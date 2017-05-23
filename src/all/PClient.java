package all;

import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class PClient {
	private static IPassenger passenger;
	private Ticket ticket;
	public void firstname(Scanner keyboard) {
		System.out.println("\nFirst Name:\n");
		ticket.setFirstName(keyboard.next());
	}
	
	public void lastname(Scanner keyboard) {
		System.out.println("\nLast Name:\n");
		ticket.setLastName(keyboard.next());
	}
	
	public void address(Scanner keyboard) {
		System.out.println("\nAddress:\n");
		ticket.setAddress(keyboard.next());
	}
	
	public void phone(Scanner keyboard) {
		System.out.println("\nPhone Number:\n");
		ticket.setPhone(keyboard.next());
	}
	
	public void selectClass(Scanner keyboard) {
		System.out.println("1. F");
		System.out.println("2. B");
		System.out.println("3. E");
		int choose = validInputOption(keyboard, 3);
		if(choose == 1) {
			ticket.setTicketClass(Flight.FIRST);
		} else if(choose == 2) {
			ticket.setTicketClass(Flight.BUSINESS);
		} else if(choose == 3) {
			ticket.setTicketClass(Flight.ECONOMY);
		}
	}
	
	
	
	private int validInputOption(Scanner keyboard, int max) {
		int userChoice = 0;
		boolean valid = false;

		// Enforces a valid integer input.
		while (!valid) {
			try {
				userChoice = keyboard.nextInt();
				if(userChoice >=1 && userChoice <=max)
					valid = true;
				else {
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("Invalid Input, please enter an Integer (1 - "+max+")\n");
				valid = false;
				keyboard.nextLine();
			}
		}
		return userChoice;
	}

	private boolean initConnection(int port) {
		try {
			String registryURL = "rmi://localhost:"+port+"/" + IPassenger.INTERFACE_NAME;
			passenger = (IPassenger) Naming.lookup(registryURL);
			return true;
		} catch (ConnectException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void INIT() {
		int userChoice = 0;
		int step = 1;
		int maxChoice = 0;
		int departure = 0;
		Scanner keyboard = new Scanner(System.in);

		while (true) {
			if(step == 1) {
				maxChoice = 4;
				menu();
			} else if(step == 2) {
				maxChoice = 3;
				desmenu(userChoice);
			} else if(step == 3) {
				firstname(keyboard);
				step++;
			} else if(step == 4) {
				lastname(keyboard);
				step++;
			} else if(step == 5) {
				address(keyboard);
				step++;
			} else if(step == 6) {
				phone(keyboard);
				step++;
			} else if(step == 7) {
				selectClass(keyboard);
				step++;
			} else if(step == 8) {
				depDate(keyboard);
				step++;
				try {
					Result success = passenger.book(ticket.getFirstName(), ticket.getLastName(), ticket.getAddress(),
							ticket.getPhone(), ticket.getDestination(), ticket.getDepartureDate(), ticket.getTicketClass());
					System.out.println(success.getContent());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				step = 1;
				continue;
			}
			
			Boolean valid = false;

			// Enforces a valid integer input.
			while (!valid && step <=2) {
				try {
					userChoice = keyboard.nextInt();
					if(userChoice >=1 && userChoice <=maxChoice)
						valid = true;
					else {
						throw new Exception();
					}
				} catch (Exception e) {
					System.out.println("Invalid Input, please enter an Integer (1-"+maxChoice+")");
					valid = false;
					keyboard.nextLine();
				}
			}

			// Manage user selection.
			if(step == 1) {
				departure = userChoice;
				if(INPUT(userChoice, keyboard))
					step++;
			} else if(step == 2) {
				if (DesInput(userChoice, departure))
					step++;
				else
					step--;
			}
		}
	}
	public void depDate(Scanner keyboard) {
		System.out.println("\nPlease input your Departure Date:\n");
		boolean valid = false;
		String input = "";
		// Enforces a valid integer input.
		while (!valid) {
			try {
				input = keyboard.next();
				if(Rename.validDate(input))
					valid = true;
				else {
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("Invalid Input, please enter Date like 20161010\n");
				valid = false;
				keyboard.nextLine();
			}
		}
		ticket.setDepartureDate(input);
	}


	private boolean INPUT(int userChoice, Scanner keyboard) {
		boolean success = false;
		switch (userChoice) {
		case 1:
			success = initConnection(MTL.PORT);
			break;
		case 2:
			success = initConnection(DDO.PORT);
			break;
		case 3:
			success = initConnection(LVL.PORT);
			break;
		case 4:
			keyboard.close();
			System.exit(0);
		default:
			System.out.println("");
		}
		return success;
	}

	public static void main(String[] args) {
		new PClient().INIT();
	}
	public static void menu() {
		System.out.println("departure station (1-3) or 4.Exit");
	}

	public static void desmenu(int departure) {
		if(departure == 1) {
			System.out.println("1. WST");
			System.out.println("2. NDL");
		} else if(departure == 2) {
			System.out.println("1. MTL");
			System.out.println("2. NDL");
		} else if(departure == 3) {
			System.out.println("1. MTL");
			System.out.println("2. WST");
		}
		System.out.println("3. Back");
	}
	private boolean DesInput(int userChoice, int departure) {
		boolean success = false;
		ticket = new Ticket();
		if(departure == 1) {
			ticket.setDeparture("MTL");
		} else if(departure == 2) {
			ticket.setDeparture("WST");
		} else if(departure == 3) {
			ticket.setDeparture("NDL");
		}
		switch (userChoice) {
		case 1:
			if(departure == 1) {
				ticket.setDestination("Washington");
			} else if(departure == 2) {
				ticket.setDestination("Montreal");
			} else if(departure == 3) {
				ticket.setDestination("Montreal");
			}
			success = true;
			break;
		case 2:
			if(departure == 1) {
				ticket.setDestination("New Delhi");
			} else if(departure == 2) {
				ticket.setDestination("New Delhi");
			} else if(departure == 3) {
				ticket.setDestination("Washington");
			}
			success = true;
			break;
		case 3:
			success = false;
			break;
		default:
			System.out.println("Invalid Input, please try again.");
		}
		
		return success;
	}

}
