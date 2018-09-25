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

package com.glaf.matrix.data.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.base.DataFile;
import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.UUID32;

import com.glaf.matrix.data.mapper.DataFileMapper;
import com.glaf.matrix.data.query.DataFileQuery;
import com.glaf.matrix.data.service.IDataFileService;

@Service("dataFileService")
@Transactional(readOnly = true)
public class DataFileServiceImpl implements IDataFileService {

	protected final static Log logger = LogFactory.getLog(DataFileServiceImpl.class);

	protected static Configuration conf = BaseConfiguration.create();

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected DataFileMapper dataFileMapper;

	protected SqlSession sqlSession;

	public DataFileServiceImpl() {

	}

	@Transactional
	public void copyDataFile(String tenantId, String sourceId, String destId) {
		DataFile source = this.getDataFileWithBytesById(tenantId, sourceId);
		DataFile dest = this.getDataFileById(tenantId, destId);
		dest.setLastModified(System.currentTimeMillis());
		dest.setFilename(source.getFilename());
		dest.setData(source.getData());
		dest.setSize(source.getSize());
		dest.setContentType(source.getContentType());
		this.updateDataFileInfo(tenantId, dest);
	}

	public int count(DataFileQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return dataFileMapper.getDataFileCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, String id) {
		DataFileQuery query = new DataFileQuery();
		query.id(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		DataFile dataFile = dataFileMapper.getDataFileById(query);
		if (dataFile != null) {
			dataFileMapper.deleteDataFileById(query);
		}
	}

	@Transactional
	public void deleteDataFileByBusinessKey(String tenantId, String businessKey) {
		DataFileQuery query = new DataFileQuery();
		query.businessKey(businessKey);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		dataFileMapper.deleteDataFilesByBusinessKey(query);
	}

	@Transactional
	public void deleteDataFileByFileId(String tenantId, String fileId) {
		DataFile dataFile = this.getDataFileByFileId(tenantId, fileId);
		if (dataFile != null) {
			DataFileQuery query = new DataFileQuery();
			query.fileId(fileId);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			dataFileMapper.deleteDataFilesByFileId(query);
		}
	}

	public byte[] getBytesByFileId(String tenantId, String fileId) {
		DataFile dataFile = this.getDataFileByFileId(tenantId, fileId);

		DataFileQuery query = new DataFileQuery();
		query.fileId(fileId);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}

		if (StringUtils.equals(DBUtils.POSTGRESQL, DBConnectionFactory.getDatabaseType())) {
			dataFile = (DataFile) entityDAO.getSingleObject("getDataFileFileInfoByFileId_postgres", query);
		} else {
			dataFile = (DataFile) entityDAO.getSingleObject("getDataFileFileInfoByFileId", query);
		}
		if (dataFile != null && dataFile.getData() != null) {
			return dataFile.getData();
		}
		return null;
	}

	public byte[] getBytesById(String tenantId, String id) {
		DataFile dataFile = this.getDataFileById(tenantId, id);

		DataFileQuery query = new DataFileQuery();
		query.id(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		} else {
			query.setTableSuffix("");
		}

		if (StringUtils.equals(DBUtils.POSTGRESQL, DBConnectionFactory.getDatabaseType())) {
			dataFile = (DataFile) entityDAO.getSingleObject("getDataFileFileInfoById_postgres", query);
		} else {
			dataFile = (DataFile) entityDAO.getSingleObject("getDataFileFileInfoById", query);
		}
		if (dataFile != null && dataFile.getData() != null) {
			return dataFile.getData();
		}
		return null;
	}

	public DataFile getDataFileByFileId(String tenantId, String fileId) {
		DataFile dataFile = null;
		DataFileQuery query = new DataFileQuery();
		query.fileId(fileId);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		List<DataFile> list = dataFileMapper.getDataFilesByFileId(query);
		if (list != null && !list.isEmpty()) {
			dataFile = list.get(0);
		}
		return dataFile;
	}

	/**
	 * 根据文件名称获取数据(不包含字节流)
	 * 
	 * @param filename
	 * @return
	 */
	public DataFile getDataFileByFilename(String tenantId, String filename) {
		DataFile dataFile = null;
		DataFileQuery query = new DataFileQuery();
		query.filename(filename);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		List<DataFile> list = dataFileMapper.getDataFilesByFilename(query);
		if (list != null && !list.isEmpty()) {
			dataFile = list.get(0);
		}
		return dataFile;
	}

	public DataFile getDataFileById(String tenantId, String id) {
		DataFileQuery query = new DataFileQuery();
		query.id(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		DataFile dataFile = dataFileMapper.getDataFileById(query);
		return dataFile;
	}

	public int getDataFileCountByQueryCriteria(DataFileQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return dataFileMapper.getDataFileCount(query);
	}

	public List<DataFile> getDataFileList(DataFileQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<DataFile> list = list(query);
		List<DataFile> rows = new ArrayList<DataFile>();
		for (DataFile dataFile : list) {
			rows.add(dataFile);
		}
		return rows;
	}

	/**
	 * 根据参数获取数据(不包含字节流)
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<DataFile> getDataFileList(int start, int limit, DataFileQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		RowBounds rowBounds = new RowBounds(start, limit);
		List<DataFile> list = sqlSession.selectList("getDataFiles", query, rowBounds);
		return list;
	}

	public List<DataFile> getDataFileList(String tenantId, String serviceKey, String businessKey) {
		DataFileQuery query = new DataFileQuery();
		query.serviceKey(serviceKey);
		query.businessKey(businessKey);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		List<DataFile> list = list(query);
		List<DataFile> rows = new ArrayList<DataFile>();
		for (DataFile dataFile : list) {
			rows.add(dataFile);
		}
		return rows;
	}

	public DataFile getDataFileWithBytesByFileId(String tenantId, String fileId) {
		DataFile dataFile = this.getDataFileByFileId(tenantId, fileId);
		if (dataFile != null) {
			byte[] data = this.getBytesById(tenantId, dataFile.getId());
			dataFile.setData(data);
		}
		return dataFile;
	}

	public DataFile getDataFileWithBytesById(String tenantId, String id) {
		DataFileQuery query = new DataFileQuery();
		query.id(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		DataFile dataFile = dataFileMapper.getDataFileById(query);
		byte[] data = this.getBytesById(tenantId, id);
		dataFile.setData(data);
		return dataFile;
	}

	public InputStream getInputStreamByFileId(String tenantId, String fileId) {
		byte[] bytes = this.getBytesByFileId(tenantId, fileId);
		if (bytes != null) {
			return new BufferedInputStream(new ByteArrayInputStream(bytes));
		}
		return null;
	}

	public InputStream getInputStreamById(String tenantId, String id) {
		byte[] bytes = this.getBytesById(tenantId, id);
		if (bytes != null) {
			return new BufferedInputStream(new ByteArrayInputStream(bytes));
		}
		return null;
	}

	public DataFile getMaxDataFile(DataFileQuery query) {
		DataFile dataFile = null;
		List<DataFile> list = list(query);
		if (list != null && !list.isEmpty()) {
			dataFile = list.get(0);
			Iterator<DataFile> iterator = list.iterator();
			while (iterator.hasNext()) {
				DataFile model = iterator.next();
				if (model.getLastModified() > dataFile.getLastModified()) {
					dataFile = model;
				}
			}
		}
		return dataFile;
	}

	public DataFile getMaxDataFile(String tenantId, String businessKey) {
		DataFile dataFile = null;
		DataFileQuery query = new DataFileQuery();
		query.businessKey(businessKey);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		List<DataFile> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			dataFile = list.get(0);
			Iterator<DataFile> iterator = list.iterator();
			while (iterator.hasNext()) {
				DataFile model = iterator.next();
				if (model.getLastModified() > dataFile.getLastModified()) {
					dataFile = model;
				}
			}
		}
		return dataFile;
	}

	public DataFile getMaxDataFileWithBytes(String tenantId, String businessKey) {
		DataFile dataFile = null;
		DataFileQuery query = new DataFileQuery();
		query.businessKey(businessKey);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		List<DataFile> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			dataFile = list.get(0);
			Iterator<DataFile> iterator = list.iterator();
			while (iterator.hasNext()) {
				DataFile model = iterator.next();
				if (model.getLastModified() > dataFile.getLastModified()) {
					dataFile = model;
				}
			}
		}
		if (dataFile != null) {
			byte[] bytes = this.getBytesById(tenantId, dataFile.getId());
			dataFile.setData(bytes);
		}
		return dataFile;
	}

	@Transactional
	public String insertDataFile(String tenantId, DataFile dataFile) {
		if (StringUtils.isEmpty(dataFile.getId())) {
			dataFile.setId(UUID32.getUUID());
		}

		if (StringUtils.isNotEmpty(tenantId)) {
			dataFile.setTenantId(tenantId);
			dataFile.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}

		dataFile.setCreateDate(new Date());

		String filePath = dataFile.getPath();
		if (filePath != null && filePath.length() > 0) {
			if (filePath.startsWith("//")) {
				filePath = filePath.substring(1, filePath.length());
				dataFile.setPath(filePath);
			}
		}

		if (dataFile.getLastModified() == 0) {
			dataFile.setLastModified(System.currentTimeMillis());
		}

		if (StringUtils.equals(DBUtils.POSTGRESQL, DBConnectionFactory.getDatabaseType())) {
			dataFileMapper.insertDataFile_postgres(dataFile);
		} else {
			dataFileMapper.insertDataFile(dataFile);
		}

		return dataFile.getId();
	}

	public List<DataFile> list(DataFileQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		logger.debug("tenantId:" + query.getTenantId());
		logger.debug("tableSuffix:" + query.getTableSuffix());
		List<DataFile> list = dataFileMapper.getDataFiles(query);
		return list;
	}

	@Transactional
	public void makeMark(String tenantId, String createBy, String serviceKey, String businessKey) {
		DataFileQuery query = new DataFileQuery();
		query.serviceKey(serviceKey);
		query.createBy(createBy);
		query.status(0);

		if (StringUtils.isNotEmpty(tenantId)) {
			query.setTenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}

		List<DataFile> list = this.list(query);
		if (list != null && list.size() > 0) {
			Iterator<DataFile> iterator = list.iterator();
			while (iterator.hasNext()) {
				DataFile model = (DataFile) iterator.next();
				if (StringUtils.isNotEmpty(businessKey)) {
					model.setBusinessKey(businessKey);
					model.setStatus(1);

					String filePath = model.getPath();
					if (filePath != null && filePath.length() > 0) {
						if (filePath.startsWith("//")) {
							filePath = filePath.substring(1, filePath.length());
							model.setPath(filePath);
						}
					}

					this.updateDataFile(tenantId, model);
				}
			}
		}
	}

	@Transactional
	public void makeMark(String tenantId, String createBy, String serviceKey, String businessKey, int status) {
		DataFileQuery query = new DataFileQuery();
		query.serviceKey(serviceKey);
		query.createBy(createBy);
		query.status(0);

		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}

		List<DataFile> list = this.list(query);
		if (list != null && list.size() > 0) {
			Iterator<DataFile> iterator = list.iterator();
			while (iterator.hasNext()) {
				DataFile model = (DataFile) iterator.next();
				if (StringUtils.isNotEmpty(businessKey)) {
					model.setBusinessKey(businessKey);
					model.setStatus(status);

					String filePath = model.getPath();
					if (filePath != null && filePath.length() > 0) {
						if (filePath.startsWith("//")) {
							filePath = filePath.substring(1, filePath.length());
							model.setPath(filePath);
						}
					}
					this.updateDataFile(tenantId, model);
				}
			}
		}
	}

	@Transactional
	public void saveAll(String tenantId, List<DataFile> dataList) {
		Iterator<DataFile> iterator = dataList.iterator();
		while (iterator.hasNext()) {
			DataFile dataFile = iterator.next();
			if (StringUtils.isNotEmpty(tenantId)) {
				dataFile.setTenantId(tenantId);
				dataFile.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			if (dataFile.getData() != null) {
				if (dataFile.getSize() <= 0) {
					dataFile.setSize(dataFile.getData().length);
				}

				String filePath = dataFile.getPath();
				if (filePath != null && filePath.length() > 0) {
					if (filePath.startsWith("//")) {
						filePath = filePath.substring(1, filePath.length());
						dataFile.setPath(filePath);
					}
				}

				this.insertDataFile(tenantId, dataFile);
			}
		}
	}

	@Transactional
	public void saveAll(String tenantId, Map<String, DataFile> dataMap) {
		DataFileQuery query = new DataFileQuery();
		List<String> names = new ArrayList<String>();
		for (String str : dataMap.keySet()) {
			names.add(str);
		}
		query.names(names);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		List<DataFile> dataList = this.list(query);
		Map<String, Object> exists = new HashMap<String, Object>();
		if (dataList != null && dataList.size() > 0) {
			Iterator<DataFile> iterator = dataList.iterator();
			while (iterator.hasNext()) {
				DataFile dataFile = iterator.next();
				if (StringUtils.isNotEmpty(tenantId)) {
					dataFile.setTenantId(tenantId);
					dataFile.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				exists.put(dataFile.getName(), dataFile);
			}
		}
		Iterator<DataFile> iterator = dataMap.values().iterator();
		while (iterator.hasNext()) {
			DataFile dataFile = iterator.next();
			if (dataFile.getData() != null) {
				if (exists.get(dataFile.getName()) != null) {
					DataFile model = (DataFile) exists.get(dataFile.getName());
					if (model.getSize() != dataFile.getData().length) {
						model.setFilename(dataFile.getFilename());
						model.setData(dataFile.getData());
						model.setSize(dataFile.getData().length);

						String filePath = model.getPath();
						if (filePath != null && filePath.length() > 0) {
							if (filePath.startsWith("//")) {
								filePath = filePath.substring(1, filePath.length());
								model.setPath(filePath);
							}
						}
						this.updateDataFileInfo(tenantId, model);
					}
				} else {
					if (dataFile.getSize() <= 0) {
						dataFile.setSize(dataFile.getData().length);
					}

					String filePath = dataFile.getPath();
					if (filePath != null && filePath.length() > 0) {
						if (filePath.startsWith("//")) {
							filePath = filePath.substring(1, filePath.length());
							dataFile.setPath(filePath);
						}
					}
					this.insertDataFile(tenantId, dataFile);
				}
			}
		}
	}

	@javax.annotation.Resource
	public void setDataFileMapper(DataFileMapper dataFileMapper) {
		this.dataFileMapper = dataFileMapper;
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Transactional
	public void updateDataFile(String tenantId, DataFile dataFile) {
		if (StringUtils.isNotEmpty(tenantId)) {
			dataFile.setTenantId(tenantId);
			dataFile.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		dataFileMapper.updateDataFile(dataFile);
	}

	@Transactional
	public void updateDataFileInfo(String tenantId, DataFile dataFile) {
		if (dataFile.getData() == null) {
			throw new RuntimeException("bytes is null");
		}
		if (dataFile.getSize() <= 0) {
			dataFile.setSize(dataFile.getData().length);
		}

		if (StringUtils.isNotEmpty(tenantId)) {
			dataFile.setTenantId(tenantId);
			dataFile.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}

		if (StringUtils.equals(DBUtils.POSTGRESQL, DBConnectionFactory.getDatabaseType())) {
			entityDAO.update("updateDataFileFileInfo_postgres", dataFile);
		} else {
			entityDAO.update("updateDataFileFileInfo", dataFile);
		}
	}

}