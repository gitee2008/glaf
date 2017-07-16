package com.glaf.test;

import java.util.Map;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.*;

import com.glaf.core.util.UUID32;

public class GeodeTest1 {

	public static void main(String[] args) throws Exception {
		System.out.println("----------------GeodeTest1---------------");
		ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334).create();
		System.out.println(cache);
		Region<String, String> region = cache
				.<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY).create("region");

		region.put("1", "Hello");
		region.put("2", "World");

		for (Map.Entry<String, String> entry : region.entrySet()) {
			System.out.format("key = %s, value = %s\n", entry.getKey(), entry.getValue());
		}

		for (int i = 0; i < 1000000; i++) {
			if (i > 0 && i % 1000 == 0) {
				System.out.println("size:" + region.size());
				System.out.println(region.get("key_" + i));
			}
			region.put("key_" + i, UUID32.getUUID());
		}
		System.out.println("size:" + region.size());
		cache.close();
	}
}