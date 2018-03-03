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
import com.glaf.heathcare.domain.GrowthRateCount;
import com.glaf.heathcare.mapper.GrowthRateCountMapper;
import com.glaf.heathcare.query.GrowthRateCountQuery;
import com.glaf.heathcare.service.GrowthRateCountService;

@Service("com.glaf.heathcare.service.growthRateCountService")
@Transactional(readOnly = true)
public class GrowthRateCountServiceImpl implements GrowthRateCountService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GrowthRateCountMapper growthRateCountMapper;

	public GrowthRateCountServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<GrowthRateCount> list) {
		for (GrowthRateCount growthRateCount : list) {
			if (growthRateCount.getId() == 0) {
				growthRateCount.setId(idGenerator.nextId("HEALTH_GROWTH_RATE_COUNT"));
			}
		}

		int batch_size = 50;
		List<GrowthRateCount> rows = new ArrayList<GrowthRateCount>(batch_size);

		for (GrowthRateCount bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					growthRateCountMapper.bulkInsertGrowthRateCount_oracle(rows);
				} else {
					growthRateCountMapper.bulkInsertGrowthRateCount(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				growthRateCountMapper.bulkInsertGrowthRateCount_oracle(rows);
			} else {
				growthRateCountMapper.bulkInsertGrowthRateCount(rows);
			}
			rows.clear();
		}
	}

	public int count(GrowthRateCountQuery query) {
		return growthRateCountMapper.getGrowthRateCountCount(query);
	}

	@Transactional
	public void deleteByCheckId(String checkId) {
		if (checkId != null) {
			growthRateCountMapper.deleteGrowthRateCountByCheckId(checkId);
		}
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGrowthRateCountCountByQueryCriteria(GrowthRateCountQuery query) {
		return growthRateCountMapper.getGrowthRateCountCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GrowthRateCount> getGrowthRateCountsByQueryCriteria(int start, int pageSize,
			GrowthRateCountQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<GrowthRateCount> rows = sqlSessionTemplate.selectList("getGrowthRateCounts", query, rowBounds);
		return rows;
	}

	public List<GrowthRateCount> list(GrowthRateCountQuery query) {
		List<GrowthRateCount> list = growthRateCountMapper.getGrowthRateCounts(query);
		return list;
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GrowthRateCountMapper")
	public void setGrowthRateCountMapper(GrowthRateCountMapper growthRateCountMapper) {
		this.growthRateCountMapper = growthRateCountMapper;
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

}
