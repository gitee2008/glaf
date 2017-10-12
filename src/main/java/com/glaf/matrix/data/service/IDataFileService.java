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

package com.glaf.matrix.data.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.base.DataFile;
import com.glaf.matrix.data.query.DataFileQuery;

/**
 * 
 * 字节流服务 本服务类提供字节流的创建、修改、删除、复制及查询服务
 */
@Transactional(readOnly = true)
public interface IDataFileService {

	int getDataFileCountByQueryCriteria(DataFileQuery query);

	/**
	 * 复制字节流
	 * 
	 * @param sourceId
	 * @param destId
	 */
	@Transactional
	void copyDataFile(String tenantId, String sourceId, String destId);

	/**
	 * 根据主键删除数据
	 * 
	 * @param id
	 */
	@Transactional
	void deleteById(String tenantId, String id);

	/**
	 * 根据资源编号删除数据
	 * 
	 * @param businessKey
	 */
	@Transactional
	void deleteDataFileByBusinessKey(String tenantId, String businessKey);

	/**
	 * 根据文件编号删除数据
	 * 
	 * @param fileId
	 */
	@Transactional
	void deleteDataFileByFileId(String tenantId, String fileId);

	/**
	 * 通过文件编号获取字节流
	 * 
	 * @param fileId
	 * @return
	 */
	byte[] getBytesByFileId(String tenantId, String fileId);

	/**
	 * 根据文件编号获取数据(不包含字节流)
	 * 
	 * @param fileId
	 * @return
	 */
	DataFile getDataFileByFileId(String tenantId, String fileId);

	/**
	 * 根据文件名称获取数据(不包含字节流)
	 * 
	 * @param filename
	 * @return
	 */
	DataFile getDataFileByFilename(String tenantId, String filename);

	/**
	 * 根据主键获取数据(不包含字节流)
	 * 
	 * @param id
	 * @return
	 */
	DataFile getDataFileById(String tenantId, String id);

	/**
	 * 根据参数获取数据(不包含字节流)
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DataFile> getDataFileList(DataFileQuery query);

	/**
	 * 根据参数获取数据(不包含字节流)
	 * 
	 * @param paramMap
	 * @return
	 */
	List<DataFile> getDataFileList(int start, int limit, DataFileQuery query);

	/**
	 * 根据资源编号获取数据(不包含字节流)
	 * 
	 * @param serviceKey
	 * @param businessKey
	 * @return
	 */
	List<DataFile> getDataFileList(String tenantId, String serviceKey, String businessKey);

	/**
	 * 根据文件编号获取数据(包含字节流)
	 * 
	 * @param fileId
	 * @return
	 */
	DataFile getDataFileWithBytesByFileId(String tenantId, String fileId);

	/**
	 * 根据文件编号获取字节流
	 * 
	 * @param fileId
	 * @return
	 */
	InputStream getInputStreamByFileId(String tenantId, String fileId);

	/**
	 * 根据主键获取字节流
	 * 
	 * @param id
	 * @return
	 */
	InputStream getInputStreamById(String tenantId, String id);

	/**
	 * 根据参数获取最大数据(不包含字节流)
	 * 
	 * @param paramMap
	 * @return
	 */
	DataFile getMaxDataFile(DataFileQuery query);

	/**
	 * 根据资源编号获取最大数据(不包含字节流)
	 * 
	 * @param businessKey
	 * @return
	 */
	DataFile getMaxDataFile(String tenantId, String businessKey);

	/**
	 * 根据资源编号获取最大数据
	 * 
	 * @param businessKey
	 * @return
	 */
	DataFile getMaxDataFileWithBytes(String tenantId, String businessKey);

	/**
	 * 新增记录
	 * 
	 * @param dataFile
	 */
	@Transactional
	String insertDataFile(String tenantId, DataFile dataFile);

	/**
	 * 将记录标记为正式
	 * 
	 * @param createBy
	 *            创建者
	 * @param serviceKey
	 *            服务标识
	 * @param businessKey
	 *            业务标识
	 */
	@Transactional
	void makeMark(String tenantId, String createBy, String serviceKey, String businessKey);

	/**
	 * 修改附件的业务状态
	 * 
	 * @param createBy
	 *            创建者
	 * @param serviceKey
	 *            服务标识
	 * @param businessKey
	 *            业务标识
	 * @param status
	 *            业务状态
	 */
	@Transactional
	void makeMark(String tenantId, String createBy, String serviceKey, String businessKey, int status);

	/**
	 * 批量保存记录
	 * 
	 * @param dataList
	 */
	@Transactional
	void saveAll(String tenantId, List<DataFile> dataList);

	/**
	 * 批量保存记录
	 * 
	 * @param dataMap
	 */
	@Transactional
	void saveAll(String tenantId, Map<String, DataFile> dataMap);

	/**
	 * 修改记录（不包含字节流）
	 * 
	 * @param model
	 */
	@Transactional
	void updateDataFile(String tenantId, DataFile model);

	/**
	 * 修改记录中的文件内容（仅修改字节流）
	 * 
	 * @param model
	 */
	@Transactional
	void updateDataFileInfo(String tenantId, DataFile model);
}