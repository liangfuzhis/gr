/**
 * @Title: DateUtil.java
 * @Package com.freeg.payment.controller.action
 * @Description: TODO
 * @author lfa
 * @date 2019.09.16
 * @version V1.0
 */
package com.gr.security.sso.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;

import javax.transaction.SystemException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <pre>
 * Description:
 * Date utility class
 * @since 1.0
 * </pre>
 */
public final class DateUtil {
	
	/**
	 * 时间格式,VO使用
	 */

    public static final String TIME_ZONE_VO = "GMT+8" ;

	public static final String TIME_FORMAT_VO = "yyyy-MM-dd HH:mm:ss" ;

    private static String defaultDatePattern = "yyyy-MM-dd HH:mm:ss";

    public static String simpleDatePattern = "yyyy-MM-dd";

    public static String simpleDate = "yyyy.MM.dd";

    public static String dateLongStringPattern = "yyyyMMddHHmmss";

    public static String dateBigLongStringPattern = "yyyyMMddHHmmssSSS";

    public static String dateStringPattern = "yyyyMMddHHmm";

    public static String dateSimplePattern = "yyyyMMdd";
    
    public static String dateSimpleMonthPattern = "yyyyMM";

    /**
     * number of milliseconds in a standard second.
     */
    public static final int MILLIS_IN_SECOND = 1000;

    /**
     * number of milliseconds in a standard minute.
     */
    public static final int MILLIS_IN_MINUTE = 60 * 1000;

    /**
     * number of milliseconds in a standard hour.
     */
    public static final int MILLIS_IN_HOUR = 60 * 60 * 1000;

    /**
     * number of milliseconds in a standard day.
     */
    public static final int MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

    /**
     * Instanced date format map
     */
    //-----------------------------------------------------------------------

    /**
     * This class can not be instanced from external.
     */
    private DateUtil() {
    }

    /**
     * 获取年月日时分秒毫秒字符串
     * @return
     */
    public static String getDateBigLongString() {
        return format(new Date(), dateBigLongStringPattern);
    }

    public static int getDateYear() {
        return new Date().getYear() + 1900;
    }

    /**
     * 获得默认的 date pattern 
     */
    public static String getDatePattern() {
        return defaultDatePattern;
    }

    /**
     *
     * @Title: format
     * @Description: 使用预设Format格式化Date成字符串
     * @param date
     * @return: String
     */
    public static String format(Date date) {
        return date == null ? " " : format(date, getDatePattern());
    }

    /**
     * 使用参数Format格式化Date成字符串
     */
    public static String format(Date date, String pattern) {
        return date == null ? " " : new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 使用预设格式将字符串转为Date
     */
    public static Date parseDate(String strDate) throws ParseException {
        return StringUtils.isBlank(strDate) ? null : parse(strDate,
                getDatePattern());
    }

    /**
     * 使用参数Format将字符串转为Date
     */
    public static Date parse(String strDate, String pattern)
            throws ParseException {
        return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(
                pattern).parse(strDate);
    }

    //-----------------------------------------------------------------------

    /**
     * Generate a key by pattern and zone
     * @param pattern the pattern
     * @param zone the time zone
     *
     * @return pattern + zone.getID()
     */
    @SuppressWarnings("unused")
    private static String getCacheKey(String pattern, TimeZone zone) {
        StringBuffer key = new StringBuffer(30);
        key.append(pattern);
        key.append(zone.getID());

        return key.toString();
    }

    //-----------------------------------------------------------------------

    /**
     * Get current date base on the default time zone
     *
     * @return current date
     */
    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * Get current date base on the passed time zone
     * if passed time zone is null, using the default time zone
     * </pre>
     * @return current date
     */
    public static Date getCurrentDate(TimeZone zone) {
        return Calendar.getInstance(zone == null ? TimeZone.getDefault() : zone).getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * get the current date without hour, minute, second & millisecond 
     * @return current date that hour, minute, second & millisecond set dto zero
     */
    public static Date getSimpleDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * Convert date dto the simple date.
     * Its hour, minute, second and millisecond field will be set dto zero.
     * </pre>
     * @param date  Original Date
     * @return date that hour, minute, second & millisecond set dto zero
     * @throws SystemException if the date is null
     */
    public static Date getSimpleDate(Date date) throws SystemException {
        if (date == null) {
            throw new SystemException("the date must not be null");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * get the current date as a java.sql.Date
     * @return java SQL date
     */
    public static java.sql.Date getSqlDate() {
        return getSqlDate(getCurrentDate());
    }

    //-----------------------------------------------------------------------

    /**
     * Cast java util date dto sql date.
     * @param date the date dto cast
     * @return java sql date; given null, return null
     */
    public static java.sql.Date getSqlDate(Date date) {
        if (date == null) {
            return null;
        }

        return new java.sql.Date(date.getTime());
    }

    //-----------------------------------------------------------------------
    public static String convertDateFormat4JQuery(String format) {
        //convert year 
        if (format.indexOf("yyyy") >= 0) {
            format = format.replace("yyyy", "yy");
        } else if (format.indexOf("yy") >= 0) {
            format = format.replace("yy", "y");
        }

        //convert month
        if (format.indexOf("MMMMM") >= 0) {
            format = format.replace("MMMMM", "MM");
        } else if (format.indexOf("MMM") >= 0) {
            format = format.replace("MMM", "M");
        } else if (format.indexOf("MM") >= 0) {
            format = format.replace("MM", "mm");
        } else if (format.indexOf("M") >= 0) {
            format = format.replace("M", "m");
        }

        return format;
    }

    //-----------------------------------------------------------------------

    /**
     * dto judge the passed date is just include year, month & day
     * @param d
     * @return null or date time/time stamp return false otherwise return true
     */
    public static boolean isDate(Date d) {
        if (d == null) {
            return false;
        }

        Calendar cal = createDate(d);
        if (cal.get(Calendar.HOUR_OF_DAY) == 0
                && cal.get(Calendar.MINUTE) == 0
                && cal.get(Calendar.SECOND) == 0
                && cal.get(Calendar.MILLISECOND) == 0) {
            return true;
        }
        return false;
    }

    //-----------------------------------------------------------------------
    @SneakyThrows
    private static Calendar createDate(Date d) {
        if (d == null) {
            throw new SystemException("the date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal;
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * Adds or subtracts the specified amount of time dto the given calendar field, based on the calendar's rules. For example, dto subtract 5 days from the current time of the calendar, you can achieve it by calling:
     * add(Calendar.DAY_OF_MONTH, -5).
     * </pre>
     * @param d the date dto add
     * @param field the calendar field
     * @param amount the amount of date or time dto be added dto the field.
     * @return the new date object with the amount added
     */
    @SneakyThrows
    public static Date add(Date d, int field, int amount) {
        if (d == null) {
            throw new SystemException("the date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(field, amount);
        return cal.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * Adds a number of years dto a date returning a new object.
     * The original date object is unchanged.
     * </pre>
     * @param d the date, not null
     * @param amount the amount dto add, may be negative
     * @return the new date object with the amount added
     * @throws if the date is null
     */
    public static Date addYears(Date d, int amount) {
        return add(d, Calendar.YEAR, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * Adds a number of months dto a date returning a new object.
     * The original date object is unchanged.
     * </pre>
     * @param d the date, not null
     * @param amount the amount dto add, may be negative
     * @return the new date object with the amount added
     * @throws if the date is null
     */
    public static Date addMonths(Date d, int amount) {
        return add(d, Calendar.MONTH, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * Adds a number of weeks dto a date returning a new object.
     * The original date object is unchanged.
     * </pre>
     * @param d the date, not null
     * @param amount the amount dto add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addWeeks(Date d, int amount) {
        return add(d, Calendar.WEEK_OF_YEAR, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * Adds a number of days dto a date returning a new object.
     * The original date object is unchanged.
     * </pre>
     * @param d the date, not null
     * @param amount the amount dto add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addDays(Date d, int amount) {
        return add(d, Calendar.DAY_OF_MONTH, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * Adds a number of hours dto a date returning a new object.
     * The original date object is unchanged.
     * </pre>
     * @param d the date, not null
     * @param amount the amount dto add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addHours(Date d, int amount) {
        return add(d, Calendar.HOUR_OF_DAY, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * Adds a number of minutes dto a date returning a new object.
     * The original date object is unchanged.
     * </pre>
     * @param d the date, not null
     * @param amount the amount dto add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addMinutes(Date d, int amount) {
        return add(d, Calendar.MINUTE, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * Adds a number of seconds dto a date returning a new object.
     * The original date object is unchanged.
     * </pre>
     * @param d the date, not null
     * @param amount the amount dto add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addSeconds(Date d, int amount) {
        return add(d, Calendar.SECOND, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * Adds a number of milliseconds dto a date returning a new object.
     * The original date object is unchanged.
     * </pre>
     * @param d the date, not null
     * @param amount the amount dto add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addMilliseconds(Date d, int amount) {
        return add(d, Calendar.MILLISECOND, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * get the day of week for current date
     * SUNDAY: 1
     * MONDAY: 2
     * TUESDAY: 3
     * WEDNESDAY: 4
     * THURSDAY: 5
     * FRIDAY: 6
     * SATURDAY: 7
     *
     * </pre>
     * @return day of week index
     */
    public static int dayOfWeek() {
        return dayOfWeek(getCurrentDate());
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * get the day of week for the specified date
     * SUNDAY: 1
     * MONDAY: 2
     * TUESDAY: 3
     * WEDNESDAY: 4
     * THURSDAY: 5
     * FRIDAY: 6
     * SATURDAY: 7
     *
     * </pre>
     * @return day number of week
     */
    public static int dayOfWeek(Date d) {
        return createDate(d).get(Calendar.DAY_OF_WEEK);
    }

    //-----------------------------------------------------------------------

    /**
     * get the day of month for current date
     * @return day of month index
     */
    public static int dayOfMonth() {
        return dayOfMonth(getCurrentDate());
    }

    //-----------------------------------------------------------------------

    /**
     * get the day of month for the specified date
     * @return day number of month
     */
    public static int dayOfMonth(Date d) {
        return createDate(d).get(Calendar.DAY_OF_MONTH);
    }

    //-----------------------------------------------------------------------

    /**
     * get the day of year for current date
     * @return day of year index
     */
    public static int dayOfYear() {
        return dayOfYear(getCurrentDate());
    }

    //-----------------------------------------------------------------------

    /**
     * get the day of year for the specified date
     * @return day number of year
     */
    public static int dayOfYear(Date d) {
        return createDate(d).get(Calendar.DAY_OF_YEAR);
    }

    //-----------------------------------------------------------------------

    /**
     * get the week number of year for current date
     * @return week number of year
     */
    public static int weekOfYear() {
        return weekOfYear(getCurrentDate());
    }

    //-----------------------------------------------------------------------

    /**
     * get the week number of year for the specified date
     * @return week number of year
     */
    public static int weekOfYear(Date d) {
        return createDate(d).get(Calendar.WEEK_OF_YEAR);
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * get the first date of current date
     * current date is '2012-05-10' returns '2010-05-01'
     * </pre>
     * @return the first date of current date month
     */
    public static Date firstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * get the first date of the specified date
     * @return the first date of current date month
     */
    public static Date firstDayOfMonth(Date d) {
        Calendar cal = createDate(d);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * get the last date of current date
     * current date is '2012-02-10' returns '2010-02-29'
     * </pre>
     * @return the last date of current date month
     */
    public static Date lastDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * <pre>
     * get the last date of the specified date
     * current date is '2012-02-10' returns '2010-02-29'
     * </pre>
     * @return the last date of current date month
     */
    public static Date lastDayOfMonth(Date d) {
        Calendar cal = createDate(d);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * get the interval number of days for the specified dates
     * @param d1 date one, can not be null
     * @param d2 date two, can not be null
     * @return the interval number, if d1 is greater, a positive number returned, else if d2 is greater, a negative number returned, otherwise return 0
     */
    public static int dateDiff(Date d1, Date d2) throws SystemException {
        if (d1 == null || d2 == null) {
            throw new SystemException("passed date cannot be null");
        }

        return (int) ((getSimpleDate(d1).getTime() - getSimpleDate(d2).getTime()) / MILLIS_IN_DAY);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the year field dto a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount the amount dto set
     * @return a new Date object set with the specified value
     */
    public static Date setYear(Date date, int amount) {
        return set(date, Calendar.YEAR, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the month field dto a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount the amount dto set
     * @return a new Date object set with the specified value
     */
    public static Date setMonth(Date date, int amount) {
        return set(date, Calendar.MONTH, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the day of month field dto a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount the amount dto set
     * @return a new Date object set with the specified value
     */
    public static Date setDay(Date date, int amount) {
        return set(date, Calendar.DAY_OF_MONTH, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the hour field dto a date returning a new object.  Hours range
     * from  0-23.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount the amount dto set
     * @return a new Date object set with the specified value
     */
    public static Date setHour(Date date, int amount) {
        return set(date, Calendar.HOUR_OF_DAY, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the minute field dto a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount the amount dto set
     * @return a new Date object set with the specified value
     */
    public static Date setMinute(Date date, int amount) {
        return set(date, Calendar.MINUTE, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the second field dto a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount the amount dto set
     * @return a new Date object set with the specified value
     */
    public static Date setSecond(Date date, int amount) {
        return set(date, Calendar.SECOND, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the millisecond field dto a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount the amount dto set
     * @return a new Date object set with the specified value
     */
    public static Date setMillisecond(Date date, int amount) {
        return set(date, Calendar.MILLISECOND, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Sets the specified field dto a date returning a new object.
     * This does not use a lenient calendar.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param calendarField  the calendar field dto set the amount dto
     * @param amount the amount dto set
     * @return a new Date object set with the specified value
     */
    @SneakyThrows
    private static Date set(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new SystemException("The date must not be null");
        }
        Calendar c = createDate(date);
        c.set(calendarField, amount);
        return c.getTime();
    }

    /**
     *
     * @Title: getCurrentTimeMillis
     * @Description: 获取当前的时间戳
     * @param @return  
     * @return Long
     * @throws
     */
    public static Long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static String getTimePath() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "/" + month + "/" + day + "/";
    }

    /**
     * 获取当天 00:00:00
     * @Title: getStartTimeOfDay
     * @Description: TODO
     * @param @param date
     * @param @return
     * @return Date
     * @throws
     */
    public static Date getStartTimeOfDay() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return (Date) currentDate.getTime().clone();
    }

    /**
     * 获取当天 23:59:59
     * @Title: getEndTimeOfDay
     * @Description: TODO
     * @param @param date
     * @param @return
     * @return Date
     * @throws
     */
    public static Date getEndTimeOfDay() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return (Date) currentDate.getTime().clone();
    }

    /**
     * 获取昨天
     * @Title: getStartTimeOfDay
     * @Description: TODO
     * @param @param date
     * @param @return
     * @return Date
     * @throws
     */
    public static Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * 获取该天前七天
     * @Title: getEndTimeOfDay
     * @Description: TODO
     * @param @param date
     * @param @return
     * @return Date
     * @throws
     */
    public static Date getBeforeSevenDay(Date date) {
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.add(Calendar.DATE, -7);
        return day.getTime();
    }


    public static int getDateSpace(Date date1, Date date2)
            throws ParseException {

        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();

        calst.setTime(date1);
        caled.setTime(date2);

        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int days = ((int)(caled.getTime().getTime()/1000)-(int)(calst.getTime().getTime()/1000))/3600/24;

        return days;
    }
    /**
     *    获取当前时间到零点的秒数
     * @author zengkai
     * @date 2018/7/10 16:34
     * @param
     * @return
     */
    public static long getTomorrowZeroSeconds() {
        long current = System.currentTimeMillis();// 当前时间毫秒数
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long tomorrowzero = calendar.getTimeInMillis();
        long tomorrowzeroSeconds = (tomorrowzero - current) / 1000;
        return tomorrowzeroSeconds;
    }

    /**
     *    获取当前时间的年和季度信息，返回格式 2018/3
     * @author zengkai
     * @date 2018/8/4 11:13
     * @return
     */
    public static String getYearAndQuarter(){
        String result = "";
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;
        switch (month){
            case 1:
            case 2:
            case 3:
                result = year+"/"+1;
                break;
            case 4:
            case 5:
            case 6:
                result = year+"/"+2;
                break;
            case 7:
            case 8:
            case 9:
                result = year+"/"+3;
                break;
            case 10:
            case 11:
            case 12:
                result = year+"/"+4;
                break;
            default:
                break;
        }
        return result;
    }
    
    
    /**
     * 
     * @Title:		isValidDate
     * @Description:验证时间格式是否正确
     * @param dateStr 时间内容字串
     * @param pattern 时间格式
     * @return
     * boolean
     * @throws:
     */
	public static boolean isValidDate(String dateStr,String pattern) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(dateStr);
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}

    public static void main(String[] args) throws ParseException {
    	
    	String aa = "1-2-11" ;
    	System.out.println(isValidDate(aa, simpleDatePattern));
    	
    }
}
