package com.upwork.station.model.listener;

import com.upwork.station.model.Pump;
import com.upwork.station.model.PumpStatus;

/**
 * This event is triggered when the status of a Pump is changed
 * 
 * @author ikrussirl
 *
 */
public class StatusChangedEvent {

	private PumpStatus oldStatus;
	private PumpStatus newStatus;
	private Pump pump;

	public StatusChangedEvent(PumpStatus oldStatus, PumpStatus newStatus, Pump pump) {
		super();
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
		this.pump = pump;
	}

	public PumpStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(PumpStatus oldStatus) {
		this.oldStatus = oldStatus;
	}

	public PumpStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(PumpStatus newStatus) {
		this.newStatus = newStatus;
	}

	public Pump getPump() {
		return pump;
	}

	public void setPump(Pump pump) {
		this.pump = pump;
	}

}
