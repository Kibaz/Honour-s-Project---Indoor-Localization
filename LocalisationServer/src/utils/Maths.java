package utils;

public class Maths {
	
	// Constants acquired by experimentation and calibration
	private static final double SIGNAL_STR_AT_1_METRE = -30;
	/* Range of environmental factor from 2-4 */
	private static final double SIGNAL_DECAY = 4; // Signal Decay exponent
	private static final double PATH_LOSS = 0;
	
	private static final double WAVELENGTH = 0.125;
	
	
	/*
	 * Calculate the distance using RSSI (Received Signal Strength Indicator)
	 * Based on the known signal strength at 1 metre
	 * Formula:- Distance = 10 ^ (Measured Power(RSSI at 1 Metre) - RSSI /10 * N)
	 */
	public static double calculateDistanceFromRSSI(double receivedStr)
	{
		return Math.pow(10, (SIGNAL_STR_AT_1_METRE - receivedStr) / (10 * SIGNAL_DECAY));
	}
	
	/*
	 * General formula for determining path loss at a certain distance
	 * With consideration of fade margin of RF signals is given by
	 * P(X) = 10nlog(d/d0)+ 20 log((4 * PI * d0) / Wavelength of 2.4GHz signal)
	 * Where n = signal decay exponent
	 * d = distance between transmitter and receiver
	 * d0 = reference distance (typically 1m)
	 */
	
	/*
	 * Second distance approximation method taking into account
	 * Path loss, wavelength of 2.4GHz signal and fade margin
	 */
	public static double calculateDistanceFromRSSIWithPathLoss(double receivedStr)
	{
		return Math.pow(10, (PATH_LOSS - (20*Math.log((4*Math.PI*1)/WAVELENGTH))/10 * SIGNAL_DECAY));
	}

}
