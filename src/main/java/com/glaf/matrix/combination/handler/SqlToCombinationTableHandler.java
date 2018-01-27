package com.glaf.matrix.combination.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.StringTools;
 

import com.glaf.matrix.combination.domain.CombinationApp;
import com.glaf.matrix.combination.domain.CombinationHistory;
import com.glaf.matrix.combination.service.CombinationAppService;
import com.glaf.matrix.combination.service.CombinationHistoryService;
import com.glaf.matrix.combination.task.CombinationAppTask;
import com.glaf.matrix.data.util.ExecutionUtils;
import com.glaf.matrix.handler.DataExecutionHandler;

public class SqlToCombinationTableHandler implements DataExecutionHandler {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public void execute(Object id, Map<String, Object> context) {
		long syncId = Long.parseLong(id.toString());
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		CombinationAppService combinationAppService = ContextFactory
				.getBean("com.glaf.matrix.combination.service.combinationAppService");
		CombinationHistoryService combinationHistoryService = ContextFactory
				.getBean("com.glaf.matrix.combination.service.combinationHistoryService");
		CombinationApp combinationApp = combinationAppService.getCombinationApp(syncId);
		if (combinationApp == null || !StringUtils.equals(combinationApp.getActive(), "Y")) {
			return;
		}
		List<Long> databaseIds = StringTools.splitToLong(combinationApp.getTargetDatabaseIds());
		if (databaseIds != null && !databaseIds.isEmpty()) {
			String jobNo = null;
			Database database = null;
			int runDay = DateUtils.getNowYearMonthDay();
			CombinationAppTask task = null;
			for (long targetDatabaseId : databaseIds) {
				jobNo = "combination_app_" + combinationApp.getId() + "_" + targetDatabaseId + "_" + runDay;
				try {
					database = databaseService.getDatabaseById(targetDatabaseId);
					if (database != null) {
						ExecutionUtils.put(jobNo, new Date());
						long start = System.currentTimeMillis();
						task = new CombinationAppTask(combinationApp.getSrcDatabaseId(), targetDatabaseId, syncId,
								jobNo, context);

						CombinationHistory his = new CombinationHistory();
						his.setCreateBy("system");
						his.setDatabaseId(targetDatabaseId);
						his.setDatabaseName(database.getTitle());
						his.setDeploymentId(combinationApp.getDeploymentId());
						his.setSyncId(combinationApp.getId());

						if (!task.execute()) {
							his.setStatus(-1);
						} else {
							his.setStatus(1);
						}
						his.setTotalTime((int) (System.currentTimeMillis() - start));

						combinationHistoryService.save(his);
					}
				} catch (Exception ex) {
				}
			}
		}
	}

}
