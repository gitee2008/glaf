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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

public class ProcessStarterListener implements TaskListener {
	private static final long serialVersionUID = 1L;

	protected final static Log logger = LogFactory.getLog(ProcessStarterListener.class);

	protected Expression sendMail;

	protected Expression subject;

	protected Expression content;

	protected Expression taskName;

	protected Expression taskContent;

	protected Expression templateId;

	public void notify(DelegateTask delegateTask) {
		logger.debug("----------------------------------------------------");
		logger.debug("------------------ProcessStarterListener------------");
		logger.debug("----------------------------------------------------");

	}

	public void setContent(Expression content) {
		this.content = content;
	}

	public void setSendMail(Expression sendMail) {
		this.sendMail = sendMail;
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

}