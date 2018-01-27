/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.report.jxls;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

 
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;

import net.sf.jxls.report.ReportManager;

import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ClassUtils;

import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.ReflectUtils;
import com.glaf.report.bean.ReportBean;
import com.glaf.report.bean.ReportContainer;
import com.glaf.report.data.ReportDataSet;
import com.glaf.report.data.ReportDefinition;
import com.glaf.report.data.ReportRowSet;

public class Jxls2ReportContainer {
	private static class Jxls2ReportContainerHolder {
		public static Jxls2ReportContainer instance = new Jxls2ReportContainer();
	}

	protected final static Log logger = LogFactory.getLog(Jxls2ReportContainer.class);

	public final static Jxls2ReportContainer getContainer() {
		return Jxls2ReportContainerHolder.instance;
	}

	public static Jxls2ReportContainer getInstance() {
		return Jxls2ReportContainerHolder.instance;
	}

	private Jxls2ReportContainer() {

	}

	public byte[] execute(String reportId, String actorId, Map<String, Object> context) {
		if (context == null) {
			context = new java.util.HashMap<String, Object>();
		}

		context.put("actorId", actorId);
		LoginContext loginContext = null;
		try {
			loginContext = IdentityFactory.getLoginContext(actorId);
		} catch (Exception ex) {
			logger.error(ex);
		}
		if (loginContext != null) {
			context.put("loginContext", loginContext);
			context.put("currUser", loginContext.getUser());
			context.put("roles", loginContext.getRoles());
			context.put("perms", loginContext.getPermissions());
		}
		context.put("now", new java.util.Date());
		context.put("currDate", new java.util.Date());
		context.put("calendar", java.util.Calendar.getInstance());
		context.put("time", java.util.Calendar.getInstance().getTime());

		ReportDefinition reportDefinition = ReportContainer.getContainer().getReportDefinition(reportId);
		if (reportDefinition != null && reportDefinition.getData() != null) {
			ReportBean reportBean = new ReportBean();
			logger.debug("reportId:" + reportId);
			Map<String, Object> dataMap = reportBean.populate(reportId, actorId, context);
			if (dataMap != null) {
				context.putAll(dataMap);
				logger.debug("---------------dataMap------------");
				logger.debug("dataMap:" + dataMap);
			}
			byte[] data = null;
			Connection connection = null;
			InputStream is = null;
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			try {
				bais = new ByteArrayInputStream(reportDefinition.getData());
				is = new BufferedInputStream(bais);
				baos = new ByteArrayOutputStream();
				bos = new BufferedOutputStream(baos);
				connection = DBConnectionFactory.getConnection();
				List<ReportDataSet> dataSetList = reportDefinition.getDataSetList();
				if (dataSetList != null && !dataSetList.isEmpty()) {
					for (ReportDataSet rds : dataSetList) {
						List<ReportRowSet> rowSetList = rds.getRowSetList();
						if (rowSetList != null && !rowSetList.isEmpty()) {
							for (ReportRowSet rs : rowSetList) {
								String rptMgr = rs.getRptMgr();
								String rptMgrMapping = rs.getRptMgrMapping();
								ReportManager rm = null;
								if ("sql".equals(rptMgr)) {
									rm = new ReportManagerImpl(connection, context);
								} else {
									String rptMgrClassName = rs.getRptMgrClassName();
									if (StringUtils.isNotEmpty(rptMgrClassName)) {
										rm = (ReportManager) ClassUtils.instantiateObject(rptMgrClassName);
										try {
											ReflectUtils.setFieldValue(rm, "connection", connection);
										} catch (Exception ex) {
										}
										try {
											ReflectUtils.setFieldValue(rm, "properties", rs.getProperties());
										} catch (Exception ex) {
										}
									}
								}
								if (rm != null) {
									context.put(rptMgrMapping, rm);
								}
							}
						}
					}
				}

				ReportManager mybatis = new MyBatisReportManagerImpl(connection, context);
				context.put("mybatis", mybatis);

				ReportManager mybatisx = new MyBatisJsonReportManagerImpl(connection, context);
				context.put("mybatisx", mybatisx);

				logger.debug("context:" + context);
				Context context2 = PoiTransformer.createInitialContext();

				Set<Entry<String, Object>> entrySet = context.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					String key = entry.getKey();
					Object value = entry.getValue();
					context2.putVar(key, value);
					// logger.debug(key);
				}

				org.jxls.util.JxlsHelper.getInstance().processTemplate(is, bos, context2);
				bos.flush();
				baos.flush();

				data = baos.toByteArray();
				return data;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			} finally {
				JdbcUtils.close(connection);
				com.glaf.core.util.IOUtils.closeStream(is);
				com.glaf.core.util.IOUtils.closeStream(bais);
				com.glaf.core.util.IOUtils.closeStream(baos);
				com.glaf.core.util.IOUtils.closeStream(bos);
			}
		}

		return null;
	}

}