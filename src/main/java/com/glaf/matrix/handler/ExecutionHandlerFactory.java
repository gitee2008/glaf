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

package com.glaf.matrix.handler;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.ReflectUtils;
import com.glaf.matrix.config.HandlerProperties;
import com.glaf.matrix.data.domain.SyntheticFlow;
import com.glaf.matrix.data.query.SyntheticFlowQuery;
import com.glaf.matrix.data.service.SyntheticFlowService;

public class ExecutionHandlerFactory {

	protected static ConcurrentMap<String, DataExecutionHandler> handlerMap = new ConcurrentHashMap<String, DataExecutionHandler>();

	protected static final Log logger = LogFactory.getLog(ExecutionHandlerFactory.class);

	protected static volatile SyntheticFlowService syntheticFlowService;

	static {
		 

		try {
			Properties props = HandlerProperties.getProperties();
			if (props != null) {
				Enumeration<?> e = props.keys();
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					String value = props.getProperty(key);
					DataExecutionHandler handler = (DataExecutionHandler) ReflectUtils.instantiate(value);
					handlerMap.put(key, handler);
				}
			}
		} catch (java.lang.Throwable ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 后置处理
	 * 
	 * @param id
	 * @param context
	 */
	public static void doAfter(Object id, Map<String, Object> context) {
		if (id != null) {
			String currentStep = String.valueOf(id);
			SyntheticFlowQuery query = new SyntheticFlowQuery();
			query.currentStep(currentStep);
			List<SyntheticFlow> flows = getSyntheticFlowService().list(query);
			if (flows != null && !flows.isEmpty()) {
				for (SyntheticFlow flow : flows) {
					/**
					 * 跳过完全相同的
					 */
					if (StringUtils.equals(currentStep, flow.getNextType() + "_" + flow.getNextStep())) {
						continue;
					}
					if (StringUtils.equals(flow.getCurrentType(), flow.getNextType())) {
						DataExecutionHandler handler = handlerMap.get(flow.getNextType());
						if (handler != null) {
							logger.debug("##############################doAfter#############################");
							logger.debug("handler:" + handler.getClass().getName());
							context.put("_id_", id);
							context.put("_flow_", flow);

							int retry = 0;
							int errorCount = 0;
							boolean success = false;
							Exception e = null;
							while (retry < 3 && !success) {
								try {
									retry++;
									handler.execute(flow.getNextStep(), context);
									success = true;
								} catch (Exception ex) {
									e = ex;
									errorCount++;
								}
							}
							if (!success && errorCount > 0) {
								throw new RuntimeException(e);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 前置处理
	 * 
	 * @param id
	 * @param context
	 */
	public static void doBefore(Object id, Map<String, Object> context) {
		if (id != null) {
			String currentStep = String.valueOf(id);
			SyntheticFlowQuery query = new SyntheticFlowQuery();
			query.currentStep(currentStep);
			List<SyntheticFlow> flows = getSyntheticFlowService().list(query);
			if (flows != null && !flows.isEmpty()) {
				for (SyntheticFlow flow : flows) {
					/**
					 * 跳过完全相同的
					 */
					if (StringUtils.equals(currentStep, flow.getPreviousType() + "_" + flow.getPreviousStep())) {
						continue;
					}
					if (StringUtils.equals(flow.getCurrentType(), flow.getPreviousType())) {
						DataExecutionHandler handler = handlerMap.get(flow.getPreviousType());
						if (handler != null) {
							logger.debug("###############################doBefore############################");
							logger.debug("handler:" + handler.getClass().getName());
							context.put("_id_", id);
							context.put("_flow_", flow);

							int retry = 0;
							int errorCount = 0;
							boolean success = false;
							Exception e = null;
							while (retry < 3 && !success) {
								try {
									retry++;
									handler.execute(flow.getPreviousStep(), context);
									success = true;
								} catch (Exception ex) {
									e = ex;
									errorCount++;
								}
							}
							if (!success && errorCount > 0) {
								throw new RuntimeException(e);
							}
						}
					}
				}
			}
		}
	}

	public static SyntheticFlowService getSyntheticFlowService() {
		if (syntheticFlowService == null) {
			syntheticFlowService = ContextFactory.getBean("syntheticFlowService");
		}
		return syntheticFlowService;
	}

	private ExecutionHandlerFactory() {

	}

}
