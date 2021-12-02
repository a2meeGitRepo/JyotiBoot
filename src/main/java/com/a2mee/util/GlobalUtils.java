package com.a2mee.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * @author admin
 *
 */
public final class GlobalUtils {

	/**
	 * @see Will return a secure random string literal.
	 * @return String
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * @see will return a 6 digit random integer.
	 * @return integer
	 */
	public static int randomInt() {
		return new Random().nextInt(999999);
	}

	/**
	 * @see Will return true if given object is null.
	 * @param Object
	 * @return boolean
	 */
	public static boolean isNull(Object anObj) {
		return anObj == null ? true : false;
	}

	/**
	 * @see Will return true if given object is empty.
	 * @param Collection
	 * @return boolean
	 */
	public static boolean isEmpty(Collection<?> anObj) {
		return anObj != null ? anObj.isEmpty() : true;
	}
	
	/**
	 * @see Will return a system default local date instance
	 * @param java.util.Date
	 * @return java.time.LocalDate
	 */
	public static LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * @see Will return a system default LocalDateTime instance
	 * @param java.util.Date
	 * @return java.time.LocalDateTime
	 */
	public static LocalDateTime toLocalDateTime(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static LocalDateTime getCurrentDateTime() {
		return LocalDateTime.now();
	}

	/**
	 * @see will return integer value 0 if dates are equal, will return 1 if
	 *      date1 is after date2 and will return -1 if date1 is before date2
	 * @param date1
	 * @param date2
	 * @return int
	 */
	public static int compareDate(Date date1, Date date2) {
		return isNull(date1) && isNull(date2) ? 0 : date1.compareTo(date2);
	}

	/**
	 * @see Will compare two strings and return result either true or false
	 * @param param_one
	 * @param param_two
	 * @return boolean
	 */
	public static boolean compareStrings(String param_one, String param_two) {
		return !isNull(param_one) && !isNull(param_two) ? param_one.equals(param_two) : false;
	}

	/**
	 * @see Will split given string from given regex and returns String[].
	 * @param input
	 * @param regex
	 * @return String[]
	 */
	public static String[] splitString(String input, String regex) {
		return !isNull(input) && !isNull(regex) ? input.split(regex) : null;
	}

	public static boolean isStringNullOrEmpty(String stringObj) {
		return !isNull(stringObj) ? stringObj.equals("") : true;
	}

	/**
	 * @see Will return dd/MM/yy format String date
	 * @param date
	 * @return String
	 */
	public static String getDate(Date date) {
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}
	/*
	 *	pass util date 
	 *	and in out by given 
	 *	Iot date and time format
	 *	
	 *	created by suraj
	 */
	public static String getIotDateTimeFormat(Date date) {
		return new SimpleDateFormat("yyyyMMddhhmmss").format(date);
	}

	/**
	 * @throws NumberFormatException
	 * @param double
	 * @return String
	 * @see This method will convert and return any valid BigDecimal value into
	 *      indian currency format String.
	 */
	public static String toINR(double amount) throws NumberFormatException {
		return NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(amount);
	}
	
	
	public static LocalDate toDate(String date) {
		String format = "yyyy-M-d";
		return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
	}
	
	public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
	    return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
	}
	
}
