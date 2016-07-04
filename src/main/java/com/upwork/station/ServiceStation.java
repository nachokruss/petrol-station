package com.upwork.station;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.upwork.station.exception.ValidationException;
import com.upwork.station.model.FuelType;
import com.upwork.station.model.Kiosk;
import com.upwork.station.model.Kiosk.TotalFuel;
import com.upwork.station.model.Nozzle;
import com.upwork.station.model.Pump;
import com.upwork.station.model.PumpStatus;
import com.upwork.station.model.Transaction;
import com.upwork.station.model.listener.PumpListener;
import com.upwork.station.model.listener.SysoutPumpDisplay;

/**
 * Service Station app main class.
 *
 */
public class ServiceStation {
	
	private static final int REFUEL = 0;
	private static final int MAKE_PAYMENT = 1;
	private static final int RESET_PUMP = 2;
	private static final int VIEW_PUMPS_STATE = 3;
	private static final int TRANSACITONS_PER_PUMP = 4;
	private static final int VIEW_TOTAL_AMOUNTS = 5;
	private static final int EXIT = 6;
	private static final int NUMBER_OF_PUMPS = 5;
	
	private List<Pump> pumps = new ArrayList<Pump>();
	private Kiosk kiosk;
	
	public ServiceStation() {
		kiosk = new Kiosk();
		createPumps(kiosk);
	}

	private void createPumps(Kiosk kiosk) {
		for (int i = 0; i < NUMBER_OF_PUMPS; i++) {
			addPump(i, kiosk);
		}
	}
	
	private void addPump(Integer pumpId, Kiosk kiosk) {
		Pump pump = new Pump(pumpId);
		PumpListener pumpListener = new SysoutPumpDisplay(pumpId);
		pump.addListener(pumpListener);
		pump.addListener(kiosk);
		pumps.add(pump);
	}

	public static void main(String[] args) {
		ServiceStation app = new ServiceStation();
		app.run();
	}
	
	public void run() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to Applegreen service station");
		System.out.println("This petrol station has " + NUMBER_OF_PUMPS + " pumps that can serve unleaded or diesel.");
		while (true) {
			printMainMenu();
			int menuOption = scanner.nextInt();
			System.out.println("Selected option: " + menuOption);
			System.out.println("=====================================");
			try {
				switch (menuOption) {
				case REFUEL:
					refuel(scanner);
					break;
				case MAKE_PAYMENT:
					makePayment(scanner);
					break;
				case RESET_PUMP:
					resetPump(scanner);
					break;
				case VIEW_PUMPS_STATE:
					viewPumpsState(scanner);
					break;
				case TRANSACITONS_PER_PUMP:
					transactionsPerPump(scanner);
					break;
				case VIEW_TOTAL_AMOUNTS:
					viewTotalAMounts(scanner);
					break;
				case EXIT:
					exit(scanner);
					break;
				default:
					System.out.println("Wrong option: " + menuOption);
					break;
				}
			} catch (ValidationException e) {
				System.out.println(e.getMessage() + ", Please try again.");
			} catch (Exception e) {
				System.out.println("Unexpected error: " + e.getMessage() + ", Please try again.");
			}
		}
	}
	
	private void viewTotalAMounts(Scanner scanner) {
		System.out.println("Total Amounts for the day:");
		Map<FuelType, TotalFuel> totalPerFuelType = kiosk.getTotalFuel();
		for (Map.Entry<FuelType, TotalFuel> totalFuel : totalPerFuelType.entrySet()) {
			FuelType fuelType = totalFuel.getKey();
			BigDecimal liters = totalFuel.getValue().getLiters();
			BigDecimal amount = totalFuel.getValue().getAmount();
			System.out.println(liters + " liters of " + fuelType + " for an amount of: " + amount);
		}
		System.out.println("Total Amount: " + kiosk.getTotalAmount());
	}

	private void transactionsPerPump(Scanner scanner) {
		Map<Pump, List<Transaction>> transactionsPerPump = kiosk.getTransactionPerPump();
		if (transactionsPerPump.size() > 0) {
			for (Map.Entry<Pump, List<Transaction>> entry : transactionsPerPump.entrySet()) {
				printPumpTransactions(entry.getKey(), entry.getValue());
			}
		} else {
			System.out.println("No transactions registed.");

		}
	}

	private void printPumpTransactions(Pump pump, List<Transaction> transactions) {
		System.out.println("A total of " + transactions.size() + " transactions were made on Pump Number: "
				+ pump.getId());
		int transactionNumber = 0;
		for (Transaction transaction : transactions) {
			transactionNumber++;
			System.out.println("Transaction Number: " + transactionNumber);
			System.out.println(transaction);
		}
	}

	private void viewPumpsState(Scanner scanner) throws ValidationException {
		for (Pump pump : pumps) {
			System.out.println(pump);
		}
	}

	private void resetPump(Scanner scanner) {
		Pump pump = getPump(scanner);
		pump.reset();
	}

	private void makePayment(Scanner scanner) throws ValidationException {
		List<Pump> pumpsDuePayment = getPumpsDuePayment();
		if (pumpsDuePayment.size() > 0) {
			Pump pump = selectPumpForPayment(scanner, pumpsDuePayment);
			Transaction transaction = kiosk.pay(pump);
			System.out.println("Printing Recipt:");
			System.out.println(transaction);
		} else {
			System.out.println("No Pump is due payment");
		}

	}

	private Pump selectPumpForPayment(Scanner scanner, List<Pump> pumpsDuePayment) throws ValidationException {
		System.out.println("Select a Pump to make a payment:");
		for (Pump pump : pumpsDuePayment) {
			System.out.println("   " + pump.getId() + ": Amount Due: " + pump.getTotalAmountDue());
		}
		int pumpId = scanner.nextInt();
		if (pumps.get(pumpId) == null || pumps.get(pumpId).getStatus() != PumpStatus.PAYMENT_DUE) {
			System.out.println("Invalid pump number.");
		}
		return pumps.get(pumpId);
	}

	private List<Pump> getPumpsDuePayment() {
		List<Pump> pumpsDuePayment = new ArrayList<Pump>();
		for (Pump pump : pumps) {
			if (pump.getStatus() == PumpStatus.PAYMENT_DUE) {
				pumpsDuePayment.add(pump);
			}
		}
		return pumpsDuePayment;
	}

	private void exit(Scanner scanner) {
		System.out.println("Exiting program...");
		scanner.close();
		System.exit(0);
	}

	private void refuel(Scanner scanner) throws ValidationException {
		Pump pump = getPump(scanner);
		checkIfReady(pump);
		Nozzle nozzle = liftNozzle(scanner, pump);
		refuel(scanner, pump, nozzle);
		pump.placeBack(nozzle);
	}

	private void checkIfReady(Pump pump) throws ValidationException {
		if (pump.getStatus() != PumpStatus.READY) {
			throw new ValidationException("The selected Pump is not Ready");
		}
	}

	private void refuel(Scanner scanner, Pump pump, Nozzle nozzle) throws ValidationException {
		System.out.println("Enter amount:");
		BigDecimal amount = new BigDecimal(scanner.next());
		pump.refuel(nozzle, amount);
	}

	private Nozzle liftNozzle(Scanner scanner, Pump pump) throws ValidationException {
		System.out.println("Enter Nozzle Number to Lift:");
		for (Nozzle nozzle : pump.getNozzles()) {
			System.out.println("   " + nozzle);
		}
		
		int nozzleNumber = scanner.nextInt();
		return pump.lift(nozzleNumber);
	}

	private Pump getPump(Scanner scanner) {
		System.out.println("Enter Pump Number:");
		int pumpNumber = scanner.nextInt();
		if (pumpNumber < 0 || pumpNumber >= NUMBER_OF_PUMPS) {
			throw new RuntimeException("Invalid Pump Number");
		}
		return pumps.get(pumpNumber);
	}

	private void printMainMenu() {
		System.out.println("");
		System.out.println("=====================================");
		System.out.println("Please select an option:");
		System.out.println(" Customer Options:");
		System.out.println("   " + REFUEL + ": Refuel");
		System.out.println("   " + MAKE_PAYMENT + ": Make payment");
		System.out.println(" Kiosk Attendant Options:");
		System.out.println("   " + RESET_PUMP + ": Reset Pump");
		System.out.println("   " + VIEW_PUMPS_STATE + ": View Pumps State");
		System.out.println(" Manager Reports:");
		System.out.println("   " + TRANSACITONS_PER_PUMP + ": Transactions Per Pump");
		System.out.println("   " + VIEW_TOTAL_AMOUNTS + ": Total Amounts");
		System.out.println(" " + EXIT + ": Exit");
		System.out.println("=====================================");
	}
}
