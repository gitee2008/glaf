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

package com.glaf.remote.website.springmvc;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.glaf.base.modules.sys.model.IdentityToken;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.IdentityTokenService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.online.domain.UserOnline;
import com.glaf.base.online.service.UserOnlineService;
import com.glaf.base.utils.ContextUtil;

import com.glaf.core.config.Environment;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.domain.SystemProperty;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.IOUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.UUID32;
import com.glaf.core.web.callback.CallbackProperties;
import com.glaf.core.web.callback.LoginCallback;

import com.glaf.remote.domain.RemotePermission;
import com.glaf.remote.query.RemotePermissionQuery;
import com.glaf.remote.service.RemotePermissionService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/remote/login")
@RequestMapping("/remote/login")
public class RemoteLoginController {
	protected static final Log logger = LogFactory.getLog(RemoteLoginController.class);

	protected SysUserService sysUserService;

	protected UserOnlineService userOnlineService;

	protected IdentityTokenService identityTokenService;

	protected RemotePermissionService remotePermissionService;

	public RemoteLoginController() {

	}

	@RequestMapping("/doLogin")
	public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String token = request.getParameter("token");
		String ipAddr = RequestUtils.getIPAddress(request);
		try {
			if (StringUtils.isNotEmpty(token)) {
				IdentityToken tkModel = identityTokenService.getIdentityTokenByToken(token);
				if (tkModel != null && StringUtils.equals(ipAddr, tkModel.getClientIP())
						&& (System.currentTimeMillis() - tkModel.getTimeMillis() < tkModel.getTimeLive() * 1000)) {
					SysUser user = sysUserService.findById(tkModel.getUserId());
					if (user != null && user.getLocked() == 0 && !user.isSystemAdministrator()) {

						String account = user.getActorId();

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
							if (pt != null && StringUtils.equals(pt.getValue(), "true")) {
								logger.debug("#################3#########################");
								String loginIP = null;
								UserOnline userOnline = userOnlineService.getUserOnline(account);
								logger.debug("userOnline:" + userOnline);
								boolean timeout = false;
								if (userOnline != null) {
									loginIP = userOnline.getLoginIP();
									if (userOnline.getCheckDateMs() > 0 && System.currentTimeMillis()
											- userOnline.getCheckDateMs() > timeoutSeconds * 1000) {
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
										String responseDataType = request.getParameter("responseDataType");
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
											return new ModelAndView("/modules/login", modelMap);
										}
									}
								}
							}
						}

						HttpSession session = request.getSession(true);// 重写Session
						logger.debug("session:" + session.getId());

						user.setLastLoginDate(new Date());
						if (!user.isSystemAdministrator()) {
							user.setLastLoginIP(ipAddr);
						}
						user.setLockLoginTime(null);
						user.setLoginRetry(0);
						sysUserService.updateUser(user);
						ContextUtil.put(user.getActorId(), user);// 传入全局变量

						identityTokenService.deleteById(tkModel.getId());// 登录成功后删除一次性令牌。

						IdentityToken identityToken = new IdentityToken();
						java.util.Random random = new java.util.Random();
						identityToken
								.setId(DateUtils.getNowYearMonthDayHHmmss() + "_" + user.getActorId() + "_" + ipAddr);
						identityToken.setClientIP(ipAddr);
						identityToken.setNonce(UUID32.getUUID());
						identityToken.setTimeLive(7200);// 7200秒
						identityToken.setToken(StringTools.getRandomString(random.nextInt(100)));
						identityToken.setType("Session");
						identityToken.setUserId(user.getActorId());
						this.identityTokenService.save(identityToken);

						String systemName = Environment.getCurrentSystemName();

						if (StringUtils.isEmpty(systemName)) {
							systemName = "default";
						}

						RequestUtils.setLoginUser(request, response, systemName, user.getActorId());

						try {
							UserOnline online = new UserOnline();
							online.setActorId(user.getActorId());
							online.setName(user.getName());
							online.setCheckDate(new Date());
							online.setLoginDate(new Date());
							if (!user.isSystemAdministrator()) {
								online.setLoginIP(ipAddr);
							}
							userOnlineService.login(online);
						} catch (Exception ex) {
							// ex.printStackTrace();
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
											callback.afterLogin(user.getActorId(), request, response);
										} else {
											LoginCallbackThread command = new LoginCallbackThread(callback,
													user.getActorId(), request, response);
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

						LoginContext loginContext = IdentityFactory.getLoginContext(user.getActorId());
						logger.debug(loginContext.toJsonObject().toJSONString());
						String redirectUrl = request.getParameter("redirectUrl");
						if (StringUtils.isNotEmpty(redirectUrl)) {
							try {
								response.sendRedirect(redirectUrl);
								return null;
							} catch (IOException e) {
							}
						}

						String redirectUrl_enc = request.getParameter("redirectUrl_enc");
						if (StringUtils.isNotEmpty(redirectUrl_enc)) {
							try {
								response.sendRedirect(RequestUtils.decodeURL(redirectUrl_enc));
								return null;
							} catch (IOException e) {
							}
						}
						String responseDataType = request.getParameter("responseDataType");
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

						return new ModelAndView("/modules/main", modelMap);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/modules/login", modelMap);
	}

	@ResponseBody
	@RequestMapping("/getToken")
	public byte[] getToken(HttpServletRequest request) throws IOException {
		JSONObject json = new JSONObject();
		String type = request.getParameter("type");
		String userId = request.getParameter("userId");
		String clientIP = request.getParameter("clientIP");
		try {
			if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(userId)) {
				RemotePermissionQuery query = new RemotePermissionQuery();
				query.type(type);
				List<RemotePermission> list = remotePermissionService.list(query);
				if (list != null && !list.isEmpty()) {
					String remoteIP = RequestUtils.getIPAddress(request);
					for (RemotePermission p : list) {
						if (StringUtils.isNotEmpty(p.getRemoteIP())) {
							if (StringUtils.equals(remoteIP, p.getRemoteIP())) {
								SysUser user = sysUserService.findById(userId);
								if (user != null) {
									IdentityToken identityToken = new IdentityToken();
									java.util.Random random = new java.util.Random();
									identityToken.setId(DateUtils.getNowYearMonthDay() + "_" + userId + "_" + clientIP);
									identityToken.setClientIP(clientIP);
									identityToken.setNonce(UUID32.getUUID());
									identityToken.setTimeLive(60);// 60秒
									identityToken.setToken(StringTools.getRandomString(random.nextInt(200)));
									identityToken.setType("Login");
									identityToken.setUserId(userId);
									this.identityTokenService.save(identityToken);
									json.put("token", identityToken.getToken());
									break;
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		return json.toJSONString().getBytes();
	}

	@javax.annotation.Resource(name = "identityTokenService")
	public void setIdentityTokenService(IdentityTokenService identityTokenService) {
		this.identityTokenService = identityTokenService;
	}

	@javax.annotation.Resource(name = "com.glaf.remote.service.remotePermissionService")
	public void setRemotePermissionService(RemotePermissionService remotePermissionService) {
		this.remotePermissionService = remotePermissionService;
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
