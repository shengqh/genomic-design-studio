package edu.vanderbilt.cqs;

import java.util.Calendar;

public final class Holiday {
	private static Calendar newDate(int nYear, int nMonth, int nDay) {
		Calendar cal = Calendar.getInstance();
		cal.set(nYear, nMonth, nDay);
		return cal;
	}

	public static Calendar NewYearsDayObserved(int nYear) {
		int nMonth = Calendar.JANUARY; // January
		int nMonthDecember = Calendar.DECEMBER; // December

		Calendar cal = Calendar.getInstance();
		cal.set(nYear, nMonth, 1);

		int nX = cal.get(Calendar.DAY_OF_WEEK);
		switch (nX) {
		case 0: // Sunday
			return newDate(nYear, nMonth, 2);
		case 1: // Monday
		case 2: // Tuesday
		case 3: // Wednesday
		case 4: // Thursday
		case 5: // Friday
			return newDate(nYear, nMonth, 1);
		default:
			// Saturday, then observe on friday of previous year
			return newDate(--nYear, nMonthDecember, 31);
		}
	}

	public static Calendar MemorialDayObserved(int nYear) {
		// Last Monday in May
		int nMonth = Calendar.MAY; // May

		Calendar cal = Calendar.getInstance();
		cal.set(nYear, nMonth, 31);

		int nX = cal.get(Calendar.DAY_OF_WEEK);
		switch (nX) {
		case 0: // Sunday
			return newDate(nYear, nMonth, 25);
		case 1: // Monday
			return newDate(nYear, nMonth, 31);
		case 2: // Tuesday
			return newDate(nYear, nMonth, 30);
		case 3: // Wednesday
			return newDate(nYear, nMonth, 29);
		case 4: // Thursday
			return newDate(nYear, nMonth, 28);
		case 5: // Friday
			return newDate(nYear, nMonth, 27);
		default: // Saturday
			return newDate(nYear, nMonth, 26);
		}
	}

	public static Calendar IndependenceDayObserved(int nYear) {
		int nMonth = Calendar.JULY; // May

		Calendar cal = Calendar.getInstance();
		cal.set(nYear, nMonth, 4);

		int nX = cal.get(Calendar.DAY_OF_WEEK);
		switch (nX) {
		case 0: // Sunday
			return newDate(nYear, nMonth, 5);
		case 1: // Monday
		case 2: // Tuesday
		case 3: // Wednesday
		case 4: // Thursday
		case 5: // Friday
			return newDate(nYear, nMonth, 4);
		default:
			// Saturday
			return newDate(nYear, nMonth, 3);
		}
	}

	public static Calendar LaborDayObserved(int nYear) {
		// The first Monday in September
		int nMonth = Calendar.SEPTEMBER;

		Calendar cal = Calendar.getInstance();
		cal.set(nYear, nMonth, 1);

		int nX = cal.get(Calendar.DAY_OF_WEEK);
		switch (nX) {
		case 0: // Sunday
			return newDate(nYear, nMonth, 2);
		case 1: // Monday
			return newDate(nYear, nMonth, 7);
		case 2: // Tuesday
			return newDate(nYear, nMonth, 6);
		case 3: // Wednesday
			return newDate(nYear, nMonth, 5);
		case 4: // Thursday
			return newDate(nYear, nMonth, 4);
		case 5: // Friday
			return newDate(nYear, nMonth, 3);
		default: // Saturday
			return newDate(nYear, nMonth, 2);
		}
	}

	public static Calendar ThanksgivingObserved(int nYear) {
		int nMonth = Calendar.NOVEMBER;

		Calendar cal = Calendar.getInstance();
		cal.set(nYear, nMonth, 1);

		int nX = cal.get(Calendar.DAY_OF_WEEK);
		switch (nX) {
		case 0: // Sunday
			return newDate(nYear, nMonth, 26);
		case 1: // Monday
			return newDate(nYear, nMonth, 25);
		case 2: // Tuesday
			return newDate(nYear, nMonth, 24);
		case 3: // Wednesday
			return newDate(nYear, nMonth, 23);
		case 4: // Thursday
			return newDate(nYear, nMonth, 22);
		case 5: // Friday
			return newDate(nYear, nMonth, 28);
		default: // Saturday
			return newDate(nYear, nMonth, 27);
		}
	}

	public static Calendar ChristmasDayObserved(int nYear) {
		int nMonth = Calendar.DECEMBER;

		Calendar cal = Calendar.getInstance();
		cal.set(nYear, nMonth, 25);

		int nX = cal.get(Calendar.DAY_OF_WEEK);
		switch (nX) {
		case 0: // Sunday
			return newDate(nYear, nMonth, 26);
		case 1: // Monday
		case 2: // Tuesday
		case 3: // Wednesday
		case 4: // Thursday
		case 5: // Friday
			return newDate(nYear, nMonth, 25);
		default:
			// Saturday
			return newDate(nYear, nMonth, 24);
		}
	}

	private static boolean isSameDay(Calendar day, Calendar anotherDay) {
		if (day.get(Calendar.YEAR) != anotherDay.get(Calendar.YEAR)) {
			return false;
		}

		if (day.get(Calendar.DAY_OF_YEAR) != anotherDay
				.get(Calendar.DAY_OF_YEAR)) {
			return false;
		}

		return true;
	}

	public static boolean isHoliday(Calendar day) {
		int year = day.get(Calendar.YEAR);

		if (isSameDay(day, NewYearsDayObserved(year))) {
			return true;
		}

		if (isSameDay(day, MemorialDayObserved(year))) {
			return true;
		}

		if (isSameDay(day, IndependenceDayObserved(year))) {
			return true;
		}

		if (isSameDay(day, LaborDayObserved(year))) {
			return true;
		}

		if (isSameDay(day, ThanksgivingObserved(year))) {
			return true;
		}

		if (isSameDay(day, ChristmasDayObserved(year))) {
			return true;
		}

		return false;
	}
}
