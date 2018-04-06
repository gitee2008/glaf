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

package com.glaf.base.business;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.Constants;

import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.AuthorizeService;
import com.glaf.base.modules.sys.service.SysApplicationService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.modules.sys.util.SysUserJsonFactory;

public class AuthorizeBean {
	protected static final Log logger = LogFactory.getLog(AuthorizeBean.class);

	protected SysApplicationService sysApplicationService;

	protected AuthorizeService authorizeService;

	protected SysUserService sysUserService;

	public AuthorizeBean() {

	}

	public AuthorizeService getAuthorizeService() {
		if (authorizeService == null) {
			authorizeService = ContextFactory.getBean("authorizeService");
		}
		return authorizeService;
	}

	public SysApplicationService getSysApplicationService() {
		if (sysApplicationService == null) {
			sysApplicationService = ContextFactory.getBean("sysApplicationService");
		}
		return sysApplicationService;
	}

	public SysUserService getSysUserService() {
		if (sysUserService == null) {
			sysUserService = ContextFactory.getBean("sysUserService");
		}
		return sysUserService;
	}

	public SysUser getUser(String account) {
		SysUser sysUser = null;
		if (account != null) {
			String cacheKey = Constants.CACHE_USER_ALL_KEY + account;
			if (SystemConfig.getBoolean("use_query_cache")) {
				String text = CacheFactory.getString(Constants.CACHE_USER_REGION, cacheKey);
				if (StringUtils.isNotEmpty(text)) {
					try {
						JSONObject jsonObject = JSON.parseObject(text);
						sysUser = SysUserJsonFactory.jsonToObject(jsonObject);
					} catch (Exception ex) {
					}
				}
			}
			if (sysUser == null) {
				sysUser = getSysUserService().findByAccountWithAll(account);
				if (sysUser != null && SystemConfig.getBoolean("use_query_cache")) {
					CacheFactory.put(Constants.CACHE_USER_REGION, cacheKey, sysUser.toJsonObject().toJSONString());
				}
			}
		}

		return sysUser;
	}

	public void setAuthorizeService(AuthorizeService authorizeService) {
		this.authorizeService = authorizeService;
	}

	public void setSysApplicationService(SysApplicationService sysApplicationService) {
		this.sysApplicationService = sysApplicationService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

}