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

package com.glaf.base.modules.sys.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.base.modules.sys.mapper.MembershipMapper;
import com.glaf.base.modules.sys.model.Membership;
import com.glaf.base.modules.sys.query.MembershipQuery;
import com.glaf.base.modules.sys.service.MembershipService;
import com.glaf.core.id.IdGenerator;

@Service("membershipService")
@Transactional(readOnly = true)
public class MembershipServiceImpl implements MembershipService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private IdGenerator idGenerator;

	private MembershipMapper membershipMapper;

	private SqlSessionTemplate sqlSessionTemplate;

	public MembershipServiceImpl() {

	}

	public int count(MembershipQuery query) {
		return membershipMapper.getMembershipCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			membershipMapper.deleteMembershipById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> rowIds) {
		if (rowIds != null && !rowIds.isEmpty()) {
			MembershipQuery query = new MembershipQuery();
			query.rowIds(rowIds);
			membershipMapper.deleteMemberships(query);
		}
	}

	@Transactional
	public void deleteMemberships(String actorId, String type) {
		MembershipQuery query = new MembershipQuery();
		query.actorId(actorId);
		query.type(type);
		membershipMapper.deleteActorMemberships(query);
	}

	public Membership getMembership(Long id) {
		if (id == null) {
			return null;
		}
		return membershipMapper.getMembershipById(id);
	}

	public int getMembershipCountByQueryCriteria(MembershipQuery query) {
		return membershipMapper.getMembershipCount(query);
	}

	/**
	 * 获取用户的成员
	 * 
	 * @param actorId
	 * @param type
	 * @return
	 */
	public List<Membership> getMemberships(String actorId, String type) {
		MembershipQuery query = new MembershipQuery();
		query.actorId(actorId);
		query.type(type);
		return membershipMapper.getMemberships(query);
	}

	public List<Membership> getMembershipsByQueryCriteria(int start, int pageSize, MembershipQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getMemberships", query, rowBounds);
	}

	public List<Membership> list(MembershipQuery query) {
		return membershipMapper.getMemberships(query);
	}

	@Transactional
	public void save(Membership membership) {
		if (membership.getId() == 0) {
			membership.setId(idGenerator.nextId());
			membership.setModifyDate(new Date());
			membershipMapper.insertMembership(membership);
		} else {
			membership.setModifyDate(new Date());
			membershipMapper.updateMembership(membership);
		}
	}

	@Transactional
	public void saveMemberships(Long nodeId, String roleId, String type, List<Membership> memberships) {
		MembershipQuery query = new MembershipQuery();
		query.nodeId(nodeId);
		query.roleId(roleId);
		query.type(type);
		List<Membership> list = membershipMapper.getMemberships(query);
		if (list != null && !list.isEmpty()) {
			for (Membership m : list) {
				membershipMapper.deleteMembershipById(m.getId());
			}
		}
		if (memberships != null && !memberships.isEmpty()) {
			for (Membership m : memberships) {
				m.setNodeId(nodeId);
				m.setRoleId(roleId);
				m.setType(type);
				m.setId(idGenerator.nextId());
				m.setModifyDate(new java.util.Date());
				membershipMapper.insertMembership(m);
			}
		}
	}

	@Transactional
	public void saveMemberships(Long nodeId, String type, List<Membership> memberships) {
		MembershipQuery query = new MembershipQuery();
		query.nodeId(nodeId);
		query.type(type);
		List<Membership> list = membershipMapper.getMemberships(query);
		if (list != null && !list.isEmpty()) {
			for (Membership m : list) {
				membershipMapper.deleteMembershipById(m.getId());
			}
		}
		if (memberships != null && !memberships.isEmpty()) {
			for (Membership m : memberships) {
				m.setNodeId(nodeId);
				m.setType(type);
				m.setId(idGenerator.nextId());
				m.setModifyDate(new java.util.Date());
				membershipMapper.insertMembership(m);
			}
		}
	}

	/**
	 * 保存成员关系
	 * 
	 * @param actorId 用户编号
	 * @param type    类型
	 * @param nodeIds 节点集合
	 */
	@Transactional
	public void saveMemberships(String actorId, String type, List<Long> nodeIds) {
		MembershipQuery query = new MembershipQuery();
		query.actorId(actorId);
		query.type(type);
		List<Membership> list = membershipMapper.getMemberships(query);
		if (list != null && !list.isEmpty()) {
			for (Membership m : list) {
				membershipMapper.deleteMembershipById(m.getId());
			}
		}
		if (nodeIds != null && !nodeIds.isEmpty()) {
			for (Long nodeId : nodeIds) {
				Membership m = new Membership();
				m.setActorId(actorId);
				m.setId(idGenerator.nextId());
				m.setModifyDate(new Date());
				m.setNodeId(nodeId);
				m.setType(type);
				membershipMapper.insertMembership(m);
			}
		}
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setMembershipMapper(MembershipMapper membershipMapper) {
		this.membershipMapper = membershipMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
