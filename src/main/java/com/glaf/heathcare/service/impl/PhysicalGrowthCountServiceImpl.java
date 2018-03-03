package com.glaf.heathcare.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.heathcare.domain.PhysicalGrowthCount;
import com.glaf.heathcare.mapper.PhysicalGrowthCountMapper;
import com.glaf.heathcare.query.PhysicalGrowthCountQuery;
import com.glaf.heathcare.service.PhysicalGrowthCountService;

@Service("com.glaf.heathcare.service.physicalGrowthCountService")
@Transactional(readOnly = true)
public class PhysicalGrowthCountServiceImpl implements PhysicalGrowthCountService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PhysicalGrowthCountMapper physicalGrowthCountMapper;

	public PhysicalGrowthCountServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<PhysicalGrowthCount> list) {
		for (PhysicalGrowthCount physicalGrowthCount : list) {
			if (physicalGrowthCount.getId() == 0) {
				physicalGrowthCount.setId(idGenerator.nextId("HEALTH_PHYSICAL_GROWTH_COUNT"));
			}
		}

		int batch_size = 50;
		List<PhysicalGrowthCount> rows = new ArrayList<PhysicalGrowthCount>(batch_size);

		for (PhysicalGrowthCount bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					physicalGrowthCountMapper.bulkInsertPhysicalGrowthCount_oracle(rows);
				} else {
					physicalGrowthCountMapper.bulkInsertPhysicalGrowthCount(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				physicalGrowthCountMapper.bulkInsertPhysicalGrowthCount_oracle(rows);
			} else {
				physicalGrowthCountMapper.bulkInsertPhysicalGrowthCount(rows);
			}
			rows.clear();
		}
	}

	public int count(PhysicalGrowthCountQuery query) {
		return physicalGrowthCountMapper.getPhysicalGrowthCountCount(query);
	}

	@Transactional
	public void deleteByCheckId(String checkId) {
		if (checkId != null) {
			physicalGrowthCountMapper.deletePhysicalGrowthCountByCheckId(checkId);
		}
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPhysicalGrowthCountCountByQueryCriteria(PhysicalGrowthCountQuery query) {
		return physicalGrowthCountMapper.getPhysicalGrowthCountCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PhysicalGrowthCount> getPhysicalGrowthCountsByQueryCriteria(int start, int pageSize,
			PhysicalGrowthCountQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PhysicalGrowthCount> rows = sqlSessionTemplate.selectList("getPhysicalGrowthCounts", query, rowBounds);
		return rows;
	}

	public List<PhysicalGrowthCount> list(PhysicalGrowthCountQuery query) {
		List<PhysicalGrowthCount> list = physicalGrowthCountMapper.getPhysicalGrowthCounts(query);
		return list;
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.PhysicalGrowthCountMapper")
	public void setPhysicalGrowthCountMapper(PhysicalGrowthCountMapper physicalGrowthCountMapper) {
		this.physicalGrowthCountMapper = physicalGrowthCountMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
