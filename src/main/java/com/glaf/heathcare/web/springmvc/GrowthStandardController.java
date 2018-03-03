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

import java.io.BufferedReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;

import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.query.GrowthStandardQuery;
import com.glaf.heathcare.service.GrowthStandardService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/growthStandard")
@RequestMapping("/heathcare/growthStandard")
public class GrowthStandardController {
	protected static final Log logger = LogFactory.getLog(GrowthStandardController.class);

	protected GrowthStandardService growthStandardService;

	public GrowthStandardController() {

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
					GrowthStandard growthStandard = growthStandardService.getGrowthStandard(Long.valueOf(x));
					if (growthStandard != null
							&& (StringUtils.equals(growthStandard.getCreateBy(), loginContext.getActorId())
									|| loginContext.isSystemAdministrator())) {
						growthStandardService.deleteById(growthStandard.getId());
					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			GrowthStandard growthStandard = growthStandardService.getGrowthStandard(Long.valueOf(id));
			if (growthStandard != null && (StringUtils.equals(growthStandard.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {
				growthStandardService.deleteById(growthStandard.getId());
				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		GrowthStandard growthStandard = growthStandardService.getGrowthStandard(RequestUtils.getLong(request, "id"));
		if (growthStandard != null) {
			request.setAttribute("growthStandard", growthStandard);
		}

		request.setAttribute("can_write", false);

		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("can_write", true);
		}

		List<Integer> years = new ArrayList<Integer>();
		List<Integer> months = new ArrayList<Integer>();

		for (int i = 0; i <= 7; i++) {
			years.add(i);
		}

		for (int i = 0; i <= 11; i++) {
			months.add(i);
		}

		request.setAttribute("years", years);
		request.setAttribute("months", months);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("growthStandard.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/growthStandard/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/importData")
	public byte[] importData(HttpServletRequest request) throws IOException {
		boolean status = false;
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			try {
				MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> fileMap = req.getFileMap();
				Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
				int maxSize = 5 * FileUtils.MB_SIZE;
				for (Entry<String, MultipartFile> entry : entrySet) {
					MultipartFile mFile = entry.getValue();
					if (mFile.getOriginalFilename() != null && mFile.getSize() > 0 && mFile.getSize() < maxSize) {
						String standardType = req.getParameter("standardType");
						String type = req.getParameter("type");
						String sex = req.getParameter("sex");
						BufferedReader input = new BufferedReader(new StringReader(new String(mFile.getBytes())));
						Map<Integer, GrowthStandard> dataMap = new HashMap<Integer, GrowthStandard>();
						List<GrowthStandard> rows = new ArrayList<GrowthStandard>();

						int index = 0;
						int sort = 0;
						String item = null;
						String line = null;
						StringTokenizer token = null;

						if (StringUtils.equals(type, "2") || StringUtils.equals(type, "3")) {
							while ((line = input.readLine()) != null) {
								if (index > 0) {
									sort = 0;
									GrowthStandard std = new GrowthStandard();
									std.setCreateBy(loginContext.getActorId());
									token = new StringTokenizer(line);
									while (token.hasMoreTokens()) {
										item = token.nextToken();
										switch (sort) {
										case 0:
											dataMap.put(Integer.parseInt(item), std);
											break;
										case 1:
											std.setNegativeFourDSDeviation(Double.parseDouble(item));
											break;
										case 2:
											std.setNegativeThreeDSDeviation(Double.parseDouble(item));
											break;
										case 3:
											std.setNegativeTwoDSDeviation(Double.parseDouble(item));
											break;
										case 4:
											std.setNegativeOneDSDeviation(Double.parseDouble(item));
											break;
										case 5:
											std.setMedian(Double.parseDouble(item));
											break;
										case 6:
											std.setOneDSDeviation(Double.parseDouble(item));
											break;
										case 7:
											std.setTwoDSDeviation(Double.parseDouble(item));
											break;
										case 8:
											std.setThreeDSDeviation(Double.parseDouble(item));
											break;
										case 9:
											std.setFourDSDeviation(Double.parseDouble(item));
											break;
										default:
											break;
										}
										sort++;
									}
								}
								index++;
							}

							index = 0;
							for (int moon = 0; moon <= 72; moon++) {
								if (moon > 0 && moon % 3 == 0) {
									index++;
								}
								if (moon > 0 && moon % 12 == 0) {
									index++;
								}
								if (moon > 0) {
									GrowthStandard std = dataMap.get(moon * 30 + index);
									if (std != null) {
										std.setAge(moon / 12);
										std.setAgeOfTheMoon(moon);
										rows.add(std);
									}
								} else {
									GrowthStandard std = dataMap.get(0);
									if (std != null) {
										std.setAge(0);
										std.setAgeOfTheMoon(0);
										rows.add(std);
									}
								}
							}
							growthStandardService.saveAll(type, sex, standardType, rows);
						} else if (StringUtils.equals(type, "4")) {
							while ((line = input.readLine()) != null) {
								if (index > 0) {
									sort = 0;
									GrowthStandard std = new GrowthStandard();
									std.setCreateBy(loginContext.getActorId());
									token = new StringTokenizer(line);
									while (token.hasMoreTokens()) {
										item = token.nextToken();
										switch (sort) {
										case 0:
											std.setHeight(Double.parseDouble(item));
											break;
										case 1:
											std.setNegativeFourDSDeviation(Double.parseDouble(item));
											break;
										case 2:
											std.setNegativeThreeDSDeviation(Double.parseDouble(item));
											break;
										case 3:
											std.setNegativeTwoDSDeviation(Double.parseDouble(item));
											break;
										case 4:
											std.setNegativeOneDSDeviation(Double.parseDouble(item));
											break;
										case 5:
											std.setMedian(Double.parseDouble(item));
											break;
										case 6:
											std.setOneDSDeviation(Double.parseDouble(item));
											break;
										case 7:
											std.setTwoDSDeviation(Double.parseDouble(item));
											break;
										case 8:
											std.setThreeDSDeviation(Double.parseDouble(item));
											break;
										case 9:
											std.setFourDSDeviation(Double.parseDouble(item));
											break;
										default:
											break;
										}
										sort++;
									}
									rows.add(std);
								}
								index++;
							}
							growthStandardService.saveAll(type, sex, standardType, rows);
						}
					}
				}
				status = true;
			} catch (Exception ex) {
				status = false;
				logger.error("error import data", ex);
			}
		}
		return ResponseUtils.responseResult(status);
	}

	@ResponseBody
	@RequestMapping("/importData2")
	public byte[] importData2(HttpServletRequest request) throws IOException {
		boolean status = false;
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			try {
				MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> fileMap = req.getFileMap();
				Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
				int maxSize = 5 * FileUtils.MB_SIZE;
				for (Entry<String, MultipartFile> entry : entrySet) {
					MultipartFile mFile = entry.getValue();
					if (mFile.getOriginalFilename() != null && mFile.getSize() > 0 && mFile.getSize() < maxSize) {
						String standardType = req.getParameter("standardType");
						String type = req.getParameter("type");
						String sex = req.getParameter("sex");
						BufferedReader input = new BufferedReader(new StringReader(new String(mFile.getBytes())));
						List<GrowthStandard> rows = new ArrayList<GrowthStandard>();

						int index = 0;
						int sort = 0;
						String item = null;
						String line = null;
						StringTokenizer token = null;
						if (StringUtils.equals(type, "2") || StringUtils.equals(type, "3")
								|| StringUtils.equals(type, "5")) {
							while ((line = input.readLine()) != null) {
								if (index > 0) {
									sort = 0;
									GrowthStandard std = new GrowthStandard();
									std.setCreateBy(loginContext.getActorId());
									token = new StringTokenizer(line);
									while (token.hasMoreTokens()) {
										item = token.nextToken();
										switch (sort) {
										case 0:
											std.setAgeOfTheMoon(Integer.parseInt(item));
											break;
										case 1:
											std.setNegativeThreeDSDeviation(Double.parseDouble(item));
											break;
										case 2:
											std.setNegativeTwoDSDeviation(Double.parseDouble(item));
											break;
										case 3:
											std.setNegativeOneDSDeviation(Double.parseDouble(item));
											break;
										case 4:
											std.setMedian(Double.parseDouble(item));
											break;
										case 5:
											std.setOneDSDeviation(Double.parseDouble(item));
											break;
										case 6:
											std.setTwoDSDeviation(Double.parseDouble(item));
											break;
										case 7:
											std.setThreeDSDeviation(Double.parseDouble(item));
											break;
										default:
											break;
										}
										sort++;
									}
									rows.add(std);
								}
								index++;
							}
						} else if (StringUtils.equals(type, "4")) {
							while ((line = input.readLine()) != null) {
								if (index > 0) {
									sort = 0;
									GrowthStandard std = new GrowthStandard();
									std.setCreateBy(loginContext.getActorId());
									token = new StringTokenizer(line);
									while (token.hasMoreTokens()) {
										item = token.nextToken();
										switch (sort) {
										case 0:
											std.setHeight(Double.parseDouble(item));
											break;
										case 1:
											std.setNegativeThreeDSDeviation(Double.parseDouble(item));
											break;
										case 2:
											std.setNegativeTwoDSDeviation(Double.parseDouble(item));
											break;
										case 3:
											std.setNegativeOneDSDeviation(Double.parseDouble(item));
											break;
										case 4:
											std.setMedian(Double.parseDouble(item));
											break;
										case 5:
											std.setOneDSDeviation(Double.parseDouble(item));
											break;
										case 6:
											std.setTwoDSDeviation(Double.parseDouble(item));
											break;
										case 7:
											std.setThreeDSDeviation(Double.parseDouble(item));
											break;
										default:
											break;
										}
										sort++;
									}
									rows.add(std);
								}
								index++;
							}
						}
						if (rows.size() > 0) {
							growthStandardService.saveAll(type, sex, standardType, rows);
						}
					}
				}
				status = true;
			} catch (Exception ex) {
				status = false;
				logger.error("error import data", ex);
			}
		}
		return ResponseUtils.responseResult(status);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		GrowthStandardQuery query = new GrowthStandardQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;
		orderName = ParamUtils.getString(params, "sortName");
		order = ParamUtils.getString(params, "sortOrder");

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = growthStandardService.getGrowthStandardCountByQueryCriteria(query);
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

			List<GrowthStandard> list = growthStandardService.getGrowthStandardsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (GrowthStandard growthStandard : list) {
					JSONObject rowJSON = growthStandard.toJsonObject();
					rowJSON.put("id", growthStandard.getId());
					rowJSON.put("rowId", growthStandard.getId());
					rowJSON.put("growthStandardId", growthStandard.getId());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}

			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		request.setAttribute("can_write", false);

		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("can_write", true);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/growthStandard/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("growthStandard.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/heathcare/growthStandard/query", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveGrowthStandard")
	public byte[] saveGrowthStandard(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		if (loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			Map<String, Object> params = RequestUtils.getParameterMap(request);
			GrowthStandard growthStandard = new GrowthStandard();
			try {
				Tools.populate(growthStandard, params);
				growthStandard.setAge(RequestUtils.getInt(request, "age"));
				growthStandard.setMonth(RequestUtils.getInt(request, "month"));
				growthStandard.setSex(request.getParameter("sex"));
				growthStandard.setHeight(RequestUtils.getDouble(request, "height"));
				growthStandard.setWeight(RequestUtils.getDouble(request, "weight"));
				growthStandard.setPercent3(RequestUtils.getDouble(request, "percent3"));
				growthStandard.setPercent15(RequestUtils.getDouble(request, "percent15"));
				growthStandard.setPercent50(RequestUtils.getDouble(request, "percent50"));
				growthStandard.setPercent85(RequestUtils.getDouble(request, "percent85"));
				growthStandard.setPercent97(RequestUtils.getDouble(request, "percent97"));
				growthStandard.setOneDSDeviation(RequestUtils.getDouble(request, "oneDSDeviation"));
				growthStandard.setTwoDSDeviation(RequestUtils.getDouble(request, "twoDSDeviation"));
				growthStandard.setThreeDSDeviation(RequestUtils.getDouble(request, "threeDSDeviation"));
				growthStandard.setFourDSDeviation(RequestUtils.getDouble(request, "fourDSDeviation"));
				growthStandard.setMedian(RequestUtils.getDouble(request, "median"));
				growthStandard.setNegativeOneDSDeviation(RequestUtils.getDouble(request, "negativeOneDSDeviation"));
				growthStandard.setNegativeTwoDSDeviation(RequestUtils.getDouble(request, "negativeTwoDSDeviation"));
				growthStandard.setNegativeThreeDSDeviation(RequestUtils.getDouble(request, "negativeThreeDSDeviation"));
				growthStandard.setNegativeFourDSDeviation(RequestUtils.getDouble(request, "negativeFourDSDeviation"));
				growthStandard.setStandardType(request.getParameter("standardType"));
				growthStandard.setType(request.getParameter("type"));
				growthStandard.setCreateBy(actorId);
				this.growthStandardService.save(growthStandard);

				return ResponseUtils.responseJsonResult(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.growthStandardService")
	public void setGrowthStandardService(GrowthStandardService growthStandardService) {
		this.growthStandardService = growthStandardService;
	}

	@RequestMapping("/showImport")
	public ModelAndView showImport(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		request.setAttribute("can_write", false);

		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("can_write", true);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("growthStandard.showImport");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/growthStandard/showImport", modelMap);
	}

	@RequestMapping("/showImport2")
	public ModelAndView showImport2(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		request.setAttribute("can_write", false);

		if (loginContext.isSystemAdministrator()) {
			request.setAttribute("can_write", true);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("growthStandard.showImport2");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/heathcare/growthStandard/showImport2", modelMap);
	}

	@RequestMapping("/view")
	public ModelAndView view(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		GrowthStandard growthStandard = growthStandardService.getGrowthStandard(RequestUtils.getLong(request, "id"));
		request.setAttribute("growthStandard", growthStandard);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view);
		}

		String x_view = ViewProperties.getString("growthStandard.view");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view);
		}

		return new ModelAndView("/heathcare/growthStandard/view");
	}

}
