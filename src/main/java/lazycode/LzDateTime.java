package lazycode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LzDateTime {
    Date date = null;
    Calendar calendar = Calendar.getInstance();

    public LzDateTime() {
    }

    public LzDateTime(Date date) {
        this.convertDate(date);
    }

    public Date getDate() {
        return date;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    void initCalendar(Date date) {
        // 设置时间
        this.calendar.setTime(date);
    }

    public String toString(String format) {
        return new SimpleDateFormat(format).format(this.date.getTime());
    }

    @Override
    public String toString() {
        return this.toString("yyyy-MM-dd HH:mm:ss.SS");
    }

    public LzDateTime convertStringDateTry(String datetimeString) throws Exception {
        String[] formats = {
                "yyyy-MM-dd HH:mm:ss.SS",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm",
                "yyyy-MM-dd HH",
                "yyyy-MM-dd",
                "yyyy-MM",
                "yyyy",
        };

        Date date = null;
        for (String format : formats) {
            try {
                date = new SimpleDateFormat(format).parse(datetimeString);
                break;
            } catch (Exception ignored) {
            }
        }
        if (date != null) {
            this.date = date;
            this.initCalendar(this.date);
            return this;
        } else {
            throw new Exception("非法的日期格式字符串");
        }
    }

    public LzDateTime convertString(String datetimeString, String format) throws ParseException {
        /*
        yyyy：年
        MM：月
        dd：日
        hh：1~12小时制(1-12)
        HH：24小时制(0-23)
        mm：分
        ss：秒
        S：毫秒
        E：星期几
        D：一年中的第几天
        F：一月中的第几个星期(会把这个月总共过的天数除以7)
        w：一年中的第几个星期
        W：一月中的第几星期(会根据实际情况来算)
        a：上下午标识
        k：和HH差不多，表示一天24小时制(1-24)。
        K：和hh差不多，表示一天12小时制(0-11)。
        z：表示时区
         */

        // yyyy-MM-dd HH:mm:ss
        this.date = new SimpleDateFormat(format).parse(datetimeString);
        this.initCalendar(this.date);
        return this;
    }

    public LzDateTime convertMinutes(long minutes) {
        this.date = new Date(minutes);
        this.initCalendar(this.date);
        return this;
    }

    public LzDateTime convertNow() {
        this.date = new Date();
        this.initCalendar(this.date);
        return this;
    }

    public LzDateTime convertDate(Date date) {
        this.date = date;
        this.initCalendar(this.date);
        return this;
    }

    public long minutes() {
        return this.date.getTime();
    }

    public int year() {
        return this.calendar.get(Calendar.YEAR);
    }

    public int month() {
        // 0, 1, 2 ... 11
        return this.calendar.get(Calendar.MONTH);
    }

    public int day() {
        return this.calendar.get(Calendar.DATE);
    }

    public int hour() {
        // 1, 2, 3, ... 23, 0
        return this.calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int minute() {
        // 1, 2, ... 59, 0
        return this.calendar.get(Calendar.MINUTE);
    }

    public int second() {
        // 1, 2, ... 59, 0
        return this.calendar.get(Calendar.SECOND);
    }

    public int monthDays() {
        Calendar cd = Calendar.getInstance();
        cd.setTime(this.date);
        cd.set(Calendar.DATE, 1);
        cd.roll(Calendar.DATE, -1);
        return cd.get(Calendar.DATE);
    }

    public int day_of_week() {
        // 周日(1),  周一(2), .... 周六(7)
        int n = this.calendar.get(Calendar.DAY_OF_WEEK);
        if (n == 1) {
            return 7;
        } else {
            return n - 1;
        }
    }

    public int day_of_year() {
        return this.calendar.get(Calendar.DAY_OF_YEAR);
    }

    public int week_of_year() {
        return this.calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public LzDateTime addDate(int field, int n) {
        this.calendar.add(field, n);
        this.date = this.calendar.getTime();
        return this;
    }

    public LzDateTime addYear(int n) {
        return this.addDate(Calendar.YEAR, n);
    }

    public LzDateTime addMonth(int n) {
        return this.addDate(Calendar.MONTH, n);
    }

    public LzDateTime addDay(int n) {
        return this.addDate(Calendar.DATE, n);
    }

    public LzDateTime addHour(int n) {
        return this.addDate(Calendar.HOUR, n);
    }

    public LzDateTime addMinute(int n) {
        return this.addDate(Calendar.MINUTE, n);
    }

    public LzDateTime addSecond(int n) {
        return this.addDate(Calendar.SECOND, n);
    }

    public LzDateTime addMillisecond(int n) {
        return this.addDate(Calendar.MILLISECOND, n);
    }


    public boolean isMonday() {
        return this.day_of_week() == 1;
    }

    public boolean isSunday() {
        return this.day_of_week() == 7;
    }

    public boolean isMonthStart() {
        return this.day() == 1;
    }

    public boolean isMonthEnd() {
        return this.month() == this.monthDays();
    }

    public Date week_monday_date() {
        if (this.isMonday()) {
            return this.date;
        } else {
            return new LzDateTime(this.date).addDay(-this.day_of_week() + 1).getDate();
        }
    }

    public Date week_sunday_date() {
        if (this.isSunday()) {
            return this.date;
        } else {
            return new LzDateTime(this.week_monday_date()).addDay(6).getDate();
        }
    }

    public Date month_start_date() {
        if (this.day() == 1) {
            return this.date;
        } else {
            return new LzDateTime(this.date).addDay(-this.day() + 1).getDate();
        }
    }

    public Date month_end_date() {
        if (this.isMonthEnd()) {
            return this.date;
        } else {
            return new LzDateTime(this.month_start_date()).addDay(this.monthDays() - 1).getDate();
        }
    }

    public int[][] calendarDayArray() {
        int[][] array = new int[6][7];
        int startWeekend = new LzDateTime(this.month_start_date()).day_of_week();
        int days = this.monthDays();
        int day = 1;

        Arrays.fill(array[0], 0);
        for (int i = startWeekend - 1; i < 7; i++) {
            array[0][i] = day++;
            days--;
        }

        for (int i = 1; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (days > 0) {
                    array[i][j] = day++;
                    days--;
                } else {
                    array[i][j] = 0;
                }
            }
        }
        return array;
    }

    public boolean equalsDate(Date date1, Date date2, int minLevel) throws Exception {
        // new Date(), new Date(), Calendar.MONTH  => true
        String format = null;
        switch (minLevel) {
            case Calendar.YEAR: {
                format = "yyyy";
                break;
            }
            case Calendar.MONTH: {
                format = "yyyy-MM";
                break;
            }
            case Calendar.DATE: {
                format = "yyyy-MM-dd";
                break;
            }
            case Calendar.HOUR: {
                format = "yyyy-MM-dd HH";
                break;
            }
            case Calendar.MINUTE: {
                format = "yyyy-MM-dd HH:mm";
                break;
            }
            case Calendar.SECOND: {
                format = "yyyy-MM-dd HH:mm:ss";
                break;
            }
            case Calendar.MILLISECOND: {
                format = "yyyy-MM-dd HH:mm:ss.SS";
                break;
            }
            default: {
                throw new Exception("传入 minLevel 参数不合法");
            }
        }
        String dataStr1 = new SimpleDateFormat(format).format(date1);
        String dataStr2 = new SimpleDateFormat(format).format(date2);
        return dataStr1.equals(dataStr2);
    }

    public long dateCompare(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    public long dateCompare(Date date) {
        return this.date.getTime() - date.getTime();
    }

    // [ start : end ]
    public Date[] dateRange(Date start, Date end, int type, int num) throws Exception {
        if (this.dateCompare(start, end) > 0) {
            throw new Exception("传入的开始日期不能小于结束日期");
        }

        List<Date> dates = new ArrayList<>();
        dates.add(start);

        switch (type) {
            case Calendar.YEAR:
            case Calendar.MONTH:
            case Calendar.DATE:
            case Calendar.HOUR:
            case Calendar.MINUTE:
            case Calendar.SECOND: {
                LzDateTime lzdStart = new LzDateTime(start);
                while (lzdStart.addDate(type, num).dateCompare(end) < 0) {
                    dates.add(lzdStart.getDate());
                }
                break;
            }
            default: {
                throw new Exception("传入 type 参数不合法");
            }
        }
        return dates.toArray(new Date[0]);
    }


}

