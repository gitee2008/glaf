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

package com.glaf.base.district.web.springmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.glaf.base.district.domain.District;
import com.glaf.base.district.service.DistrictService;

import com.glaf.core.base.BaseTree;
import com.glaf.core.base.TreeModel;
import com.glaf.core.tree.helper.TreeHelper;
import com.glaf.core.util.RequestUtils;

@Controller("/district")
@RequestMapping("/district")
public class DistrictController {
	protected static final Log logger = LogFactory.getLog(DistrictController.class);

	protected DistrictService districtService;

	public DistrictController() {

	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		String name = request.getParameter("name");
		long parentId = RequestUtils.getLong(request, "parentId");
		JSONArray result = new JSONArray();
		List<District> list = null;
		if (parentId == 0 && StringUtils.isNotEmpty(name)) {
			District district = districtService.getDistrictByName(name);
			if (district != null) {
				parentId = district.getId();
			}
		}
		list = districtService.getDistrictList(parentId);
		if (list != null && !list.isEmpty()) {
			for (District district : list) {
				JSONObject rowJSON = district.toJsonObject();
				rowJSON.put("id", district.getId());
				rowJSON.put("pId", district.getParentId());
				rowJSON.put("districtId", district.getId());
				result.add(rowJSON);
			}
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/district/list", modelMap);
	}

	@javax.annotation.Resource
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@ResponseBody
	@RequestMapping("/treeJson")
	public byte[] treeJson(HttpServletRequest request) throws IOException {
		logger.debug("params:" + RequestUtils.getParameterMap(request));
		JSONArray array = new JSONArray();
		Long parentId = RequestUtils.getLong(request, "id", 0);
		List<District> districts = null;
		if (parentId != null) {
			districts = districtService.getDistrictList(parentId);
		}
		if (districts != null && !districts.isEmpty()) {
			Map<Long, TreeModel> treeMap = new HashMap<Long, TreeModel>();
			List<TreeModel> treeModels = new ArrayList<TreeModel>();
			List<Long> districtIds = new ArrayList<Long>();
			for (District district : districts) {
				if (district.getLocked() != 0) {
					continue;
				}
				TreeModel tree = new BaseTree();
				tree.setId(district.getId());
				tree.setParentId(district.getParentId());
				tree.setCode(district.getCode());
				tree.setName(district.getName());
				tree.setSortNo(district.getSortNo());
				tree.setIconCls("tree_folder");
				treeModels.add(tree);
				districtIds.add(district.getId());
				treeMap.put(district.getId(), tree);
			}
			// logger.debug("treeModels:" + treeModels.size());
			TreeHelper treeHelper = new TreeHelper();
			JSONArray jsonArray = treeHelper.getTreeJSONArray(treeModels);
			for (int i = 0, len = jsonArray.size(); i < len; i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				json.put("isParent", true);
			}
			// logger.debug(jsonArray.toJSONString());
			return jsonArray.toJSONString().getBytes("UTF-8");
		}
		return array.toJSONString().getBytes("UTF-8");
	}

}
