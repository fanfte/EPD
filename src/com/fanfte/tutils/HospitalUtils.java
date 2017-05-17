package com.fanfte.tutils;

public class HospitalUtils {

	public static String initInfusion(String str) {
		String result = "";
		result = HttpUtils.postDevice(str);
		System.out.println("post result  " + result);
		return result;
	}

	public static String startInfusion(String str) {
		String result = "";
		result = HttpUtils.postInfusionStart(str);
		System.out.println("post result  " + result);
		return result;
	}

	public static String heartBeat(String str) {
		String result = "";
		result = HttpUtils.heartBeats(str);
		System.out.println("heartBeats result  " + result);
		return result;
	}

	public static String deleteInfusion(String str) {
		String result = "";
		result = HttpUtils.delete(str);
		System.out.println("delete result  " + result);
		return result;
	}
	
	public static String getJsonData(String s) {
		String result = "";
		if(s.contains("Message") && s.contains("ResultCode")) {
			String resultCode = s.substring(s.indexOf("ResultCode") + 12, s.indexOf("Message") - 2);
			System.out.println("resultCode" + resultCode);
			if(resultCode.equals("-1")) {
				System.out.println("init error");
				result = "error";
			}
			if(resultCode.equals("0")) {
				result = s.substring(s.indexOf("Data") + 6, s.indexOf("CurrentTime") - 2);
				System.out.println(result);
			}
		}
		return result;
	}

}
