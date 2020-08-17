package com.wdullaer.materialdatetimepicker.date.dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间操作
 * <p>
 * Created by suntongwei on 2018/1/1.
 */
public class DateTime implements Cloneable {

    public static final String FORMAT_FULL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_FULL_DATE = "yyyy-MM-dd";
    public static final String FORMAT_HH_MM = "HH:mm";
    public static final String FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 一分钟
     */
    public static final long ONE_MINUTE = 1000L * 60;

    /**
     * 一小时
     */
    public static final long ONE_HOUR = ONE_MINUTE * 60;

    /**
     * 一天
     */
    public static final long ONE_DAY = ONE_HOUR * 24;

    private final Calendar calendar = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);

    private DateTime() {
    }

    private DateTime(long timeInMillis) {
        setTimeInMillis(timeInMillis);
    }

    private DateTime(int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    public static DateTime now() {
        return create(System.currentTimeMillis());
    }

    public static DateTime create(long currentTimeMillis) {
        return new DateTime(currentTimeMillis);
    }

    public static DateTime create(int year, int month, int day) {
        return new DateTime(year, month, day);
    }

    /**
     * 描述：String类型的日期时间转化为Date类型.
     *
     * @param strDate String形式的日期时间
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return DateTime
     */
    public static DateTime create(String strDate, String format) {
        DateTime dateTime = new DateTime();
        try {
            Date date = new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE).parse(strDate);
            dateTime.calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public String formatDateTime() {
        return formatDateTime(FORMAT_FULL_DATE);
    }

    public String formatDateTime(String format) {
        return new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE).format(calendar.getTimeInMillis());
    }

    public DateTime setTimeInMillis(long timeInMillis) {
        calendar.setTimeInMillis(timeInMillis);
        return this;
    }

    /**
     * @return 返回TimeInMillis
     */
    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }

    /**
     * @return 返回年
     */
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * @return 返回月
     */
    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * @return 返回天
     */
    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return 返回小时
     */
    public int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * @return 返回分钟
     */
    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * @return 返回当月最后一天
     */
    public int getMaxDayOfMonth() {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return 是否是今天
     */
    public boolean isToday() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return 是否是昨天
     */
    public boolean isYesterday() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, -1);
        return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return 是否是今年
     */
    public boolean isThisYear() {
        return Calendar.getInstance().get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
    }

    /**
     * @return 是否同一天
     */
    public boolean isSameDay(DateTime otherDateTime) {
        return getYear() == otherDateTime.getYear()
                && getMonth() == otherDateTime.getMonth()
                && getDay() == otherDateTime.getDay();
    }

    /**
     * 增加月数
     *
     * @param month 月
     */
    public DateTime addMonth(int month) {
        calendar.add(Calendar.MONTH, month);
        return this;
    }

    /**
     * 增加年
     *
     * @param year 年
     */
    public DateTime addYear(int year) {
        calendar.add(Calendar.YEAR, year);
        return this;
    }

    /**
     * 增加日
     *
     * @param day 日
     */
    public DateTime addDay(int day) {
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return this;
    }

    public DateTime addMinute(int minute) {
        calendar.add(Calendar.MINUTE, minute);
        return this;
    }

    public DateTime setYear(int year) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.YEAR, year);
        int maxDay = getMaxDayOfMonth();
        if (day > maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        return this;
    }

    public DateTime setMonth(int month) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month);
        int maxDay = getMaxDayOfMonth();
        if (day > maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        return this;
    }

    public DateTime setDay(int day) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.get(Calendar.DAY_OF_MONTH);
        return this;
    }

    /**
     * clone
     */
    @Override
    public DateTime clone() {
        return DateTime.create(getTimeInMillis());
    }

    /**
     * 描述：String类型的日期时间转化为Date类型.
     *
     * @param strDate String形式的日期时间
     * @return DateTime
     */
    public static DateTime create(String strDate) {
        DateTime dateTime = new DateTime();
        try {
            Date date = new SimpleDateFormat(FORMAT_FULL_DATE, Locale.SIMPLIFIED_CHINESE).parse(strDate);
            dateTime.calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    /**
     * 获取指定日期是周几
     * 参数为null时表示获取当前日期是周几
     *
     * @return
     */
    public static String getWeekOfDate(long time) {
        Date date = new Date(time);
        return getWeek(date);
    }

    public static String getWeek(Date date) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    //判断选择的日期是否是今天
    public static boolean isToday(long time) {
        return isThisTime(time, "yyyy-MM-dd");
    }

    //判断选择的日期是否是本周
    public static boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        // 判断今天是否是周日，如果是周日，计算时间向前退一周
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            paramWeek = paramWeek - 1;
        }
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }


    //判断选择的日期是否是本月
    public static boolean isThisMonth(long time) {
        return isThisTime(time, "yyyy-MM");
    }

    public static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }

    public static String getFormat(long timestamp) {
        DateTime dateTime = DateTime.create(timestamp * 1000);
        String format = dateTime.formatDateTime("M月d日") + (dateTime.isToday() ? "今天" : DateTime.getWeekOfDate(timestamp * 1000)) + " " + dateTime.formatDateTime("HH:mm");
        return format;
    }

    public static boolean isInTimeFromNow(long time, long timeIntetevel) {
        if (time == 0 || time - System.currentTimeMillis() < 0) {
            return false;
        }
        return time - System.currentTimeMillis() <= timeIntetevel;
    }

    /**
     * 1.计算结束日期需把年月日分开，先把合同开始日期T0(yyyy-MM-dd)的年月日分别记为yyyy,MM,dd
     * <p>
     * 2.依次计算所有结束日期t1/t2/t3/...：
     * <p>
     * tn的月为MM+n，若MM+n>12，则tn的年为yyyy+1，tn的月为1；
     * <p>
     * tn的日为dd-1，若dd-1<1，则tn的日为31，tn的月-1（月-1<1时，年-1，月为12），若tn的日>tn的月的最大天数（注意闰年2月的最大天数），则tn的日为tn的月的最大天数；
     *
     * @param startTime
     * @param n
     * @return
     */
    public static DateTime getDateTimeAfterNMonths(String startTime, int n) {
        DateTime dateTime = DateTime.create(startTime);
        int startYear = dateTime.getYear();
        int startMonth = dateTime.getMonth() + 1;
        int startDay = dateTime.getDay();
        if (startMonth + n > 12) {
            //跨年
            startYear = startYear + (startMonth + n) / 12;
            startMonth = (startMonth + n) % 12;
            if (startMonth == 0) {
                startYear = startYear - 1;
                startMonth = 12;
            }
        } else {
            if (startMonth + n == 12) {
                startMonth = 12;
            } else {
                startMonth = startMonth + n;
            }
        }
        if (startDay == 1) {
            startMonth = startMonth - 1;
            startDay = DateTime.create(startYear, startMonth - 1, 1).getMaxDayOfMonth();
        } else {
            DateTime endDateTime = DateTime.create(startYear, startMonth - 1, 1);
            int maxEndDayOfMonth = endDateTime.getMaxDayOfMonth();
//            System.out.println("startDay=" + startDay + ",maxEndDayOfMonth=" + maxEndDayOfMonth);
            if (startDay > maxEndDayOfMonth) {
                startDay = maxEndDayOfMonth;
            } else {
                startDay = startDay - 1;
            }
        }
        DateTime newDate = DateTime.create(startYear, startMonth - 1, startDay);
        System.err.println(n + "个月:" + startTime + "~" + newDate.formatDateTime());
        return newDate;
    }

    public static void main(String[] args) {
//        String weekOfDate = getWeekOfDate(System.currentTimeMillis());
//        System.out.println("weekOfDate = [" + weekOfDate + "]");
//        long timestamp = 1583976600;
//        DateTime dateTime = DateTime.create(timestamp * 1000);
//        String weekOfDate = DateTime.getWeekOfDate(timestamp * 1000);
//        String res = dateTime.formatDateTime("MM月dd日") + (dateTime.isToday() ? "今天" : weekOfDate) + " " + dateTime.formatDateTime("HH:mm");
//        System.out.println("res = [" + res + "]");

//        DateTime dateTime = DateTime.create("2020-02-01");
//        DateTime otherDateTime = DateTime.create("2020-02-05");
//        int days = (int) ((otherDateTime.getTimeInMillis()-dateTime.getTimeInMillis()) / (24*60 * 60 * 1000));
//        System.out.println("days = [" + days + "]");
        Integer i = Integer.parseInt("1");
        Integer i1 = Integer.parseInt("1");
        System.out.println("args = [" + i.equals(i1) + "]");
    }
}
