package com.ctqh.mobile.common.datatime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * 时间工具
 * 
 * @author serv_dev
 */

public class DateUtil {

	public static long OneDayMill = 24 * 60 * 60 * 1000L;
	/**
	 * 格式为 yyyy-MM-dd HH:mm:ss
	 */
	private static DateTimeFormatter yyyyMMddHHmmss=DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * 时间获取前一天的Calendar
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar dateToPriDayCalendar(Date date) {
		return new DateTime(date).plusDays(-1).toCalendar(null);
	}

	/**
	 * 获取当前时间的下周一0点
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar dateToNextMonDay(Date date) {
		return new DateTime(date).dayOfWeek().withMaximumValue().plusDays(1).toCalendar(null);
	}

	/**
	 * DATE 转换Calendar
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar dateToCalendar(Date date) {
		return new DateTime(date).toCalendar(null);
	}

	/**
	 * 获取当前时间的上以周一0点
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar dateToPriMonDay(Date date) {
		Calendar c = dateToPriDayCalendar(date);
		c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR) - 1);
		c.set(Calendar.DAY_OF_WEEK, 2);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c;
	}

	/**
	 * @param giftDate
	 * @param date
	 */
	public static boolean isSameDay(Date one, Date two) {
		return DateUtils.isSameDay(one, two);
	}

	/**
	 * 得到date的明天此时
	 * @param date
	 */
	public static Date dateToNextDay(Date date) {
		return new DateTime(date).plusDays(1).toDate();

	}

	/**
	 * 某月有多少天，如果是闰年，会把2月+1天
	 * @param c
	 * @return
	 */
	public static int daysInMonth(GregorianCalendar c) {
		int[] daysInMonths = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		daysInMonths[1] += c.isLeapYear(c.get(GregorianCalendar.YEAR)) ? 1 : 0;
		return daysInMonths[c.get(GregorianCalendar.MONTH)];
	}

	/**
	 * 得到这个月还剩余多少天
	 * @param date
	 * @return
	 */
	public static int shenyuDaysInMonth(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int mDays = daysInMonth(calendar);
		return mDays - day;
	}

	public static int getWeek() {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		return dayOfWeek;
	}

	/**
	 * 得到给定日期的毫秒数
	 * @param strDate
	 * @return
	 */
	public static long getMillis(String strDate) {
		long mis = DateTime.parse(strDate, yyyyMMddHHmmss).getMillis();
		return mis;
	}
	/**
	 * 得到给定日期的毫秒数
	 * @param strDate
	 * @return
	 */
	public static long getMillis(String strDate,DateTimeFormatter format) {
		long mis = DateTime.parse(strDate, format).getMillis();
		return mis;
	}

	/**
	 * 格式为yyyy-MM-dd
	 */
	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 返回两个日期相差的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static int getDistDates(Date startDate, Date endDate) {
		try {
			startDate = yyyyMMdd.parse(yyyyMMdd.format(startDate));
			endDate = yyyyMMdd.parse(yyyyMMdd.format(endDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int totalDate = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		long timestart = calendar.getTimeInMillis();
		calendar.setTime(endDate);
		long timeend = calendar.getTimeInMillis();
		totalDate = (int) Math.abs((timeend - timestart) / OneDayMill);
		return totalDate;
	}

	public static String getStringDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public static String getNowStringDate() {
		return DateTime.now().toString(yyyyMMddHHmmss);
	}
	
	/**
	 * 得到给定毫秒日期的0点时的毫秒
	 * @return
	 */
	public static long get0Millis(long millis){
		String nowDD = DateFormatUtils.format(new Date(millis), "yyyy-MM-dd");
		long now = DateTime.parse(nowDD).getMillis();
		return now;
	}
	
	/**
	 * 得到给定毫秒日期的毫秒
	 * @return
	 */
	public static long getMillisOfDay(long millis){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		
		long stamp = h*60*60*1000 + m*60*1000;;
		return stamp;
	}
	
	
	public static String getYYYYMMDD(Date date){
		String nowDD = DateFormatUtils.format(date, "yyyy-MM-dd");
		return nowDD;
	}
	
}
