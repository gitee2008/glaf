package com.glaf.core.util;

import java.util.List;

import com.glaf.core.config.Environment;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.factory.DatabaseFactory;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.IDatabaseService;

public final class EnvUtils {

	public static String getTenantPartition(LoginContext loginContext) {
		if (loginContext.getTenant() != null) {
			return String.valueOf(loginContext.getTenant().getId());
		}
		throw new RuntimeException("user tenant is null");
	}

	public static String getTenantPartition(String userId) {
		LoginContext loginContext = IdentityFactory.getLoginContext(userId);
		if (loginContext.getTenant() != null) {
			return String.valueOf(loginContext.getTenant().getId());
		}
		throw new RuntimeException("user tenant is null");
	}

	public static void setEnv(LoginContext loginContext, boolean write) {
		if (loginContext.getTenant() != null) {
			long databaseId = loginContext.getTenant().getDatabaseId();
			if (databaseId > 0) {
				IDatabaseService databaseService = ContextFactory.getBean("databaseService");
				Database database = databaseService.getDatabaseById(databaseId);
				if (write) {
					if (database != null) {
						/**
						 * 切换到主库的环境名称
						 */
						Environment.setCurrentSystemName(database.getName());
					}
				} else {
					List<Database> list = DatabaseFactory.getInstance().getSlaveDatabases(databaseId);
					if (list != null && !list.isEmpty()) {
						java.util.Random rand = new java.util.Random();
						Database db = list.get(rand.nextInt(list.size() - 1));
						/**
						 * 切换到随机一个读库的环境名称
						 */
						Environment.setCurrentSystemName(db.getName());
					} else {
						if (database != null) {
							/**
							 * 未做读写分离， 切换到读写库的环境名称
							 */
							Environment.setCurrentSystemName(database.getName());
						}
					}
				}
			}
		}
	}

	private EnvUtils() {

	}

}
