package utils;

public class Maths {
	
	private static final double SIGNAL_STR_AT_1_METRE = -40;
	/* Range of environmental factor from 2-4 */
	private static final double ENV_FACTOR = 2;
	
	/*
	 * Calculate the distance using RSSI (Received Signal Strength Indicator)
	 * Based on the known signal strength at 1 metre
	 * Formula:- Distance = 10 ^ (Measured Power(RSSI at 1 Metre) - RSSI /10 * N)
	 */
	public static double calculateDistanceFromRSSI(double receivedStr)
	{
		return Math.pow(10, (SIGNAL_STR_AT_1_METRE - receivedStr) / (10 * ENV_FACTOR));
	}

}
