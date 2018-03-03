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
import com.glaf.heathcare.domain.GradeMedicalExaminationCount;
import com.glaf.heathcare.domain.GrowthRateCount;
import com.glaf.heathcare.domain.PhysicalGrowthCount;
import com.glaf.heathcare.mapper.GradeMedicalExaminationCountMapper;
import com.glaf.heathcare.query.GradeMedicalExaminationCountQuery;
import com.glaf.heathcare.query.GrowthRateCountQuery;
import com.glaf.heathcare.query.PhysicalGrowthCountQuery;
import com.glaf.heathcare.service.GradeMedicalExaminationCountService;
import com.glaf.heathcare.service.GrowthRateCountService;
import com.glaf.heathcare.service.PhysicalGrowthCountService;

@Service("com.glaf.heathcare.service.gradeMedicalExaminationCountService")
@Transactional(readOnly = true)
public class GradeMedicalExaminationCountServiceImpl implements GradeMedicalExaminationCountService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GradeMedicalExaminationCountMapper gradeMedicalExaminationCountMapper;

	protected GrowthRateCountService growthRateCountService;

	protected PhysicalGrowthCountService physicalGrowthCountService;

	public GradeMedicalExaminationCountServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<GradeMedicalExaminationCount> list) {
		for (GradeMedicalExaminationCount gradeMedicalExaminationCount : list) {
			if (gradeMedicalExaminationCount.getId() == 0) {
				gradeMedicalExaminationCount.setId(idGenerator.nextId("HEALTH_MEDICAL_EXAMINATIONCOUNT"));
			}
		}

		int batch_size = 50;
		List<GradeMedicalExaminationCount> rows = new ArrayList<GradeMedicalExaminationCount>(batch_size);

		for (GradeMedicalExaminationCount bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					gradeMedicalExaminationCountMapper.bulkInsertGradeMedicalExaminationCount_oracle(rows);
				} else {
					gradeMedicalExaminationCountMapper.bulkInsertGradeMedicalExaminationCount(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				gradeMedicalExaminationCountMapper.bulkInsertGradeMedicalExaminationCount_oracle(rows);
			} else {
				gradeMedicalExaminationCountMapper.bulkInsertGradeMedicalExaminationCount(rows);
			}
			rows.clear();
		}
	}

	public int count(GradeMedicalExaminationCountQuery query) {
		return gradeMedicalExaminationCountMapper.getGradeMedicalExaminationCountCount(query);
	}

	/**
	 * 根据checkId删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	public void deleteByCheckId(String checkId) {
		gradeMedicalExaminationCountMapper.deleteGradeMedicalExaminationCountByCheckId(checkId);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGradeMedicalExaminationCountCountByQueryCriteria(GradeMedicalExaminationCountQuery query) {
		return gradeMedicalExaminationCountMapper.getGradeMedicalExaminationCountCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GradeMedicalExaminationCount> getGradeMedicalExaminationCountsByQueryCriteria(int start, int pageSize,
			GradeMedicalExaminationCountQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<GradeMedicalExaminationCount> rows = sqlSessionTemplate.selectList("getGradeMedicalExaminationCounts",
				query, rowBounds);
		return rows;
	}

	public List<GradeMedicalExaminationCount> list(GradeMedicalExaminationCountQuery query) {
		List<GradeMedicalExaminationCount> list = gradeMedicalExaminationCountMapper
				.getGradeMedicalExaminationCounts(query);
		return list;
	}

	public List<GradeMedicalExaminationCount> getGradeMedicalExaminationCountList(String tenantId, String checkId) {
		GradeMedicalExaminationCountQuery query = new GradeMedicalExaminationCountQuery();
		query.tenantId(tenantId);
		query.checkId(checkId);
		List<GradeMedicalExaminationCount> list = gradeMedicalExaminationCountMapper
				.getGradeMedicalExaminationCounts(query);
		if (list != null && !list.isEmpty()) {
			GrowthRateCountQuery q1 = new GrowthRateCountQuery();
			q1.tenantId(tenantId);
			q1.checkId(checkId);
			List<GrowthRateCount> list1 = growthRateCountService.list(q1);

			PhysicalGrowthCountQuery q2 = new PhysicalGrowthCountQuery();
			q2.tenantId(tenantId);
			q2.checkId(checkId);
			List<PhysicalGrowthCount> list2 = physicalGrowthCountService.list(q2);

			for (GradeMedicalExaminationCount cnt : list) {
				if (list1 != null && !list1.isEmpty()) {
					for (GrowthRateCount c1 : list1) {
						if (StringUtils.equals(cnt.getGradeId(), c1.getGradeId())) {
							if (StringUtils.equals(c1.getType(), "both")) {
								cnt.setBothRate(c1);
							} else if (StringUtils.equals(c1.getType(), "height")) {
								cnt.setHeightRate(c1);
							} else if (StringUtils.equals(c1.getType(), "weight")) {
								cnt.setWeightRate(c1);
							}
						}
					}
				}

				if (list2 != null && !list2.isEmpty()) {
					for (PhysicalGrowthCount c2 : list2) {
						if (StringUtils.equals(cnt.getGradeId(), c2.getGradeId())) {
							if (StringUtils.equals(c2.getType(), "H/A")) {
								cnt.setHaCount(c2);
							} else if (StringUtils.equals(c2.getType(), "W/A")) {
								cnt.setWaCount(c2);
							} else if (StringUtils.equals(c2.getType(), "W/H")) {
								cnt.setWhCount(c2);
							}
						}
					}
				}
			}
		}
		return list;
	}

	@Transactional
	public void saveAll(String tenantId, String checkId, List<GradeMedicalExaminationCount> list) {
		growthRateCountService.deleteByCheckId(checkId);
		physicalGrowthCountService.deleteByCheckId(checkId);
		gradeMedicalExaminationCountMapper.deleteGradeMedicalExaminationCountByCheckId(checkId);
		if (list != null && !list.isEmpty()) {
			List<GrowthRateCount> rows1 = new ArrayList<GrowthRateCount>();
			List<PhysicalGrowthCount> rows2 = new ArrayList<PhysicalGrowthCount>();

			for (GradeMedicalExaminationCount cnt : list) {
				cnt.setCheckId(checkId);
				cnt.setTenantId(tenantId);
				if (cnt.getBothRate() != null) {
					cnt.getBothRate().setCheckId(checkId);
					cnt.getBothRate().setTenantId(tenantId);
					cnt.getBothRate().setType("both");
					rows1.add(cnt.getBothRate());
				}
				if (cnt.getHeightRate() != null) {
					cnt.getHeightRate().setCheckId(checkId);
					cnt.getHeightRate().setTenantId(tenantId);
					cnt.getHeightRate().setType("height");
					rows1.add(cnt.getHeightRate());
				}
				if (cnt.getWeightRate() != null) {
					cnt.getWeightRate().setCheckId(checkId);
					cnt.getWeightRate().setTenantId(tenantId);
					cnt.getWeightRate().setType("weight");
					rows1.add(cnt.getWeightRate());
				}
				if (cnt.getHaCount() != null) {
					cnt.getHaCount().setCheckId(checkId);
					cnt.getHaCount().setTenantId(tenantId);
					cnt.getHaCount().setType("H/A");
					rows2.add(cnt.getHaCount());
				}
				if (cnt.getWaCount() != null) {
					cnt.getWaCount().setCheckId(checkId);
					cnt.getWaCount().setTenantId(tenantId);
					cnt.getWaCount().setType("W/A");
					rows2.add(cnt.getWaCount());
				}
				if (cnt.getWhCount() != null) {
					cnt.getWhCount().setCheckId(checkId);
					cnt.getWhCount().setTenantId(tenantId);
					cnt.getWhCount().setType("W/H");
					rows2.add(cnt.getWhCount());
				}
			}

			growthRateCountService.bulkInsert(rows1);
			physicalGrowthCountService.bulkInsert(rows2);
			this.bulkInsert(list);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GradeMedicalExaminationCountMapper")
	public void setGradeMedicalExaminationCountMapper(
			GradeMedicalExaminationCountMapper gradeMedicalExaminationCountMapper) {
		this.gradeMedicalExaminationCountMapper = gradeMedicalExaminationCountMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.growthRateCountService")
	public void setGrowthRateCountService(GrowthRateCountService growthRateCountService) {
		this.growthRateCountService = growthRateCountService;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.physicalGrowthCountService")
	public void setPhysicalGrowthCountService(PhysicalGrowthCountService physicalGrowthCountService) {
		this.physicalGrowthCountService = physicalGrowthCountService;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
