package all;

import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MClient {
	private static final String LOG_PATH = Rename.LOG_DIR+"LOG_Manager"+"/";
	private String managerName = "default";
	private static IManager manager;
	
	private void showMenu() {
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
	
	private void bookmenu() {
//		System.out.println(m);
//		System.out.println(m1);
//		System.out.println(m2);
//		System.out.println(m3);
//		System.out.println(m4);
		String s = "-"+managerName + " Choose ";
		
		Scanner keyboard = new Scanner(System.in);
		int userChoice = validInputOption(keyboard, 4);
		String type = "";
		switch (userChoice) {
		case 1:
//			s+=m1;
			type = Flight.FIRST;
			break;
		case 2:
//			s+=m2;
			type = Flight.BUSINESS;
			break;
		case 3:
//			s+=m3;
			type = Flight.ECONOMY;
			break;
		case 4:
//			s+=m4;
			type = Flight.ALL_CLASS;
			break;
		default:
			System.out.println("");
		}
		try {
			String value = manager.getCount(type);
			s = "Count:" + value;
			System.out.println(s);
			Rename.log(LOG_PATH+managerName+".txt", s);
		} catch (RemoteException e) {
			e.printStackTrace();
			Rename.error(LOG_PATH+managerName+".txt", e.getMessage());
		}
	}
	
	private int showCityMenu(Scanner keyboard) {
		System.out.println("1. ");
		System.out.println("2. ");
		System.out.println("3. ");
		
		return validInputOption(keyboard, 3);
	}
	
	private void showeditflight(int recordId) {
		String m[] = {"Please select the field name (1-6)",
					"1.  place",
					"2.  date",
					"3. Destination ",
					"4. First  ",
					"5. Business  ",
					"6. Economy  "};
		System.out.println(m[0]);
		System.out.println(m[1]);
		System.out.println(m[2]);
		System.out.println(m[3]);
		System.out.println(m[4]);
		System.out.println(m[5]);
		System.out.println(m[6]);
		
		Scanner keyboard = new Scanner(System.in);
		int userChoice = validInputOption(keyboard, 6);
		
//		s = "-"+managerName + " Choose " + m[userChoice];
//		Log.log(LOG_PATH+managerName+".txt", s);
		
		int seats = -1;
		String fieldName = "";
		String value = "";
		if(userChoice == 1 || userChoice == 3) {
			int city = showCityMenu(keyboard);
			if(city == 1) {
				value = "Montreal";
			} else if(city == 2) {
				value = "Washington";
			} else if(city == 3) {
				value = "New Delhi";
			}
//			s = "-"+managerName + " Choose " + value;
//			Log.log(LOG_PATH+managerName+".txt", s);
		} else {
			System.out.println("Please enter new field value");
			if(userChoice > 3) {
				while (seats < 0) {
					try {
						seats = keyboard.nextInt();
					} catch (Exception e) {
						System.out.println("Invalid Input, please enter the number");
						seats = -1;
						keyboard.nextLine();
					}
				}
				value = seats + "";
			} else if(userChoice == 2) {
				boolean valid = false;
				// Enforces a valid integer input.
				while (!valid) {
					try {
						value = keyboard.next();
						if(Rename.validDate(value))
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
			} else {
				value = keyboard.next();
			}
//			s = "-"+managerName + " Enter New Value: " + value;
//			Log.log(LOG_PATH+managerName+".txt", s);
		}
		try {
			switch (userChoice) {
			case 1:
				fieldName = Flight.DEPARTURE;
				break;
			case 2:
				fieldName = Flight.DATE;
				break;
			case 3:
				fieldName = Flight.DESTINATION;
				break;
			case 4:
				fieldName = Flight.F_SEATS;
				break;
			case 5:
				fieldName = Flight.B_SEATS;
				break;
			case 6:
				fieldName = Flight.E_SEATS;
				break;
			default:
				System.out.println("Invalid Input, please try again.");
			}
			Result result = manager.edit(recordId, fieldName, value);
//			s = "-"+managerName + " " + result.getContent();
//			Log.log(LOG_PATH+managerName+".txt", s);
			System.out.println(result.getContent());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void editmenu() {
		System.out.println("enter something");
		Scanner keyboard = new Scanner(System.in);
		boolean valid = false;
		int userInput = 0;
		
		while (!valid) {
			try {
				userInput = keyboard.nextInt();
				valid=true;
			} catch (Exception e) {
				valid = false;
				keyboard.nextLine();
			}
		}
		showeditflight(userInput);
	}

	private void showmenu(Scanner keyboard) {
		String m1 = "1. Get   Count";
		String m2 = "2. Edit Flight ";
		String m3 = "3. Exit";
//		System.out.println(m);
		System.out.println(m1);
		System.out.println(m2);
		System.out.println(m3);
		String s = "-"+managerName + " Choose ";
		int userChoice = validInputOption(keyboard, 3);
		switch (userChoice) {
		case 1:
			s+=m1;
			bookmenu();
			break;
		case 2:
			s+=m2;
			editmenu();
			break;
		case 3:
			s+=m3;
			keyboard.close();
			System.exit(0);
		default:
			System.out.println("Invalid Input, please try again.");
		}
		init();
	}
	
	private boolean initConnection(int port) {
		try {
			String registryURL = "rmi://localhost:"+port+"/" + IManager.INTERFACE_NAME;
			manager = (IManager) Naming.lookup(registryURL);
			return true;
		} catch (ConnectException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean validManagerId(String input) {
		String pat = "(MTL|WST|NDL)\\d{4}" ;
        Pattern p = Pattern.compile(pat) ;
        Matcher m = p.matcher(input) ;
        return m.matches();
	}
	
	private int getport(String input) {
		String pat = "(MTL)\\d{4}" ;
        if(Pattern.compile(pat).matcher(input).matches()) {
        	return MTL.PORT;
        }
        pat = "(WST)\\d{4}" ;
        if(Pattern.compile(pat).matcher(input).matches()) {
        	return DDO.PORT;
        }
		pat = "(NDL)\\d{4}" ;
		if(Pattern.compile(pat).matcher(input).matches()) {
        	return LVL.PORT;
        }
		return 0;
	}
	
	private void init() {
		String userInput = "";
		Scanner keyboard = new Scanner(System.in);
		boolean valid = false;
		
		showMenu();

		// Enforces a valid integer input.
		while (!valid) {
			try {
				userInput = keyboard.next();
				valid = validManagerId(userInput);
				if (!valid) {
					System.out.println("");
				}
			} catch (Exception e) {
				System.out.println("");
				valid = false;
				keyboard.nextLine();
			}
		}
		managerName = userInput;
		if (initConnection(getport(userInput))) {
			showmenu(keyboard);
		} else {
			init();
		}
	}
	
	public static void main(String[] args) {
		Rename.createDir(LOG_PATH);
		new MClient().init();
	}
}
