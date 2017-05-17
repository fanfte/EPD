package com.fanfte.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fanfte.global.GlobalConstants;
import com.fanfte.model.JsonResult;
import com.fanfte.model.StartResutData;
import com.fanfte.tutils.HospitalUtils;
import com.fanfte.tutils.HttpUtils;
import com.fanfte.tutils.JsonUtil;
import com.fanfte.tutils.JsonUtils;
import com.fanfte.tutils.SystemUtils;

public class TestSendData {

	
	private String jsonString = "{\"Data\":\"null\",\"CurrentTime\":"
			+ "\"2017-03-28T12:16:29.4316554+08:00\",\"ResultCode\":-1,\"Message\":\"SysInfo)\"}";
	
	private String mac = "0x123858";
	
	private String ip = "192.168.1.14";
	
	private String seqNo = "2ea0e462-d845-4bc0-ad17-5ecc01102a5a";
	@Test
	public void testInit() {
		String s = HttpUtils.postDevice(JsonUtils.makeDeviceJson(mac, ip, ""));
		System.out.println("post result  " + s);
	}
	@Test
	public void testStart() {
		String s = HttpUtils.postInfusionStart(JsonUtils.makeInfusionStartJson(mac, ip, "", ""));
		System.out.println("post result  " + s);
	}
	
	@Test
	public void testHeartBeat() {
		String s = HttpUtils.heartBeats(JsonUtils.makeHeartBeatJson(mac, ip, seqNo, "3", "2", "1", 2));
		System.out.println("heartBeats result  " + s);
	}
	
	@Test
	public void testDelete() {
		String s = HttpUtils.delete(JsonUtils.makeEndJson(mac, "2", "2", seqNo));
		System.out.println("delete result  " + s);
	}
	
	@Test
	public void delete() throws IOException {
		Map<String, String> properties = SystemUtils.GetAllProperties(GlobalConstants.localPath + "\\" + "SeqNo.opts");
		Set<String> keySet = properties.keySet();
		for(Object key : keySet) {
			String seqNo = properties.get(key);
			String mac = (String) key;
			System.out.println(key + ":" +  properties.get(key));
			String s = HttpUtils.delete(JsonUtils.makeEndJson(mac, "2", "2", seqNo));
			System.out.println("delete result  " + s);
		}
	}
	
	@Test
	public void testJson() {
		JsonResult obj = (JsonResult) JsonUtil.getInstance().json2Obj(jsonString, JsonResult.class);
		System.out.println(obj.getMessage());
	}
	
	@Test
	public void testHeartBeat2() {
		String result = HttpUtils.heartBeats(JsonUtils.makeHeartBeatJson(mac, ip, seqNo, "3", "2", "1", 2));
		System.out.println("result" + result);
		String data = HospitalUtils.getJsonData(result);
		System.out.println("data" + data);
		StartResutData jsonData = (StartResutData) JsonUtil.getInstance().json2Obj(data.toLowerCase(), StartResutData.class);
		System.out.println(jsonData.getMac());
	}

}
