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

package com.glaf.base.info.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.UUID32;
import com.glaf.base.info.model.InfoCategory;
import com.glaf.base.info.mapper.InfoCategoryMapper;
import com.glaf.base.info.query.InfoCategoryQuery;
import com.glaf.base.info.service.InfoCategoryService;
import com.glaf.base.info.util.InfoCategoryJsonFactory;

@Service("infoCategoryService")
@Transactional(readOnly = true)
public class InfoCategoryServiceImpl implements InfoCategoryService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected InfoCategoryMapper infoCategoryMapper;

	protected ITableDataService tableDataService;

	public InfoCategoryServiceImpl() {

	}

	public int count(InfoCategoryQuery query) {
		return infoCategoryMapper.getInfoCategoryCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			infoCategoryMapper.deleteInfoCategoryById(id);
		}
	}

	/**
	 * 获取某个分类的直接子节点列表
	 * 
	 * @param parentId
	 * @return
	 */
	public List<InfoCategory> getCategoryList(long parentId) {
		InfoCategoryQuery query = new InfoCategoryQuery();
		query.parentId(parentId);
		List<InfoCategory> list = infoCategoryMapper.getInfoCategories(query);
		return list;
	}

	/**
	 * 获取某个用户创建的全部分类列表
	 * 
	 * @param createBy
	 * @return
	 */
	public List<InfoCategory> getCategoryList(String tenantId) {
		InfoCategoryQuery query = new InfoCategoryQuery();
		query.tenantId(tenantId);
		query.setOrderBy(" E.TREEID_ asc");
		List<InfoCategory> list = infoCategoryMapper.getInfoCategories(query);
		return list;
	}

	/**
	 * 获取某个用户创建的某个分类的子分类列表
	 * 
	 * @param createBy
	 * @param parentId
	 * @return
	 */
	public List<InfoCategory> getCategoryList(String tenantId, long parentId) {
		InfoCategoryQuery query = new InfoCategoryQuery();
		query.tenantId(tenantId);
		query.parentId(parentId);
		query.setOrderBy(" E.TREEID_ asc");
		List<InfoCategory> list = infoCategoryMapper.getInfoCategories(query);
		return list;
	}

	/**
	 * 获取某个用户创建的某类型的子分类列表
	 * 
	 * @param createBy
	 * @param type
	 * @return
	 */
	public List<InfoCategory> getCategoryList(String tenantId, String type) {
		InfoCategoryQuery query = new InfoCategoryQuery();
		query.tenantId(tenantId);
		query.type(type);
		query.setOrderBy(" E.TREEID_ asc");
		List<InfoCategory> list = infoCategoryMapper.getInfoCategories(query);
		return list;
	}

	protected String getTreeId(Map<Long, InfoCategory> dataMap, InfoCategory tree) {
		long parentId = tree.getParentId();
		long id = tree.getId();
		InfoCategory parent = dataMap.get(parentId);
		if (parent != null && parent.getId() != 0) {
			if (StringUtils.isEmpty(parent.getTreeId())) {
				return getTreeId(dataMap, parent) + id + "|";
			}
			if (!parent.getTreeId().endsWith("|")) {
				parent.setTreeId(parent.getTreeId() + "|");
			}
			return parent.getTreeId() + id + "|";
		}
		return tree.getTreeId();
	}

	public InfoCategory getInfoCategory(Long id) {
		if (id == null) {
			return null;
		}
		String cacheKey = "base_cat_" + id;
		if (CacheFactory.getString("base_cat", cacheKey) != null) {
			String text = CacheFactory.getString("base_cat", cacheKey);
			JSONObject json = JSON.parseObject(text);
			InfoCategory infoCategory = InfoCategoryJsonFactory.jsonToObject(json);
			return infoCategory;
		}
		InfoCategory infoCategory = infoCategoryMapper.getInfoCategoryById(id);
		if (infoCategory != null) {
			JSONObject json = InfoCategoryJsonFactory.toJsonObject(infoCategory);
			CacheFactory.put("base_cat", cacheKey, json.toJSONString());
		}
		return infoCategory;
	}

	public InfoCategory getInfoCategoryByUUID(String uuid) {
		if (uuid == null) {
			return null;
		}
		String cacheKey = "base_cat_" + uuid;
		if (CacheFactory.getString("base_cat", cacheKey) != null) {
			String text = CacheFactory.getString("base_cat", cacheKey);
			JSONObject json = JSON.parseObject(text);
			InfoCategory infoCategory = InfoCategoryJsonFactory.jsonToObject(json);
			return infoCategory;
		}
		InfoCategory infoCategory = infoCategoryMapper.getInfoCategoryByUUID(uuid);
		if (infoCategory != null) {
			JSONObject json = InfoCategoryJsonFactory.toJsonObject(infoCategory);
			CacheFactory.put("base_cat", cacheKey, json.toJSONString());
		}
		return infoCategory;
	}

	public int getInfoCategoryCountByQueryCriteria(InfoCategoryQuery query) {
		return infoCategoryMapper.getInfoCategoryCount(query);
	}

	public List<InfoCategory> getInfoCategorysByQueryCriteria(int start, int pageSize, InfoCategoryQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<InfoCategory> rows = sqlSessionTemplate.selectList("getInfoCategories", query, rowBounds);
		return rows;
	}

	public List<InfoCategory> list(InfoCategoryQuery query) {
		List<InfoCategory> list = infoCategoryMapper.getInfoCategories(query);
		return list;
	}

	public void loadChildren(List<InfoCategory> list, long parentId) {
		InfoCategoryQuery query = new InfoCategoryQuery();
		query.setParentId(Long.valueOf(parentId));
		List<InfoCategory> nodes = this.list(query);
		if (nodes != null && !nodes.isEmpty()) {
			for (InfoCategory node : nodes) {
				list.add(node);
				this.loadChildren(list, node.getId());
			}
		}
	}

	@Transactional
	public void save(InfoCategory infoCategory) {
		if (infoCategory.getId() == 0) {
			infoCategory.setId(idGenerator.nextId());
			infoCategory.setCreateDate(new Date());
			infoCategory.setUuid(UUID32.getUUID());
			if (infoCategory.getParentId() > 0) {
				InfoCategory parent = this.getInfoCategory(infoCategory.getParentId());
				if (parent != null && parent.getTreeId() != null) {
					infoCategory.setTreeId(parent.getTreeId() + infoCategory.getId() + "|");
				}
			} else {
				infoCategory.setTreeId(infoCategory.getId() + "|");
			}

			infoCategoryMapper.insertInfoCategory(infoCategory);
		} else {
			infoCategory.setLastUpdateDate(new Date());
			this.update(infoCategory);
		}
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
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@javax.annotation.Resource
	public void setInfoCategoryMapper(InfoCategoryMapper infoCategoryMapper) {
		this.infoCategoryMapper = infoCategoryMapper;
	}

	@Transactional
	public boolean update(InfoCategory bean) {
		InfoCategory model = this.getInfoCategory(bean.getId());
		/**
		 * 如果节点移动了位置，即移动到别的节点下面去了
		 */
		if (model.getParentId() != bean.getParentId()) {
			List<InfoCategory> list = new java.util.concurrent.CopyOnWriteArrayList<InfoCategory>();
			this.loadChildren(list, bean.getId());
			if (!list.isEmpty()) {
				for (InfoCategory node : list) {
					/**
					 * 不能移动到ta自己的子节点下面去
					 */
					if (bean.getParentId() == node.getId()) {
						throw new RuntimeException("Can't change node into children");
					}
				}
				/**
				 * 修正所有子节点的treeId
				 */
				InfoCategory oldParent = this.getInfoCategory(model.getParentId());
				InfoCategory newParent = this.getInfoCategory(bean.getParentId());
				if (oldParent != null && newParent != null && StringUtils.isNotEmpty(oldParent.getTreeId())
						&& StringUtils.isNotEmpty(newParent.getTreeId())) {
					TableModel tableModel = new TableModel();
					tableModel.setTableName("INFO_CATEGORY");
					ColumnModel idColumn = new ColumnModel();
					idColumn.setColumnName("ID_");
					idColumn.setJavaType("Long");
					tableModel.setIdColumn(idColumn);

					ColumnModel treeColumn = new ColumnModel();
					treeColumn.setColumnName("TREEID_");
					treeColumn.setJavaType("String");
					tableModel.addColumn(treeColumn);

					for (InfoCategory node : list) {
						String treeId = node.getTreeId();
						if (StringUtils.isNotEmpty(treeId)) {
							treeId = StringTools.replace(treeId, oldParent.getTreeId(), newParent.getTreeId());
							idColumn.setValue(node.getId());
							treeColumn.setValue(treeId);
							tableDataService.updateTableData(tableModel);
						}
					}
				}
			}
		}

		if (bean.getParentId() != 0) {
			InfoCategory parent = this.getInfoCategory(bean.getParentId());
			if (parent != null) {
				if (StringUtils.isNotEmpty(parent.getTreeId())) {
					bean.setTreeId(parent.getTreeId() + bean.getId() + "|");
				}
			}
		}

		infoCategoryMapper.updateInfoCategory(bean);

		String cacheKey = "base_cat_" + bean.getId();
		CacheFactory.remove("base_cat", cacheKey);
		cacheKey = "base_cat_" + bean.getUuid();
		CacheFactory.remove("base_cat", cacheKey);

		return true;
	}

}
