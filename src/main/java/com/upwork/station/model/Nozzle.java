package com.upwork.station.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a nozzle in a pump
 * 
 * @author ikrussirl
 *
 */
public class Nozzle {
	
	private int id;
	private String marketingName;
	private BigDecimal pricePerLitre;
	private BigDecimal refuelAmount;
	private FuelType fuelType;
	
	public Nozzle(int id, String marketingName, BigDecimal pricePerLitre, FuelType fuelType) {
		this.id = id;
		this.marketingName = marketingName;
		this.pricePerLitre = pricePerLitre;
		this.fuelType = fuelType;
		refuelAmount = BigDecimal.ZERO;
	}
	
	protected void refuel(BigDecimal amount) {
		refuelAmount = amount;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMarketingName() {
		return marketingName;
	}
	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}
	public BigDecimal getPricePerLitre() {
		return pricePerLitre;
	}
	public void setPricePerLitre(BigDecimal pricePerLitre) {
		this.pricePerLitre = pricePerLitre;
	}
	public FuelType getFuelType() {
		return fuelType;
	}
	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}
	
	public BigDecimal getRefuelAmount() {
		return refuelAmount;
	}

	public void reset() {
		refuelAmount = BigDecimal.ZERO;
	}

	public BigDecimal getLitersRefuled() {
		return refuelAmount.divide(pricePerLitre, RoundingMode.HALF_UP);
	}
	
	@Override
	public String toString() {
		return this.getId() + " - " + this.getFuelType();
	}

}
