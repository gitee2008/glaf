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

import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

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

import com.alibaba.fastjson.JSONObject;
import com.glaf.base.callback.LoginCallbackThread;
import com.glaf.base.config.BaseConfiguration;
import com.glaf.base.modules.sys.model.IdentityToken;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.IdentityTokenService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.online.domain.UserOnline;
import com.glaf.base.online.service.UserOnlineService;
import com.glaf.base.utils.ContextUtil;
import com.glaf.base.utils.ParamUtil;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.Environment;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.IOUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.UUID32;
import com.glaf.core.util.security.RSAUtils;
import com.glaf.core.web.callback.CallbackProperties;
import com.glaf.core.web.callback.LoginCallback;
import com.glaf.sms.service.SmsVerifyMessageService;

@Controller("/userSmsResetPassword")
@RequestMapping("/userSmsResetPassword")
public class UserSmsResetPasswordController {

	protected static final Log logger = LogFactory.getLog(UserSmsResetPasswordController.class);

	protected static Configuration conf = BaseConfiguration.create();

	protected SysUserService sysUserService;

	protected UserOnlineService userOnlineService;

	protected IdentityTokenService identityTokenService;

	protected SmsVerifyMessageService smsVerifyMessageService;

	/**
	 * 手机短信验证通过后自动登录并显示修改密码页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/changePwd")
	public ModelAndView changePwd(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		String mobile = request.getParameter("mobile");
		String verificationCode = request.getParameter("verificationCode");

		verificationCode = RSAUtils.decryptBase64String(verificationCode, "RSA/None/PKCS1Padding");

		long dateAfter = System.currentTimeMillis() - DateUtils.MINUTE * 10;
		if (!smsVerifyMessageService.verify(mobile, verificationCode, dateAfter)) {
			return this.resetPwd(request, response, modelMap);
		}

		SysUser bean = sysUserService.findByMobile(mobile);
		if (bean != null) {
			String systemName = ParamUtil.getParameter(request, Constants.SYSTEM_NAME);
			if (StringUtils.isNotEmpty(systemName)) {
				Environment.setCurrentSystemName(systemName);
			} else {
				Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
			}

			RequestUtils.setLoginUser(request, response, systemName, bean.getUserId());
			String ipAddr = RequestUtils.getIPAddress(request);
			try {
				UserOnline online = new UserOnline();
				online.setActorId(bean.getActorId());
				online.setName(bean.getName());
				online.setCheckDate(new Date());
				online.setLoginDate(new Date());
				online.setLoginIP(ipAddr);
				userOnlineService.login(online);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			Properties props = CallbackProperties.getProperties();
			if (props != null && props.keys().hasMoreElements()) {
				Enumeration<?> e = props.keys();
				while (e.hasMoreElements()) {
					String className = (String) e.nextElement();
					try {
						Object obj = ClassUtils.instantiateObject(className);
						if (obj instanceof LoginCallback) {
							LoginCallback callback = (LoginCallback) obj;
							if (StringUtils.equals(className, "com.glaf.shiro.ShiroLoginCallback")) {
								callback.afterLogin(bean.getUserId(), request, response);
							} else {
								LoginCallbackThread command = new LoginCallbackThread(callback, bean.getUserId(),
										request, response);
								com.glaf.core.util.ThreadFactory.execute(command);
								Thread.sleep(50);
							}
						}
					} catch (Exception ex) {
						logger.error(ex);
					}
				}
			}
		} else {
			return this.resetPwd(request, response, modelMap);
		}

		String x_view = ViewProperties.getString("user.smsChangePwd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/user/smsChangePwd", modelMap);
	}

	@ResponseBody
	@RequestMapping("/getKey")
	public void getKey(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String mobile = request.getParameter("mobile");
		logger.debug("mobile:" + mobile);
		if (StringUtils.isNotEmpty(mobile)) {
			OutputStream output = null;
			try {
				SysUser user = sysUserService.findByMobile(mobile);
				if (user != null && ContextUtil.increaseUser(user.getActorId()) < 500) {
					IdentityToken identityToken = new IdentityToken();
					java.util.Random random = new java.util.Random();
					String rand1 = StringTools.getRandomString(random.nextInt(50));
					String rand2 = StringTools.getRandomString(random.nextInt(50));
					String ip = RequestUtils.getIPAddress(request);
					identityToken.setId(DateUtils.getNowYearMonthDay() + "_" + user.getActorId() + "_" + ip);
					identityToken.setClientIP(ip);
					identityToken.setNonce(UUID32.getUUID());
					identityToken.setRand1(rand1);
					identityToken.setRand2(rand2);
					identityToken.setTimeLive(600);// 600秒
					identityToken.setToken(StringTools.getRandomString(random.nextInt(100)));
					identityToken.setType("Login");
					identityToken.setUserId(user.getActorId());
					this.identityTokenService.save(identityToken);

					json.put("x_y", rand1);
					json.put("x_z", rand2);
					json.put("token", identityToken.getToken());
					json.put("public_key", RSAUtils.getDefaultRSAPublicKey());

					request.setCharacterEncoding("UTF-8");
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json; charset=UTF-8");
					output = response.getOutputStream();
					output.write(json.toJSONString().getBytes("UTF-8"));
					output.flush();
					return;
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			} finally {
				IOUtils.closeStream(output);
			}
		}
	}

	/**
	 * 显示手机短信重置密码页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping
	public ModelAndView resetPwd(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		if (SystemConfig.getBoolean("enableSmsRegVerification")) {
			request.setAttribute("enableSmsRegVerification", true);
		}

		String x_view = ViewProperties.getString("user.smsResetPwd");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/user/smsResetPwd", modelMap);
	}

	@javax.annotation.Resource(name = "identityTokenService")
	public void setIdentityTokenService(IdentityTokenService identityTokenService) {
		this.identityTokenService = identityTokenService;
	}

	@javax.annotation.Resource(name = "com.glaf.sms.service.smsVerifyMessageService")
	public void setSmsVerifyMessageService(SmsVerifyMessageService smsVerifyMessageService) {
		this.smsVerifyMessageService = smsVerifyMessageService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@javax.annotation.Resource
	public void setUserOnlineService(UserOnlineService userOnlineService) {
		this.userOnlineService = userOnlineService;
	}

}
