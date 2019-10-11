package cn.itechyou.cms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * 根据自定义格式返回日期格式化器
     * 
     * @param pattern
     * @return
     */
    public static SimpleDateFormat getSdf(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 获取当前月份
     * 
     * @return
     */
    public static String getCurrentMonth() {
        return getSdf("yyyyMM").format(new Date());
    }
    
    /**
     * 获取当前月份
     * @param pattern 日期格式
     * @return
     */
    public static String getCurrentMonth(String pattern) {
        return getSdf(pattern).format(new Date());
    }

    /**
     * 获取当前日期
     * 
     * @return
     */
    public static String getCurrentDate() {
        return getSdf("yyyyMMdd").format(new Date());
    }
    
    /**
     * 获取当前日期
     * @param pattern 日期格式
     * @return
     */
    public static String getCurrentDate(String pattern) {
        return getSdf(pattern).format(new Date());
    }

    /**
     * 计算月份加减
     * 
     * @param source
     * @param i
     * @return
     */
    public static String addMonth(String source, int i) {
        Calendar calendar;
        try {
            calendar = Calendar.getInstance();
            calendar.setTime(getSdf("yyyyMM").parse(source));
            calendar.add(Calendar.MONTH, i);
        } catch (ParseException e) {
            return "";
        }
        return getSdf("yyyyMM").format(calendar.getTime());
    }
    
    /**
     * 计算日期加减
     * 
     * @param source
     *            日期字符串
     * @param i
     *            加减天数
     * @return
     */
    public static String addDay(String source, int i) {
        Calendar calendar;
        try {
            calendar = Calendar.getInstance();
            calendar.setTime(getSdf("yyyyMMdd").parse(source));
            calendar.add(Calendar.DAY_OF_MONTH, i);
        } catch (ParseException e) {
            return "";
        }
        return getSdf("yyyyMMdd").format(calendar.getTime());
    }
    
    /**
     * 计算日期加减
     * 
     * @param source
     *            日期字符串
     * @param i
     *            加减天数
     * @param pattern 日期格式
     * @return
     */
    public static String addDay(String source, int i,String pattern) {
        Calendar calendar;
        try {
            calendar = Calendar.getInstance();
            calendar.setTime(getSdf(pattern).parse(source));
            calendar.add(Calendar.DAY_OF_MONTH, i);
        } catch (ParseException e) {
            return "";
        }
        return getSdf(pattern).format(calendar.getTime());
    }

    /**
     * 转换字符串日期格式
     * 
     * @param sourcePattern
     *            原始格式
     * @param targetPattern
     *            目标格式
     * @param dateStr
     *            日期字符串
     * @return
     * @throws ParseException
     */
    public static String convertStrDateFormat(String sourcePattern,
            String targetPattern, String dateStr) throws ParseException {
        return getSdf(targetPattern).format(
                getSdf(sourcePattern).parse(dateStr));
    }
    
    /**
     * 处理特殊日期格式  2015-06-09T00:00:00+08:00
     * @param complexDateStr
     * @return
     */
    public static String getComplexDate(String complexDateStr){
    	return complexDateStr.split("T")[0];
    }

    /**
	 * 获取当前年的上一个月的年度
	 * @Description: TODO
	 * @return
	 * @return int
	 */
	public static int getYear(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		int year = calendar.get(Calendar.YEAR);
		return year;
	}
	
	/**
	 * 获取当前年的上一个月的月度
	 * @Description: TODO
	 * @return
	 * @return int
	 */
	public static int getMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		int month = calendar.get(Calendar.MONTH)+1;
		return month;
	}
	
	/**
	 * 获取当前天上一年的年度
	 * @Description: TODO
	 * @return
	 * @return int
	 */
	public static int getBeforeYear(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, -1);
		int year = calendar.get(Calendar.YEAR);
		return year;
	}
	
	/**
	 * 转换月份格式
	 * @Description: TODO
	 * @param month
	 * @return
	 * @return String
	 */
	public static String transfer(int month) {
		
		if(month < 10){
			return "0"+month;
		}
		return month+"";
	}
	
	 /**
	 * 获取当前年份
	 * @Description: TODO
	 * @return
	 * @return int
	 */
	public static int getCurrYear(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		return year;
	}
	
	/**
	 * 获取当前月份
	 * @Description: TODO
	 * @return
	 * @return int
	 */
	public static int getCurrMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int month = calendar.get(Calendar.MONTH)+1;
		return month;
	}
	
	/**
	 * 获取当前时间前两个月的月份
	 * @Description: TODO
	 * @return
	 * @return int
	 */
	public static String getBefore2Month(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -2);
		String year = calendar.get(Calendar.YEAR)+"";
		return year;
	}
	
	/**
	 * 格式化月份
	 * @Description: TODO
	 * @return
	 * @return int
	 */
	public static String formatMonth(String source){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Date date = null;
		String month = "";
		Calendar calendar = Calendar.getInstance();
		try {
			date = sdf.parse(source);
			calendar.setTime(date);
			int months = calendar.get(Calendar.MONTH)+1;
			month = months+"月";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return month;
	}
	
	 /**
     * 根据传递的参数获取具体月份
     * 
     * @param source
     * @param i
     * @return
     */
    public static String getMonth(String source) {
        Calendar calendar = Calendar.getInstance();
        try {
			calendar.setTime(getSdf("yyyyMM").parse(source));
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
        return String.valueOf(calendar.get(Calendar.MONTH)+1);
    }
    
	public static void main(String[] args) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String format = sdf.format(date);
		String addMonth = addMonth(format, -12);
		System.out.println(addMonth);
	}
}
