package edu.vanderbilt.cqs;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

public final class Utils {
	public static String md5(String input) {
		if (null == input)
			return null;

		try {
			// Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			// Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			// Converts message digest value in base 16 (hex)
			return new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return input;
		}
	}

	public static Date getNextDay(Date from, int dayOfWeek) {
		Calendar now = Calendar.getInstance();

		if (from != null) {
			now.setTime(from);
			now.add(Calendar.DAY_OF_YEAR, 1);
		}

		while (true) {
			int weekday = now.get(Calendar.DAY_OF_WEEK);

			if (weekday == dayOfWeek) {
				break;
			}

			now.add(Calendar.DAY_OF_YEAR, 1);
		}

		return now.getTime();
	}

}
