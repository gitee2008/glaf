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

package com.glaf.sms.web.springmvc;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.DataRequest;
import com.glaf.core.base.DataRequest.SortDescriptor;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RSA;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.sms.domain.SmsClient;
import com.glaf.sms.query.SmsClientQuery;
import com.glaf.sms.service.SmsClientService;
import com.glaf.sms.util.SmsClientDomainFactory;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sys/smsClient")
@RequestMapping("/sys/smsClient")
public class SmsClientController {
	protected static final Log logger = LogFactory.getLog(SmsClientController.class);

	protected SmsClientService smsClientService;

	public SmsClientController() {

	}

	@ResponseBody
	@RequestMapping("/checkCode")
	public byte[] checkCode(HttpServletRequest request) {
		try {
			String sysCode = RequestUtils.getParameter(request, "sysCode");
			SmsClient client = smsClientService.getSmsClientBySysCode(sysCode);
			if (client != null) {
				return ResponseUtils.responseJsonResult(false);
			} else {
				return ResponseUtils.responseJsonResult(true);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/delete")
	public byte[] delete(HttpServletRequest request, ModelMap modelMap) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);

		String id = RequestUtils.getString(request, "id");
		String ids = request.getParameter("ids");
		if (StringUtils.isNotEmpty(ids)) {
			StringTokenizer token = new StringTokenizer(ids, ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x)) {
					SmsClient smsClient = smsClientService.getSmsClient(String.valueOf(x));
					if (smsClient != null && (StringUtils.equals(smsClient.getCreateBy(), loginContext.getActorId())
							|| loginContext.isSystemAdministrator())) {

					}
				}
			}
			return ResponseUtils.responseResult(true);
		} else if (id != null) {
			SmsClient smsClient = smsClientService.getSmsClient(String.valueOf(id));
			if (smsClient != null && (StringUtils.equals(smsClient.getCreateBy(), loginContext.getActorId())
					|| loginContext.isSystemAdministrator())) {

				return ResponseUtils.responseResult(true);
			}
		}
		return ResponseUtils.responseResult(false);
	}

	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		SmsClient smsClient = smsClientService.getSmsClient(request.getParameter("id"));
		if (smsClient != null) {
			request.setAttribute("smsClient", smsClient);
		}

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("smsClient.edit");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sms/smsClient/edit", modelMap);
	}

	@ResponseBody
	@RequestMapping("/getRSAKey")
	public byte[] getRsaKey(HttpServletRequest request) {
		try {
			Map<String, Object> map = RSA.init();
			String publicKey = RSA.getPublicKey(map);
			String privateKey = RSA.getPrivateKey(map);
			JSONObject ret = new JSONObject();
			ret.put("statusCode", 200);
			ret.put("publicKey", publicKey);
			ret.put("privateKey", privateKey);
			return ret.toJSONString().getBytes("UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SmsClientQuery query = new SmsClientQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);

		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
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

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		int total = smsClientService.getSmsClientCountByQueryCriteria(query);
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

			List<SmsClient> list = smsClientService.getSmsClientsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SmsClient smsClient : list) {
					JSONObject rowJSON = smsClient.toJsonObject();
					rowJSON.put("id", smsClient.getId());
					rowJSON.put("rowId", smsClient.getId());
					rowJSON.put("smsClientId", smsClient.getId());
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

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/sms/smsClient/list", modelMap);
	}

	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}
		String x_view = ViewProperties.getString("smsClient.query");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}
		return new ModelAndView("/sms/smsClient/query", modelMap);
	}

	@RequestMapping("/read")
	@ResponseBody
	public byte[] read(HttpServletRequest request, @RequestBody DataRequest dataRequest) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SmsClientQuery query = new SmsClientQuery();
		Tools.populate(query, params);
		query.deleteFlag(0);
		query.setActorId(loginContext.getActorId());
		query.setLoginContext(loginContext);
		query.setDataRequest(dataRequest);
		SmsClientDomainFactory.processDataRequest(dataRequest);

		if (!loginContext.isSystemAdministrator()) {
			String actorId = loginContext.getActorId();
			query.createBy(actorId);
		}

		int start = 0;
		int limit = 10;
		String orderName = null;
		String order = null;

		int pageNo = dataRequest.getPage();
		limit = dataRequest.getPageSize();

		start = (pageNo - 1) * limit;

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = 1;
		}

		JSONObject result = new JSONObject();
		int total = smsClientService.getSmsClientCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

			if (dataRequest.getSort() != null && !dataRequest.getSort().isEmpty()) {
				SortDescriptor sort = dataRequest.getSort().get(0);
				orderName = sort.getField();
				order = sort.getDir();
				logger.debug("orderName:" + orderName);
				logger.debug("order:" + order);
			}
			if (StringUtils.isNotEmpty(orderName)) {
				query.setSortColumn(orderName);
				if (StringUtils.equals(order, "desc")) {
					query.setSortOrder(" desc ");
				}
			}
			List<SmsClient> list = smsClientService.getSmsClientsByQueryCriteria(start, limit, query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SmsClient smsClient : list) {
					JSONObject rowJSON = smsClient.toJsonObject();
					rowJSON.put("id", smsClient.getId());
					rowJSON.put("rowId", smsClient.getId());
					rowJSON.put("smsClientId", smsClient.getId());
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

	@ResponseBody
	@RequestMapping("/saveSmsClient")
	public byte[] saveSmsClient(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		String actorId = loginContext.getActorId();
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SmsClient smsClient = new SmsClient();
		try {
			Tools.populate(smsClient, params);
			smsClient.setSubject(request.getParameter("subject"));
			smsClient.setRemoteIP(request.getParameter("remoteIP"));
			smsClient.setSysCode(request.getParameter("sysCode"));
			smsClient.setSysPwd(request.getParameter("sysPwd"));

			smsClient.setPublicKey(request.getParameter("publicKey"));
			smsClient.setPrivateKey(request.getParameter("privateKey"));
			smsClient.setPeerPublicKey(request.getParameter("peerPublicKey"));
			smsClient.setToken(request.getParameter("token"));
			smsClient.setType(request.getParameter("type"));
			smsClient.setFrequence(RequestUtils.getInt(request, "frequence"));
			smsClient.setLimit(RequestUtils.getInt(request, "limit"));

			if (smsClient.getId() != null && !smsClient.getId().isEmpty()) {

			} else {
				smsClient.setCreateBy(actorId);
				smsClient.setCreateTime(new Date());
			}
			String locked = request.getParameter("locked");
			if (locked != null && !locked.isEmpty()) {
				smsClient.setLocked(RequestUtils.getInt(request, "locked"));
			}
			String encryptionFlag = request.getParameter("encryptionFlag");
			if (encryptionFlag != null && !encryptionFlag.isEmpty()) {
				smsClient.setEncryptionFlag(request.getParameter("encryptionFlag"));
			}

			this.smsClientService.save(smsClient);

			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.sms.service.smsClientService")
	public void setSmsClientService(SmsClientService smsClientService) {
		this.smsClientService = smsClientService;
	}

}
