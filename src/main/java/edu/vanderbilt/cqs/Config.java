package edu.vanderbilt.cqs;

import java.util.Calendar;
import java.util.Date;

public abstract class Config {
	public static int DefaultLimitCountPerDay = 7;
	
	public static int DefaultCloseRegistrationHour = 14;
	
	public static int LimitCountPerDay = DefaultLimitCountPerDay;

	public static int CloseRegistrationHour = DefaultCloseRegistrationHour;
	
	public static boolean hasPassed(Date day) {
		Calendar sday = Calendar.getInstance();
		sday.setTime(day);
		sday.set(Calendar.HOUR_OF_DAY, CloseRegistrationHour);
		sday.set(Calendar.MINUTE, 0);
		sday.set(Calendar.SECOND, 0);
		Calendar now = Calendar.getInstance();
		return sday.before(now);
	}
}
