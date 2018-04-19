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

package com.glaf.base.modules.sys.web.springmvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.model.TenantConfig;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.base.modules.sys.service.TenantConfigService;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/tenant/config")
@RequestMapping("/tenant/config")
public class TenantConfigController {
	protected static final Log logger = LogFactory.getLog(TenantConfigController.class);

	protected DictoryService dictoryService;

	protected TenantConfigService tenantConfigService;

	protected ITableDataService tableDataService;

	public TenantConfigController() {

	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		TenantConfig tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
		if (tenantConfig != null) {
			request.setAttribute("tenantConfig", tenantConfig);
		}

		List<Dictory> dicts = dictoryService.getDictoryListByCategory("CAT_MEAL");
		request.setAttribute("dicts", dicts);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("tenantConfig.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/tenant/config/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveTenantConfig")
	public byte[] saveTenantConfig(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isTenantAdmin()) {
			String actorId = loginContext.getActorId();
			TenantConfig tenantConfig = new TenantConfig();
			try {
				tenantConfig = tenantConfigService.getTenantConfigByTenantId(loginContext.getTenantId());
				if (tenantConfig == null) {
					tenantConfig = new TenantConfig();
					tenantConfig.setTenantId(loginContext.getTenantId());
					tenantConfig.setCreateBy(actorId);
				}
 
				tenantConfig.setSysName(request.getParameter("sysName"));
				tenantConfig.setShareFlag(request.getParameter("shareFlag"));
				tenantConfig.setTypeId(RequestUtils.getLong(request, "typeId"));
				tenantConfig.setUpdateBy(actorId);

				this.tenantConfigService.save(tenantConfig);

				List<TableModel> rows = new ArrayList<TableModel>();

				TableModel table2 = new TableModel();
				table2.setTableName("HEALTH_DIETARY_TEMPLATE");
				table2.addStringColumn("SHAREFLAG_", tenantConfig.getShareFlag());

				ColumnModel idColumn = new ColumnModel();
				idColumn.setColumnName("TENANTID_");
				idColumn.setJavaType("String");
				idColumn.setValue(loginContext.getTenantId());
				table2.setIdColumn(idColumn);

				rows.add(table2);

				tableDataService.updateAllTableData(rows);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				//ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource
	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@javax.annotation.Resource
	public void setTenantConfigService(TenantConfigService tenantConfigService) {
		this.tenantConfigService = tenantConfigService;
	}

}
