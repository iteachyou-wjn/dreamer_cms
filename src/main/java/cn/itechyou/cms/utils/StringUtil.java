package cn.itechyou.cms.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串解析
 * @author 王天培QQ:78750478
 * @version 
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
public class StringUtil {

	/**
	 * 字符串变量实体类
	 */
	private static StringBuilder sb = new StringBuilder();

	/**
	 * 验证邮箱格式是否正确
	 * 
	 * @param email 邮箱地址
	 * @return 如果正确，则返回true，否则返回false
	 */
	public static boolean checkEmail(String email) {
		String regex = "^[a-zA-Z][a-zA-Z0-9._-]*\\@\\w+(\\.)*\\w+\\.\\w+$";
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(email);
		return matcher.matches();
	}

	/**
	 * 过滤html，将一些字符进行转义
	 * 
	 * @param html 网页源码
	 * @return 返回过滤后的html
	 */
	public static String formatHTMLIn(String html) {
		html = html.replaceAll("&", "&amp;");
		html = html.replaceAll("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		html = html.replaceAll("\"", "&quot;");
		return html;
	}

	/**
	 * 解析html
	 * 
	 * @param html 网页源码
	 * @return 返回解析过的html
	 */
	public static String formatHTMLOut(String html) {
		html = html.replaceAll("&amp;", "&");
		html = html.replaceAll("&lt;", "<");
		html = html.replaceAll("&gt;", ">");
		html = html.replaceAll("&quot;", "\"");
		return html;
	}

	/**
	 * 截取字符长度
	 * 
	 * @param str 需截取的字符串
	 * @param length 截取长度
	 * @return 返回截取后的字符串
	 */
	public static String subString(String str, int length) {
		if (isBlank(str))
			return "";
		if (str.getBytes().length <= length)
			return str;
		char ch[] = null;
		if (str.length() >= length)
			ch = str.substring(0, length).toCharArray();
		else
			ch = str.toCharArray();
		int readLen = 0;
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < ch.length; i++) {
			String c = String.valueOf(ch[i]);
			readLen += c.getBytes().length;
			if (readLen > length)
				return sb.toString();
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 验证长度
	 * 
	 * @param str 需验证的字符串
	 * @param minLength 字符串的最小长度
	 * @param maxLength 字符串的最大长度
	 * @return 如果验证通过，则返回true，否则返回false
	 */
	public static boolean checkLength(String str, int minLength, int maxLength) {
		if (str != null) {
			int len = str.length();
			if (minLength == 0)
				return len <= maxLength;
			else if (maxLength == 0)
				return len >= minLength;
			else
				return (len >= minLength && len <= maxLength);
		}
		return false;
	}

	/**
	 * 解码，将字符串解码成UTF-8编码
	 * 
	 * @param str 需要解码的字符串
	 * @return 返回解码后的字符串
	 */
	public static String decodeStringByUTF8(String str) {
		if (isBlank(str))
			return "";
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return "";
	}

	/**
	 * 转码，将字符串转码成UTF-8编码
	 * 
	 * @param str 需要转码的字符串
	 * @return 返回转码后的字符串
	 */
	public static String encodeStringByUTF8(String str) {
		if (isBlank(str))
			return "";
		try {
			return URLEncoder.encode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return "";
	}

	/**
	 * 程序内部字符串转码，将ISO-8859-1转换成utf-8
	 * 
	 * @param str 需要转码的字符串
	 * @return 返回utf8编码字符串
	 */
	public static String isoToUTF8(String str) {
		if (isBlank(str))
			return "";
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return "";
	}

	/**
	 * 程序内部字符串转码，将utf-8转换成ISO-8859-1
	 * 
	 * @param str 需要转码的字符串
	 * @return 返回转码后的字符串
	 */
	public static String utf8ToISO(String str) {
		if (isBlank(str))
			return "";
		try {
			return new String(str.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
		}
		return "";
	}

	/**
	 * 程序内部字符串转码，将utf-8转换成gb2312
	 * @param str 需要转码的字符串
	 * @return 返回转码后的字符串
	 */
	public static String utf8Togb2312(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			case '+':
				sb.append(' ');
				break;
			case '%':
				try {
					sb.append((char) Integer.parseInt(str.substring(i + 1, i + 3), 16));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException();
				}
				i += 2;
				break;
			default:
				sb.append(c);
				break;
			}
		}
		// Undo conversion to external encoding
		String result = sb.toString();
		String res = null;
		try {
			byte[] inputBytes = result.getBytes("8859_1");
			res = new String(inputBytes, "UTF-8");
		} catch (Exception e) {
		}
		return res;
	}

	/**
	 * 格式化时间，转换成字符串
	 * 
	 * @param date 要转换格式的时间
	 * @param pattern 要转换成的时间格式 eg: yy/MM/dd HH:mm
	 * @return 返回转换后的时间字符串
	 */
	public static String getFormatDateStr(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 字段串是否为空
	 * 
	 * @param str 要判断是否为空的字符串
	 * @return 如果为空，则返回true，否则返回false
	 */
	public static boolean isBlank(String str) {
		return (str == null || str.trim().equals("") || str.length() < 0);
	}

	/**
	 * 对象是否为空
	 * 
	 * @param str object对象
	 * @return 如果为空，则返回true，否则返回false
	 */
	public static boolean isBlank(Object str) {
		return (str == null || str.toString().trim().equals("") || str.toString().length() < 0);
	}

	/**
	 * 判断是否为数组
	 * 
	 * @param args
	 * @return 如果是数组，则返回true，否则返回false
	 */
	public static boolean isBlank(String[] args) {
		return args == null || args.length == 0 ? true : false;
	}
	
	/**
	 * 一次性判断多个或单个对象不为空。
	 * 
	 * @param objects
	 * @author zhou-baicheng
	 * @return 只要有一个元素不为Blank，则返回true
	 */
	public static boolean isNotBlank(Object... objects) {
		return !isBlank(objects);
	}
	
	public static boolean isNotBlank(String... objects) {
		Object[] object = objects;
		return !isBlank(object);
	}
	
	public static boolean isNotBlank(String str) {
		Object object = str;
		return !isBlank(object);
	}
	
	/**
	 * 判断字符串是否是数字类型
	 * 
	 * @param str 字符串
	 * @return 如果是数字类型，则返回true，否则返回false
	 */
	public static boolean isInteger(String str) {
		if (isBlank(str))
			return false;
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断对象是否是数字类型
	 * 
	 * @param str 对象
	 * @return 如果是数字类型，则返回true，否则返回false
	 */
	public static boolean isInteger(Object str) {
		String temp = str + "";
		if (isBlank(str))
			return false;
		try {
			Integer.parseInt(temp);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 当传入的值为NULL是将其转化为String
	 * 
	 * @param str 字符串
	 * @return 返回字符串
	 */
	public static String null2String(String str) {
		if (str == null || str.equals("") || str.trim().length() == 0) {
			return str = "";
		}
		return str;
	}

	/**
	 * 将String型转换成Int型并判断String是否是NULL
	 * 
	 * @param str 字符串
	 * @return 返回数字
	 */
	public static int string2Int(String str) {
		int valueInt = 0;
		if (!StringUtil.isBlank(str)) {
			valueInt = Integer.parseInt(str);
		}
		return valueInt;
	}

	/**
	 * 变量形态转换 int型转为String型
	 * 
	 * @param comment 整型数字
	 * @return 返回字符串
	 */
	public static String int2String(int comment) {
		String srt = "";
		srt = Integer.toString(comment);
		return srt;
	}

	/**
	 * 判断是否是大于0的参数
	 * 
	 * @param str 字符串参数
	 * @return 如果是大于，则返回true，否则返回false
	 */
	public static boolean isMaxZeroInteger(Object str) {
		if (isBlank(str))
			return false;
		try {
			int temp = Integer.parseInt(str.toString());
			return temp > 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断长整型
	 * 
	 * @param str 字符串
	 * @return 如果是长整型，则返回true，否则返回false
	 */
	public static boolean isLong(String str) {
		if (isBlank(str))
			return false;
		try {
			Long.parseLong(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断长整型数组
	 * 
	 * @param str String数组
	 * @return 如果是长整型，则返回true，否则返回false
	 */
	public static boolean isLongs(String str[]) {
		try {
			for (int i = 0; i < str.length; i++)
				Long.parseLong(str[i]);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断数字数组
	 * 
	 * @param str String数组
	 * @return 如果是数字，则返回true，否则返回false
	 */
	public static boolean isIntegers(String str[]) {
		try {
			for (int i = 0; i < str.length; i++)
				Integer.parseInt(str[i]);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断数字数组
	 * 
	 * @param str String数组
	 * @return 如果是数组，则返回true，否则返回false
	 */
	public static boolean isDoubles(String str[]) {
		try {
			for (int i = 0; i < str.length; i++)
				Double.parseDouble(str[i]);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * md5加密
	 * 
	 * @param plainText 文本内容
	 * @return 返回加密后的字符串
	 */
	@Deprecated
	public static String Md5(String plainText) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i = 0;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	/**
	 * 微信支付签名MD5加密算法
	 * @param plainText 需要加密的字符串
	 * @param coding 字符串编码
	 * @return 加密后的字符串
	 */
	public static String Md5(String plainText,String coding) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes(coding));
			byte b[] = md.digest();
			int i = 0;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}	
	
	/**
	 * 字符串转长整型数组
	 * 
	 * @param str String字符串
	 * @return 返回长整型数组
	 */
	public static long[] stringsToLongs(String str[]) {
		long lon[] = new long[str.length];
		for (int i = 0; i < lon.length; i++)
			lon[i] = Long.parseLong(str[i]);
		return lon;
	}

	/**
	 * 字符串转数字型数组
	 * 
	 * @param str String字符串
	 * @return 返回数字型数组
	 */
	public static Integer[] stringsToIntegers(String str[]) {
		Integer array[] = new Integer[str.length];
		for (int i = 0; i < array.length; i++)
			array[i] = Integer.parseInt(str[i]);
		return array;
	}
	
	/**
	 * 字符串转数字型数组
	 * 
	 * @param str String字符串
	 * @return 返回数字型数组
	 */
	public static int[] stringsToInts(String str[]) {
		int array[] = new int[str.length];
		for (int i = 0; i < array.length; i++)
			array[i] = Integer.parseInt(str[i]);
		return array;
	}

	
	/**
	 * 字符串转double型数组
	 * 
	 * @param str String字符串
	 * @return 返回数字型数组
	 */
	public static double[] stringsToDoubles(String str[]) {
		double array[] = new double[str.length];
		for (int i = 0; i < array.length; i++)
			array[i] = Double.parseDouble(str[i]);
		return array;
	}

	/**
	 * 除去字符串数组中相同的值
	 * 
	 * @param str String数组
	 * @return 返回除去相同值后的数组
	 */
	@SuppressWarnings("unchecked")
	public static String[] delLopStrings(String str[]) {
		@SuppressWarnings("rawtypes")
		ArrayList list = new ArrayList();
		for (int i = 0; i < str.length; i++) {
			if (!list.contains(str[i]))
				list.add(str[i]);
		}
		String array[] = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = (String) list.get(i);
		}
		return array;
	}

	/**
	 * 字符串转布尔型数组
	 * 
	 * @param str 字符串数组
	 * @return 返回布尔型数组
	 */
	public static boolean[] stringsToBooleans(String str[]) {
		boolean array[] = new boolean[str.length];
		for (int i = 0; i < array.length; i++)
			array[i] = Boolean.parseBoolean(str[i]);
		return array;
	}

	/**
	 * 判断字符串是否是日期类型 
	 * 
	 * @param str 字符串
	 * @return 如果是日期类型，则返回true，否则返回false
	 */
	public static boolean isTimestamp(String str) {
		try {
			@SuppressWarnings("unused")
			Date d = java.sql.Date.valueOf(str);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 获取当前页
	 * 
	 * @param pageNo 页数 
	 * @return 返回当前页数
	 */
	public static int getPageStart(String pageNo) {
		int istart = 1;
		if (isBlank(pageNo)) {
			return istart;
		}
		try {
			istart = Integer.parseInt(pageNo) < 0 ? istart : Integer.parseInt(pageNo);
		} catch (NumberFormatException e) {
		}
		return istart;
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 返回获取当前系统时间字符串
	 */
	public static String getDateSimpleStr() {
		return String.valueOf(System.currentTimeMillis());
	}

	/**
	 * 字符串TO长整型
	 * 
	 * @param args String数组
	 * @return 返回长整型数组
	 */
	public static Long[] StrToLong(String[] args) {
		if (args == null)
			return null;
		Long[] _ref = new Long[args.length];
		for (int i = 0; i < args.length; i++) {
			_ref[i] = new Long(args[i]);
		}
		return _ref;
	}

	/**
	 * 字符串TO整型
	 * 
	 * @param args String数组
	 * @return 返回整型数组
	 */
	public static Integer[] StrToInteger(String[] args) {
		if (args == null)
			return null;
		Integer[] _ref = new Integer[args.length];
		for (int i = 0; i < args.length; i++) {
			_ref[i] = new Integer(args[i]);
		}
		return _ref;
	}

	/**
	 * 获取日期字符串
	 * 
	 * @param day 日期
	 * @param fomStr 日期格式
	 * @return 返回日期字符串
	 */
	public static String getSimpleDateStr(Date day, String fomStr) {
		SimpleDateFormat format = new SimpleDateFormat(fomStr);
		return format.format(day);
	}

	/**
	 * 字符串返回时间
	 * 
	 * @param str 字符串类型的时间
	 * @return 返回时间
	 */
	public static Date getDateForStr(String str) {
		java.sql.Date sqlDate = java.sql.Date.valueOf(str);
		return sqlDate;
	}
	

	/**
	 * 指定时间增加天数
	 * 
	 * @param time  时间
	 * @param day 天数
	 * @return 返回新的时间
	 */
	public static Date addDays(Date time, int day) {
		if (time == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + day);
		return c.getTime();
	}

	/**
	 * 指定时间增加月份
	 * 
	 * @param time 时间
	 * @param month 月份
	 * @return 返回新的时间
	 */
	public static Date addMonths(Date time, int month) {
		if (time == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) + month);
		return c.getTime();
	}

	/**
	 * 组合ip
	 * 
	 * @param ip ip的字节数组形式
	 * @return 返回字符串形式的ip
	 */
	public static String getIpStringFromBytes(byte[] ip) {
		sb.delete(0, sb.length());
		sb.append(ip[0] & 0xFF);
		sb.append('.');
		sb.append(ip[1] & 0xFF);
		sb.append('.');
		sb.append(ip[2] & 0xFF);
		sb.append('.');
		sb.append(ip[3] & 0xFF);
		return sb.toString();
	}

	/**
	 * 判断IP是否相等
	 * 
	 * @param ip1 IP的字节数组形式
	 * @param ip2 IP的字节数组形式
	 * @return 如果相等，则返回true，否则返回false
	 */
	public static boolean isIpEquals(byte[] ip1, byte[] ip2) {
		return (ip1[0] == ip2[0] && ip1[1] == ip2[1] && ip1[2] == ip2[2] && ip1[3] == ip2[3]);
	}

	/**
	 * 根据某种编码方式将字节数组转换成字符串
	 * 
	 * @param b
	 *            字节数组
	 * @param offset
	 *            要转换的起始位置
	 * @param len
	 *            要转换的长度
	 * @param encoding
	 *            编码方式
	 * @return 如果encoding不支持，返回一个缺省编码的字符串
	 */
	public static String getString(byte[] b, int offset, int len, String encoding) {
		try {
			return new String(b, offset, len, encoding);
		} catch (UnsupportedEncodingException e) {
			return new String(b, offset, len);
		}
	}

	/**
	 * 字符转换二进制数据
	 * 
	 * @param src 字节数组
	 * @return 返回字符串
	 */
	public static String stringToBinary(byte[] src) {
		StringBuffer sb = new StringBuffer();
		byte[][] des = new byte[src.length][16];
		for (int i = 0; i < src.length; i++)
			for (int j = 0; j < 16; j++)
				des[i][j] = (byte) ((src[i] >> j) & 0x1);

		for (int i = 0; i < src.length; i++) {
			for (int j = 0; j < 16; j++)
				sb.append(des[i][j]);
		}
		return sb.toString();
	}

	/**
	 * 生成随机数
	 * 
	 * @param len 随机数长度
	 * @return 返回随机数
	 */
	public static String randomNumber(int len) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			sb.append(Math.abs(random.nextInt()) % 10);
		}
		return sb.toString();
	}

	/**
	 * 系统时间秒
	 * 
	 * @return 返回系统时间
	 */
	public static String timeForString() {
		Long l = System.currentTimeMillis();
		return String.valueOf(Math.abs(l.intValue()));
	}

	/**
	 * 获取参数
	 * 
	 * @param str 字符串
	 * @return 如果字符串为null 则返回一个空字符串
	 */
	public static String getParString(String str) {
		if (StringUtil.isBlank(str))
			return "";
		return str;
	}

	/**
	 * 判断是否是中文字符
	 * 
	 * @param chChar 字符
	 * @return 如果中文字符，则返回true，否则返回false
	 */
	public static boolean isChinese(char chChar) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(chChar);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param phoneNumber 手机号码
	 * @return 如果是手机号，则返回true，否则返回false
	 */
	public static boolean isMobile(String phoneNumber) {
		phoneNumber = phoneNumber.trim();
		String pattern = "^[1][1-8][0-9]{9}";
		return phoneNumber.matches(pattern);
	}

	public static String formatResource(Object[] info, String require) {
		require = require.replaceAll("\'", "\"");
		String result = MessageFormat.format(require, info);
		return result.replaceAll("\"", "\'");
	}

	/**
	 * 计算两个时间间隔天数
	 * 
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @return 返回间隔天数
	 */
	public static int getDaysBetween(Calendar beginDate, Calendar endDate) {
		if (beginDate.after(endDate)) {
			java.util.Calendar swap = beginDate;
			beginDate = endDate;
			endDate = swap;
		}
		int days = endDate.get(Calendar.DAY_OF_YEAR) - beginDate.get(Calendar.DAY_OF_YEAR);
		int y2 = endDate.get(Calendar.YEAR);
		if (beginDate.get(Calendar.YEAR) != y2) {
			beginDate = (Calendar) beginDate.clone();
			do {
				days += beginDate.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
				beginDate.add(Calendar.YEAR, 1);
			} while (beginDate.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * 读取文件后缀名称
	 * 
	 * @param filePath
	 *            文件路径 格式如:/../a.txt
	 * @return 返回文件后缀名
	 */
	public static String getFileFix(String filePath) {
		String temp = "";
		if (filePath != null) {
			temp = filePath.substring(filePath.indexOf("."), filePath.length());
		}
		return temp;
	}

	/**
	 * 将数据流转换成字符串
	 * 
	 * @param dataFlow
	 *            数据流
	 * @return 返回字符串
	 */
	public static String convertStreamToString(InputStream dataFlow) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(dataFlow));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				dataFlow.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}


	/**
	 * 检测字符串,处理utf-8的4个字节的问题
	 * @param str 需要处理的字符串
	 * @return 返回处理后的字符串
	 */
	public static String checkStr(String str) {
		String s = null;
		char[] cc = str.toCharArray();
		for (int i = 0; i < cc.length; i++) {
			boolean b = isValidChar(cc[i]);
			if (!b)
				cc[i] = ' ';
		}
		s = String.valueOf(cc);
		return s;
	}

	/**
	 * 判断是否是有效的中文字
	 */
	/**
	 * 
	 * @param ch 字符
	 * @return 如果是有效中文，则返回true，否则返回false
	 */
	private static boolean isValidChar(char ch) {
		if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
			return true;
		if ((ch >= 0x4e00 && ch <= 0x7fff) || (ch >= 0x8000 && ch <= 0x952f))
			return true;// 简体中文汉字编码
		return false;
	}

	/**
	 * 除去字符窜中重复的字符
	 * 
	 * @param content
	 *            　原始内容
	 * @param target
	 *            　重复内容
	 * @return　返回除去后的字符串
	 */
	public static String removeRepeatStr(String content, String target) {
		// int index = content.indexOf(target);
		// content = checkRepeatStr( content,target,index);
		StringBuffer sb = new StringBuffer(content);
		for (int i = 0; i < sb.length()-1; i++) {

			if (sb.substring(i, i + target.length()).equals(target) && sb.substring(i, i + target.length()).equals(sb.substring(i + 1, i + target.length() + 1))) {
				sb.delete(i, i + target.length());
				if (i + target.length() + 1 > sb.length()) {
					break;
				} else {
					i--;
				}
			}

		}
		return sb.toString();
	}

	/**
	 * 验证邮箱格式
	 * 
	 * @param email
	 *            邮箱
	 * @return 如果是邮箱，则返回true，否则返回false
	 */
	public static Boolean isEmail(String email) {
		boolean tag = true;
		final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	/**
	 * 组织url 的get请求地址
	 * 
	 * @param url
	 *            原地址
	 * @param parm
	 *            参数 推荐格式:参数=值
	 * @return 饭胡新的地址
	 */
	public static String buildUrl(String url, String parm) {
		if (url.indexOf("?") > 0) {
			return url += "&" + parm;
		} else {
			return url += "?" + parm;
		}
	}
	
	/**
	 * 组织path路径, 例如:buildPath(a,b,c); 返回:a/b/c
	 * @param params 所有对象
	 * @return 返回新的路径地址
	 */
	public static String buildPath(Object... params) {
		String temp = "";
		for(Object o:params) {
			temp+=File.separator+o;
		} 
		return temp;
	}

	/**
	 * 组织url 的get请求地址
	 * 
	 * @param url
	 *            原地址
	 * @param parms
	 *            参数集合 格式:key参数=值value
	 * @return 返回新的地址
	 */
	@SuppressWarnings("rawtypes")
	public static String buildUrl(String url, Map parms) {
		Iterator key = parms.keySet().iterator();
		String paramsStr = "";
		while (key.hasNext()) {
			Object temp = key.next();
			if (isBlank(parms.get(temp))) {
				continue;
			}
			if (paramsStr != "") {
				paramsStr += "&";
			}
			paramsStr += (temp + "=" + parms.get(temp));
		}

		if (paramsStr != "") {
			if (url.indexOf("?") > 0) {
				return url += "&" + paramsStr;
			} else {
				return url += "?" + paramsStr;
			}
		}
		return url;
	}
	
	/**
	 * 根据java属性的字段大写转换为数据表字段，前提是数据库字段设计规范必需遵守ms开发规范
	 * @param property 属性
	 * @return 大写数据库字段
	 */ 
	public static String javaProperty2DatabaseCloumn(String property) {
		 	String[] ss = property.split("(?<!^)(?=[A-Z])");
	        StringBuffer sb = new StringBuffer();
	        for(int i = 0 ;i < ss.length; i ++){
	        	sb.append(ss[i]);
	        	if (i<ss.length-1) {
	        		sb.append("_");
	        	}
	        }
	        if (!StringUtil.isBlank(sb)) {
	        	return sb.toString().toUpperCase();
	        } else {
	        	return null;
	        }
	}

	/**
	 * 使用 Map按key进行排序
	 * 
	 * @param map 需要排序的Map
	 * @return 返回排序后的Map
	 */
	public static Map<String, String> sortMapByKey(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
		sortMap.putAll(map);
		return sortMap;
	}
	
    /** 
     * 使用 Map按value进行排序 
     * @param map 需要排序的Map
     * @return 返回排序后的Map
     */  
    public static Map<String, String> sortMapByValue(Map<String, String> map) {  
        if (map == null || map.isEmpty()) {  
            return null;  
        }  
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();  
        List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(map.entrySet());  
        Collections.sort(entryList, new MapValueComparator());  
        Iterator<Map.Entry<String, String>> iter = entryList.iterator();  
        Map.Entry<String, String> tmpEntry = null;  
        while (iter.hasNext()) {  
            tmpEntry = iter.next();  
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
        }  
        return sortedMap;  
    }  
    
    /**
     * 验证字符串是否符合单号规则，
     * 申通单号由12位数字组成，常见以268**、368**、58**等开头
     * EMS单号由13位字母和数字组成，开头和结尾二位是字母，中间是数字
     * 顺丰单号由12位数字组成，常见以电话区号后三位开头
     * 圆通单号由10位字母数字组成，常见以1*、2*、6*、8*、D*及V*等开头
     * 中通单号由12位数字组成，常见以2008**、6**、010等开头
     * 韵达单号由13位数字组成，常见以10*、12*、19*等开头
     * 天天单号由14位数字组成，常见以6**、5*、00*等开头
     * 汇通快递查询单号由13位数字编码组成，常见以0*或者B*、H*开头
     * 速尔的快递单号由12位数字组成的
     * 德邦的货运单号现在是以1或2开头的8位数字组成
     * 宅急送单号由10位数字组成，常见以7**、6**、5**等开头
     * @param str 需要验证的字符串
     * @return 如果是单号，则返回true，否则返回false
     */
    public static boolean isExpressNo(String str) {
    	if (StringUtil.isBlank(str)) {
    		return false;
    	}
    	//根据长度来判断
    	if (str.length()==13)  { //邮政EMS
    		return true;
    	} else if (str.length()==12)  { //中通
    		return true;
    	}
    	return true;
    }
    
    /**
	 * 把数组转换成set
	 * 
	 * @param array
	 * @return
	 */
	public static Set<?> array2Set(Object[] array) {
		Set<Object> set = new TreeSet<Object>();
		for (Object id : array) {
			if (null != id) {
				set.add(id);
			}
		}
		return set;
	}
}  

class MapKeyComparator implements Comparator<String> {
	/**
	 * 根据字典排序比较字符串
	 * @param str1 需要比较的字符串1
	 * @param str2 需要比较的字符串2
	 * @return 为0相等，为负数则Str1< str2, 大于0 则str1>str2
	 */
	public int compare(String str1, String str2) {
		return str1.compareTo(str2);
	}
}

class MapValueComparator implements Comparator<Map.Entry<String, String>> {  
	/**
	 * 根据字典排序比较Map
	 * @param str1 需要比较的Map1
	 * @param str2 需要比较的Map2
	 * @return 为0相等，为负数则Str1< str2, 大于0 则str1>str2
	 */
    public int compare(Entry<String, String> str1, Entry<String, String> str2) {  
        return str1.getValue().compareTo(str2.getValue());  
    }  
}