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
package com.glaf.heathcare.tenant;

import java.util.Map;

import com.glaf.base.modules.sys.model.SysTenant;

public interface ITenantReportPreprocessor {

	/**
	 * 报表预处理
	 * 
	 * @param tenant
	 *            租户信息
	 * @param params
	 *            参数
	 */
	void prepare(SysTenant tenant, Map<String, Object> params);

}
