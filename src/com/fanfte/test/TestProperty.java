package com.fanfte.test;

import java.io.IOException;

import org.junit.Test;

import com.fanfte.tutils.SystemUtils;

public class TestProperty {
	
	@Test
	public void testWrite() {
//		SystemUtils.writeProperty("aaa", "seqno1");
		try {
			SystemUtils.WriteProperties("D:\\hibernate_works\\Hospital\\" + "SeqNo.opts", "vvv", "2223");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRead() {
		String readProperty = SystemUtils.readProperty("D:\\hibernate_works\\Hospital\\" + "SeqNo.opts", "aaa");
		System.out.println("result " + readProperty);
	}
	
}
