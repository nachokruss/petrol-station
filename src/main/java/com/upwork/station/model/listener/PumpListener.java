package com.upwork.station.model.listener;

/**
 * This interface represents listeners that can listen on events triggred by
 * Pumps
 * 
 * @author ikrussirl
 *
 */
public interface PumpListener {

	public void statusChanged(StatusChangedEvent event);

}
