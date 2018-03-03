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

package com.glaf.heathcare.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.security.LoginContext;
import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;

@Transactional(readOnly = true)
public interface DietaryTemplateService {

	@Transactional
	void bulkInsert(List<DietaryTemplate> list);

	/**
	 * 计算各项成分
	 * 
	 * @param templateId
	 */
	@Transactional
	void calculate(long templateId);

	/**
	 * 复制食谱模板
	 * 
	 * @param loginContext
	 * @param suitNo
	 * @param sysFlag
	 * @param targetSuitNo
	 */
	@Transactional
	void copyTemplates(LoginContext loginContext, int suitNo, String sysFlag, int targetSuitNo);

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(long templateId);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(List<Long> templateIds);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	DietaryTemplate getDietaryTemplate(long templateId);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getDietaryTemplateCountByQueryCriteria(DietaryTemplateQuery query);

	List<DietaryTemplate> getDietaryTemplates(int dayOfWeek, int suitNo, String sysFlag);

	List<DietaryTemplate> getDietaryTemplates(int suitNo, String sysFlag);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<DietaryTemplate> getDietaryTemplatesByQueryCriteria(int start, int pageSize, DietaryTemplateQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<DietaryTemplate> list(DietaryTemplateQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(DietaryTemplate dietaryTemplate);

	@Transactional
	void saveAs(DietaryTemplate dietaryTemplate);

	@Transactional
	void updateAll(List<DietaryTemplate> list);

}
