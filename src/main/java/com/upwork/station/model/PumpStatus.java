package com.upwork.station.model;

public enum PumpStatus {

	STAND_BY ("Stand By"),
	READY ("Ready"),
	PAYMENT_DUE("Payment Due");
	
	PumpStatus(String displayText) {
		this.displayText = displayText;
	}
	
	private String displayText;
	
	public String getDisplayText() {
		return displayText;
	}
	
}
