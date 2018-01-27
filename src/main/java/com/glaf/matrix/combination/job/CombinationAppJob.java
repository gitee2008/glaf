package com.glaf.matrix.combination.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.job.BaseJob;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.StringTools;
import com.glaf.matrix.data.domain.ExecutionLog;
import com.glaf.matrix.data.util.ExecutionLogFactory;
import com.glaf.matrix.data.util.ExecutionUtils;
import com.glaf.matrix.combination.domain.CombinationApp;
import com.glaf.matrix.combination.domain.CombinationHistory;
import com.glaf.matrix.combination.query.CombinationAppQuery;
import com.glaf.matrix.combination.service.CombinationAppService;
import com.glaf.matrix.combination.service.CombinationHistoryService;
import com.glaf.matrix.combination.task.CombinationAppTask;
import com.glaf.matrix.util.SysParams;

public class CombinationAppJob extends BaseJob {

	protected static AtomicLong lastExecuteTime = new AtomicLong(System.currentTimeMillis());

	@Override
	public void runJob(JobExecutionContext context) throws JobExecutionException {
		logger.debug("-------------------------CombinationAppJob-----------------------");
		if ((System.currentTimeMillis() - lastExecuteTime.get()) < DateUtils.MINUTE) {
			return;
		}
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		CombinationAppService combinationAppService = ContextFactory
				.getBean("com.glaf.matrix.combination.service.combinationAppService");
		CombinationHistoryService combinationHistoryService = ContextFactory
				.getBean("com.glaf.matrix.combination.service.combinationHistoryService");
		CombinationAppQuery query = new CombinationAppQuery();
		query.autoSyncFlag("Y");
		query.active("Y");

		List<CombinationApp> rows = combinationAppService.list(query);
		if (rows != null && !rows.isEmpty()) {
			Map<String, Object> parameter = new HashMap<String, Object>();
			SysParams.putInternalParams(parameter);
			int runDay = DateUtils.getNowYearMonthDay();
			long ts = System.currentTimeMillis();
			CombinationAppTask task = null;
			List<ExecutionLog> logs = null;
			Database database = null;
			String jobNo = null;
			for (CombinationApp app : rows) {
				if (StringUtils.isNotEmpty(app.getTargetDatabaseIds())) {
					List<Long> targetDatabaseIds = StringTools.splitToLong(app.getTargetDatabaseIds(), ",");
					for (long targetDatabaseId : targetDatabaseIds) {
						jobNo = "combination_app_" + app.getId() + "_" + targetDatabaseId + "_" + runDay;
						if (!ExecutionUtils.contains(jobNo)) {
							try {
								CombinationHistory last = combinationHistoryService
										.getLatestCombinationHistory(app.getId(), targetDatabaseId);
								if (last != null && last.getStatus() == 1) {
									/**
									 * 判断是否满足时间间隔
									 */
									if ((ts - last.getCreateTime().getTime()) < app.getInterval() * 60 * 1000) {
										continue;
									}
								}
								long start = System.currentTimeMillis();
								/**
								 * 判断是否可以执行
								 */
								logs = ExecutionLogFactory.getInstance().getTodayExecutionLogs("combination_app",
										String.valueOf(app.getId()));
								if (!ExecutionLogFactory.getInstance().canExecution(logs, jobNo)) {
									continue;
								}

								database = databaseService.getDatabaseById(targetDatabaseId);
								if (database != null) {
									ExecutionUtils.put(jobNo, new Date());
									task = new CombinationAppTask(app.getSrcDatabaseId(), targetDatabaseId, app.getId(),
											jobNo, parameter);
									CombinationHistory his = new CombinationHistory();
									his.setCreateBy("system");
									his.setDatabaseId(targetDatabaseId);
									his.setDeploymentId(app.getDeploymentId());
									his.setSyncId(app.getId());
									if (!task.execute()) {
										his.setStatus(-1);
									} else {
										his.setStatus(1);
									}
									his.setTotalTime((int) (System.currentTimeMillis() - start));
									his.setDatabaseName(database.getTitle() + "[" + database.getHost() + "]");
									combinationHistoryService.save(his);
								}
							} catch (Exception ex) {
								logger.error(ex);
							} finally {
								ExecutionUtils.remove(jobNo);
							}
						}
					}
				}
			}
		}
		lastExecuteTime.set(System.currentTimeMillis());
	}

}
