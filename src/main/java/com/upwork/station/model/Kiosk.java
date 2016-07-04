package com.upwork.station.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.upwork.station.exception.ValidationException;
import com.upwork.station.model.listener.PumpListener;
import com.upwork.station.model.listener.StatusChangedEvent;

/**
 * Represents a Service Station Kiosk and implements Kiosk attendant related
 * opereations
 * 
 * 
 * @author ikrussirl
 *
 */
public class Kiosk implements PumpListener {

	private Scanner scanner;
	private Map<Pump, List<Transaction>> transactions = new HashMap<Pump, List<Transaction>>();
	private BigDecimal totalAmount = BigDecimal.ZERO;
	private Map<FuelType, TotalFuel> totalFuel = new HashMap<FuelType, TotalFuel>();

	public Kiosk() {
		scanner = new Scanner(System.in);
	}

	public void statusChanged(StatusChangedEvent event) {
		System.out.println("KIOSK - Pump status changed to: " + event.getNewStatus().getDisplayText());
		if (event.getOldStatus() == PumpStatus.READY && event.getNewStatus() == PumpStatus.STAND_BY) {
			System.out.println("Set to Pump to ready Y/N?");
			String answer = scanner.next();
			if (answer.equalsIgnoreCase("Y")) {
				event.getPump().setReady();
			}
		}
	}

	/**
	 * Pay the petrol refueled on a given pump
	 * 
	 * @param pump on which the payment due.
	 * @return
	 * @throws ValidationException
	 */
	public Transaction pay(Pump pump) throws ValidationException {
		BigDecimal amount = pump.getTotalAmountDue();
		Nozzle nozzle = pump.getUsedNozzle();
		Transaction transaction = new Transaction(amount, pump, nozzle);
		addTransaction(transaction, pump);
		addTotalFuel(nozzle, amount);
		totalAmount = totalAmount.add(amount);
		pump.reset();
		return transaction;
	}

	private void addTotalFuel(Nozzle nozzle, BigDecimal amount) {
		FuelType fuelType = nozzle.getFuelType();
		if (!totalFuel.containsKey(fuelType)) {
			totalFuel.put(fuelType, new TotalFuel(BigDecimal.ZERO, BigDecimal.ZERO));
		}
		TotalFuel currentTotalFuel = totalFuel.get(fuelType);
		TotalFuel newTotalFuel = new TotalFuel(currentTotalFuel.getAmount().add(amount),
				currentTotalFuel.getLiters().add(nozzle.getLitersRefuled()));
		totalFuel.put(fuelType, newTotalFuel);
	}

	private void addTransaction(Transaction transaction, Pump pump) {
		if (!transactions.containsKey(pump)) {
			transactions.put(pump, new ArrayList<Transaction>());
		}
		transactions.get(pump).add(transaction);
	}

	public Map<Pump, List<Transaction>> getTransactionPerPump() {
		return transactions;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public Map<FuelType, TotalFuel> getTotalFuel() {
		return totalFuel;
	}

	public static class TotalFuel {

		private BigDecimal amount;
		private BigDecimal liters;

		public TotalFuel(BigDecimal amount, BigDecimal liters) {
			super();
			this.amount = amount;
			this.liters = liters;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public BigDecimal getLiters() {
			return liters;
		}

		public void setLiters(BigDecimal liters) {
			this.liters = liters;
		}

	}

}
