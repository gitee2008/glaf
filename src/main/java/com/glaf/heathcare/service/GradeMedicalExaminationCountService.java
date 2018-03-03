package com.glaf.heathcare.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;

@Transactional(readOnly = true)
public interface GradeMedicalExaminationCountService {

	/**
	 * 根据checkId删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByCheckId(String checkId);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getGradeMedicalExaminationCountCountByQueryCriteria(GradeMedicalExaminationCountQuery query);

	/**
	 * 获取某个学校某次体检的汇总结果
	 * 
	 * @param tenantId
	 * @param checkId
	 * @return
	 */
	List<GradeMedicalExaminationCount> getGradeMedicalExaminationCountList(String tenantId, String checkId);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<GradeMedicalExaminationCount> getGradeMedicalExaminationCountsByQueryCriteria(int start, int pageSize,
			GradeMedicalExaminationCountQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<GradeMedicalExaminationCount> list(GradeMedicalExaminationCountQuery query);

	@Transactional
	void saveAll(String tenantId, String checkId, List<GradeMedicalExaminationCount> list);

}
