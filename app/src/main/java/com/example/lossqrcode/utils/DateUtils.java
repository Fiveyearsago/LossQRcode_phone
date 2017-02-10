package com.example.lossqrcode.utils;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
 
public class DateUtils {
 
    private static final long MILLIS_IN_A_SECOND = 1000;
 
    private static final long SECONDS_IN_A_MINUTE = 60;
 
    private static final long MINUTES_IN_AN_HOUR = 60;
 
    private static final long HOURS_IN_A_DAY = 24;
 
    private static final int DAYS_IN_A_WEEK = 7;
 
    private static final int MONTHS_IN_A_YEAR = 12;
 
    //private static final int[] daysInMonth = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
 
    /**
     * ��С���ڣ��趨Ϊ1000��1��1��
     */
    public static final Date MIN_DATE = date(1000, 1, 1);
 
    /**
     * ������ڣ��趨Ϊ8888��1��1��
     */
    public static final Date MAX_DATE = date(8888, 1, 1);
     
 
    /**
     * ���������չ������ڶ���ע���·��Ǵ�1��ʼ�����ģ���monthΪ1����1�·ݡ�
     * @param year ��
     * @param month �¡�ע��1����1�·ݣ��������ơ�
     * @param day ��
     * @return
     */
    public static Date date(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
     
    /**
     * �����������ڣ�������ʱ�䣩֮������������
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static int getYearDiff(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new InvalidParameterException(
                    "date1 and date2 cannot be null!");
        }
        if (date1.after(date2)) {
            throw new InvalidParameterException("date1 cannot be after date2!");
        }
 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH);
        int day1 = calendar.get(Calendar.DATE);
 
        calendar.setTime(date2);
        int year2 = calendar.get(Calendar.YEAR);
        int month2 = calendar.get(Calendar.MONTH);
        int day2 = calendar.get(Calendar.DATE);
 
        int result = year2 - year1;
        if (month2 < month1) {
            result--;
        } else if (month2 == month1 && day2 < day1) {
            result--;
        }
        return result;
    }
 
    /**
     * �����������ڣ�������ʱ�䣩֮������������
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static int getMonthDiff(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new InvalidParameterException(
                    "date1 and date2 cannot be null!");
        }
        if (date1.after(date2)) {
            throw new InvalidParameterException("date1 cannot be after date2!");
        }
 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH);
        int day1 = calendar.get(Calendar.DATE);
 
        calendar.setTime(date2);
        int year2 = calendar.get(Calendar.YEAR);
        int month2 = calendar.get(Calendar.MONTH);
        int day2 = calendar.get(Calendar.DATE);
 
        int months = 0;
        if (day2 >= day1) {
            months = month2 - month1;
        } else {
            months = month2 - month1 - 1;
        }
        return (year2 - year1) * MONTHS_IN_A_YEAR + months;
    }
 
    /**
     * ͳ����������֮�����������������date1����������date2
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static int getDayDiff(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new InvalidParameterException(
                    "date1 and date2 cannot be null!");
        }
        Date startDate = org.apache.commons.lang3.time.DateUtils.truncate(
                date1, Calendar.DATE);
        Date endDate = org.apache.commons.lang3.time.DateUtils.truncate(date2,
                Calendar.DATE);
        if (startDate.after(endDate)) {
            throw new InvalidParameterException("date1 cannot be after date2!");
        }
        long millSecondsInOneDay = HOURS_IN_A_DAY * MINUTES_IN_AN_HOUR
                * SECONDS_IN_A_MINUTE * MILLIS_IN_A_SECOND;
        return (int) ((endDate.getTime() - startDate.getTime()) / millSecondsInOneDay);
    }
 
    /**
     * ����time2��time1����ٷ��ӣ��������ڲ���
     * 
     * @param time1
     * @param time2
     * @return
     */
    public static int getMinuteDiffByTime(Date time1, Date time2) {
        long startMil = 0;
        long endMil = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time1);
        calendar.set(1900, 1, 1);
        startMil = calendar.getTimeInMillis();
        calendar.setTime(time2);
        calendar.set(1900, 1, 1);
        endMil = calendar.getTimeInMillis();
        return (int) ((endMil - startMil) / MILLIS_IN_A_SECOND / SECONDS_IN_A_MINUTE);
    }
 
    /**
     * ����ָ�����ڵ�ǰһ��
     * 
     * @param date
     * @return
     */
    public static Date getPrevDay(Date date) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, -1);
    }
 
    /**
     * ����ָ�����ڵĺ�һ��
     * 
     * @param date
     * @return
     */
    public static Date getNextDay(Date date) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, 1);
    }
 
    /**
     * �ж�date1�Ƿ���date2֮�󣬺���ʱ�䲿��
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isDateAfter(Date date1, Date date2) {
        Date theDate1 = org.apache.commons.lang3.time.DateUtils.truncate(date1,
                Calendar.DATE);
        Date theDate2 = org.apache.commons.lang3.time.DateUtils.truncate(date2,
                Calendar.DATE);
        return theDate1.after(theDate2);
    }
 
    /**
     * �ж�date1�Ƿ���date2֮ǰ������ʱ�䲿��
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isDateBefore(Date date1, Date date2) {
        return isDateAfter(date2, date1);
    }
 
    /**
     * �ж�time1�Ƿ���time2֮�󣬺������ڲ���
     * 
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isTimeAfter(Date time1, Date time2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(time1);
        calendar1.set(1900, 1, 1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(time2);
        calendar2.set(1900, 1, 1);
        return calendar1.after(calendar2);
    }
 
    /**
     * �ж�time1�Ƿ���time2֮ǰ���������ڲ���
     * 
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isTimeBefore(Date time1, Date time2) {
        return isTimeAfter(time2, time1);
    }
 
    /**
     * �ж����������Ƿ�ͬһ�죨����ʱ�䲿�֣�
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        return org.apache.commons.lang3.time.DateUtils.isSameDay(date1, date2);
    }
 
    /**
     * �ж������������Ƿ�ͬһ�죨����ʱ�䲿�֣�
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Calendar date1, Calendar date2) {
        return org.apache.commons.lang3.time.DateUtils.isSameDay(date1, date2);
    }
 
    /**
     * ���ַ�����ʽ�����ڱ�ʾ����Ϊ���ڶ���
     * 
     * @param dateString
     * @return
     */
    public static Date parseDate(String dateString) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(
                    dateString, new String[] { "yyyy-MM-dd", "yyyy-M-d",
                            "yyyy-MM-d", "yyyy-M-dd" });
        } catch (ParseException e) {
            return null;
        }
    }
 
    /**
     * ���ַ�����ʽ��ʱ���ʾ����Ϊ����ʱ�����
     * 
     * @param timeString
     * @return
     */
    public static Date parseTime(String timeString) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(
                    timeString, new String[] { "hh:mm:ss", "h:m:s", "hh:mm",
                            "h:m" });
        } catch (ParseException e) {
            return null;
        }
    }
 
    /**
     * ���ַ�����ʽ������ʱ���ʾ����Ϊʱ�����
     * 
     * @param timeString
     * @return
     */
    public static Date parseDateTime(String timeString) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(
                    timeString, new String[] { "yyyy-MM-dd HH:mm:ss",
                            "yyyy-M-d H:m:s", "yyyy-MM-dd H:m:s",
                            "yyyy-M-d HH:mm:ss" });
        } catch (ParseException e) {
            return null;
        }
    }
 
    /**
     * ������������֮�����������X��������
     * 
     * @param fromDate
     *            ��ʼ����
     * @param toDate
     *            ��������
     * @param dayOfWeek
     *            ���ڣ�������������������
     * @return
     */
    public static int getWeekDaysBetween(Date fromDate, Date toDate,
            int dayOfWeek) {
        int result = 0;
        Date firstDate = getFirstWeekdayBetween(fromDate, toDate, dayOfWeek);
        if (firstDate == null) {
            return 0;
        }
        Calendar aDay = Calendar.getInstance();
        aDay.setTime(firstDate);
        while (aDay.getTime().before(toDate)) {
            result++;
            aDay.add(Calendar.DATE, DAYS_IN_A_WEEK);
        }
        return result;
    }
 
    /**
     * ��ȡ����������֮��ĵ�һ������X
     * 
     * @param fromDate
     *            ��ʼ����
     * @param toDate
     *            ��������
     * @param dayOfWeek
     *            ���ڣ�������������������
     * @return
     */
    public static Date getFirstWeekdayBetween(Date fromDate, Date toDate,
            int dayOfWeek) {
        Calendar aDay = Calendar.getInstance();
        aDay.setTime(fromDate);
        while (aDay.getTime().before(toDate)) {
            if (aDay.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                return aDay.getTime();
            }
            aDay.add(Calendar.DATE, 1);
        }
        return null;
    }
 
    /**
     * ȡ�ò���yearָ������ݵ�������
     * 
     * @param year
     * @return
     */
    public static int getDaysInYear(int year) {
        Calendar aDay = Calendar.getInstance();
        aDay.set(year, 1, 1);
        Date from = aDay.getTime();
        aDay.set(year + 1, 1, 1);
        Date to = aDay.getTime();
        return getDayDiff(from, to);
    }
 
    /**
     * ȡ��ָ�����µ�������
     * 
     * @param year
     * @param month
     * @return
     */
    public static int getDaysInMonth(int year, int month) {
        Calendar aDay = Calendar.getInstance();
        aDay.set(year, month, 1);
        Date from = aDay.getTime();
        if (month == Calendar.DECEMBER) {
            aDay.set(year + 1, Calendar.JANUARY, 1);
        } else {
            aDay.set(year, month + 1, 1);
        }
        Date to = aDay.getTime();
        return getDayDiff(from, to);
    }
 
    /**
     * ���ָ�����ڵ����
     * 
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        return getFieldValue(date, Calendar.YEAR);
    }
 
    /**
     * ���ָ�����ڵ��·�
     * 
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        return getFieldValue(date, Calendar.MONTH) + 1;
    }
 
    /**
     * ���ָ�������ǵ���ĵڼ���
     * 
     * @param date
     * @return
     */
    public static int getDayOfYear(Date date) {
        return getFieldValue(date, Calendar.DAY_OF_YEAR);
    }
 
    /**
     * ���ָ�������ǵ��µĵڼ���
     * 
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        return getFieldValue(date, Calendar.DAY_OF_MONTH);
    }
 
    /**
     * ���ָ�������ǵ��ܵĵڼ���
     * 
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        return getFieldValue(date, Calendar.DAY_OF_WEEK);
    }
 
    private static int getFieldValue(Date date, int field) {
        if (date == null) {
            throw new InvalidParameterException("date cannot be null!");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(field);
    }
 
    /**
     * ���ָ������֮��һ��ʱ�ڵ����ڡ�����ĳ����֮��3������ڵȡ�
     * @param origDate ��׼����
     * @param amount ʱ������
     * @param timeUnit ʱ�䵥λ�����ꡢ�¡��յȡ���Calendar�еĳ�������
     * @return
     */
    public static final Date dateAfter(Date origDate, int amount, int timeUnit) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(origDate);
        calendar.add(timeUnit, amount);
        return calendar.getTime();
    }
 
    /**
     * ���ָ������֮ǰһ��ʱ�ڵ����ڡ�����ĳ����֮ǰ3������ڵȡ�
     * @param origDate ��׼����
     * @param amount ʱ������
     * @param timeUnit ʱ�䵥λ�����ꡢ�¡��յȡ���Calendar�еĳ�������
     * @return
     */
    public static final Date dateBefore(Date origDate, int amount, int timeUnit) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(origDate);
        calendar.add(timeUnit, -amount);
        return calendar.getTime();
    }
}