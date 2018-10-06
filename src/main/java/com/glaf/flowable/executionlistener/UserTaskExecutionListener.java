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

package com.glaf.flowable.executionlistener;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.impl.db.DbSqlSession;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.context.Context;
import org.flowable.idm.api.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import com.glaf.core.util.StringTools;

public class UserTaskExecutionListener implements ExecutionListener {

	private static final long serialVersionUID = 1L;

	protected final static Log logger = LogFactory.getLog(UserTaskExecutionListener.class);

	protected Expression statementId;

	protected Expression roleId;

	protected Expression expression;

	protected Expression outputVar;

	@Override
	public void notify(DelegateExecution execution) {
		logger.debug("----------------------------------------------------");
		logger.debug("-----------------UserTaskExecutionListener----------");
		logger.debug("----------------------------------------------------");

		CommandContext commandContext = Context.getCommandContext();

		if (execution != null && outputVar != null) {
			Map<String, Object> paramMap = new java.util.HashMap<String, Object>();
			paramMap.putAll(execution.getVariables());

			String statement = "getMyRoleUsers";

			String output = (String) outputVar.getValue(execution);

			if (statementId != null && statementId.getExpressionText() != null) {
				statement = statementId.getExpressionText();
				logger.debug("statementId:" + statement);
			}

			if (roleId != null && roleId.getExpressionText() != null) {
				logger.debug("roleId:" + roleId.getExpressionText());
				paramMap.put("roleId", roleId.getExpressionText());
			}

			if (StringUtils.isNotEmpty(statement)) {
				DbSqlSession dbSqlSession = commandContext.getSession(DbSqlSession.class);
				SqlSession sqlSession = dbSqlSession.getSqlSession();
				List<?> list = sqlSession.selectList(statement, paramMap);
				if (list != null && !list.isEmpty()) {
					Collection<String> users = new java.util.HashSet<String>();

					for (Object object : list) {
						if (object instanceof User) {
							String actorId = ((User) object).getId();
							if (!users.contains(actorId)) {
								users.add(actorId);
							}
						} else if (object instanceof com.glaf.core.identity.User) {
							String actorId = ((com.glaf.core.identity.User) object).getActorId();
							if (!users.contains(actorId)) {
								users.add(actorId);
							}
						}
					}

					logger.debug("users:" + users);

					if (users.size() > 0) {
						execution.setVariable(output, users);
					}

				} else {
					String expr = (String) expression.getValue(execution);
					if (expr != null) {
						if (expr.startsWith("user(") && expr.endsWith("")) {
							expr = StringTools.replaceIgnoreCase(expr, "user(", "");
							expr = StringTools.replaceIgnoreCase(expr, ")", "");
							execution.setVariable(output, expr);
						} else if (expr.startsWith("users(") && expr.endsWith("")) {
							expr = StringTools.replaceIgnoreCase(expr, "users(", "");
							expr = StringTools.replaceIgnoreCase(expr, ")", "");
							List<String> candidateUsers = StringTools.split(expr, ",");
							execution.setVariable(output, candidateUsers);
						}
					}
				}
			}
		}
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public void setOutputVar(Expression outputVar) {
		this.outputVar = outputVar;
	}

	public void setRoleId(Expression roleId) {
		this.roleId = roleId;
	}

	public void setStatementId(Expression statementId) {
		this.statementId = statementId;
	}

}