package com.fanfte.datatransfer;

import java.util.ArrayList;

public class DataHandler {
	public static ThreadLocal<ArrayList<Double>> datas = new ThreadLocal<ArrayList<Double>>();
	public static ThreadLocal<ArrayList<Double>> averageWeight = new ThreadLocal<ArrayList<Double>>();
	private static ThreadLocal<Double> averageSpeed = new ThreadLocal<Double>() {
		protected Double initialValue() {
			return 0d;
		}
	};
	private static ThreadLocal<Double> oldWeight = new ThreadLocal<Double>();
	private static ThreadLocal<Double> newWeight = new ThreadLocal<Double>();
	private static ThreadLocal<Double> diff = new ThreadLocal<Double>();
	private static Integer count = 0;
	private static ThreadLocal<Double> remainingTime = new ThreadLocal<Double>();
	private static ThreadLocal<Double> bottleWeight = new ThreadLocal<Double>() {
		protected Double initialValue() {return 54.0d;};
	};
	private static Integer startFlag = 0; 
	private static Integer stopFlag = 0;
	private static ThreadLocal<Double> startWeight = new ThreadLocal<Double>();;
	private static String mac;
	private static String ip;
	public ThreadLocal<ArrayList<Double>> getDatas() {
		return datas;
	}
	public static void setDatas(ThreadLocal<ArrayList<Double>> datas) {
		DataHandler.datas = datas;
	}
	public ThreadLocal<ArrayList<Double>> getAverageWeight() {
		return averageWeight;
	}
	public static void setAverageWeight(ThreadLocal<ArrayList<Double>> averageWeight) {
		DataHandler.averageWeight = averageWeight;
	}
	public ThreadLocal<Double> getAverageSpeed() {
		return averageSpeed;
	}
	public static void setAverageSpeed(ThreadLocal<Double> averageSpeed) {
		DataHandler.averageSpeed = averageSpeed;
	}
	public ThreadLocal<Double> getOldWeight() {
		return oldWeight;
	}
	public static void setOldWeight(ThreadLocal<Double> oldWeight) {
		DataHandler.oldWeight = oldWeight;
	}
	public ThreadLocal<Double> getNewWeight() {
		return newWeight;
	}
	public static void setNewWeight(ThreadLocal<Double> newWeight) {
		DataHandler.newWeight = newWeight;
	}
	public ThreadLocal<Double> getDiff() {
		return diff;
	}
	public static void setDiff(ThreadLocal<Double> diff) {
		DataHandler.diff = diff;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		DataHandler.count = count;
	}
	public static ThreadLocal<Double> getRemainingTime() {
		return remainingTime;
	}
	public static void setRemainingTime(ThreadLocal<Double> remainingTime) {
		DataHandler.remainingTime = remainingTime;
	}
	public static ThreadLocal<Double> getBottleWeight() {
		return bottleWeight;
	}
	public static void setBottleWeight(ThreadLocal<Double> bottleWeight) {
		DataHandler.bottleWeight = bottleWeight;
	}
	public Integer getStartFlag() {
		return startFlag;
	}
	public void setStartFlag(Integer startFlag) {
		DataHandler.startFlag = startFlag;
	}
	public ThreadLocal<Double> getStartWeight() {
		return startWeight;
	}
	public static void setStartWeight(ThreadLocal<Double> startWeight) {
		DataHandler.startWeight = startWeight;
	}
	public String getMac() {
		return mac;
	}
	public static void setMac(String mac) {
		DataHandler.mac = mac;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		DataHandler.ip = ip;
	}
	public static Integer getStopFlag() {
		return stopFlag;
	}
	public void setStopFlag(Integer stopFlag) {
		DataHandler.stopFlag = stopFlag;
	}
	
	
}
