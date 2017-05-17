package com.fanfte.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestArray {
	@Test
	public void test1() {
		List<String> l = new ArrayList<String>();
		l.add("a");
		l.add("b");
		System.out.println(l.get(1));
	}
	
}
