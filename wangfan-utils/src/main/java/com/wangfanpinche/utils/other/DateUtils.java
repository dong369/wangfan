package com.wangfanpinche.utils.other;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
	
	/**
	 * Date ==> string
	 * @param date
	 * @param pattern 规则
	 * @return
	 */
	public static String toString(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	

	/**
	 * LocalDateTime ==> string
	 * @param date
	 * @param pattern 规则
	 * @return
	 */
	public static String toString(LocalDateTime date, String pattern) {
		return date.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * LocalDate ==> string
	 * @param date
	 * @param pattern 规则
	 * @return
	 */
	public static String toString(LocalDate date, String pattern) {
		return date.format(DateTimeFormatter.ofPattern(pattern));
	}
	
	/**
	 * Date ==> string
	 * @param date
	 * @param pattern 规则
	 * @return
	 * @throws ParseException 
	 */
	public static Date parse(String date, String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(date);
	}
	

}
