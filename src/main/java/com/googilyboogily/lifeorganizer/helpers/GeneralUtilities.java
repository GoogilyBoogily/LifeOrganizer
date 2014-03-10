package com.googilyboogily.lifeorganizer.helpers;

import android.text.format.Time;

public class GeneralUtilities {

	// Returns a prettily formatted time/date
	public static String getCurrentTimeAndDate() {
		String currentTime;

		Time now = new Time(Time.getCurrentTimezone());
		now.setToNow();

		// Time/date format: hh:MM:SS am/pm Month DD, YYYY: "%I:%M:%S %p  %B %d, %Y"
		// Link for formatting: http://www.cplusplus.com/reference/ctime/strftime/
		currentTime = now.format("%I:%M:%S %p  %B %d, %Y");

		if (currentTime.startsWith("0")) {
			currentTime = currentTime.replaceFirst("0", "");
		} // end if

		return currentTime;
	} // end getCurrentTimeAndDate()

} // end class
