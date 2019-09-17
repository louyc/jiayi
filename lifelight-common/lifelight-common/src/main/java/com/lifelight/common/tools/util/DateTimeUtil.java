package com.lifelight.common.tools.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public static String YYYYMMDD = "yyyy-MM-dd";

    public static String getFormatDate(Date d) {
        if (null != d) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            return sf.format(d);
        } else {
            return null;
        }
    }

    public static String getFormatDateTime(Date d) {
        if (null != d) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sf.format(d);
        } else {
            return null;
        }
    }

    /**
     * 计算当前天到满月天
     * 
     * @param date
     * @return 满一整月的天
     */
    public static Date calculateMonth(int day, Date end) {
        int mol = day % 30;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, mol);
        return c.getTime();
    }

    /**
     * 获取相差几个月
     * 
     * @param start
     * @param end
     * @return
     */
    public static int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param start
     *            较小的时间
     * @param end
     *            较大的时间
     * @return 相差天数
     */
    public static int daysBetween(Date start, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            start = sdf.parse(sdf.format(start));
            end = sdf.parse(sdf.format(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        long time1 = cal.getTimeInMillis();
        cal.setTime(end);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 剩余多少小时
     * 
     * @param start
     * @param end
     * @return
     */
    public static int hoursBetween(Date start, Date end) {
        long cur = start.getTime();
        long expiry = end.getTime();
        long diff = expiry - cur;
        return (int) diff / (60 * 60 * 1000);

    }

    /**
     * 相差多少分钟
     * 
     * @param start
     * @param end
     * @return
     */
    public static int minuteBetween(Date start, Date end) {
        long cur = start.getTime();
        long expiry = end.getTime();
        long diff = expiry - cur;
        return (int) diff / (60 * 1000);

    }

    /**
     * 两个日期相差多少秒
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static int getTimeDelta(Date date1, Date date2) {
        long timeDelta = (date1.getTime() - date2.getTime()) / 1000;// 单位是秒
        int secondsDelta = (int) timeDelta;
        return secondsDelta;
    }

    /**
     * 获取当前时间的上一月的最后一天的23:59:59
     * 
     * @param date
     * @return
     */
    public static Date getpreMonthLastDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
        int MaxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, MaxDay);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    /**
     * 获取当前时间的上一月的最后一天的23:59:59
     * 
     * @param date
     * @return
     */
    public static Date getpreMonthLastDayEnd(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
        int MaxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (MaxDay >= day) {
            MaxDay = day;
        }
        c.set(Calendar.DAY_OF_MONTH, MaxDay - 1);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    /**
     * 指定时间的上月的指定的日期的00:00:00
     * 
     * @param date
     *            时间
     * @param day
     *            日期
     * @return
     */
    public static Date getpreMonthThisDayBegin(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
        int MaxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (MaxDay >= day) {
            MaxDay = day;
        }
        c.set(Calendar.DAY_OF_MONTH, MaxDay);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 指定日期的00:00:00
     * 
     * @param date
     *            时间
     * @param day
     *            日期
     * @return
     */
    public static Date getThisMonthThisDayBegin(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int MaxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (MaxDay >= day) {
            MaxDay = day;
        }
        c.set(Calendar.DAY_OF_MONTH, MaxDay);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 指定的日期的23:59:59
     * 
     * @param date
     *            时间
     * @param day
     *            日期
     * @return
     */
    public static Date getThisMonthThisDayEnd(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int MaxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (MaxDay >= day) {
            MaxDay = day;
        }
        c.set(Calendar.DAY_OF_MONTH, MaxDay);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    /**
     * 获取当前时间的上个月的第一天的0:0:0
     * 
     * @param date
     * @return
     */
    public static Date preMonthFirstDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
        int minDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, minDay);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getNextMonthFirstDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
        int minDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, minDay);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 获取当前时间的前一天的23:59:59
     * 
     * @return
     */
    public static Date getPreLastTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.add(Calendar.DAY_OF_YEAR, -1);
        return c.getTime();
    }

    /**
     * 当前时间加上秒数
     * 
     * @param date
     * @param second
     * @return
     */
    public static Date modifySecond(Date date, int second) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, second);
        return c.getTime();
    }

    /**
     * 当前时间加上分钟数
     * 
     * @param date
     * @param minute
     * @return
     */
    public static Date modifyMinutes(Date date, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minute);
        return c.getTime();
    }

    /**
     * 获取当前时间的前一天的0:0:0
     * 
     * @return
     */
    public static Date getPreFirstTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DAY_OF_YEAR, -1);
        return c.getTime();
    }

    /**
     * 计算当前时间前n天
     * 
     * @param date
     * @return
     */
    public static Date getBeforeDay(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -n);
        date = calendar.getTime();
        return date;
    }

    /**
     * 计算当前时间后n天
     * 
     * @param date
     * @return
     */
    public static Date getAfterDay(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +n);
        date = calendar.getTime();
        return date;
    }

    /**
     * 计算当前时间前n小时
     * 
     * @param date
     * @return
     */
    public static Date getBeforeHour(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -n);
        date = calendar.getTime();
        return date;
    }

    /**
     * 计算当前时间前n分钟
     * 
     * @param date
     * @return
     */
    public static Date getBeforeMinutes(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -n);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取当前时间的后一天的0:0:0
     * 
     * @return
     */
    public static Date getNextFirstTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    /**
     * 获取201501格式的数据
     * 
     * @param date
     * @return
     */
    public static int getYearMonth(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
        return Integer.parseInt(sf.format(date));
    }

    /**
     * 获取上个月今天的时间函数
     * 
     * @param date
     * @return
     */
    public static Date getPriMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        return c.getTime();
    }

    /**
     * 获取几月前的日期
     * 
     * @param date
     * @param month
     *            (为正数是为前，负数为后)
     * @return
     */
    public static Date getBeforeMonth(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - month);
        date = cal.getTime();
        return date;
    }

    /**
     * 取得月第一天
     * 
     * @param date
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得月最后一天
     * 
     * @param date
     * @return
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 计算某天后的日期
     * 
     * @param days
     * @return day
     */
    public static Date getAfterDaysTimeDate(int days) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    /**
     * 计算某天起始时刻
     * 
     * @param day
     * @return day
     */
    public static String getStartTimeOfDate(Date day) {
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        DateFormat fmt = null;
        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = fmt.format(c.getTime());
        String date = dateTime.split(" ")[0];
        return date + " 00:00:00";
    }

    /**
     * 计算某天结束时刻
     * 
     * @param day
     * @return day
     */
    public static String getEndTimeOfDate(Date day) {
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        DateFormat fmt = null;
        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = fmt.format(c.getTime());
        String date = dateTime.split(" ")[0];
        return date + " 23:59:59";
    }

    /**
     * 计算某天起始时刻(string 类型参数)
     * 
     * @param day
     * @return day
     */
    public static String getStartTimeOfDate(String day) {
        String date = day.split(" ")[0];
        return date + " 00:00:00";
    }

    /**
     * 计算某天结束时刻(string 类型参数)
     * 
     * @param day
     * @return day
     */
    public static String getEndTimeOfDate(String day) {
        String date = day.split(" ")[0];
        return date + " 23:59:59";
    }

    /**
     * 日期是否相等
     * 
     * @param d
     * @param date
     * @return
     */
    public static boolean isSameDate(Date d, Date date) {
        String d1 = getFormatDate(d);
        String date1 = getFormatDate(date);
        return d1.equals(date1);
    }

    /**
     * 日期和时间点是否相等
     * 
     * @param d
     * @param date
     * @return
     */
    public static boolean isSameDateAndHours(Date start, Date end) {
        int hoursBetween = hoursBetween(start, end);
        return hoursBetween == 0 ? true : false;
    }

    /**
     * 字符串转日期
     * 
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String date, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            return sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据当前月份获取日期
     * 
     * @param month
     * @return
     */
    public static Date getCurMonthDate(int month) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
        try {
            Date d = sf.parse(month + "");
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * 获取当前时间前一个小时的时间
     * 
     * @return
     */
    public static String beforeOneHourToNowDate() {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
    }

    /**
     * 获取当前时间的整点时间
     * 
     * @return
     */
    public static String hourOfNowDate() {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
    }

    /**
     * 是否是两个时间的时间
     * 
     * @param nowdate
     *            需要比对的当前时间
     * @param startdate
     *            需要比对的开始时间
     * @param enddate
     *            需要比对的结束时间
     * @return
     */
    public static boolean isBetweenDate(Date nowdate, Date startdate, Date enddate) {
        long now = nowdate.getTime();
        long start = startdate.getTime();
        long end = enddate.getTime();
        return now >= start && now <= end ? true : false;
    }

    /**
     * 得到指定月的天数 时间格式 yyyy-MM-dd
     */
    @SuppressWarnings("deprecation")
    public static int getMonthDays(Date date) {
        int year = date.getYear();
        int month = date.getMonth();
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthDays(int yearMonth) {
        int year = yearMonth / 100;
        int month = yearMonth % 100 - 1;
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 判断时间格式 格式必须为“YYYY-MM-dd” 2004-2-30 是无效的 2003-2-29 是无效的
     * 
     * @param sDate
     * @return
     */
    public static boolean isValidDate(String str) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = (Date) formatter.parse(str);
            return str.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将yyyy-MM-dd 转换为日期
     * 
     * @param day
     * @return
     * @throws ParseException
     */
    public static Date parseDay(String day) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.parse(day);
    }

    /**
     * 
     * 比较两个时间的大小
     * 
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;// dt1 在dt2后
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;// dt1在dt2前
            } else {
                return 0;// dt1等于dt2
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 比较时间大小
     * 
     * @param time1
     * @param time2
     * @return 0:相等,1：time1大于time2,-1:time1小于time2
     */
    public static int compare_time(String time1, String time2) {
        DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1 = java.util.Calendar.getInstance();
        Calendar c2 = java.util.Calendar.getInstance();
        try {
            c1.setTime(df.parse(time1));
            c2.setTime(df.parse(time2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c1.compareTo(c2);
    }

    /**
     * 比较时间大小
     * 
     * @param time1
     * @param time2
     * @return 0:相等,1：time1大于time2,-1:time1小于time2
     */
    public static int compare_time(Date time1, Date time2) {
        Calendar c1 = java.util.Calendar.getInstance();
        Calendar c2 = java.util.Calendar.getInstance();
        c1.setTime(time1);
        c2.setTime(time2);
        return c1.compareTo(c2);
    }
}
