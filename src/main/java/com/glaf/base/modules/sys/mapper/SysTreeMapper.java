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

package com.glaf.base.modules.sys.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.query.SysTreeQuery;

@Component
public interface SysTreeMapper {

	void bulkInsertSysTree(List<SysTree> list);

	void bulkInsertSysTree_oracle(List<SysTree> list);

	void deleteSysTreeById(long id);

	void deleteSysTrees(SysTreeQuery query);

	List<SysTree> getDictorySysTrees(SysTreeQuery query);

	List<SysTree> getRelationSysTrees(SysTreeQuery query);

	SysTree getSysTreeById(long id);

	int getSysTreeCount(SysTreeQuery query);

	List<SysTree> getSysTrees(SysTreeQuery query);

	void insertSysTree(SysTree model);

	void updateSysTree(SysTree model);

}
