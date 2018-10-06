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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.impl.db.DbSqlSession;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.context.Context;
import org.flowable.idm.api.User;
import org.flowable.task.service.delegate.DelegateTask;

import com.glaf.core.util.StringTools;

public class RoleUsersMultiInstanceTaskListener implements TaskListener {
	private static final long serialVersionUID = 1L;

	protected final static Log logger = LogFactory.getLog(RoleUsersMultiInstanceTaskListener.class);

	protected Expression statementId;

	protected Expression roleId;

	protected Expression deptId;

	protected Expression outputVar;

	protected Expression userIds;

	protected Expression message;

	protected Expression sendMail;

	protected Expression subject;

	protected Expression content;

	protected Expression taskName;

	protected Expression taskContent;

	protected Expression templateId;

	public void notify(DelegateTask delegateTask) {
		logger.debug("----------------------------------------------------");
		logger.debug("--------------RoleUsersMultiInstanceTaskListener----");
		logger.debug("----------------------------------------------------");

		logger.debug("task name:" + delegateTask.getName());

		Map<String, Object> paramMap = new java.util.HashMap<String, Object>();

		String statement = null;
		if (statementId != null) {
			statement = statementId.getExpressionText();
			logger.debug("statementId:" + statement);
		}
		if (roleId != null) {
			logger.debug("roleId:" + roleId.getExpressionText());
			paramMap.put("roleId", roleId.getExpressionText());
		}
		if (deptId != null) {

		}
		if (message != null) {

		}
		if (StringUtils.isEmpty(statement)) {
			statement = "getMyRoleUsers";
		}

		Collection<String> assigneeList = new java.util.HashSet<String>();

		if (!paramMap.isEmpty()) {
			paramMap.putAll(delegateTask.getVariables());
			// logger.debug("sessions:"+commandContext.getSessions());
			CommandContext commandContext = Context.getCommandContext();
			DbSqlSession dbSqlSession = commandContext.getSession(DbSqlSession.class);
			SqlSession sqlSession = dbSqlSession.getSqlSession();
			List<?> list = sqlSession.selectList(statement, paramMap);
			if (list != null && !list.isEmpty()) {
				for (Object object : list) {
					if (object instanceof User) {
						String actorId = ((User) object).getId();
						if (!assigneeList.contains(actorId)) {
							assigneeList.add(actorId);
						}
					} else if (object instanceof com.glaf.core.identity.User) {
						String actorId = ((com.glaf.core.identity.User) object).getActorId();
						if (!assigneeList.contains(actorId)) {
							assigneeList.add(actorId);
						}
					}
				}
				logger.debug("assigneeList:" + assigneeList);

				if (assigneeList.size() > 0) {
					if (outputVar != null) {
						String output = (String) outputVar.getValue(delegateTask);
						delegateTask.setVariable(output, assigneeList);
					} else {
						delegateTask.setVariable("assigneeList", assigneeList);
					}
				}
			}
		}

		if (assigneeList.isEmpty()) {
			if (userIds != null) {
				String tmpUsers = userIds.getExpressionText();
				assigneeList = StringTools.split(tmpUsers);
				logger.debug("assigneeList:" + assigneeList);
				if (assigneeList.size() > 0) {
					if (outputVar != null) {
						String output = (String) outputVar.getValue(delegateTask);
						delegateTask.setVariable(output, assigneeList);
					} else {
						delegateTask.setVariable("assigneeList", assigneeList);
					}
				}
			}
		}

	}

	public void setContent(Expression content) {
		this.content = content;
	}

	public void setDeptId(Expression deptId) {
		this.deptId = deptId;
	}

	public void setMessage(Expression message) {
		this.message = message;
	}

	public void setOutputVar(Expression outputVar) {
		this.outputVar = outputVar;
	}

	public void setRoleId(Expression roleId) {
		this.roleId = roleId;
	}

	public void setSendMail(Expression sendMail) {
		this.sendMail = sendMail;
	}

	public void setStatementId(Expression statementId) {
		this.statementId = statementId;
	}

	public void setSubject(Expression subject) {
		this.subject = subject;
	}

	public void setTaskContent(Expression taskContent) {
		this.taskContent = taskContent;
	}

	public void setTaskName(Expression taskName) {
		this.taskName = taskName;
	}

	public void setTemplateId(Expression templateId) {
		this.templateId = templateId;
	}

	public void setUserIds(Expression userIds) {
		this.userIds = userIds;
	}

}