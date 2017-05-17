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
		long nd = 1000*24*60*60;//һ��ĺ�����
		long nh = 1000*60*60;//һСʱ�ĺ�����
		long nm = 1000*60;//һ���ӵĺ�����
		long ns = 1000;//һ���ӵĺ�����
		
		long diff;
		
		try {
			//�������ʱ��ĺ���ʱ�����
			long starttime = sdf.parse(startTime).getTime();
			long endtime = sdf.parse(endTime).getTime();
			diff = sdf.parse(endTime).getTime() - sdf.parse(startTime).getTime();
			long day = diff/nd;//����������
			long hour = diff%nd/nh;//��������Сʱ
			long min = diff%nd%nh/nm;//�������ٷ���
			long sec = diff%nd%nh%nm/ns;//����������
			//������
			System.out.println("ʱ����"+day+"��"+hour+"Сʱ"+min+"����"+sec+"�롣");
			System.out.println("��� ��" + diff / 1000 +  "��");
			} catch (ParseException e) {
			e.printStackTrace();
		}
	   
	}
}
