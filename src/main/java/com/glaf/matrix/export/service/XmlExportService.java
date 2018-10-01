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

package com.glaf.matrix.export.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.query.XmlExportQuery;

@Transactional(readOnly = true)
public interface XmlExportService {

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(String id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(List<String> ids);

	/**
	 * 导出某个节点及子孙节点的定义
	 * 
	 * @param expId
	 * @return
	 */
	JSONObject exportJson(String expId);

	/**
	 * 获取全部子孙节点
	 * 
	 * @param nodeParentId
	 * @return
	 */
	List<XmlExport> getAllChildren(long nodeParentId);

	/**
	 * 获取直接子节点及导出项
	 * 
	 * @param nodeParentId
	 * @return
	 */
	List<XmlExport> getChildrenWithItems(long nodeParentId);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	XmlExport getXmlExport(String id);

	XmlExport getXmlExportByNodeId(long nodeId);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getXmlExportCountByQueryCriteria(XmlExportQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<XmlExport> getXmlExportsByQueryCriteria(int start, int pageSize, XmlExportQuery query);

	/**
	 * 批量保存
	 * 
	 * @param expId
	 * @param jsonObject
	 */
	@Transactional
	void importAll(String expId, JSONObject jsonObject);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<XmlExport> list(XmlExportQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(XmlExport xmlExport);

	/**
	 * 另存记录
	 * 
	 * @param expId
	 * @param createBy
	 */
	@Transactional
	String saveAs(String expId, String createBy);

}
