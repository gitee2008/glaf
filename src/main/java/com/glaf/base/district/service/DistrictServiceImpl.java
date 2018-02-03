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

package com.glaf.base.district.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.district.domain.District;
import com.glaf.base.district.mapper.DistrictMapper;
import com.glaf.base.district.query.DistrictQuery;
import com.glaf.base.district.util.DistrictJsonFactory;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.base.TreeModel;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.query.TreeModelQuery;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.StringTools;

@Service("districtService")
@Transactional(readOnly = true)
public class DistrictServiceImpl implements DistrictService {
	protected final static Log logger = LogFactory.getLog(DistrictServiceImpl.class);

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected SqlSession sqlSession;

	protected DistrictMapper districtMapper;

	protected ITableDataService tableDataService;

	public DistrictServiceImpl() {

	}

	public int count(DistrictQuery query) {
		return districtMapper.getDistrictCount(query);
	}

	public void deleteById(long id) {
		DistrictQuery query = new DistrictQuery();
		query.parentId(id);
		List<District> children = this.list(query);
		if (children == null || children.isEmpty()) {
			districtMapper.deleteDistrictById(id);
		}
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear("district");
		}
	}

	public District getDistrict(long id) {
		String cacheKey = "sys_district_" + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("district", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return DistrictJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
				}
			}
		}
		District district = districtMapper.getDistrictById(id);
		if (district != null && SystemConfig.getBoolean("use_query_cache")) {
			JSONObject json = district.toJsonObject();
			CacheFactory.put("district", cacheKey, json.toJSONString());
		}
		return district;
	}

	/**
	 * 根据编码获取一条记录
	 * 
	 * @return
	 */
	public District getDistrictByCode(String code) {
		String cacheKey = "sys_district_" + code;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("district", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return DistrictJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
				}
			}
		}
		District district = districtMapper.getDistrictByCode(code);
		if (district != null && SystemConfig.getBoolean("use_query_cache")) {
			JSONObject json = district.toJsonObject();
			CacheFactory.put("district", cacheKey, json.toJSONString());
		}
		return district;
	}

	/**
	 * 根据名称获取一条记录
	 * 
	 * @return
	 */
	public District getDistrictByName(String name) {
		String cacheKey = "sys_district_" + name;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("district", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return DistrictJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
				}
			}
		}

		DistrictQuery query = new DistrictQuery();
		query.nameLike(name);
		List<District> rows = districtMapper.getDistricts(query);
		if (rows != null && !rows.isEmpty()) {
			District district = rows.get(0);
			if (district != null && SystemConfig.getBoolean("use_query_cache")) {
				JSONObject json = district.toJsonObject();
				CacheFactory.put("district", cacheKey, json.toJSONString());
			}
			return district;
		}
		return null;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDistrictCountByQueryCriteria(DistrictQuery query) {
		return districtMapper.getDistrictCount(query);
	}

	public List<District> getDistrictList(long parentId) {
		String cacheKey = "sys_districts_" + parentId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("district", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray array = JSON.parseArray(text);
					return DistrictJsonFactory.arrayToList(array);
				} catch (Exception ex) {
				}
			}
		}
		DistrictQuery query = new DistrictQuery();
		query.parentId(parentId);
		List<District> rows = list(query);
		if (rows != null && !rows.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = DistrictJsonFactory.listToArray(rows);
				CacheFactory.put("district", cacheKey, array.toJSONString());
			}
		}
		return rows;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<District> getDistrictsByQueryCriteria(int start, int pageSize, DistrictQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<District> rows = sqlSession.selectList("getDistricts", query, rowBounds);
		return rows;
	}

	public int getDistrictTreeModelCount(TreeModelQuery query) {
		return districtMapper.getDistrictTreeModelCount(query);
	}

	public List<TreeModel> getDistrictTreeModels(TreeModelQuery query) {
		return districtMapper.getDistrictTreeModels(query);
	}

	protected String getTreeId(Map<Long, District> dataMap, District tree) {
		long parentId = tree.getParentId();
		long id = tree.getId();
		District parent = dataMap.get(parentId);
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

	public List<District> list(DistrictQuery query) {
		List<District> list = districtMapper.getDistricts(query);
		return list;
	}

	public void loadChildren(List<District> list, long parentId) {
		DistrictQuery query = new DistrictQuery();
		query.setParentId(parentId);
		List<District> nodes = this.list(query);
		if (nodes != null && !nodes.isEmpty()) {
			for (District node : nodes) {
				list.add(node);
				this.loadChildren(list, node.getId());
			}
		}
	}

	@Transactional
	public void save(District district) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear("district");
		}
		if (district.getId() == 0) {
			district.setLevel(1);
			district.setId(idGenerator.nextId());
			if (district.getParentId() != 0) {
				District parent = this.getDistrict(district.getParentId());
				if (parent != null) {
					district.setLevel(parent.getLevel() + 1);
					if (parent.getTreeId() != null) {
						district.setTreeId(parent.getTreeId() + district.getId() + "|");
					}
				}
			} else {
				district.setTreeId(district.getId() + "|");
			}
			districtMapper.insertDistrict(district);
		} else {
			District model = this.getDistrict(district.getId());
			if (model != null) {
				this.update(district);
			} else {
				district.setLevel(1);
				district.setId(idGenerator.nextId());
				if (district.getParentId() != 0) {
					District parent = this.getDistrict(district.getParentId());
					if (parent != null) {
						district.setLevel(parent.getLevel() + 1);
						if (parent.getTreeId() != null) {
							district.setTreeId(parent.getTreeId() + district.getId() + "|");
						}
					}
				} else {
					district.setTreeId(district.getId() + "|");
				}
				districtMapper.insertDistrict(district);
			}
		}
	}

	@javax.annotation.Resource
	public void setDistrictMapper(DistrictMapper districtMapper) {
		this.districtMapper = districtMapper;
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

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@Transactional
	public boolean update(District bean) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear("district");
		}
		District model = this.getDistrict(bean.getId());
		/**
		 * 如果节点移动了位置，即移动到别的节点下面去了
		 */
		if (model.getParentId() != bean.getParentId()) {
			List<District> list = new ArrayList<District>();
			this.loadChildren(list, bean.getId());
			if (!list.isEmpty()) {
				for (District node : list) {
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
				District oldParent = this.getDistrict(model.getParentId());
				District newParent = this.getDistrict(bean.getParentId());
				if (oldParent != null && newParent != null && StringUtils.isNotEmpty(oldParent.getTreeId())
						&& StringUtils.isNotEmpty(newParent.getTreeId())) {
					TableModel tableModel = new TableModel();
					tableModel.setTableName("SYS_DISTRICT");
					ColumnModel idColumn = new ColumnModel();
					idColumn.setColumnName("ID_");
					idColumn.setJavaType("Long");
					tableModel.setIdColumn(idColumn);

					ColumnModel treeColumn = new ColumnModel();
					treeColumn.setColumnName("TREEID_");
					treeColumn.setJavaType("String");
					tableModel.addColumn(treeColumn);

					for (District node : list) {
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
			District parent = this.getDistrict(bean.getParentId());
			if (parent != null) {
				if (StringUtils.isNotEmpty(parent.getTreeId())) {
					bean.setTreeId(parent.getTreeId() + bean.getId() + "|");
				}
			}
		}

		districtMapper.updateDistrict(bean);

		return true;
	}

}