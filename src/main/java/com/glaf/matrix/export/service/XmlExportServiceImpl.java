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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.security.Authentication;
import com.glaf.core.util.UUID32;
import com.glaf.matrix.export.domain.XmlExport;
import com.glaf.matrix.export.domain.XmlExportItem;
import com.glaf.matrix.export.factory.XmlExportFactory;
import com.glaf.matrix.export.mapper.XmlExportMapper;
import com.glaf.matrix.export.mapper.XmlExportItemMapper;
import com.glaf.matrix.export.query.XmlExportQuery;
import com.glaf.matrix.export.util.XmlExportJsonFactory;
import com.glaf.matrix.export.query.XmlExportItemQuery;

@Service("com.glaf.matrix.export.service.xmlExportService")
@Transactional(readOnly = true)
public class XmlExportServiceImpl implements XmlExportService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected XmlExportMapper xmlExportMapper;

	protected XmlExportItemMapper xmlExportItemMapper;

	public XmlExportServiceImpl() {

	}

	public int count(XmlExportQuery query) {
		return xmlExportMapper.getXmlExportCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			xmlExportMapper.deleteXmlExportById(id);
			XmlExportFactory.clearAll();
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			XmlExportFactory.clearAll();
			for (String id : ids) {
				xmlExportMapper.deleteXmlExportById(id);
			}
		}
	}

	public JSONObject exportJson(String expId) {
		XmlExport root = this.getXmlExport(expId);
		JSONObject jsonObject = root.toJsonObject();

		List<XmlExport> children = xmlExportMapper.getChildrenXmlExports(root.getNodeId());
		if (children != null && !children.isEmpty()) {
			JSONArray array = new JSONArray();
			for (XmlExport child : children) {
				if (!"Y".equals(child.getActive())) {
					continue;
				}
				List<XmlExportItem> items = xmlExportItemMapper.getXmlExportItemsByExpId(child.getId());
				child.setItems(items);
				JSONObject json = child.toJsonObject();
				// json.remove("id");
				// json.remove("nodeId");
				// json.remove("nodeParentId");
				this.loadChild(json, child);
				array.add(json);
			}
			jsonObject.put("children", array);
		}

		return jsonObject;
	}

	/**
	 * 获取全部子孙节点
	 * 
	 * @param nodeParentId
	 * @return
	 */
	public List<XmlExport> getAllChildren(long nodeParentId) {
		List<XmlExport> list = new ArrayList<XmlExport>();
		if (nodeParentId > 0) {
			XmlExport root = xmlExportMapper.getXmlExportByNodeId(nodeParentId);
			this.load(list, root);
		} else {
			List<XmlExport> children = xmlExportMapper.getChildrenXmlExports(0);
			if (children != null && !children.isEmpty()) {
				for (XmlExport child : children) {
					child.setLevel(1);
					this.load(list, child);
				}
			}
		}
		return list;
	}

	/**
	 * 获取直接子节点及导出项
	 * 
	 * @param nodeParentId
	 * @return
	 */
	public List<XmlExport> getChildrenWithItems(long nodeParentId) {
		XmlExport parent = this.getXmlExportByNodeId(nodeParentId);
		List<XmlExport> children = xmlExportMapper.getChildrenXmlExports(nodeParentId);
		if (children != null && !children.isEmpty()) {
			for (XmlExport child : children) {
				List<XmlExportItem> items = xmlExportItemMapper.getXmlExportItemsByExpId(child.getId());
				child.setItems(items);
				child.setParent(parent);
			}
		}
		return children;
	}

	public XmlExport getXmlExport(String expId) {
		if (expId == null) {
			return null;
		}
		XmlExport xmlExport = xmlExportMapper.getXmlExportById(expId);
		if (xmlExport != null) {
			XmlExportItemQuery query = new XmlExportItemQuery();
			query.expId(expId);
			List<XmlExportItem> items = xmlExportItemMapper.getXmlExportItems(query);
			xmlExport.setItems(items);
		}
		return xmlExport;
	}

	public XmlExport getXmlExportByNodeId(long nodeId) {
		return xmlExportMapper.getXmlExportByNodeId(nodeId);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getXmlExportCountByQueryCriteria(XmlExportQuery query) {
		return xmlExportMapper.getXmlExportCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<XmlExport> getXmlExportsByQueryCriteria(int start, int pageSize, XmlExportQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<XmlExport> rows = sqlSessionTemplate.selectList("getXmlExports", query, rowBounds);
		return rows;
	}

	@Transactional
	public void importAll(String expId, JSONObject jsonObject) {
		XmlExport model = this.getXmlExport(expId);
		if (model != null) {
			XmlExport child = XmlExportJsonFactory.jsonToObject(jsonObject);
			child.setId(UUID32.getUUID());
			child.setNodeId(idGenerator.nextId("SYS_XML_EXPORT"));
			child.setNodeParentId(model.getNodeId());
			child.setCreateTime(new Date());
			child.setCreateBy(Authentication.getAuthenticatedActorId());
			child.setActive("Y");

			xmlExportMapper.insertXmlExport(child);

			if (child.getItems() != null && !child.getItems().isEmpty()) {
				for (XmlExportItem item : child.getItems()) {
					item.setId(UUID32.getUUID());
					item.setCreateTime(new Date());
					item.setCreateBy(Authentication.getAuthenticatedActorId());
					item.setExpId(child.getId());
					item.setLocked(0);
					xmlExportItemMapper.insertXmlExportItem(item);
				}
			}
			if (jsonObject.containsKey("children")) {
				JSONArray array = jsonObject.getJSONArray("children");
				int len = array.size();
				for (int i = 0; i < len; i++) {
					JSONObject json = array.getJSONObject(i);
					this.importAll(child.getId(), json);
				}
			}
		}
	}

	public List<XmlExport> list(XmlExportQuery query) {
		List<XmlExport> list = xmlExportMapper.getXmlExports(query);
		return list;
	}

	public void load(List<XmlExport> list, XmlExport root) {
		if (root.getParent() != null) {
			root.setLevel(root.getParent().getLevel() + 1);
		}
		logger.debug("-------------------level" + root.getLevel() + "----------------");
		StringBuilder buff = new StringBuilder();
		for (int i = 0; i < root.getLevel(); i++) {
			buff.append("&nbsp;&nbsp;");
		}
		root.setBlank(buff.toString());

		if (!list.contains(root)) {
			list.add(root);
		}

		List<XmlExport> children = xmlExportMapper.getChildrenXmlExports(root.getNodeId());
		if (children != null && !children.isEmpty()) {
			for (XmlExport child : children) {
				child.setParent(root);
				if (child.getParent() != null) {
					child.setLevel(child.getParent().getLevel() + 1);
				}
				StringBuilder buff2 = new StringBuilder();
				for (int i = 0; i < child.getLevel(); i++) {
					buff2.append("&nbsp;&nbsp;");
				}
				child.setBlank(buff2.toString());
				root.addChild(child);
				logger.debug("-------------------level" + child.getLevel() + "----------------");
				if (!list.contains(child)) {
					list.add(child);
				}
				this.load(list, child);
			}
		}
	}

	protected void loadChild(JSONObject jsonObject, XmlExport xmlExport) {
		List<XmlExport> children = xmlExportMapper.getChildrenXmlExports(xmlExport.getNodeId());
		if (children != null && !children.isEmpty()) {
			JSONArray array = new JSONArray();
			for (XmlExport child : children) {
				if (!"Y".equals(child.getActive())) {
					continue;
				}
				List<XmlExportItem> items = xmlExportItemMapper.getXmlExportItemsByExpId(child.getId());
				child.setItems(items);
				JSONObject json = child.toJsonObject();
				// json.remove("id");
				// json.remove("nodeId");
				// json.remove("nodeParentId");
				this.loadChild(json, child);
				array.add(json);
			}
			jsonObject.put("children", array);
		}
	}

	@Transactional
	public void save(XmlExport xmlExport) {
		if (xmlExport.getId() == null) {
			xmlExport.setId(UUID32.getUUID());
			xmlExport.setCreateTime(new Date());
			xmlExport.setNodeId(idGenerator.nextId("SYS_XML_EXPORT"));
			xmlExportMapper.insertXmlExport(xmlExport);
		} else {
			xmlExportMapper.updateXmlExport(xmlExport);
		}
		XmlExportFactory.clearAll();
	}

	@Transactional
	public String saveAs(String expId, String createBy) {
		XmlExport model = this.getXmlExport(expId);
		if (model != null) {
			model.setId(UUID32.getUUID());
			model.setCreateTime(new Date());
			model.setCreateBy(createBy);
			model.setNodeId(idGenerator.nextId("SYS_XML_EXPORT"));
			xmlExportMapper.insertXmlExport(model);

			if (model.getItems() != null && !model.getItems().isEmpty()) {
				for (XmlExportItem item : model.getItems()) {
					item.setId(UUID32.getUUID());
					item.setCreateTime(new Date());
					item.setCreateBy(createBy);
					item.setExpId(model.getId());
					xmlExportItemMapper.insertXmlExportItem(item);
				}
			}

			XmlExportFactory.clearAll();

			return model.getId();
		}
		return null;
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
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.export.mapper.XmlExportItemMapper")
	public void setXmlExportItemMapper(XmlExportItemMapper xmlExportItemMapper) {
		this.xmlExportItemMapper = xmlExportItemMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.export.mapper.XmlExportMapper")
	public void setXmlExportMapper(XmlExportMapper xmlExportMapper) {
		this.xmlExportMapper = xmlExportMapper;
	}

}
