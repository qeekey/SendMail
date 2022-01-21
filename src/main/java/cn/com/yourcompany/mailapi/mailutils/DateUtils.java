package cn.com.yourcompany.mailapi.mailutils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

    public static String FORMAT_DAY="yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_YMD="yyyyMMdd";
    public static String FORMAT_YMDHH="yyyyMMddHH";
    public static String FORMAT_YMD_DAY="yyyy-MM-dd";
    public static String FORMAT_YMDH="yyMMddHH";


    public static String FORMAT_DAY_MS="yyyy-MM-dd-HH-mm-ss-SSS";
    public static String FORMAT_DAY_S="yyyy-MM-dd-HH-mm-ss";
    public static String SHORT_YMD = "yyMMdd";

    public static String FORMAT_YMDHH_00 = "yyyyMMdd00";
    public static String FORMAT_YMDHH_23 = "yyyyMMdd23";

    /**
     * 日期转换成年月日的字符串
     * @param date
     * @return str
     */
    public static String DateToStrYMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YMD);
        String str = format.format(date);
        return str;
    }


    public static String DateToStrYMDH(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YMDH);
        String str = format.format(date);
        return str;
    }

    public static String DateToStrYYYYMMDDHH(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YMDHH);
        String str = format.format(date);
        return str;
    }

    public static String DateToStrYMDHMS(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DAY_S);
        String str = format.format(date);
        return str;
    }

    public static String DateToStrYMDMS(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DAY_MS);
        String str = format.format(date);
        return str;
    }

    public static String dateToyyyymmdd00(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YMDHH_00);
        String str = format.format(date);
        return str;
    }

    public static String dateToyyyymmdd23(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YMDHH_23);
        String str = format.format(date);
        return str;
    }

    /**
     *
     * @param ymd eg. 2015-11-11
     * @return eg. 151111
     */
    public static String pasreYMD2ShortDay(String ymd) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YMD_DAY);
        SimpleDateFormat toFormat = new SimpleDateFormat(SHORT_YMD);
        String re = "";
        re = toFormat.format(format.parse(ymd));
        return re;
    }

    /**
     * 根据制定的格式，将指定的date转化成String
     * @param date
     * @param type 制定返回的格式，可以使用本类中的static值
     * @return
     */
    public static String DateToString(Date date, String type) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        return format.format(date);
    }
    /**
     * 返回特定日期前n天的日期
     * @param date
     * @param days
     * @return
     */
    public static Date getBeforeDate(Date date,int days)
    {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_DAY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) - days);
        return calendar.getTime();
    }

    /**
     * 返回特定日期前n天的日期
     * @param date
     * @param days
     * @return
     */
    public static long getBeforeDateLong(Date date,int days)
    {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_DAY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) - days);
        return calendar.getTimeInMillis();
    }

    /**
     * 返回特定日期后n天的日期
     * @param date
     * @param date
     * @return
     */
    public static Date getAfterDate(Date date,int days)
    {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_DAY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) + days);
        return calendar.getTime();
    }

    public static int getDaySub(String beginDateStr,String endDateStr)
    {
        int day=0;
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YMD_DAY);
        Date beginDate;
        Date endDate;
        try
        {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);
            day = daysBetween( beginDate, endDate);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate,Date bdate) throws ParseException
    {
        SimpleDateFormat sdf=new SimpleDateFormat(FORMAT_YMD_DAY);
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 返回日期所在的周
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY); // 周开始
        calendar.setMinimalDaysInFirstWeek(4);  // 跨年的时候
        calendar.setTime(date);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        String strWeek = week < 10 ? "0" + week : String.valueOf(week);
        int year = calendar.getWeekYear() % 1000;
        return String.format("%d%s", year, strWeek);
    }

    /**
     * 生成指定日期的时间区间,支持小时和分天
     * @param start 时间区间开始时间(含)
     * @param end 时间区间结束时间(含)
     * @param type hour, date
     * @return
     */
    public static List<String> getTimeInterval(String start, String end, String type)  {
        List<String> dateList = new ArrayList<>();

        String format = "yyyyMMdd";
        int calendarType = Calendar.DATE;
        if (type.equalsIgnoreCase("hour")) {
            format = "yyyyMMddHH";
            calendarType = Calendar.HOUR;
            start = start.length() == 8 ? start + "00" : start;
            end = end.length() == 8 ? end + "23" : end;

        }else if (type.equalsIgnoreCase("week")) {
            return dateList;
        }


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date sDate = null;
        Date eEnd= null;
        try {
            sDate = sdf.parse(start);
            eEnd=sdf.parse(end);
            while(sDate.getTime()<=eEnd.getTime()){
                cal.setTime(sDate);

                dateList.add((new SimpleDateFormat(format)).format(cal.getTime()));

                cal.add(calendarType, 1);

                sDate = cal.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateList;
    }


    /**
     * 获取指定时间对应的毫秒数
     * @param time "HH:mm:ss"
     * @return
     */
    public static long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取周一
     * @param pos pos=1本周一, pos=-7上周一, pos=7下周一
     * @return
     */
    public static Date getWeekOfMonday(int pos){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, pos);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String monday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        System.out.println(cal.getTime()+ "," + monday);
        return cal.getTime();

    }


    public static Date str2Date(String strDate, String format) {

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }

    public static boolean dateBetweenAandB(Date date, Date A, Date B) {
        long d = date.getTime();
        long a = A.getTime();
        long b = B.getTime();
        if (d >= a && d <= b) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 两个日期之间包含的年
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static Integer[] getBetweenYears(String startDate, String endDate)  {
        List<Integer> yearList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.SHORT_YMD);

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int startYear = cal.get(Calendar.YEAR);

        Calendar cal2 = Calendar.getInstance();
        try {
            cal2.setTime(sdf.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int endYear = cal2.get(Calendar.YEAR);
        // star > end
        if (cal.getTime().getTime() > cal2.getTime().getTime()) {
            return new Integer[]{};
        } else {
            if (startYear == endYear) {
                yearList.add(startYear);
            } else {
                yearList.add(startYear);
                while (true) {
                    cal.add(Calendar.YEAR, 1);
                    int year = cal.get(Calendar.YEAR);

                    yearList.add(year);
                    if (year == endYear) {
                        break;
                    }
                }
            }
        }
        Integer[] s = yearList.toArray(new Integer[yearList.size()]);
        return s;

    }

    public static int compareDate(String sDate1, String sDate2, String format) throws ParseException {
        DateFormat df = new SimpleDateFormat(format);

        Date d1 = df.parse(sDate1);
        Date d2 = df.parse(sDate2);

        if (d1.getTime() > d2.getTime()) {
            return 1;
        } else if ( d1.getTime() < d2.getTime()) {
            return -1;
        } else {
            return 0;
        }

    }

    /**
     * 日期所在周的上周一
     * @param date
     * @return
     */
    public static Date getLastWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, -7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 日期所在周的上周日
     * @param date
     * @return
     */
    public static Date getLastWeekSunDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }


    /**
     * 日期所在周的周一
     * @param date
     * @return
     */
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }


    /**
     * 日期所在周的下周一
     * @param date
     * @return
     */
    public static Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }


    public static List<String> gen24HourList(Date date)  {
        List<String> dateList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        try {
            cal.set(Calendar.HOUR, 0);
            Date sDate = cal.getTime();

            cal.set(Calendar.HOUR, 23);
            Date eDate = cal.getTime();
            while(sDate.getTime()<=eDate.getTime()){
                cal.setTime(sDate);
                dateList.add((new SimpleDateFormat(FORMAT_YMDHH)).format(cal.getTime()));
                cal.add(Calendar.HOUR, 1);
                sDate = cal.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateList;
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YMD_DAY);
//        Date d = sdf.parse("2016-05-11");
//        Date a = sdf.parse("2016-05-10");
//        Date b = sdf.parse("2016-05-10");
//        System.out.println(dateBetweenAandB(d, a, b));

        String lastMonday = DateToStrYMDH(getLastWeekMonday(new Date()));
        String lastSunday = DateToStrYMDH(getLastWeekSunDay(new Date()));


        String d1 = DateToStrYMD(getThisWeekMonday(new Date()));
        String d2 = DateToStrYMD(getNextWeekMonday(new Date()));

        System.out.println(lastMonday);
        System.out.println(lastSunday);
        List<String> list =gen24HourList(str2Date("171101", SHORT_YMD));
        System.out.println(list);
        Date date = sdf.parse("2017-12-04");
        System.out.println(getWeekOfDate(date));
    }
}
