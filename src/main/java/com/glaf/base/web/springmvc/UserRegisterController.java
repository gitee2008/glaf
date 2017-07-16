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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.config.BaseConfiguration;
import com.glaf.base.district.domain.District;
import com.glaf.base.district.service.DistrictService;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.SysRoleService;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.util.RequestUtils;

@Controller("/register")
@RequestMapping("/register")
public class UserRegisterController {

	protected static final Log logger = LogFactory.getLog(UserRegisterController.class);

	protected static Configuration conf = BaseConfiguration.create();

	protected DistrictService districtService;

	protected SysUserService sysUserService;

	protected SysRoleService sysRoleService;

	protected SysTreeService sysTreeService;

	protected SysTenantService sysTenantService;

	public UserRegisterController() {

	}

	@ResponseBody
	@RequestMapping("/create")
	public byte[] create(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String json = request.getParameter("json");
		JSONObject jsonObject = JSON.parseObject(json);
		JSONObject result = new JSONObject();
		String userId = jsonObject.getString("x");
		if (!isUserId(userId)) {
			result.put("status", 500);
			result.put("message", "用户名不合法，请换个再来！");
			return result.toJSONString().getBytes("UTF-8");
		}
		String password = jsonObject.getString("y");
		String mail = jsonObject.getString("mail");
		String mobile = jsonObject.getString("mobile");
		String tenantName = jsonObject.getString("tenantName");
		if (StringUtils.isNotEmpty(tenantName) && sysTenantService.getSysTenantByName(tenantName) != null) {
			result.put("status", 500);
			result.put("message", "组织机构已经存在，请换个再来！");
			return result.toJSONString().getBytes("UTF-8");
		}
		if (StringUtils.isNotEmpty(mail) && sysUserService.findByMail(mail) != null) {
			result.put("status", 500);
			result.put("message", "邮箱已经存在，请换个再来！");
			return result.toJSONString().getBytes("UTF-8");
		}
		if (StringUtils.isNotEmpty(mobile) && sysUserService.findByMobile(mobile) != null) {
			result.put("status", 500);
			result.put("message", "手机号码已经存在，请换个再来！");
			return result.toJSONString().getBytes("UTF-8");
		}
		SysUser user = sysUserService.findByAccount(userId);
		int status = 0;
		if (user != null) {
			status = 400;
			result.put("status", 400);
			result.put("message", "用户已经存在！");
		} else {
			user = new SysUser();
			user.setActorId(userId);
			user.setPasswordHash(password);
			user.setName(jsonObject.getString("name"));
			if (user.getName() == null) {
				user.setName(userId);
			}
			user.setMobile(jsonObject.getString("mobile"));
			user.setEmail(jsonObject.getString("mail"));
			user.setEvection(0);
			user.setCreateBy("web");
			user.setUpdateBy("web");

			SysTenant tenant = new SysTenant();
			tenant.setName(tenantName);
			tenant.setProperty(request.getParameter("property"));
			tenant.setCreateBy(user.getActorId());
			tenant.setUpdateBy(user.getActorId());

			tenant.setProvinceId(RequestUtils.getLong(request, "provinceId"));
			if (tenant.getProvinceId() > 0) {
				District m = districtService.getDistrict(tenant.getProvinceId());
				tenant.setProvince(m.getName());
			}
			tenant.setCityId(RequestUtils.getLong(request, "cityId"));
			if (tenant.getCityId() > 0) {
				District m = districtService.getDistrict(tenant.getCityId());
				tenant.setCity(m.getName());
			}
			tenant.setAreaId(RequestUtils.getLong(request, "areaId"));
			if (tenant.getAreaId() > 0) {
				District m = districtService.getDistrict(tenant.getAreaId());
				tenant.setArea(m.getName());
			}
			tenant.setTownId(RequestUtils.getLong(request, "townId"));
			if (tenant.getTownId() > 0) {
				District m = districtService.getDistrict(tenant.getTownId());
				tenant.setTown(m.getName());
			}

			try {
				sysTenantService.register(tenant, user);
				status = 200;
			} catch (Exception ex) {
				status = 500;
				logger.error(ex);
			}
			if (status == 200) {// 保存成功
				result.put("status", 200);
				result.put("message", "注册成功");
			} else if (status == 500) {
				result.put("status", 500);
				result.put("message", "注册失败");
			}
		}

		return result.toJSONString().getBytes("UTF-8");
	}

	public boolean isUserId(String sourceString) {
		if (sourceString == null || sourceString.trim().length() < 2 || sourceString.trim().length() > 25) {
			return false;
		}
		char[] sourceChrs = sourceString.toCharArray();
		Character chr = Character.valueOf(sourceChrs[0]);
		if (!((chr.charValue() == 95) || (65 <= chr.charValue() && chr.charValue() <= 90)
				|| (97 <= chr.charValue() && chr.charValue() <= 122))) {
			return false;
		}
		for (int i = 1; i < sourceChrs.length; i++) {
			chr = Character.valueOf(sourceChrs[i]);
			if (!((chr.charValue() == 95) || (47 <= chr.charValue() && chr.charValue() <= 57)
					|| (65 <= chr.charValue() && chr.charValue() <= 90)
					|| (97 <= chr.charValue() && chr.charValue() <= 122))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 显示用户注册页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping
	public ModelAndView register(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		List<District> provinces = districtService.getDistrictList(0);
		request.setAttribute("provinces", provinces);

		String x_view = ViewProperties.getString("user.register");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/user/register", modelMap);
	}

	@javax.annotation.Resource
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@javax.annotation.Resource
	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setSysTreeService(SysTreeService sysTreeService) {
		this.sysTreeService = sysTreeService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

}
