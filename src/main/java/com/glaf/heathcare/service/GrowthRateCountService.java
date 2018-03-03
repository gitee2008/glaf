package com.glaf.heathcare.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;

@Transactional(readOnly = true)
public interface GrowthRateCountService {

	@Transactional
	void bulkInsert(List<GrowthRateCount> list);

	@Transactional
	void deleteByCheckId(String checkId);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getGrowthRateCountCountByQueryCriteria(GrowthRateCountQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<GrowthRateCount> getGrowthRateCountsByQueryCriteria(int start, int pageSize, GrowthRateCountQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<GrowthRateCount> list(GrowthRateCountQuery query);

}
