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
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.core.util.Tools;
import com.glaf.sms.domain.SmsVerifyMessage;
import com.glaf.sms.query.SmsVerifyMessageQuery;
import com.glaf.sms.send.SmsSendFactory;
import com.glaf.sms.service.SmsVerifyMessageService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/sms/smsVerifyMessage")
@RequestMapping("/sms/smsVerifyMessage")
public class SmsVerifyMessageController {
	protected static final Log logger = LogFactory.getLog(SmsVerifyMessageController.class);

	protected SysUserService sysUserService;

	protected SmsVerifyMessageService smsVerifyMessageService;

	public SmsVerifyMessageController() {

	}

	@ResponseBody
	@RequestMapping("/createLoginSms")
	public byte[] createLoginSms(HttpServletRequest request) {
		String mobile = request.getParameter("mobile");
		SmsVerifyMessage smsVerifyMessage = null;
		try {
			if (StringUtils.isNotEmpty(mobile) && StringUtils.isNumeric(mobile)) {
				long num = Long.parseLong(mobile);
				num = num / 100000000;
				logger.debug("num=" + num);
				if (num >= 130 && num <= 199) {
					SysUser user = sysUserService.findByMobile(mobile);
					if (user != null && user.getLocked() == 0) {
						long dateAfter = System.currentTimeMillis() - DateUtils.DAY;
						if (smsVerifyMessageService.getSmsCount(mobile, dateAfter) <= 10) {
							String id = mobile + "_" + DateUtils.getNowYearMonthDayHHmm();
							smsVerifyMessage = smsVerifyMessageService.getSmsVerifyMessage(id);
							if (smsVerifyMessage == null) {
								smsVerifyMessage = new SmsVerifyMessage();
								smsVerifyMessage.setName(request.getParameter("name"));
								smsVerifyMessage.setMobile(mobile);
								smsVerifyMessage.setType("Login");
								java.util.Random rand = new java.util.Random();
								smsVerifyMessage.setVerificationCode(String.valueOf(100000 + rand.nextInt(900000)));

								this.smsVerifyMessageService.save(smsVerifyMessage);

								//VerifyMessageSmsSender sender = new VerifyMessageSmsSender();
								//sender.setSmsVerifyMessageService(smsVerifyMessageService);
								//sender.sendSms(smsVerifyMessage);
								
								SmsSendFactory.sendSms("Login", smsVerifyMessage);

								return ResponseUtils.responseJsonResult(true);
							} else {
								if (smsVerifyMessage.getStatus() != 9) {
									//VerifyMessageSmsSender sender = new VerifyMessageSmsSender();
									//sender.setSmsVerifyMessageService(smsVerifyMessageService);
									//sender.sendSms(smsVerifyMessage);
									SmsSendFactory.sendSms("Login", smsVerifyMessage);

									return ResponseUtils.responseJsonResult(true);
								}
							}
						} else {
							return ResponseUtils.responseJsonResult(false, "发送时间太频繁，请稍候再试。");
						}
					} else {
						return ResponseUtils.responseJsonResult(false, "手机号码不正确！");
					}
				} else {
					return ResponseUtils.responseJsonResult(false, "手机号码不正确！");
				}
			} else {
				return ResponseUtils.responseJsonResult(false, "手机号码不正确！");
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/createRegSms")
	public byte[] createRegSms(HttpServletRequest request) {
		String mobile = request.getParameter("mobile");
		SmsVerifyMessage smsVerifyMessage = null;
		try {
			if (StringUtils.isNotEmpty(mobile) && StringUtils.isNumeric(mobile)) {
				long num = Long.parseLong(mobile);
				num = num / 100000000;
				logger.debug("num=" + num);
				if (num >= 130 && num <= 199) {
					long dateAfter = System.currentTimeMillis() - DateUtils.DAY;
					if (smsVerifyMessageService.getSmsCount(mobile, dateAfter) <= 10) {
						String id = mobile + "_" + DateUtils.getNowYearMonthDayHHmm();
						smsVerifyMessage = smsVerifyMessageService.getSmsVerifyMessage(id);
						if (smsVerifyMessage == null) {
							smsVerifyMessage = new SmsVerifyMessage();
							smsVerifyMessage.setName(request.getParameter("name"));
							smsVerifyMessage.setMobile(mobile);
							smsVerifyMessage.setType("REG");
							java.util.Random rand = new java.util.Random();
							smsVerifyMessage.setVerificationCode(String.valueOf(1000 + rand.nextInt(9000)));

							this.smsVerifyMessageService.save(smsVerifyMessage);

							//VerifyMessageSmsSender sender = new VerifyMessageSmsSender();
							//sender.setSmsVerifyMessageService(smsVerifyMessageService);
							//sender.sendSms(smsVerifyMessage);
							SmsSendFactory.sendSms("REG", smsVerifyMessage);

							return ResponseUtils.responseJsonResult(true);
						} else {
							if (smsVerifyMessage.getStatus() != 9) {
								//VerifyMessageSmsSender sender = new VerifyMessageSmsSender();
								//sender.setSmsVerifyMessageService(smsVerifyMessageService);
								//sender.sendSms(smsVerifyMessage);
								
								SmsSendFactory.sendSms("REG", smsVerifyMessage);

								return ResponseUtils.responseJsonResult(true);
							}
						}
					} else {
						return ResponseUtils.responseJsonResult(false, "发送时间太频繁，请稍候再试。");
					}
				} else {
					return ResponseUtils.responseJsonResult(false, "手机号码不正确");
				}
			} else {
				return ResponseUtils.responseJsonResult(false, "手机号码不正确");
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, ModelMap modelMap) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		SmsVerifyMessageQuery query = new SmsVerifyMessageQuery();
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
		int total = smsVerifyMessageService.getSmsVerifyMessageCountByQueryCriteria(query);
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

			List<SmsVerifyMessage> list = smsVerifyMessageService.getSmsVerifyMessagesByQueryCriteria(start, limit,
					query);

			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();

				result.put("rows", rowsJSON);

				for (SmsVerifyMessage smsVerifyMessage : list) {
					JSONObject rowJSON = smsVerifyMessage.toJsonObject();
					rowJSON.put("id", smsVerifyMessage.getId());
					rowJSON.put("rowId", smsVerifyMessage.getId());
					rowJSON.put("smsVerifyMessageId", smsVerifyMessage.getId());
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

		return new ModelAndView("/sms/smsVerifyMessage/list", modelMap);
	}

	@javax.annotation.Resource(name = "com.glaf.sms.service.smsVerifyMessageService")
	public void setSmsVerifyMessageService(SmsVerifyMessageService smsVerifyMessageService) {
		this.smsVerifyMessageService = smsVerifyMessageService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@ResponseBody
	@RequestMapping("/verifySms")
	public byte[] verifySms(HttpServletRequest request) {
		String mobile = request.getParameter("mobile");
		String verificationCode = request.getParameter("verificationCode");
		long dateAfter = System.currentTimeMillis() - DateUtils.MINUTE * 10;
		boolean isOK = smsVerifyMessageService.verify(mobile, verificationCode, dateAfter);
		if (isOK) {
			return ResponseUtils.responseJsonResult(true);
		} else {
			return ResponseUtils.responseJsonResult(false, "验证码不正确");
		}
	}

}
