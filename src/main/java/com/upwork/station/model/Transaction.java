package com.upwork.station.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a refuel transaction that has finished and its paid.
 * 
 * @author ikrussirl
 *
 */
public class Transaction {

	private BigDecimal totalAmount;
	private BigDecimal pricePerLiter;
	private Pump pump;
	private FuelType fuelType;
	private LocalDateTime dateTime;

	public Transaction(BigDecimal totalAmount, Pump pump, Nozzle nozzle) {
		this.totalAmount = totalAmount;
		this.pump = pump;
		this.pricePerLiter = nozzle.getPricePerLitre();
		this.fuelType = nozzle.getFuelType();
		this.dateTime = LocalDateTime.now();
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getPricePerLiter() {
		return pricePerLiter;
	}

	public void setPricePerLiter(BigDecimal pricePerLiter) {
		this.pricePerLiter = pricePerLiter;
	}

	public Pump getPump() {
		return pump;
	}

	public void setPump(Pump pump) {
		this.pump = pump;
	}

	public FuelType getFuelType() {
		return fuelType;
	}

	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

}
