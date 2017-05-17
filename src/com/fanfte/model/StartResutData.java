package com.fanfte.model;

public class StartResutData {
	private String mac;
	private String sequenceno;
	public StartResutData() {
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getSequenceno() {
		return sequenceno;
	}
	public void setSequenceno(String sequenceno) {
		this.sequenceno = sequenceno;
	}
	public StartResutData(String mac, String sequenceno) {
		this.mac = mac;
		this.sequenceno = sequenceno;
	}
}
