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

package com.glaf.heathcare.web.springmvc;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.domain.Dietary;
import com.glaf.heathcare.domain.DietaryItem;
import com.glaf.heathcare.domain.DietaryTemplate;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.query.DietaryItemQuery;
import com.glaf.heathcare.service.DietaryItemService;
import com.glaf.heathcare.service.DietaryService;
import com.glaf.heathcare.service.DietaryTemplateService;
import com.glaf.heathcare.service.FoodCompositionService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/dietaryItem")
@RequestMapping("/heathcare/dietaryItem")
public class DietaryItemController {
	protected static final Log logger = LogFactory.getLog(DietaryItemController.class);

	protected DietaryService dietaryService;

	protected DietaryItemService dietaryItemService;

	protected DietaryTemplateService dietaryTemplateService;

	protected FoodCompositionService foodCompositionService;

	protected SysTreeService sysTreeService;

	public DietaryItemController() {

	}

	@RequestMapping("/dataEdit")
	public ModelAndView dataEdit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.setAttribute("canEdit", true);
		long dietaryId = 0;
		DietaryItem dietaryItem = null;
		long id = RequestUtils.getLong(request, "id");

		if (id > 0) {
			dietaryItem = dietaryItemService.getDietaryItem(loginContext.getTenantId(), id);
		}

		if (dietaryItem != null) {
			request.setAttribute("canEdit", false);
			request.setAttribute("dietaryItem", dietaryItem);
			dietaryId = dietaryItem.getDietaryId();

			if (StringUtils.equals(dietaryItem.getCreateBy(), loginContext.getActorId())) {
				request.setAttribute("canEdit", true);
			}

			if (dietaryItem.getFoodId() > 0) {
				FoodComposition foodComposition = foodCompositionService.getFoodComposition(dietaryItem.getFoodId());
				if (foodComposition != null) {
					List<FoodComposition> foods = foodCompositionService
							.getFoodCompositions(foodComposition.getNodeId());
					request.setAttribute("foods", foods);
					request.setAttribute("nodeId", foodComposition.getNodeId());
				}
			}
		}

		if (dietaryId > 0) {
			Dietary dietary = dietaryService.getDietary(loginContext.getTenantId(), dietaryId);
			if (dietary != null) {
				logger.debug("purchaseFlag:" + dietary.getPurchaseFlag());
				request.setAttribute("dietary", dietary);
				request.setAttribute("templateId", dietary.getTemplateId());
				if (StringUtils.equals(dietary.getPurchaseFlag(), "Y")) {
					request.setAttribute("canEdit", false);
				}
			}
		}

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			request.setAttribute("foodCategories", foodCategories);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryItem.dataEdit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryItem/dataEdit", modelMap);
	}

	@RequestMapping("/datalist")
	public ModelAndView datalist(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		request.setAttribute("canEdit", true);

		LoginContext loginContext = RequestUtils.getLoginContext(request);

		long dietaryId = RequestUtils.getLong(request, "dietaryId");
		if (dietaryId > 0) {
			Dietary dietary = dietaryService.getDietary(loginContext.getTenantId(), dietaryId);
			if (dietary != null) {
				logger.debug("purchaseFlag:" + dietary.getPurchaseFlag());
				request.setAttribute("dietary", dietary);
				request.setAttribute("templateId", dietary.getTemplateId());
				if (StringUtils.equals(dietary.getPurchaseFlag(), "Y")) {
					request.setAttribute("canEdit", false);
				}
			}
		}

		long templateId = RequestUtils.getLong(request, "templateId");
		if (templateId > 0) {
			DietaryTemplate tpl = dietaryTemplateService.getDietaryTemplate(templateId);
			if (tpl != null && !StringUtils.equals(tpl.getCreateBy(), loginContext.getActorId())) {
				request.setAttribute("canEdit", false);
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryItem/datalist", modelMap);
	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					DietaryItem dietaryItem = dietaryItemService.getDietaryItem(loginContext.getTenantId(),
							Long.valueOf(x));
					if (dietaryItem != null) {
						if ((StringUtils.equals(dietaryItem.getCreateBy(), loginContext.getActorId())
								|| (StringUtils.equals(dietaryItem.getTenantId(), loginContext.getTenantId())
										&& (loginContext.getRoles().contains("TenantAdmin")
												|| loginContext.getRoles().contains("HealthPhysician"))))) {
							if (dietaryItem.getDietaryId() > 0) {
								Dietary dietary = dietaryService.getDietary(loginContext.getTenantId(),
										dietaryItem.getDietaryId());
								if (dietary != null) {
									request.setAttribute("templateId", dietary.getTemplateId());
									if (StringUtils.equals(dietary.getPurchaseFlag(), "Y")) {
										return ResponseUtils.responseJsonResult(false, "已经生成采购单的食谱不能删除明细项。");
									}
								}
							}
							dietaryItemService.deleteById(loginContext.getTenantId(), dietaryItem.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			DietaryItem dietaryItem = dietaryItemService.getDietaryItem(loginContext.getTenantId(), Long.valueOf(id));
			if (dietaryItem != null) {
				if ((StringUtils.equals(dietaryItem.getCreateBy(), loginContext.getActorId())
						|| (StringUtils.equals(dietaryItem.getTenantId(), loginContext.getTenantId())
								&& (loginContext.getRoles().contains("TenantAdmin")
										|| loginContext.getRoles().contains("HealthPhysician"))))) {
					if (dietaryItem.getDietaryId() > 0) {
						Dietary dietary = dietaryService.getDietary(loginContext.getTenantId(),
								dietaryItem.getDietaryId());
						if (dietary != null) {
							request.setAttribute("templateId", dietary.getTemplateId());
							if (StringUtils.equals(dietary.getPurchaseFlag(), "Y")) {
								return ResponseUtils.responseJsonResult(false, "已经生成采购单的食谱不能删除明细项。");
							}
						}
					}
					dietaryItemService.deleteById(loginContext.getTenantId(), dietaryItem.getId());
					return ResponseUtils.responseResult(true);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@ResponseBody
	@RequestMapping("/deleteTemplateItem")
	public byte[] deleteTemplateItem(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Long id = RequestUtils.getLong(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					DietaryItem dietaryItem = dietaryItemService.getDietaryItem(Long.valueOf(x));
					if (dietaryItem != null) {
						if (loginContext.isSystemAdministrator()
								|| (StringUtils.equals(dietaryItem.getCreateBy(), loginContext.getActorId()))
								|| (StringUtils.equals(dietaryItem.getTenantId(), loginContext.getTenantId())
										&& (loginContext.getRoles().contains("TenantAdmin")
												|| loginContext.getRoles().contains("HealthPhysician")))) {
							dietaryItemService.deleteById(dietaryItem.getId());
						}
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			DietaryItem dietaryItem = dietaryItemService.getDietaryItem(Long.valueOf(id));
			if (dietaryItem != null) {
				if (loginContext.isSystemAdministrator()
						|| (StringUtils.equals(dietaryItem.getCreateBy(), loginContext.getActorId()))
						|| (StringUtils.equals(dietaryItem.getTenantId(), loginContext.getTenantId())
								&& (loginContext.getRoles().contains("TenantAdmin")
										|| loginContext.getRoles().contains("HealthPhysician")))) {
					dietaryItemService.deleteById(dietaryItem.getId());
					return ResponseUtils.responseResult(true);
				}
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		logger.debug("params:" + RequestUtils.getParameterMap(request));
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		request.setAttribute("canEdit", true);
		long templateId = 0;
		DietaryItem dietaryItem = dietaryItemService.getDietaryItem(RequestUtils.getLong(request, "id"));
		if (dietaryItem != null) {
			if (!StringUtils.equals(dietaryItem.getCreateBy(), loginContext.getActorId())) {
				request.setAttribute("canEdit", false);
			}
			templateId = dietaryItem.getTemplateId();
			request.setAttribute("dietaryItem", dietaryItem);
			if (dietaryItem.getFoodId() > 0) {
				FoodComposition foodComposition = foodCompositionService.getFoodComposition(dietaryItem.getFoodId());
				if (foodComposition != null) {
					List<FoodComposition> foods = foodCompositionService
							.getFoodCompositions(foodComposition.getNodeId());
					request.setAttribute("foods", foods);
					request.setAttribute("nodeId", foodComposition.getNodeId());
				}
			}
		}

		if (templateId > 0) {
			DietaryTemplate dietaryTemplate = dietaryTemplateService.getDietaryTemplate(templateId);
			if (dietaryTemplate != null) {
				if (!StringUtils.equals(dietaryTemplate.getCreateBy(), loginContext.getActorId())) {
					request.setAttribute("canEdit", false);
				}
			}
		}

		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			request.setAttribute("foodCategories", foodCategories);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("dietaryItem.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryItem/edit", modelMap);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		logger.debug("params:" + params);
		JSONObject result = new JSONObject();
		long dietaryId = RequestUtils.getLong(request, "dietaryId");
		long templateId = RequestUtils.getLong(request, "templateId");
		if (templateId <= 0 && dietaryId <= 0) {
			return result.toJSONString().getBytes("UTF-8");
		}

		DietaryItemQuery query = new DietaryItemQuery();

		if (dietaryId > 0) {
			query.tenantId(loginContext.getTenantId());
			query.dietaryId(dietaryId);
		} else {
			if (templateId > 0) {
				query.templateId(templateId);
			}
		}

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;
		orderName = ParamUtils.getString(params, "sortName");
		order = ParamUtils.getString(params, "sortOrder");

		int total = dietaryItemService.getDietaryItemCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortOrder(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<DietaryItem> list = dietaryItemService.list(query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (DietaryItem dietaryItem : list) {
					JSONObject rowJSON = dietaryItem.toJsonObject();
					rowJSON.put("id", dietaryItem.getId());
					rowJSON.put("itemId", dietaryItem.getId());
					rowJSON.put("dietaryItemId", dietaryItem.getId());
					rowJSON.put("startIndex", ++start);
					if (dietaryItem.getUnit() != null) {
						switch (dietaryItem.getUnit()) {
						case "G":
							rowJSON.put("unitName", "克");
							break;
						case "KG":
							rowJSON.put("unitName", "千克");
							break;
						case "ML":
							rowJSON.put("unitName", "毫升");
							break;
						case "L":
							rowJSON.put("unitName", "升");
							break;
						default:
							rowJSON.put("unitName", "克");
							break;
						}
					} else {
						rowJSON.put("unitName", "克");
					}
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		// logger.debug(result.toJSONString());
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		long templateId = RequestUtils.getLong(request, "templateId");
		if (templateId > 0) {
			DietaryTemplate dietaryTemplate = dietaryTemplateService.getDietaryTemplate(templateId);
			if (dietaryTemplate != null) {
				request.setAttribute("dietaryTemplate", dietaryTemplate);
				if (StringUtils.equals(dietaryTemplate.getCreateBy(), loginContext.getActorId())) {
					request.setAttribute("canEdit", true);
				}
				if (loginContext.isSystemAdministrator()) {
					request.setAttribute("canEditDietaryName", true);
				} else {
					if (StringUtils.equals(dietaryTemplate.getCreateBy(), loginContext.getActorId())) {
						if (StringUtils.equals(dietaryTemplate.getSysFlag(), "N")
								&& StringUtils.isNotEmpty(dietaryTemplate.getTenantId())) {
							request.setAttribute("canEditDietaryName", true);
						}
					}
				}
			}
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/dietaryItem/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("dietaryItem.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/dietaryItem/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveDietaryItem")
	public byte[] saveDietaryItem(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HealthPhysician和TenantAdmin可以增加食谱
		 */
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("HealthPhysician")
				|| loginContext.getRoles().contains("TenantAdmin")) {
			String actorId = loginContext.getActorId();
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			logger.debug("params:" + params);
			long id = RequestUtils.getLong(request, "id");
			DietaryItem dietaryItem = null;
			Dietary dietary = null;
			try {
				if (id > 0) {
					dietaryItem = dietaryItemService.getDietaryItem(loginContext.getTenantId(), id);
				}
				if (dietaryItem == null) {
					dietaryItem = new DietaryItem();
					Tools.populate(dietaryItem, params);
					dietaryItem.setCreateBy(actorId);
					dietaryItem.setTemplateId(RequestUtils.getLong(request, "templateId"));

					if (!loginContext.isSystemAdministrator()) {
						dietaryItem.setTenantId(loginContext.getTenantId());
					}
				} else {
					if (!loginContext.isSystemAdministrator()) {
						if (!StringUtils.equals(loginContext.getTenantId(), dietaryItem.getTenantId())) {
							return ResponseUtils.responseJsonResult(false, "食谱数据只能所属机构修改。");
						}
					}

					if (dietaryItem.getDietaryId() > 0) {
						dietary = dietaryService.getDietary(loginContext.getTenantId(), dietaryItem.getDietaryId());
						if (dietary != null) {
							request.setAttribute("templateId", dietary.getTemplateId());
							if (StringUtils.equals(dietary.getPurchaseFlag(), "Y")) {
								return ResponseUtils.responseJsonResult(false, "已经生成采购单的食谱不能修改明细项。");
							}
						}
					}

					Tools.populate(dietaryItem, params);
				}

				dietaryItem.setName(request.getParameter("name"));
				dietaryItem.setDescription(request.getParameter("description"));
				dietaryItem.setFoodId(RequestUtils.getLong(request, "foodId"));
				dietaryItem.setFoodName(request.getParameter("foodName"));
				dietaryItem.setQuantity(RequestUtils.getDouble(request, "quantity"));
				dietaryItem.setUnit(request.getParameter("unit"));
				if (dietary != null) {
					dietaryItem.setFullDay(dietary.getFullDay());
				}

				if (dietaryItem.getDietaryId() > 0) {
					this.dietaryItemService.save(dietaryItem);
					if (dietaryItem.getDietaryId() > 0) {
						// DietaryBean bean = new DietaryBean();
						// bean.calculate(loginContext.getTenantId(), dietaryItem.getDietaryId());
						this.dietaryService.calculate(loginContext.getTenantId(), dietaryItem.getDietaryId());
					}
				} else {
					this.dietaryItemService.saveTemplateItem(dietaryItem);
					if (dietaryItem.getTemplateId() > 0) {
						// DietaryTemplateBean bean = new DietaryTemplateBean();
						// bean.calculate(dietaryItem.getTemplateId());
						dietaryTemplateService.calculate(dietaryItem.getTemplateId());
					}
				}

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/saveTemplateItem")
	public byte[] saveTemplateItem(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		/**
		 * 角色HealthPhysician和TenantAdmin可以增加食谱
		 */
		if (loginContext.isSystemAdministrator() || loginContext.getRoles().contains("HealthPhysician")
				|| loginContext.getRoles().contains("TenantAdmin")) {
			String actorId = loginContext.getActorId();
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			logger.debug("params:" + params);
			long id = RequestUtils.getLong(request, "id");
			DietaryItem dietaryItem = null;
			Dietary dietary = null;
			try {
				if (id > 0) {
					dietaryItem = dietaryItemService.getDietaryItem(id);
				}
				if (dietaryItem == null) {
					dietaryItem = new DietaryItem();
					Tools.populate(dietaryItem, params);
					dietaryItem.setCreateBy(actorId);
					dietaryItem.setTemplateId(RequestUtils.getLong(request, "templateId"));

					if (!loginContext.isSystemAdministrator()) {
						dietaryItem.setTenantId(loginContext.getTenantId());
					}
				} else {
					if (!loginContext.isSystemAdministrator()) {
						if (!StringUtils.equals(loginContext.getTenantId(), dietaryItem.getTenantId())) {
							return ResponseUtils.responseJsonResult(false, "食谱数据只能所属机构修改。");
						}
					}

					if (dietaryItem.getDietaryId() > 0) {
						dietary = dietaryService.getDietary(loginContext.getTenantId(), dietaryItem.getDietaryId());
						if (dietary != null) {
							request.setAttribute("templateId", dietary.getTemplateId());
							if (StringUtils.equals(dietary.getPurchaseFlag(), "Y")) {
								return ResponseUtils.responseJsonResult(false, "已经生成采购单的食谱不能修改明细项。");
							}
						}
					}

					Tools.populate(dietaryItem, params);
				}

				dietaryItem.setName(request.getParameter("name"));
				dietaryItem.setDescription(request.getParameter("description"));
				dietaryItem.setFoodId(RequestUtils.getLong(request, "foodId"));
				dietaryItem.setFoodName(request.getParameter("foodName"));
				dietaryItem.setQuantity(RequestUtils.getDouble(request, "quantity"));
				dietaryItem.setUnit(request.getParameter("unit"));
				if (dietary != null) {
					dietaryItem.setFullDay(dietary.getFullDay());
				}

				this.dietaryItemService.saveTemplateItem(dietaryItem);

				if (dietaryItem.getTemplateId() > 0) {
					// DietaryTemplateBean bean = new DietaryTemplateBean();
					// bean.calculate(dietaryItem.getTemplateId());
					dietaryTemplateService.calculate(dietaryItem.getTemplateId());
				}

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryItemService")
	public void setDietaryItemService(DietaryItemService dietaryItemService) {
		this.dietaryItemService = dietaryItemService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryService")
	public void setDietaryService(DietaryService dietaryService) {
		this.dietaryService = dietaryService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.dietaryTemplateService")
	public void setDietaryTemplateService(DietaryTemplateService dietaryTemplateService) {
		this.dietaryTemplateService = dietaryTemplateService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.foodCompositionService")
	public void setFoodCompositionService(FoodCompositionService foodCompositionService) {
		this.foodCompositionService = foodCompositionService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@RequestMapping("/templateJson")
	@ResponseBody
	public byte[] templateJson(HttpServletRequest request) throws IOException {
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		JSONObject result = new JSONObject();

		long templateId = RequestUtils.getLong(request, "templateId");
		if (templateId <= 0) {
			return result.toJSONString().getBytes("UTF-8");
		}

		DietaryItemQuery query = new DietaryItemQuery();

		if (templateId > 0) {
			query.templateId(templateId);
		}

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;
		orderName = ParamUtils.getString(params, "sortName");
		order = ParamUtils.getString(params, "sortOrder");

		int total = dietaryItemService.getDietaryTemplateItemCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);

			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortOrder(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}

			List<DietaryItem> list = dietaryItemService.list(query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (DietaryItem dietaryItem : list) {
					JSONObject rowJSON = dietaryItem.toJsonObject();
					rowJSON.put("id", dietaryItem.getId());
					rowJSON.put("itemId", dietaryItem.getId());
					rowJSON.put("dietaryItemId", dietaryItem.getId());
					rowJSON.put("startIndex", ++start);
					if (dietaryItem.getUnit() != null) {
						switch (dietaryItem.getUnit()) {
						case "G":
							rowJSON.put("unitName", "克");
							break;
						case "KG":
							rowJSON.put("unitName", "千克");
							break;
						case "ML":
							rowJSON.put("unitName", "毫升");
							break;
						case "L":
							rowJSON.put("unitName", "升");
							break;
						default:
							rowJSON.put("unitName", "克");
							break;
						}
					} else {
						rowJSON.put("unitName", "克");
					}
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		// logger.debug(result.toJSONString());
		return result.toJSONString().getBytes("UTF-8");
	}

	@ResponseBody
	@RequestMapping("/updateQuantity")
	public byte[] updateQuantity(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		long id = RequestUtils.getLong(request, "id");
		double quantity = RequestUtils.getDouble(request, "quantity");
		DietaryItem dietaryItem = null;
		if (id > 0 && quantity > 0) {
			/**
			 * 角色HealthPhysician和TenantAdmin可以修改食谱
			 */
			if (loginContext.getRoles().contains("HealthPhysician")
					|| loginContext.getRoles().contains("TenantAdmin")) {
				dietaryItem = dietaryItemService.getDietaryItem(loginContext.getTenantId(), id);
				if (dietaryItem != null && StringUtils.equals(dietaryItem.getCreateBy(), loginContext.getActorId())) {
					dietaryItem.setQuantity(quantity);
					dietaryItemService.save(dietaryItem);
					return ResponseUtils.responseJsonResult(true);
				}
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/updateTemplateQuantity")
	public byte[] updateTemplateQuantity(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		long id = RequestUtils.getLong(request, "id");
		double quantity = RequestUtils.getDouble(request, "quantity");
		DietaryItem dietaryItem = null;
		if (id > 0 && quantity > 0) {
			if (loginContext.isSystemAdministrator()) {
				dietaryItem = dietaryItemService.getDietaryItem(id);
				if (dietaryItem != null) {
					dietaryItem.setQuantity(quantity);
					dietaryItemService.saveTemplateItem(dietaryItem);
					dietaryTemplateService.calculate(dietaryItem.getTemplateId());
					return ResponseUtils.responseJsonResult(true);
				}
			} else {
				/**
				 * 角色HealthPhysician和TenantAdmin可以修改食谱
				 */
				if (loginContext.getRoles().contains("HealthPhysician")
						|| loginContext.getRoles().contains("TenantAdmin")) {
					dietaryItem = dietaryItemService.getDietaryItem(id);
					if (dietaryItem != null
							&& StringUtils.equals(dietaryItem.getCreateBy(), loginContext.getActorId())) {
						dietaryItem.setQuantity(quantity);
						dietaryItemService.saveTemplateItem(dietaryItem);
						dietaryTemplateService.calculate(dietaryItem.getTemplateId());
						return ResponseUtils.responseJsonResult(true);
					}
				}
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

}
