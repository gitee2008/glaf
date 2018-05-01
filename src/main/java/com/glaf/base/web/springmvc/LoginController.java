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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.glaf.base.handler.LoginHandler;
import com.glaf.base.handler.PasswordLoginHandler;
import com.glaf.base.modules.sys.model.IdentityToken;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.AuthorizeService;
import com.glaf.base.modules.sys.service.IdentityTokenService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.online.domain.UserOnline;
import com.glaf.base.online.service.UserOnlineService;
import com.glaf.base.utils.ContextUtil;
import com.glaf.base.utils.ParamUtil;

import com.glaf.core.config.Environment;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.domain.SystemProperty;
import com.glaf.core.identity.User;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.IOUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.UUID32;
import com.glaf.core.util.security.RSAUtils;
import com.glaf.core.web.callback.CallbackProperties;
import com.glaf.core.web.callback.LoginCallback;

@Controller("/login")
@RequestMapping("/login")
public class LoginController {
	private static final Log logger = LogFactory.getLog(LoginController.class);

	protected AuthorizeService authorizeService;

	protected SysUserService sysUserService;

	protected UserOnlineService userOnlineService;

	protected IdentityTokenService identityTokenService;

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
		String password = ParamUtil.getParameter(request, "y");

		account = StringEscapeUtils.escapeHtml(account);

		String tk = request.getParameter("token");
		IdentityToken token = identityTokenService.getIdentityTokenByToken(tk);

		if (token == null) {
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
				} catch (Exception ex) {
				} finally {
					IOUtils.closeStream(output);
				}
			}
			return new ModelAndView("/login/login", modelMap);
		}

		SysUser bean = null;
		if (StringUtils.isNotEmpty(account) && StringUtils.isNotEmpty(password)) {
			try {
				LoginHandler handler = new PasswordLoginHandler();
				bean = handler.doLogin(request, response, token);
			} catch (Exception ex) {
				logger.error(ex);
			} finally {
				identityTokenService.deleteById(token.getId());// 一次性令牌，登录成功即删除
			}
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
				} catch (Exception ex) {
				} finally {
					IOUtils.closeStream(output);
				}
			}
			return new ModelAndView("/login/login", modelMap);
		} else {
			String ipAddr = RequestUtils.getIPAddress(request);
			SystemProperty p = SystemConfig.getProperty("login_limit");
			if (!(StringUtils.equals(account, "admin"))) {
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
						if (userOnline.getCheckDateMs() != 0
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

			// 登录成功，修改最近一次登录时间
			bean.setLastLoginDate(new Date());
			bean.setLastLoginIP(ipAddr);
			bean.setLockLoginTime(null);
			bean.setLoginRetry(0);
			sysUserService.updateUserLoginInfo(bean);

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
						// ex.printStackTrace();
						logger.error(ex);
					}
				}
			}

			LoginContext loginContext = IdentityFactory.getLoginContext(bean.getActorId());
			logger.debug("actorId:" + RequestUtils.getActorId(request));
			logger.debug(loginContext.getActorId() + "登录成功。");
			// logger.debug(loginContext.toJsonObject().toJSONString());

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
				response.sendRedirect(request.getContextPath() + "/my/home");
			} catch (IOException e) {
			}
			return null;
		}
	}

	@ResponseBody
	@RequestMapping("/getToken")
	public void getToken(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String userId = request.getParameter("userId");
		logger.debug("userId:" + userId);
		if (StringUtils.isNotEmpty(userId)) {
			OutputStream output = null;
			try {
				User user = authorizeService.getUser(userId);
				if (user != null && ContextUtil.increaseUser(userId) < 500) {
					IdentityToken identityToken = new IdentityToken();
					java.util.Random random = new java.util.Random();
					String rand1 = StringTools.getRandomString(random.nextInt(50));
					String rand2 = StringTools.getRandomString(random.nextInt(50));
					String ip = RequestUtils.getIPAddress(request);
					identityToken.setId(DateUtils.getNowYearMonthDay() + "_" + userId + "_" + ip);
					identityToken.setClientIP(ip);
					identityToken.setNonce(UUID32.getUUID());
					identityToken.setRand1(rand1);
					identityToken.setRand2(rand2);
					identityToken.setTimeLive(30);// 30秒
					identityToken.setToken(StringTools.getRandomString(random.nextInt(100)));
					identityToken.setType("Login");
					identityToken.setUserId(userId);
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
					// logger.debug("----------------------------getLoginToken--------------");
					// logger.debug(json.toJSONString());
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
		try {
			userOnlineService.logout(actorId);
			// String cacheKey = Constants.CACHE_LOGIN_CONTEXT_KEY + actorId;
			// CacheFactory.remove(Constants.CACHE_LOGIN_CONTEXT_REGION, cacheKey);
			// cacheKey = Constants.CACHE_USER_KEY + actorId;
			// CacheFactory.remove(Constants.CACHE_USER_REGION, cacheKey);
			com.glaf.shiro.ShiroSecurity.logout();
			if (request.getSession(false) != null) {
				// 退出系统，清除session对象
				RequestUtils.removeLoginUser(request, response);
				request.getSession().removeAttribute(Constants.LOGIN_INFO);
				request.getSession().invalidate();
			}
		} catch (Exception ex) {
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

		if (SystemConfig.getBoolean("enableAutoReg")) {
			request.setAttribute("enableAutoReg", true);
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

		String view = "/login/login";

		if (StringUtils.isNotEmpty(SystemConfig.getString("login_view"))) {
			view = SystemConfig.getString("login_view");
		}

		// 显示登陆页面
		return new ModelAndView(view, modelMap);
	}

	@javax.annotation.Resource
	public void setAuthorizeService(AuthorizeService authorizeService) {
		this.authorizeService = authorizeService;
	}

	@javax.annotation.Resource(name = "identityTokenService")
	public void setIdentityTokenService(IdentityTokenService identityTokenService) {
		this.identityTokenService = identityTokenService;
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