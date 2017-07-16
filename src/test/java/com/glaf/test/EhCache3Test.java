package com.glaf.test;

import com.glaf.core.cache.ehcache3.EHCache3Impl;
import com.glaf.core.cache.guava.GuavaCache;
import com.glaf.core.util.UUID32;

public class EhCache3Test {

	public static void testEHCache3() {
		long start = System.currentTimeMillis();
		com.glaf.core.cache.Cache cache = new EHCache3Impl();
		for (int i = 0; i < 10000; i++) {
			cache.put("test", "key_" + i,
					UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID());
			if (i % 100 == 0) {
				System.out.println(cache.get("test", "key_" + i));
			}
		}
		long ts = System.currentTimeMillis() - start;
		System.out.println(ts);
	}

	public static void testGuavaCache() {
		long start = System.currentTimeMillis();
		com.glaf.core.cache.Cache cache = new GuavaCache();
		for (int i = 0; i < 10000; i++) {
			cache.put("test", "key_" + i,
					UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID() + UUID32.getUUID());
			if (i % 100 == 0) {
				System.out.println(cache.get("test", "key_" + i));
			}
		}
		long ts = System.currentTimeMillis() - start;
		System.out.println(ts);
	}

	public static void main(String[] args) throws Exception {
		testGuavaCache();
		Thread.sleep(2000L);
		testEHCache3();
		Thread.sleep(2000L);
		testGuavaCache();
		Thread.sleep(2000L);
		testEHCache3();
		Thread.sleep(2000L);
		testGuavaCache();
		Thread.sleep(2000L);
		testEHCache3();
	}

}
