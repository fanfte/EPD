package com.fanfte.datatransfer;

import com.fanfte.tutils.HospitalUtils;
import com.fanfte.tutils.JsonUtils;

public class HeartBeatThread extends Thread {
	private String mac;
	private String ip;
	private String seqNo;
	
	private boolean threadAllDone = false;
	
	public boolean isThreadAllDone() {
		return threadAllDone;
	}

	public void setThreadAllDone(boolean threadAllDone) {
		this.threadAllDone = threadAllDone;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public HeartBeatThread(String mac, String ip,String seqNo) {
		this.mac = mac;
		this.ip = ip;
		this.seqNo = seqNo;
	}
	
	@Override
	public void run() {
		while(!threadAllDone) {
			try {
				Thread.sleep(10000);	
				HospitalUtils.heartBeat(JsonUtils.makeHeartBeatJson(mac, ip, seqNo, "3", "2", "1", 2));
				System.out.println("seqNo " + seqNo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
