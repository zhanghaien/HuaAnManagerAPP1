/*
 * Copyright (C) 2015 iChano incorporation's Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package luo.library.base.utils;

import android.annotation.SuppressLint;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

public class DateUtils {
	
	public static long formatTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date;
		try {
			date = sdf.parse(time);
			long millis = date.getTime();
			return millis;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String str = "";
		try {
			str = sdf.format(System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Integer.parseInt(str);
	}
	
	public static String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = "";
		try {
			str = sdf.format(System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static String parseTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = "";
		try {
			str = sdf.format(System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static String parseUpdateTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = "";
		try {
			str = sdf.format(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static String parseTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String str = "00:00";
		try {
			str = sdf.format(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static String dateToStr(Date date) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static ThreadLocal<SimpleDateFormat> fullTime = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected synchronized SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	
	public static Date parseFullTime(String fullTimeString) throws ParseException {
		return fullTime.get().parse(fullTimeString);
	}

	/**
	 * 对日期(时间)中的日进行加减计算. <br>
	 * 例子: <br>
	 * 如果Date类型的d为 2005年8月20日,那么 <br>
	 * calculateByDate(d,-10)的值为2005年8月10日 <br>
	 * 而calculateByDate(d,+10)的值为2005年8月30日 <br>
	 * 
	 * @param d
	 *            日期(时间).
	 * @param amount
	 *            加减计算的幅度.+n=加n天;-n=减n天.
	 * @return 计算后的日期(时间).
	 */
	public static Date calculateByDate(Date d, int amount) {
		return calculate(d, GregorianCalendar.DATE, amount);
	}

	public static Date calculateByMinute(Date d, int amount) {
		return calculate(d, GregorianCalendar.MINUTE, amount);
	}

	public static Date calculateByYear(Date d, int amount) {
		return calculate(d, GregorianCalendar.YEAR, amount);
	}
	public static String calculateByDate(String date,String format, int dayOffset) {
		

		SimpleDateFormat formater = new SimpleDateFormat();
		try {
			formater.applyPattern(format);
			Date time = formater.parse(date);
			long ts = time.getTime()+dayOffset*24*3600*1000L;
			Date newDate = new Date(ts);
			return date2String(format,newDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 对日期(时间)中由field参数指定的日期成员进行加减计算. <br>
	 * 例子: <br>
	 * 如果Date类型的d为 2005年8月20日,那么 <br>
	 * calculate(d,GregorianCalendar.YEAR,-10)的值为1995年8月20日 <br>
	 * 而calculate(d,GregorianCalendar.YEAR,+10)的值为2015年8月20日 <br>
	 * 
	 * @param d
	 *            日期(时间).
	 * @param field
	 *            日期成员. <br>
	 *            日期成员主要有: <br>
	 *            年:GregorianCalendar.YEAR <br>
	 *            月:GregorianCalendar.MONTH <br>
	 *            日:GregorianCalendar.DATE <br>
	 *            时:GregorianCalendar.HOUR <br>
	 *            分:GregorianCalendar.MINUTE <br>
	 *            秒:GregorianCalendar.SECOND <br>
	 *            毫秒:GregorianCalendar.MILLISECOND <br>
	 * @param amount
	 *            加减计算的幅度.+n=加n个由参数field指定的日期成员值;-n=减n个由参数field代表的日期成员值.
	 * @return 计算后的日期(时间).
	 */
	private static Date calculate(Date d, int field, int amount) {
		if (d == null)
			return null;
		GregorianCalendar g = new GregorianCalendar();
		g.setGregorianChange(d);
		g.add(field, amount);
		return g.getTime();
	}

	/**
	 * 日期(时间)转化为字符串.
	 * 
	 * @param formater
	 *            日期或时间的格式.
	 * @param aDate
	 *            java.util.Date类的实例.
	 * @return 日期转化后的字符串.
	 */
	public static String date2String(String formater, Date aDate) {
		if (formater == null || "".equals(formater))
			return null;
		if (aDate == null)
			return null;
		return (new SimpleDateFormat(formater)).format(aDate);
	}
	/**
	 * 相同日期不同格式之间转换
	 * @param date 当前格式的日期字符串
	 * @param originFormater 原格式
	 * @param destFormater 目标格式
	 * @return 目标格式的日期字符串
	 */
	public static String dateString2dateString(String date,String originFormater,String destFormater) {
		if (date == null || "".equals(date))
			return "";
		SimpleDateFormat formater = new SimpleDateFormat();
		try {
			formater.applyPattern(originFormater);
			Date time = formater.parse(date);
			return date2String(destFormater, time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
		
	}
	/**
	 * 当前日期(时间)转化为字符串.
	 * 
	 * @param formater
	 *            日期或时间的格式.
	 * @return 日期转化后的字符串.
	 */
	public static String date2String(String formater) {
		return date2String(formater, new Date());
	}

	/**
	 * 获取当前日期对应的星期数. <br>
	 * 1=星期天,2=星期一,3=星期二,4=星期三,5=星期四,6=星期五,7=星期六
	 * 
	 * @return 当前日期对应的星期数
	 */
	public static int dayOfWeek() {
		GregorianCalendar g = new GregorianCalendar();
		int ret = g.get(Calendar.DAY_OF_WEEK);
		g = null;
		return ret;
	}

	/**
	 * 获取所有的时区编号. <br>
	 * 排序规则:按照ASCII字符的正序进行排序. <br>
	 * 排序时候忽略字符大小写.
	 *
	 * @return 所有的时区编号(时区编号已经按照字符[忽略大小写]排序).
	 */
	public static String[] fecthAllTimeZoneIds() {
		Vector<String> v = new Vector<String>();
		String[] ids = TimeZone.getAvailableIDs();
		for (int i = 0; i < ids.length; i++) {
			v.add(ids[i]);
		}
		java.util.Collections.sort(v, String.CASE_INSENSITIVE_ORDER);
		v.copyInto(ids);
		v = null;
		return ids;
	}

	/**
	 * 将日期时间字符串根据转换为指定时区的日期时间.
	 *
	 * @param srcFormater
	 *            待转化的日期时间的格式.
	 * @param srcDateTime
	 *            待转化的日期时间.
	 * @param dstFormater
	 *            目标的日期时间的格式.
	 * @param dstTimeZoneId
	 *            目标的时区编号.
	 *
	 * @return 转化后的日期时间.
	 */
	public static String string2Timezone(String srcFormater, String srcDateTime, String dstFormater,
			String dstTimeZoneId) {
		if (srcFormater == null || "".equals(srcFormater))
			return null;
		if (srcDateTime == null || "".equals(srcDateTime))
			return null;
		if (dstFormater == null || "".equals(dstFormater))
			return null;
		if (dstTimeZoneId == null || "".equals(dstTimeZoneId))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);
		try {
			int diffTime = getDiffTimeZoneRawOffset(dstTimeZoneId);
			Date d = sdf.parse(srcDateTime);
			long nowTime = d.getTime();
			long newNowTime = nowTime - diffTime;
			d = new Date(newNowTime);
			return date2String(dstFormater, d);
		} catch (ParseException e) {
			return null;
		} finally {
			sdf = null;
		}
	}

	/**
	 * 将日期时间字符串根据转换为指定时区的日期时间.
	 *
	 * @param srcFormater
	 *            待转化的日期时间的格式.
	 * @param srcDateTime
	 *            待转化的日期时间.
	 * @param dstFormater
	 *            目标的日期时间的格式.
	 * @param dstTimeZoneId
	 *            目标的时区编号.
	 *
	 * @return 转化后的日期时间.
	 */
	public static String string2Timezone(String srcFormater, String srcDateTime, String dstFormater,
			String srcTimeZoneId,String dstTimeZoneId) {
		if (srcFormater == null || "".equals(srcFormater))
			return null;
		if (srcDateTime == null || "".equals(srcDateTime))
			return null;
		if (dstFormater == null || "".equals(dstFormater))
			return null;
		if (dstTimeZoneId == null || "".equals(dstTimeZoneId))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);
		try {
			int diffTime = getDiffTimeZoneRawOffset(srcTimeZoneId,dstTimeZoneId);
			Date d = sdf.parse(srcDateTime);
			long nowTime = d.getTime();
			long newNowTime = nowTime - diffTime;
			d = new Date(newNowTime);
			return date2String(dstFormater, d);
		} catch (ParseException e) {
			return null;
		} finally {
			sdf = null;
		}
	}

	/**
	 * 获取系统当前默认时区与UTC的时间差.(单位:毫秒)
	 *
	 * @return 系统当前默认时区与UTC的时间差.(单位:毫秒)
	 */
	@SuppressWarnings("unused")
	private static int getDefaultTimeZoneRawOffset() {
		return TimeZone.getDefault().getRawOffset();
	}

	/**
	 * 获取指定时区与UTC的时间差.(单位:毫秒)
	 *
	 * @param timeZoneId
	 *            时区Id
	 * @return 指定时区与UTC的时间差.(单位:毫秒)
	 */
	@SuppressWarnings("unused")
	private static int getTimeZoneRawOffset(String timeZoneId) {
		return TimeZone.getTimeZone(timeZoneId).getRawOffset();
	}

	/**
	 * 获取系统当前默认时区与指定时区的时间差.(单位:毫秒)
	 *
	 * @param timeZoneId
	 *            时区Id
	 * @return 系统当前默认时区与指定时区的时间差.(单位:毫秒)
	 */
	private static int getDiffTimeZoneRawOffset(String timeZoneId) {
		return TimeZone.getDefault().getRawOffset() - TimeZone.getTimeZone(timeZoneId).getRawOffset();
	}


	/**
	 * 获取系统当前默认时区与指定时区的时间差.(单位:毫秒)
	 *
	 * @param srcZoneId
	 *            时区Id
	 * @return 系统当前默认时区与指定时区的时间差.(单位:毫秒)
	 */
	private static int getDiffTimeZoneRawOffset(String srcZoneId, String toZoneId) {
		return TimeZone.getTimeZone(srcZoneId).getRawOffset() - TimeZone.getTimeZone(toZoneId).getRawOffset();
	}

	/**
	 * 将日期时间字符串根据转换为指定时区的日期时间.
	 *
	 * @param srcDateTime
	 *            待转化的日期时间.
	 * @param dstTimeZoneId
	 *            目标的时区编号.
	 *
	 * @return 转化后的日期时间.
	 * @see #string2Timezone(String, String, String, String)
	 */
	public static String string2TimezoneDefault(String srcDateTime, String srcTimeZoneId,String dstTimeZoneId) {
		return string2Timezone("yyyy-MM-dd'T'HH:mm:ss+0000", srcDateTime, "yyyy-MM-dd'T'HH:mm:ss'Z'", srcTimeZoneId,dstTimeZoneId);
	}

	public static String string2TimezoneUTF(String srcDateTime) {
		return string2Timezone("yyyy-MM-dd HH:mm:ss", srcDateTime, "yyyy-MM-dd'T'HH:mm:ss", "UTC");
	}






	// 获取当前日期
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}

	public static int[] getYMDArray(String datetime, String splite) {
		int[] date = {0, 0, 0, 0, 0};
		if (datetime != null && datetime.length() > 0) {
			String[] dates = datetime.split(splite);
			int position = 0;
			for (String temp : dates) {
				date[position] = Integer.valueOf(temp);
				position++;
			}
		}
		return date;
	}

	/**
	 * 将当前时间戳转化为标准时间函数
	 *
	 * @param time1
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime(String time1) {

		int timestamp = Integer.parseInt(time1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		try {
			String str = sdf.format(new Timestamp(intToLong(timestamp)));
			time = str.substring(11, 16);
			String month = str.substring(5, 7);
			String day = str.substring(8, 10);
			time = getDate(month, day) + time;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	public static String getTime(int timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		try {
			String str = sdf.format(new Timestamp(intToLong(timestamp)));
			time = str.substring(11, 16);

			String month = str.substring(5, 7);
			String day = str.substring(8, 10);
			time = getDate(month, day) + time;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	public static String getHMS(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = null;
		try {
			return sdf.format(new Date(timestamp));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 将当前时间戳转化为标准时间函数
	 *
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getHMS(String time) {

		long timestamp = Long.parseLong(time);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			String str = sdf.format(new Timestamp(timestamp));
			return str;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	// java Timestamp构造函数需传入Long型
	public static long intToLong(int i) {
		long result = (long) i;
		result *= 1000;
		return result;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDate(String month, String day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 24小时制
		Date d = new Date();
		;
		String str = sdf.format(d);
		@SuppressWarnings("unused")
		String nowmonth = str.substring(5, 7);
		String nowday = str.substring(8, 10);
		String result = null;

		int temp = Integer.parseInt(nowday) - Integer.parseInt(day);
		switch (temp) {
			case 0:
				result = "今天";
				break;
			case 1:
				result = "昨天";
				break;
			case 2:
				result = "前天";
				break;
			default:
				StringBuilder sb = new StringBuilder();
				sb.append(Integer.parseInt(month) + "月");
				sb.append(Integer.parseInt(day) + "日");
				result = sb.toString();
				break;
		}
		return result;
	}

	/* 将字符串转为时间戳 */
	public static String getTimeToStamp(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
				Locale.CHINA);
		Date date = new Date();
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String tmptime = String.valueOf(date.getTime()).substring(0, 10);

		return tmptime;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getYMD(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(timestamp));
	}

	public static String getDate(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return sdf.format(new Date(timestamp * 1000));
	}

	public static String getTimestamp() {
		long time = System.currentTimeMillis() / 1000;
		return String.valueOf(time);
	}


	public static String getDate() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(now);
		return date;
	}

	public static String getDateTime() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(now);
		return date;
	}

	public static String dateToString(Date date, String format) {
		if(date == null) {
			date = new Date();
		}

		if(format == null) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat timeFormate = new SimpleDateFormat(format);
		return timeFormate.format(date);
	}

	public static Date stringToDate(String date, String format) throws ParseException {
		if(format == null) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat timeFormate = new SimpleDateFormat(format);
		return date == null?new Date():timeFormate.parse(date);
	}

	public static int dateCompareToOtherDate(String date1, String date2) {
		boolean size = false;

		int size1;
		try {
			size1 = stringToDate(date1, "yyyy-MM-dd").compareTo(stringToDate(date2, "yyyy-MM-dd"));
		} catch (Exception var4) {
			var4.printStackTrace();
			size1 = -2;
		}

		return size1;
	}
}

