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

package com.glaf.flowable.tasklistener;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.flowable.engine.common.api.delegate.Expression;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.util.JsonUtils;

public class ResetVariableTaskListener implements TaskListener {
	private static final long serialVersionUID = 1L;

	protected final static Log logger = LogFactory.getLog(ResetVariableTaskListener.class);

	protected Expression json;

	protected Expression jsonExpression;

	@Override
	public void notify(DelegateTask delegateTask) {
		if (json != null) {
			String str = json.getExpressionText();
			if (str.startsWith("{") && str.endsWith("}")) {
				Map<String, Object> jsonMap = JsonUtils.decode(str);
				if (jsonMap != null && !jsonMap.isEmpty()) {
					Set<Entry<String, Object>> entrySet = jsonMap.entrySet();
					for (Entry<String, Object> entry : entrySet) {
						String name = entry.getKey();
						Object value = entry.getValue();
						if (value != null) {
							logger.debug("set variable " + name + " = " + value);

						}
					}
				}
			}
		}

		if (jsonExpression != null) {

		}
	}

	public void setJson(Expression json) {
		this.json = json;
	}

	public void setJsonExpression(Expression jsonExpression) {
		this.jsonExpression = jsonExpression;
	}

}