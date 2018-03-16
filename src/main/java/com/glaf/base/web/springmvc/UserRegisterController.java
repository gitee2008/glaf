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
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.config.BaseConfiguration;
import com.glaf.base.district.domain.District;
import com.glaf.base.district.service.DistrictService;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.SysRoleService;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.core.base.DataFile;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.matrix.data.domain.DataFileEntity;
import com.glaf.matrix.data.factory.DataFileFactory;
import com.glaf.sms.service.SmsVerifyMessageService;

@Controller("/register")
@RequestMapping("/register")
public class UserRegisterController {

	protected static final Log logger = LogFactory.getLog(UserRegisterController.class);

	protected static Configuration conf = BaseConfiguration.create();

	protected DistrictService districtService;

	protected SysUserService sysUserService;

	protected SysRoleService sysRoleService;

	protected SysTenantService sysTenantService;

	protected SmsVerifyMessageService smsVerifyMessageService;

	public UserRegisterController() {

	}

	@ResponseBody
	@RequestMapping("/create")
	public byte[] create(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String responseDataType = request.getParameter("responseDataType");
		String json = request.getParameter("json");
		JSONObject jsonObject = JSON.parseObject(json);
		JSONObject result = new JSONObject();
		String userId = jsonObject.getString("x");
		if (!isUserId(userId)) {
			result.put("status", 500);
			result.put("message", "用户名不合法，只能是大小写字母及数字，请换个再来！");
			return result.toJSONString().getBytes("UTF-8");
		}
		String password = jsonObject.getString("y");
		String mail = jsonObject.getString("mail");
		String mobile = jsonObject.getString("mobile");
		String tenantName = jsonObject.getString("tenantName");

		String mobileVerifyFlag = "N";

		/**
		 * 判断是否开启手机短信验证
		 */
		if (SystemConfig.getBoolean("enableSmsRegVerification")) {
			String verificationCode = jsonObject.getString("verificationCode");
			long dateAfter = System.currentTimeMillis() - DateUtils.MINUTE * 10;
			boolean isOK = smsVerifyMessageService.verify(mobile, verificationCode, dateAfter);
			if (isOK) {
				mobileVerifyFlag = "Y";
			} else {
				result.put("status", 500);
				result.put("statusCode", 500);
				result.put("message", "短信验证码不正确！");
				return result.toJSONString().getBytes("UTF-8");
			}
		}

		if (StringUtils.isNotEmpty(tenantName) && sysTenantService.getSysTenantByName(tenantName) != null) {
			result.put("status", 500);
			result.put("statusCode", 500);
			result.put("message", "机构名称已经存在，请换个再来！");
			logger.debug(result.toJSONString());
			return result.toJSONString().getBytes("UTF-8");
		}
		if (StringUtils.isNotEmpty(mail) && sysUserService.findByMail(mail) != null) {
			result.put("status", 500);
			result.put("statusCode", 500);
			result.put("message", "邮箱已经存在，请换个再来！");
			logger.debug(result.toJSONString());
			return result.toJSONString().getBytes("UTF-8");
		}
		if (StringUtils.isNotEmpty(mobile) && sysUserService.findByMobile(mobile) != null) {
			result.put("status", 500);
			result.put("statusCode", 500);
			result.put("message", "手机号码已经存在，请换个再来！");
			logger.debug(result.toJSONString());
			return result.toJSONString().getBytes("UTF-8");
		}
		SysUser user = sysUserService.findByAccount(userId);
		int status = 0;
		if (user != null) {
			status = 400;
			result.put("status", 400);
			result.put("statusCode", 400);
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
			user.setMobileVerifyFlag(mobileVerifyFlag);
			user.setCreateBy("web");
			user.setUpdateBy("web");

			SysTenant tenant = new SysTenant();
			tenant.setName(tenantName);
			tenant.setProperty(request.getParameter("property"));
			tenant.setCreateBy(user.getActorId());
			tenant.setUpdateBy(user.getActorId());

			tenant.setProvinceId(jsonObject.getLongValue("provinceId"));
			if (tenant.getProvinceId() > 0) {
				District m = districtService.getDistrict(tenant.getProvinceId());
				tenant.setProvince(m.getName());
			}
			tenant.setCityId(jsonObject.getLongValue("cityId"));
			if (tenant.getCityId() > 0) {
				District m = districtService.getDistrict(tenant.getCityId());
				tenant.setCity(m.getName());
			}
			tenant.setAreaId(jsonObject.getLongValue("areaId"));
			if (tenant.getAreaId() > 0) {
				District m = districtService.getDistrict(tenant.getAreaId());
				tenant.setArea(m.getName());
			}
			tenant.setTownId(jsonObject.getLongValue("townId"));
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

			try {
				CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
						request.getSession().getServletContext());

				if (multipartResolver.isMultipart(request)) {
					MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
					Map<String, MultipartFile> fileMap = req.getFileMap();
					Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
					for (Entry<String, MultipartFile> entry : entrySet) {
						MultipartFile mFile = entry.getValue();
						String filename = mFile.getOriginalFilename();
						if (mFile.getBytes() != null && filename != null && mFile.getSize() <= FileUtils.MB_SIZE * 5) {

							if (filename.indexOf("/") != -1) {
								filename = filename.substring(filename.lastIndexOf("/") + 1, filename.length());
							} else if (filename.indexOf("\\") != -1) {
								filename = filename.substring(filename.lastIndexOf("\\") + 1, filename.length());
							}
							DataFile dataFile = new DataFileEntity();
							dataFile.setId(tenant.getTenantId() + "_tc_image");
							dataFile.setLastModified(System.currentTimeMillis());
							dataFile.setCreateBy(user.getActorId());
							dataFile.setFilename(filename);
							dataFile.setName("trading_certificate");
							dataFile.setContentType(mFile.getContentType());
							dataFile.setSize((int) mFile.getSize());
							dataFile.setType("T_C");
							dataFile.setStatus(status);
							dataFile.setServiceKey("T_C");
							dataFile.setBusinessKey(tenant.getTenantId());
							DataFileFactory.getInstance().insertDataFile(tenant.getTenantId(), dataFile,
									mFile.getBytes());
							break;
						}
					}
				}
			} catch (Exception ex) {
				logger.error(ex);
			}

			if (status == 200) {// 保存成功
				result.put("status", 200);
				result.put("statusCode", 200);
				result.put("message", "注册成功");
			} else if (status == 500) {
				result.put("status", 500);
				result.put("statusCode", 500);
				result.put("message", "注册失败");
			}
		}
		if (StringUtils.equals(responseDataType, "json")) {
			logger.debug(result.toJSONString());
			return result.toJSONString().getBytes("UTF-8");
		} else {
			response.sendRedirect(request.getContextPath() + "/login?x=" + userId);
		}
		return null;
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

		if (SystemConfig.getBoolean("enableSmsRegVerification")) {
			request.setAttribute("enableSmsRegVerification", true);
		}

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

	@javax.annotation.Resource(name = "com.glaf.sms.service.smsVerifyMessageService")
	public void setSmsVerifyMessageService(SmsVerifyMessageService smsVerifyMessageService) {
		this.smsVerifyMessageService = smsVerifyMessageService;
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
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@ResponseBody
	@RequestMapping("/verify")
	public byte[] verify(HttpServletRequest request) throws IOException {
		String json = request.getParameter("json");
		JSONObject jsonObject = JSON.parseObject(json);
		JSONObject result = new JSONObject();
		String userId = jsonObject.getString("x");
		if (!isUserId(userId)) {
			result.put("status", 500);
			result.put("statusCode", 500);
			result.put("message", "用户名不合法，只能是大小写字母及数字，请换个再来！");
			return result.toJSONString().getBytes("UTF-8");
		}

		String mail = jsonObject.getString("mail");
		String mobile = jsonObject.getString("mobile");
		String tenantName = jsonObject.getString("tenantName");

		/**
		 * 判断是否开启手机短信验证
		 */
		if (SystemConfig.getBoolean("enableSmsRegVerification")) {
			String verificationCode = jsonObject.getString("verificationCode");
			long dateAfter = System.currentTimeMillis() - DateUtils.MINUTE * 10;
			boolean isOK = smsVerifyMessageService.verify(mobile, verificationCode, dateAfter);
			if (!isOK) {
				result.put("status", 500);
				result.put("statusCode", 500);
				result.put("message", "短信验证码不正确！");
				return result.toJSONString().getBytes("UTF-8");
			}
		}

		if (StringUtils.isNotEmpty(tenantName) && sysTenantService.getSysTenantByName(tenantName) != null) {
			result.put("status", 500);
			result.put("statusCode", 500);
			result.put("message", "机构名称已经存在，请换个再来！");
			logger.debug(result.toJSONString());
			return result.toJSONString().getBytes("UTF-8");
		}
		if (StringUtils.isNotEmpty(mail) && sysUserService.findByMail(mail) != null) {
			result.put("status", 500);
			result.put("statusCode", 500);
			result.put("message", "邮箱已经存在，请换个再来！");
			logger.debug(result.toJSONString());
			return result.toJSONString().getBytes("UTF-8");
		}
		if (StringUtils.isNotEmpty(mobile) && sysUserService.findByMobile(mobile) != null) {
			result.put("status", 500);
			result.put("statusCode", 500);
			result.put("message", "手机号码已经存在，请换个再来！");
			logger.debug(result.toJSONString());
			return result.toJSONString().getBytes("UTF-8");
		}
		SysUser user = sysUserService.findByAccount(userId);
		int status = 0;
		if (user != null) {
			status = 400;
			result.put("status", 400);
			result.put("statusCode", 400);
			result.put("message", "用户已经存在！");
		} else {
			if (status == 0) {// 验证成功
				result.put("status", 200);
				result.put("statusCode", 200);
			} else if (status == 500) {
				result.put("status", 500);
				result.put("statusCode", 500);
			}
		}
		logger.debug(result.toJSONString());
		return result.toJSONString().getBytes("UTF-8");
	}
}
