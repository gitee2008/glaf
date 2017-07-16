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

package com.glaf.matrix.category.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.service.ITableDataService;
import com.glaf.matrix.category.domain.CategoryAccess;
import com.glaf.matrix.category.domain.CategoryOwner;
import com.glaf.matrix.category.domain.CategorySubordinate;
import com.glaf.matrix.category.domain.CategoryView;
import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.model.SysOrganization;
import com.glaf.base.modules.sys.model.SysRole;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.SysOrganizationService;
import com.glaf.base.modules.sys.service.SysRoleService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.matrix.category.domain.Category;
import com.glaf.matrix.category.mapper.CategoryAccessMapper;
import com.glaf.matrix.category.mapper.CategoryMapper;
import com.glaf.matrix.category.mapper.CategoryOwnerMapper;
import com.glaf.matrix.category.mapper.CategorySubordinateMapper;
import com.glaf.matrix.category.mapper.CategoryViewMapper;
import com.glaf.matrix.category.query.CategoryQuery;
import com.glaf.matrix.category.service.CategoryService;

@Service("com.glaf.matrix.category.service.categoryService")
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected CategoryMapper categoryMapper;

	protected CategoryAccessMapper categoryAccessMapper;

	protected CategoryOwnerMapper categoryOwnerMapper;

	protected CategoryViewMapper categoryViewMapper;

	protected CategorySubordinateMapper categorySubordinateMapper;

	protected IDatabaseService databaseService;

	protected ITableDataService tableDataService;

	protected SysUserService sysUserService;

	protected SysRoleService sysRoleService;

	protected SysOrganizationService sysOrganizationService;

	public CategoryServiceImpl() {

	}

	public int count(CategoryQuery query) {
		return categoryMapper.getCategoryCount(query);
	}

	@Transactional
	public void createAccessor(long categoryId, String actorId) {
		CategoryAccess model = new CategoryAccess();
		model.setId(idGenerator.nextId("SYS_CATEGORY_ACCESS"));
		model.setActorId(actorId);
		model.setCategoryId(categoryId);
		categoryAccessMapper.deleteAccessor(model);
		categoryAccessMapper.insertCategoryAccess(model);
	}

	@Transactional
	public void deleteAccessor(long categoryId, String actorId) {
		CategoryAccess model = new CategoryAccess();
		model.setActorId(actorId);
		model.setCategoryId(categoryId);
		categoryAccessMapper.deleteAccessor(model);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			CategoryQuery query = new CategoryQuery();
			query.parentId(id);
			int count = categoryMapper.getCategoryCount(query);
			if (count > 0) {
				throw new RuntimeException(" Children nodes exists");
			}
			categoryMapper.deleteCategoryById(id);
			categoryAccessMapper.deleteCategoryAccessByCategoryId(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				categoryMapper.deleteCategoryById(id);
				categoryAccessMapper.deleteCategoryAccessByCategoryId(id);
			}
		}
	}

	public List<CategoryAccess> getAllCategoryAccesses() {
		return categoryAccessMapper.getAllCategoryAccesses();
	}

	public Category getCategory(Long id) {
		if (id == null) {
			return null;
		}
		Category category = categoryMapper.getCategoryById(id);
		if (category != null) {
			List<CategoryAccess> accesses = categoryAccessMapper.getCategoryAccessesByCategoryId(id);
			if (accesses != null && !accesses.isEmpty()) {
				for (CategoryAccess access : accesses) {
					category.addAccessor(access.getActorId());
				}
			}
			List<CategoryOwner> owners = categoryOwnerMapper.getCategoryOwneresByCategoryId(id);
			category.setOwners(owners);

			List<CategorySubordinate> subordinates = categorySubordinateMapper.getCategorySubordinatesByCategoryId(id);
			if (subordinates != null && !subordinates.isEmpty()) {
				for (CategorySubordinate sub : subordinates) {
					category.addSubordinate(sub.getSubordinateId());
				}
			}
		}
		return category;
	}

	protected List<CategoryAccess> getCategoryAccesses(long categoryId) {
		List<CategoryAccess> accesses = categoryAccessMapper.getCategoryAccessesByCategoryId(categoryId);
		if (accesses != null && !accesses.isEmpty()) {
		}
		return accesses;
	}

	/**
	 * 根据code获取一个分类信息
	 * 
	 * @return
	 */
	public Category getCategoryByCode(String code) {
		return categoryMapper.getCategoryByCode(code);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getCategoryCountByQueryCriteria(CategoryQuery query) {
		return categoryMapper.getCategoryCount(query);
	}

	/**
	 * 获取某个用户的分类列表，包含全部的下级节点
	 */
	public List<Category> getCategories(String actorId) {
		CategoryQuery query = new CategoryQuery();
		List<Category> categories = new ArrayList<Category>();
		List<Category> list = categoryMapper.getCategories(query);
		if (list != null && !list.isEmpty()) {
			/**
			 * 取得某个用户的分类节点
			 */
			List<CategoryAccess> x_accesses = categoryAccessMapper.getCategoryAccessesByActorId(actorId);
			List<CategorySubordinate> all_subs = categorySubordinateMapper.getAllCategorySubordinates();
			List<SysRole> roles = sysUserService.getUserRoles(actorId);
			List<String> roleCodes = new ArrayList<String>();
			List<Long> categoryIds = new ArrayList<Long>();
			Map<Long, Category> categoryMap = new HashMap<Long, Category>();
			Map<Long, List<CategorySubordinate>> categorySubMap = new HashMap<Long, List<CategorySubordinate>>();

			if (x_accesses != null && !x_accesses.isEmpty()) {
				for (CategoryAccess access : x_accesses) {
					if (access.getDynamic() == null) {// 原始授权,并非动态权限
						categoryIds.add(access.getCategoryId());// 用户拥有的分类集合
					}
				}
			}

			if (roles != null && !roles.isEmpty()) {
				for (SysRole role : roles) {
					roleCodes.add(role.getCode());// 用户拥有的角色集合
				}
			}

			for (Category category : list) {
				categoryMap.put(category.getId(), category);
				/**
				 * 默认创建者拥有该分类
				 */
				if (StringUtils.equals(category.getCreateBy(), actorId)) {
					if (!categories.contains(category)) {
						categories.add(category);
					}
				}

				/**
				 * 系统管理员拥有该分类
				 */
				if (roleCodes.contains(SysConstants.SYSTEM_ADMINISTRATOR)) {
					if (!categories.contains(category)) {
						categories.add(category);
					}
				}

				/**
				 * 分类管理员拥有该分类
				 */
				if (roleCodes.contains(SysConstants.PROJECT_ADMIN)) {
					if (!categories.contains(category)) {
						categories.add(category);
					}
				}

				if (all_subs != null && !all_subs.isEmpty()) {
					for (CategorySubordinate sub : all_subs) {
						if (category.getId() == sub.getCategoryId()) {
							List<CategorySubordinate> subs = categorySubMap.get(category.getId());
							if (subs == null) {
								subs = new ArrayList<CategorySubordinate>();
							}
							subs.add(sub);
							categorySubMap.put(category.getId(), subs);
						}
					}
				}
			}

			for (Category category : list) {
				if (categoryIds.contains(category.getId())) {
					/**
					 * 包含分类及全部子分类
					 */
					CategoryQuery q = new CategoryQuery();
					q.treeIdLike(category.getTreeId() + "%");
					List<Category> rows = categoryMapper.getCategories(q);
					if (rows != null && !rows.isEmpty()) {
						for (Category p : rows) {
							if (!categories.contains(p)) {
								categories.add(p);
							}
						}
					}
					if (!categories.contains(category)) {
						categories.add(category);
					}
				}
			}

			for (Category category : list) {
				/**
				 * 检查是否为从属分类，如果是从属节点，也一起加入
				 */
				List<CategorySubordinate> subs = categorySubMap.get(category.getId());
				if (subs != null && !subs.isEmpty()) {
					for (CategorySubordinate sub : subs) {
						Category p = categoryMap.get(sub.getSubordinateId());
						if (p != null && !categories.contains(p)) {
							Category parent = categoryMap.get(p.getParentId());
							if (categories.contains(parent)) {// 如果包含了父节点，才能包含子节点
								categories.add(p);
							}
						}
					}
				}
			}
		}
		return categories;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<Category> getCategoriesByQueryCriteria(int start, int pageSize, CategoryQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Category> rows = sqlSessionTemplate.selectList("getCategories", query, rowBounds);
		return rows;
	}

	public List<Category> list(CategoryQuery query) {
		List<Category> list = categoryMapper.getCategories(query);
		if (list != null && !list.isEmpty()) {
		}
		return list;
	}

	@Transactional
	public void save(Category category) {
		if (category.getId() == 0) {
			category.setId(idGenerator.nextId("SYS_CATEGORY"));
			category.setCreateTime(new Date());
			long parentId = category.getParentId();
			if (parentId > 0) {
				Category parent = this.getCategory(parentId);
				if (parent != null) {
					category.setLevel(parent.getLevel() + 1);
					if (parent.getTreeId() != null) {
						category.setTreeId(parent.getTreeId() + category.getId() + "|");
					}
				}
			} else {
				category.setLevel(0);
				category.setTreeId(category.getId() + "|");
			}
			categoryMapper.insertCategory(category);
		} else {
			long parentId = category.getParentId();
			if (parentId > 0 && parentId != category.getId()) {
				Category parent = this.getCategory(parentId);
				if (parent != null) {
					category.setLevel(parent.getLevel() + 1);
					if (parent.getTreeId() != null) {
						category.setTreeId(parent.getTreeId() + category.getId() + "|");
					}
				}
			} else {
				category.setLevel(0);
				category.setTreeId(category.getId() + "|");
			}
			category.setUpdateTime(new Date());
			categoryMapper.updateCategory(category);
		}

		if (category.getOwners() != null) {
			categoryOwnerMapper.deleteCategoryOwnerByCategoryId(category.getId());
			for (CategoryOwner owner : category.getOwners()) {
				owner.setId(idGenerator.nextId("SYS_CATEGORY_OWNER"));
				owner.setActorId(owner.getActorId());
				owner.setCategoryId(category.getId());
				categoryOwnerMapper.insertCategoryOwner(owner);
			}
		}

		if (category.getActorIds() != null) {
			categoryAccessMapper.deleteCategoryAccessByCategoryId(category.getId());
			for (String actorId : category.getActorIds()) {
				CategoryAccess model = new CategoryAccess();
				model.setId(idGenerator.nextId("SYS_CATEGORY_ACCESS"));
				model.setActorId(actorId);
				model.setCategoryId(category.getId());
				categoryAccessMapper.insertCategoryAccess(model);
			}
		}

		if (category.getSubordinateIds() != null) {
			categorySubordinateMapper.deleteCategorySubordinateByCategoryId(category.getId());
			for (Long subordinateId : category.getSubordinateIds()) {
				CategorySubordinate model = new CategorySubordinate();
				model.setId(idGenerator.nextId("SYS_CATEGORY_SUBORDINATE"));
				model.setSubordinateId(subordinateId);
				model.setCategoryId(category.getId());
				categorySubordinateMapper.insertCategorySubordinate(model);
			}
		}

	}

	/**
	 * 保存分类访问者
	 * 
	 * @return
	 */
	@Transactional
	public void saveAccessors(long categoryId, Collection<String> accessors) {
		categoryAccessMapper.deleteCategoryAccessByCategoryId(categoryId);
		for (String actorId : accessors) {
			CategoryAccess model = new CategoryAccess();
			model.setId(idGenerator.nextId("SYS_CATEGORY_ACCESS"));
			model.setActorId(actorId);
			model.setCategoryId(categoryId);
			categoryAccessMapper.deleteAccessor(model);
			categoryAccessMapper.insertCategoryAccess(model);
		}
	}

	/**
	 * 保存分类访问者
	 * 
	 * @return
	 */
	@Transactional
	public void saveAccessors(String accessor, Collection<Long> categoryIds) {
		categoryAccessMapper.deleteCategoryAccessByActorId(accessor);
		for (Long categoryId : categoryIds) {
			CategoryAccess model = new CategoryAccess();
			model.setId(idGenerator.nextId("SYS_CATEGORY_ACCESS"));
			model.setActorId(accessor);
			model.setCategoryId(categoryId);
			categoryAccessMapper.deleteAccessor(model);
			categoryAccessMapper.insertCategoryAccess(model);
		}
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
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

	@javax.annotation.Resource(name = "com.glaf.matrix.category.mapper.CategoryAccessMapper")
	public void setCategoryAccessMapper(CategoryAccessMapper categoryAccessMapper) {
		this.categoryAccessMapper = categoryAccessMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.category.mapper.CategoryMapper")
	public void setCategoryMapper(CategoryMapper categoryMapper) {
		this.categoryMapper = categoryMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.category.mapper.CategoryOwnerMapper")
	public void setCategoryOwnerMapper(CategoryOwnerMapper categoryOwnerMapper) {
		this.categoryOwnerMapper = categoryOwnerMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.category.mapper.CategorySubordinateMapper")
	public void setCategorySubordinateMapper(CategorySubordinateMapper categorySubordinateMapper) {
		this.categorySubordinateMapper = categorySubordinateMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.category.mapper.CategoryViewMapper")
	public void setCategoryViewMapper(CategoryViewMapper categoryViewMapper) {
		this.categoryViewMapper = categoryViewMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setSysOrganizationService(SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}

	@javax.annotation.Resource
	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	/**
	 * 保存一条分类信息
	 * 
	 * @return
	 */
	@Transactional
	public void update(Category category) {
		long parentId = category.getParentId();
		if (parentId > 0 && parentId != category.getId()) {
			Category parent = this.getCategory(parentId);
			if (parent != null) {
				category.setLevel(parent.getLevel() + 1);
				if (parent.getTreeId() != null) {
					category.setTreeId(parent.getTreeId() + category.getId() + "|");
				}
			}
		} else {
			category.setLevel(0);
			category.setTreeId(category.getId() + "|");
		}
		category.setUpdateTime(new Date());
		categoryMapper.updateCategory(category);
	}

	/**
	 * 更新用户分类视图
	 * 
	 * @param userId
	 */
	@Transactional
	public void updateCategoryView(String userId) {
		categoryViewMapper.deleteCategoryViewByUserId(userId);// 删除分类视图

		TableModel table = new TableModel();
		table.setTableName("SYS_CATEGORY_ACCESS");
		table.addStringColumn("ACTORID_", userId);
		table.addStringColumn("DYNAMIC_", "Y");
		tableDataService.deleteTableData(table);// 删除动态生成的权限

		List<Category> categories = this.getCategories(userId);
		if (categories != null && !categories.isEmpty()) {
			SysUser user = sysUserService.findByAccount(userId);
			List<SysOrganization> organizations = sysOrganizationService.getSysOrganizationList();
			List<CategoryAccess> x_accesses = categoryAccessMapper.getCategoryAccessesByActorId(userId);
			List<String> accesses = new ArrayList<String>();
			Map<Long, String> organizationMap = new HashMap<Long, String>();
			if (organizations != null && !organizations.isEmpty()) {
				for (SysOrganization organization : organizations) {
					organizationMap.put(organization.getId(), organization.getName());
				}
			}

			if (x_accesses != null && !x_accesses.isEmpty()) {
				for (CategoryAccess access : x_accesses) {
					if (access.getDynamic() == null) {// 原始授权,并非动态权限
						accesses.add(access.getActorId().toLowerCase() + "_" + access.getCategoryId());
					}
				}

			}

			for (Category category : categories) {
				CategoryView model = new CategoryView();
				model.setUid(userId + "/" + category.getId());
				model.setId(category.getId());
				model.setParentId(category.getParentId());
				model.setName(category.getName());
				model.setCode(category.getCode());
				model.setDescription(category.getDescription());
				model.setDiscriminator(category.getDiscriminator());
				model.setIcon(category.getIcon());
				model.setIconCls(category.getIconCls());
				model.setLevel(category.getLevel());
				model.setSort(category.getSort());
				model.setLocked(category.getLocked());
				model.setTitle(category.getTitle());
				model.setTreeId(category.getTreeId());
				model.setType(category.getType());
				model.setUrl(category.getUrl());
				model.setUserId(userId);
				if (StringUtils.isNotEmpty(user.getName())) {
					model.setUserName(user.getName());
				} else {
					model.setUserName(userId);
				}
				if (organizationMap.get(user.getOrganizationId()) != null) {
					model.setOrgName(organizationMap.get(user.getOrganizationId()));
				}
				categoryViewMapper.insertCategoryView(model);

				if (!accesses.contains(userId.toLowerCase() + "_" + category.getId())) {
					CategoryAccess access = new CategoryAccess();
					access.setActorId(userId);
					access.setCategoryId(category.getId());
					access.setDynamic("Y");
					access.setId(idGenerator.nextId("SYS_CATEGORY_ACCESS"));
					categoryAccessMapper.insertCategoryAccess(access);
				}
			}
		}
	}

	/**
	 * 更新节点的treeId
	 * 
	 * @param treeIdMap
	 */
	@Transactional
	public void updateTreeIds(Map<Long, String> treeIdMap) {
		TableModel tableModel = new TableModel();
		tableModel.setTableName("SYS_CATEGORY");
		ColumnModel idColumn = new ColumnModel();
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableModel.setIdColumn(idColumn);

		ColumnModel treeColumn = new ColumnModel();
		treeColumn.setColumnName("TREEID_");
		treeColumn.setJavaType("String");
		tableModel.addColumn(treeColumn);

		Iterator<Long> iterator = treeIdMap.keySet().iterator();
		while (iterator.hasNext()) {
			Long id = iterator.next();
			String value = treeIdMap.get(id);
			if (value != null) {
				idColumn.setValue(id);
				treeColumn.setValue(value);
				tableDataService.updateTableData(tableModel);
			}
		}
	}

}
