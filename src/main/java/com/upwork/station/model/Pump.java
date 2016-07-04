package com.upwork.station.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.upwork.station.exception.ValidationException;
import com.upwork.station.model.listener.PumpListener;
import com.upwork.station.model.listener.StatusChangedEvent;

/**
 * Represents a Pump in a service station
 * 
 * @author ikrussirl
 *
 */
public class Pump {
	
	private Integer id;
	private PumpStatus status;
	private Map<Integer, Nozzle> nozzles = new HashMap<Integer, Nozzle>();
	private Collection<PumpListener> listeners = new ArrayList<PumpListener>();
	private Nozzle usedNozzle;
	
	public Pump(Integer id) {
		this.id = id;
		this.status = PumpStatus.READY;
		addNozzle(1, "mkt name", new BigDecimal("1.30"), FuelType.UNLEADED);
		addNozzle(2, "mkt name", new BigDecimal("1.20"), FuelType.DIESEL);
	}

	private void addNozzle(Integer id, String marketingName, BigDecimal pricePerLiter, FuelType fuelType) {
		Nozzle nozzle = new Nozzle(id, marketingName, pricePerLiter, fuelType);
		nozzles.put(nozzle.getId(), nozzle);
	}
	
	/**
	 * lifts a given nozzle
	 * 
	 * @param nozzleId nozzle to lift
	 * @return
	 * @throws ValidationException
	 */
	public Nozzle lift(Integer nozzleId) throws ValidationException {
		checkValidNozzleId(nozzleId);
		setStatus(PumpStatus.STAND_BY);
		return nozzles.get(nozzleId);
	}
	
	/**
	 * Places a Nozzle back in place.
	 * @param nozzle
	 */
	public void placeBack(Nozzle nozzle) {
		setStatus(PumpStatus.PAYMENT_DUE);
		usedNozzle = nozzle;
	}
	
	/**
	 * Sets the Pump as ready.
	 */
	public void setReady() {
		setStatus(PumpStatus.READY);
	}
	
	/**
	 * Resets nozzles and pump
	 */
	public void reset() {
		for (Nozzle nozzle : nozzles.values()) {
			nozzle.reset();
		}
		usedNozzle = null;
		setReady();
	}
	
	/**
	 * Starts refueling on a given nozzle
	 * 
	 * @param nozzle nozzle to use for refueling
	 * @param amount amount of petrol to refuel
	 * @throws ValidationException
	 */
	public void refuel(Nozzle nozzle, BigDecimal amount) throws ValidationException {
		if (status == PumpStatus.READY) {
			nozzle.refuel(amount);
		} else {
			throw new ValidationException("Cannot refuel until status is set to Ready");
		}
	}
	
	private void checkValidNozzleId(Integer nozzleId) throws ValidationException {
		if (!nozzles.containsKey(nozzleId)) {
			throw new ValidationException("invalid nozzle id: " + nozzleId);
		}
	}
	
	public void addListener(PumpListener pumpListener) {
		listeners.add(pumpListener);
	}
	
	public PumpStatus getStatus() {
		return status;
	}
	
	public Collection<Nozzle> getNozzles() {
		return nozzles.values();
	}
	
	public void setStatus(PumpStatus newStatus) {
		PumpStatus oldStatus = this.status;
		this.status = newStatus;
		notifyListeners(oldStatus, newStatus);
	}
	
	public BigDecimal getTotalAmountDue() {
		return usedNozzle.getRefuelAmount();
	}

	private void notifyListeners(PumpStatus oldStatus, PumpStatus newStatus) {
		for (PumpListener listener : listeners) {
			listener.statusChanged(new StatusChangedEvent(oldStatus, newStatus, this));
		}
	}
	
	public Integer getId() {
		return id;
	}
	
	public Nozzle getUsedNozzle() {
		return usedNozzle;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Pump Number: ")
				.append(this.getId())
				.append(", State: ")
				.append(this.getStatus());
		if (this.getStatus() == PumpStatus.PAYMENT_DUE) {
			sb.append(", Amount Due: ")
			.append(this.getTotalAmountDue())
			.append(", Fuel Type: ")
			.append(this.getUsedNozzle().getFuelType());
		}
		return sb.toString();

	}

}
