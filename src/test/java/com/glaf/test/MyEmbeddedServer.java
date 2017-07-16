package com.glaf.test;

import org.apache.geode.distributed.ServerLauncher;

public class MyEmbeddedServer {

	public static void main(String[] args) {
		ServerLauncher serverLauncher = new ServerLauncher.Builder().setMemberName("server1").setServerPort(40404)
				.set("jmx-manager", "false").set("jmx-manager-start", "false").build();

		serverLauncher.start();

		System.out.println("Cache server successfully started");
	}
}
