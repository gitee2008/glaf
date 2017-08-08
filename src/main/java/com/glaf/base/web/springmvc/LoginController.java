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
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
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
import com.glaf.base.handler.LoginHandler;
import com.glaf.base.handler.PasswordLoginHandler;
import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.online.domain.UserOnline;
import com.glaf.base.online.service.UserOnlineService;
import com.glaf.base.utils.ContextUtil;
import com.glaf.base.utils.ParamUtil;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.Environment;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.domain.SystemProperty;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.security.RSAUtils;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.Constants;
import com.glaf.core.util.IOUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.UUID32;
import com.glaf.core.web.callback.CallbackProperties;
import com.glaf.core.web.callback.LoginCallback;

@Controller("/login")
@RequestMapping("/login")
public class LoginController {
	private static final Log logger = LogFactory.getLog(LoginController.class);

	private static Configuration conf = BaseConfiguration.create();

	private SysUserService sysUserService;

	private UserOnlineService userOnlineService;

	/**
	 * 登录
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/doLogin")
	public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.debug("---------------------doLogin--------------------");
		HttpSession session = request.getSession(false);
		RequestUtils.setRequestParameterToAttribute(request);

		String systemName = ParamUtil.getParameter(request, Constants.SYSTEM_NAME);
		if (StringUtils.isNotEmpty(systemName)) {
			Environment.setCurrentSystemName(systemName);
		} else {
			Environment.setCurrentSystemName(Environment.DEFAULT_SYSTEM_NAME);
		}

		String responseDataType = request.getParameter("responseDataType");

		logger.debug("currentSystemName:" + Environment.getCurrentSystemName());
		logger.debug("params:" + RequestUtils.getParameterMap(request));

		// 获取参数
		String account = ParamUtil.getParameter(request, "x");
		String password2 = ParamUtil.getParameter(request, "y");

		SysUser bean = null;
		if (StringUtils.isNotEmpty(account) && StringUtils.isNotEmpty(password2)) {
			if (session == null) {
				if (StringUtils.isNotEmpty(responseDataType) && StringUtils.equals(responseDataType, "json")) {
					OutputStream output = null;
					try {
						request.setCharacterEncoding("UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setContentType("application/json; charset=UTF-8");
						output = response.getOutputStream();
						byte[] bytes = ResponseUtils.responseJsonResult(10001, "未初始化登录会话。");
						output.write(bytes);
						output.flush();
						return null;
					} catch (Exception e) {
					} finally {
						IOUtils.closeStream(output);
					}
				}
				return new ModelAndView("/login/login", modelMap);
			}

			String rand = (String) session.getAttribute("x_y");
			String rand2 = (String) session.getAttribute("x_z");

			if (StringUtils.isEmpty(rand) && StringUtils.isEmpty(rand2)) {
				if (StringUtils.isNotEmpty(responseDataType) && StringUtils.equals(responseDataType, "json")) {
					OutputStream output = null;
					try {
						request.setCharacterEncoding("UTF-8");
						response.setCharacterEncoding("UTF-8");
						response.setContentType("application/json; charset=UTF-8");
						output = response.getOutputStream();
						byte[] bytes = ResponseUtils.responseJsonResult(10002, "登录信息不正确。");
						output.write(bytes);
						output.flush();
						return null;
					} catch (Exception e) {
					} finally {
						IOUtils.closeStream(output);
					}
				}
				return new ModelAndView("/login/login", modelMap);
			}
			LoginHandler handler = new PasswordLoginHandler();
			bean = handler.doLogin(request, response);
			session.removeAttribute("x_y");// 一次性失效，用一次即过期
			session.removeAttribute("x_z");
		}

		if (bean == null) {
			if (StringUtils.isNotEmpty(responseDataType) && StringUtils.equals(responseDataType, "json")) {
				OutputStream output = null;
				try {
					request.setCharacterEncoding("UTF-8");
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json; charset=UTF-8");
					output = response.getOutputStream();
					byte[] bytes = ResponseUtils.responseJsonResult(10003, "用户名或密码不正确，登录失败。");
					output.write(bytes);
					output.flush();
					return null;
				} catch (Exception e) {
				} finally {
					IOUtils.closeStream(output);
				}
			}
			return new ModelAndView("/login/login", modelMap);
		} else {
			String ipAddr = RequestUtils.getIPAddress(request);
			SystemProperty p = SystemConfig.getProperty("login_limit");
			if (!(StringUtils.equals(account, "root") || StringUtils.equals(account, "admin"))) {
				logger.debug("#################check login limit#####################");

				SystemProperty pt = SystemConfig.getProperty("login_time_check");
				int timeoutSeconds = 300;

				if (pt != null && pt.getValue() != null && StringUtils.isNumeric(pt.getValue())) {
					timeoutSeconds = Integer.parseInt(pt.getValue());
				}
				if (timeoutSeconds < 300) {
					timeoutSeconds = 300;
				}
				if (timeoutSeconds > 3600) {
					timeoutSeconds = 3600;
				}

				/**
				 * 检测是否限制一个用户只能在一个地方登录
				 */
				if (p != null && StringUtils.equals(p.getValue(), "true")) {
					logger.debug("#################3#########################");
					String loginIP = null;
					UserOnline userOnline = userOnlineService.getUserOnline(account);
					logger.debug("userOnline:" + userOnline);
					boolean timeout = false;
					if (userOnline != null) {
						loginIP = userOnline.getLoginIP();
						if (userOnline.getCheckDateMs() != null
								&& System.currentTimeMillis() - userOnline.getCheckDateMs() > timeoutSeconds * 1000) {
							timeout = true;// 超时，说明登录已经过期
						}
						if (userOnline.getLoginDate() != null && System.currentTimeMillis()
								- userOnline.getLoginDate().getTime() > timeoutSeconds * 1000) {
							timeout = true;// 超时，说明登录已经过期
						}
					}
					logger.info("timeout:" + timeout);
					logger.info("login IP:" + loginIP);
					if (!timeout) {// 超时，说明登录已经过期，不用判断是否已经登录了
						if (loginIP != null && !(StringUtils.equals(ipAddr, loginIP))) {// 用户已在其他机器登陆
							logger.debug("用户已经在其他地方登录。");
							if (StringUtils.isNotEmpty(responseDataType)
									&& StringUtils.equals(responseDataType, "json")) {
								OutputStream output = null;
								try {
									request.setCharacterEncoding("UTF-8");
									response.setCharacterEncoding("UTF-8");
									response.setContentType("application/json; charset=UTF-8");
									output = response.getOutputStream();
									byte[] bytes = ResponseUtils.responseJsonResult(201, "用户已经在其他地方登录。");
									output.write(bytes);
									output.flush();
									return null;
								} catch (Exception e) {
								} finally {
									IOUtils.closeStream(output);
								}
							} else {
								return new ModelAndView("/login/login", modelMap);
							}
						}
					}
				}
			}

			session = request.getSession(true);// 重写Session

			// 登录成功，修改最近一次登录时间
			bean.setLastLoginDate(new Date());
			bean.setLastLoginIP(ipAddr);
			bean.setLockLoginTime(new Date());
			bean.setLoginRetry(0);
			sysUserService.updateUser(bean);

			ContextUtil.put(bean.getUserId(), bean);// 传入全局变量

			systemName = Environment.getCurrentSystemName();

			if (StringUtils.isEmpty(systemName)) {
				systemName = "default";
			}

			RequestUtils.setLoginUser(request, response, systemName, bean.getUserId());

			try {
				UserOnline online = new UserOnline();
				online.setActorId(bean.getActorId());
				online.setName(bean.getName());
				online.setCheckDate(new Date());
				online.setLoginDate(new Date());
				online.setLoginIP(ipAddr);
				online.setSessionId(session.getId());
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
						ex.printStackTrace();
						logger.error(ex);
					}
				}
			}

			LoginContext loginContext = IdentityFactory.getLoginContext(bean.getActorId());
			logger.debug("actorId:" + RequestUtils.getActorId(request));
			logger.debug(loginContext.getActorId() + "登录成功。");
			logger.debug(loginContext.toJsonObject().toJSONString());

			String redirectUrl = request.getParameter("redirectUrl");
			if (StringUtils.isNotEmpty(redirectUrl)) {
				try {
					response.sendRedirect(redirectUrl);
					return null;
				} catch (IOException e) {
				}
			}

			if (StringUtils.isNotEmpty(responseDataType) && StringUtils.equals(responseDataType, "json")) {
				OutputStream output = null;
				try {
					request.setCharacterEncoding("UTF-8");
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json; charset=UTF-8");
					output = response.getOutputStream();
					byte[] bytes = ResponseUtils.responseJsonResult(true, "登录成功！");
					output.write(bytes);
					output.flush();
					return null;
				} catch (Exception e) {
				} finally {
					IOUtils.closeStream(output);
				}
			}

			try {
				response.sendRedirect(request.getContextPath() + "/my/main");
			} catch (IOException e) {
			}
			return null;
			// return new ModelAndView("/main/main", modelMap);
		}
	}

	public String getRandomString(int length) {
		StringBuilder buffer = new StringBuilder(
				"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()[]");
		buffer.append(UUID32.getUUID());
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++) {
			sb.append(buffer.charAt(random.nextInt(range)));
		}
		return sb.toString();
	}

	@ResponseBody
	@RequestMapping("/getLoginSecurityKey")
	public void getLoginSecurityKey(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		OutputStream output = null;
		try {
			HttpSession session = request.getSession(true);
			java.util.Random random = new java.util.Random();
			String rand = this.getRandomString(random.nextInt(50));
			String rand2 = this.getRandomString(random.nextInt(50));
			if (session != null) {
				session.setAttribute("x_y", rand);
				session.setAttribute("x_z", rand2);
				json.put("x_y", rand);
				json.put("x_z", rand2);
				json.put("public_key", RSAUtils.getDefaultRSAPublicKey());
			}

			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=UTF-8");
			output = response.getOutputStream();
			output.write(json.toJSONString().getBytes("UTF-8"));
			output.flush();
			// logger.debug("----------------------------getLoginSecurityKey--------------");
			// logger.debug(json.toJSONString());
			return;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeStream(output);
		}
	}

	/**
	 * 登录
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String ip = RequestUtils.getIPAddress(request);
		/**
		 * 允许从指定的机器上通过用户名密码登录
		 */
		if (StringUtils.contains(conf.get("login.allow.ip", "127.0.0.1"), ip)
				|| StringUtils.contains(SystemConfig.getString("login.allow.ip", "127.0.0.1"), ip)) {
			String actorId = request.getParameter("x");
			String password = request.getParameter("y");
			HttpSession session = request.getSession(true);
			java.util.Random random = new java.util.Random();
			String rand = Math.abs(random.nextInt(999999)) + com.glaf.core.util.UUID32.getUUID()
					+ Math.abs(random.nextInt(999999));
			String rand2 = Math.abs(random.nextInt(999999)) + com.glaf.core.util.UUID32.getUUID()
					+ Math.abs(random.nextInt(999999));
			session = request.getSession(true);
			if (session != null) {
				rand = Base64.encodeBase64String(rand.getBytes()) + com.glaf.core.util.UUID32.getUUID();
				rand2 = Base64.encodeBase64String(rand2.getBytes()) + com.glaf.core.util.UUID32.getUUID();
				session.setAttribute("x_y", rand);
				session.setAttribute("x_z", rand2);
			}
			String url = request.getContextPath() + "/login/doLogin?x=" + actorId + "&y=" + rand + password + rand2;
			try {
				response.sendRedirect(url);
				return null;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		String userId = ParamUtil.getParameter(request, "userId");
		String token = ParamUtil.getParameter(request, "token");

		if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(token)) {
			return this.doLogin(request, response, modelMap);
		}

		String login_html = SystemConfig.getString("login_html");
		if (StringUtils.isNotEmpty(login_html) && (StringUtils.endsWithIgnoreCase(login_html, ".html")
				|| StringUtils.endsWithIgnoreCase(login_html, ".htm"))) {
			try {
				if (StringUtils.startsWith(login_html, request.getContextPath())) {
					response.sendRedirect(login_html);
				} else {
					response.sendRedirect(request.getContextPath() + login_html);
				}
				return null;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return new ModelAndView("/login/login");
	}

	/**
	 * 注销
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String actorId = RequestUtils.getActorId(request);
		// 退出系统，清除session对象
		if (request.getSession(false) != null) {
			request.getSession().removeAttribute(SysConstants.LOGIN_USER);
		}
		try {
			SystemProperty p = SystemConfig.getProperty("login_limit");
			if (p != null && StringUtils.equals(p.getValue(), "true")) {
				userOnlineService.logout(actorId);
			}
			String cacheKey = Constants.LOGIN_USER_CACHE + actorId;
			CacheFactory.remove("loginContext", cacheKey);
			cacheKey = Constants.USER_CACHE + actorId;
			CacheFactory.remove("user", cacheKey);
			com.glaf.shiro.ShiroSecurity.logout();
			if (request.getSession(false) != null) {
				request.getSession().invalidate();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.prepareLogin(request, response, modelMap);
	}

	/**
	 * 准备登录
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping
	public ModelAndView prepareLogin(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		logger.debug("---------------------prepareLogin--------------------");
		RequestUtils.setRequestParameterToAttribute(request);

		String userId = request.getParameter("userId");
		String token = request.getParameter("token");

		if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(token)) {
			logger.debug("->token" + token);
			return this.doLogin(request, response, modelMap);
		}

		String login_html = SystemConfig.getString("login_html");
		if (StringUtils.isNotEmpty(login_html) && (StringUtils.endsWithIgnoreCase(login_html, ".html")
				|| StringUtils.endsWithIgnoreCase(login_html, ".htm"))) {
			try {
				if (StringUtils.startsWith(login_html, request.getContextPath())) {
					response.sendRedirect(login_html);
				} else {
					response.sendRedirect(request.getContextPath() + login_html);
				}
				return null;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		HttpSession session = request.getSession(true);
		java.util.Random random = new java.util.Random();
		String rand = Math.abs(random.nextInt(9999)) + com.glaf.core.util.UUID32.getUUID()
				+ Math.abs(random.nextInt(9999));
		String rand2 = Math.abs(random.nextInt(9999)) + com.glaf.core.util.UUID32.getUUID()
				+ Math.abs(random.nextInt(9999));
		if (session != null) {
			rand = Base64.encodeBase64String(rand.getBytes()) + com.glaf.core.util.UUID32.getUUID();
			rand2 = Base64.encodeBase64String(rand2.getBytes()) + com.glaf.core.util.UUID32.getUUID();
			session.setAttribute("x_y", rand);
			session.setAttribute("x_z", rand2);
		}

		String view = "/login/login";

		if (StringUtils.isNotEmpty(SystemConfig.getString("login_view"))) {
			view = SystemConfig.getString("login_view");
		}

		// 显示登陆页面
		return new ModelAndView(view, modelMap);
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