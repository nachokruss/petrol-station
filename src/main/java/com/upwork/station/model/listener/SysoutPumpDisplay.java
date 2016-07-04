package com.upwork.station.model.listener;

/**
 * 
 * Listener that prints the pump status to the standard output as it changes
 * 
 * @author ikrussirl
 *
 */
public class SysoutPumpDisplay implements PumpListener {

	private Integer pumpNumber;

	public SysoutPumpDisplay(Integer pumpNumber) {
		this.pumpNumber = pumpNumber;
	}

	public void statusChanged(StatusChangedEvent event) {
		String displayText = event.getNewStatus().getDisplayText();
		System.out.println("PumpDisplay [Pump: " + pumpNumber + "] - Status: \"" + displayText + "\"");
	}

}
