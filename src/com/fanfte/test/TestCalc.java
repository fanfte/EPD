package com.fanfte.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestCalc {
	public static void main(String[] args) {
		
		calcTime("2017-3-6 12:33:11", "2017-3-6 12:38:00", "yyyy-MM-dd HH:mm:ss");
	}
	
	public static void calcTime(String startTime, String endTime, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//一秒钟的毫秒数
		
		long diff;
		
		try {
			//获得两个时间的毫秒时间差异
			long starttime = sdf.parse(startTime).getTime();
			long endtime = sdf.parse(endTime).getTime();
			diff = sdf.parse(endTime).getTime() - sdf.parse(startTime).getTime();
			long day = diff/nd;//计算差多少天
			long hour = diff%nd/nh;//计算差多少小时
			long min = diff%nd%nh/nm;//计算差多少分钟
			long sec = diff%nd%nh%nm/ns;//计算差多少秒
			//输出结果
			System.out.println("时间相差："+day+"天"+hour+"小时"+min+"分钟"+sec+"秒。");
			System.out.println("相差 ：" + diff / 1000 +  "秒");
			} catch (ParseException e) {
			e.printStackTrace();
		}
	   
	}
}
