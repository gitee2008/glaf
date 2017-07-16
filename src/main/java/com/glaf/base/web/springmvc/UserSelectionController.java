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

package com.glaf.base.web.springmvc;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.TreeModel;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.identity.User;
import com.glaf.core.query.TreeModelQuery;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.tree.component.TreeComponent;
import com.glaf.core.tree.component.TreeRepository;
import com.glaf.core.tree.helper.TreeRepositoryBuilder;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.StringTools;

@Controller
@RequestMapping("/userSelection")
public class UserSelectionController {
	protected final static Log logger = LogFactory.getLog(UserSelectionController.class);

	@RequestMapping
	public ModelAndView choose(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
		logger.debug(paramMap);
		String code = request.getParameter("code");
		String node = request.getParameter("node");
		if (StringUtils.isNotEmpty(node) && StringUtils.isNumeric(node)) {
			long nodeId = Long.parseLong(node);
			TreeModel treeModel = IdentityFactory.getTreeModelById(nodeId);
			if (treeModel != null && treeModel.getId() != 0) {
				modelMap.put("node", treeModel.getId());
				modelMap.put("text", treeModel.getName());
			}
		} else if (StringUtils.isNotEmpty(code)) {
			TreeModel treeModel = IdentityFactory.getTreeModelByCode(code);
			if (treeModel != null && treeModel.getId() != 0) {
				modelMap.put("node", treeModel.getId());
				modelMap.put("text", treeModel.getName());
			}
		} else {
			TreeModel treeModel = IdentityFactory.getTopOrganization();
			if (treeModel != null && treeModel.getId() != 0) {
				modelMap.put("node", treeModel.getId());
				modelMap.put("text", treeModel.getName());
			} else {
				modelMap.put("node", "0");
				modelMap.put("text", "组织结构");
			}
		}

		String formName = request.getParameter("formName");
		String elementId = request.getParameter("elementId");
		String elementName = request.getParameter("elementName");

		if (StringUtils.isEmpty(formName)) {
			formName = "iForm";
		}

		if (StringUtils.isEmpty(elementId)) {
			elementId = "x_users";
		}

		if (StringUtils.isEmpty(elementName)) {
			elementName = "x_users_name";
		}

		modelMap.put("formName", formName);
		modelMap.put("elementId", elementId);
		modelMap.put("elementName", elementName);

		String x_users = request.getParameter("x_selected");
		List<String> actorIds = StringTools.split(x_users, ",");
		if (actorIds != null && actorIds.size() > 0) {
			paramMap.clear();
			paramMap.put("actorIds", actorIds);
			Map<String, User> userMap = IdentityFactory.getUserMap(paramMap);
			modelMap.put("users", userMap.values());
		}

		String jx_view = request.getParameter("x_view");

		if (StringUtils.isNotEmpty(jx_view)) {
			return new ModelAndView(jx_view, modelMap);
		}

		String x_view = ViewProperties.getString("userSelection.choose");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/identity/user/choose");
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request) throws IOException {
		RequestUtils.setRequestParameterToAttribute(request);
		String node = request.getParameter("node");
		Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
		logger.debug(paramMap);
		JSONArray result = new JSONArray();
		TreeModelQuery query = new TreeModelQuery();
		if (StringUtils.isNotEmpty(node) && StringUtils.isNumeric(node) && !("0".equals(node))) {
			long nodeId = Long.parseLong(node);
			query.parentId(nodeId);
		}
		if (StringUtils.isNotEmpty(node) && StringUtils.isNumeric(node) && !("0".equals(node))
				&& !("xnode-3".equals(node))) {
			long nodeId = Long.parseLong(node);
			paramMap.put("parentId", nodeId);
			query.parentId(nodeId);
			if ("0".equals(node)) {
				paramMap.remove("node");
			} else {
				paramMap.remove("code");
			}
			List<TreeModel> treeModels = IdentityFactory.getOrganizations(query);
			if (treeModels != null && treeModels.size() > 0) {
				for (int i = 0; i < treeModels.size(); i++) {
					TreeModel treeModel = (TreeModel) treeModels.get(i);
					JSONObject row = new JSONObject();
					row.put("id", treeModel.getId());
					row.put("text", treeModel.getName());
					row.put("leaf", Boolean.valueOf(false));
					row.put("checked", Boolean.valueOf(false));
					result.add(row);
				}
			} else {
				paramMap.put("nodeId", nodeId);
				paramMap.put("organizationId", nodeId);
				Map<String, User> userMap = IdentityFactory.getUserMap(paramMap);
				if (userMap != null && userMap.size() > 0) {
					Iterator<User> iter = userMap.values().iterator();
					while (iter.hasNext()) {
						User u = (User) iter.next();
						JSONObject row = new JSONObject();
						row.put("id", u.getActorId());
						row.put("text", u.getName());
						row.put("leaf", Boolean.valueOf(true));
						row.put("checked", Boolean.valueOf(false));
						row.put("icon", request.getContextPath() + "/images/user.png");
						result.add(row);
					}
				}
			}
		} else {
			List<TreeModel> treeModels = IdentityFactory.getOrganizations(query);
			if (treeModels != null && treeModels.size() > 0) {
				TreeRepositoryBuilder builder = new TreeRepositoryBuilder();
				TreeRepository repository = builder.build(treeModels);
				TreeComponent[] components = repository.getTopTreesAsArray();
				if (components != null && components.length > 0) {
					for (int i = 0; i < components.length; i++) {
						TreeComponent component = components[i];
						JSONObject row = new JSONObject();
						row.put("id", component.getId());
						row.put("text", component.getTitle());
						row.put("leaf", Boolean.valueOf(false));
						row.put("checked", Boolean.valueOf(false));
						result.add(row);
					}
				}
			}
		}
		logger.debug(result.toJSONString());
		return result.toJSONString().getBytes("UTF-8");
	}

}
