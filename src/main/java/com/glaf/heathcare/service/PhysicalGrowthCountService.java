package com.glaf.heathcare.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;

@Transactional(readOnly = true)
public interface PhysicalGrowthCountService {

	@Transactional
	void bulkInsert(List<PhysicalGrowthCount> list);

	@Transactional
	void deleteByCheckId(String checkId);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getPhysicalGrowthCountCountByQueryCriteria(PhysicalGrowthCountQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<PhysicalGrowthCount> getPhysicalGrowthCountsByQueryCriteria(int start, int pageSize,
			PhysicalGrowthCountQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<PhysicalGrowthCount> list(PhysicalGrowthCountQuery query);

}
