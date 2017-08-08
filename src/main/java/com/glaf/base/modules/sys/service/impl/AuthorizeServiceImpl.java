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

package com.glaf.base.modules.sys.service.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.base.config.BaseConfiguration;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.AuthorizeService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.core.config.Configuration;

@Service("authorizeService")
@Transactional
public class AuthorizeServiceImpl implements AuthorizeService {
	private static final Log logger = LogFactory.getLog(AuthorizeServiceImpl.class);

	private static Configuration conf = BaseConfiguration.create();

	private SysUserService sysUserService;

	/**
	 * 用户登陆
	 * 
	 * @param account
	 * @param pwd
	 * @return
	 */
	@Transactional
	public SysUser authorize(String account, String pwd) {
		SysUser bean = sysUserService.findByAccount(account);
		if (bean != null) {
			if (bean.isDepartmentAdmin()) {
				logger.debug(account + " is department admin");
			}
			if (bean.isSystemAdmin()) {
				logger.debug(account + " is system admin");
			}
			if (bean.getLocked() == 1) {// 帐号禁止
				return null;
			}
			if (!bean.isSystemAdmin()) {
				/**
				 * 当登录重试次数大于系统默认的重试次数并且登录时间间隔没有达到系统默认的时间间隔，登录失败
				 */
				if (bean.getLoginRetry() > conf.getInt("login.retryCount", 10)) {
					if (bean.getLockLoginTime() != null) {
						if (System.currentTimeMillis() - bean.getLockLoginTime().getTime() < 60 * 1000
								* conf.getInt("login.retryMinutes", 30)) {
							sysUserService.loginFailure(account);
							return null;
						}
					} else {
						return null;
					}
				}
			}
			boolean success = sysUserService.checkPassword(account, pwd);
			if (!success) {// 密码不匹配
				logger.debug(account + "密码不匹配!");
				sysUserService.loginFailure(account);
				bean = null;
			}
		}
		return bean;
	}

	/**
	 * 用户登陆
	 * 
	 * @param account
	 * @param loginSecret
	 * @return
	 */
	@Transactional
	public SysUser authorizeLoginSecret(String account, String loginSecret) {
		SysUser bean = sysUserService.findByAccount(account);
		if (bean != null) {
			if (bean.isDepartmentAdmin()) {
				logger.debug(account + " is department admin");
			}
			if (bean.isSystemAdmin()) {
				logger.debug(account + " is system admin");
			}
			if (bean.getLocked() == 1) {// 帐号禁止
				return null;
			}
			if (!bean.isSystemAdmin()) {
				/**
				 * 当登录重试次数大于系统默认的重试次数并且登录时间间隔没有达到系统默认的时间间隔，登录失败
				 */
				if (bean.getLoginRetry() > conf.getInt("login.retryCount", 10)) {
					if (bean.getLockLoginTime() != null) {
						if (System.currentTimeMillis() - bean.getLockLoginTime().getTime() < 60 * 1000
								* conf.getInt("login.retryMinutes", 30)) {
							sysUserService.loginFailure(account);
							logger.warn(account + " login locked!");
							return null;
						}
					} else {
						return null;
					}
				}
			}
			boolean success = sysUserService.checkLoginSecret(account, loginSecret);
			if (!success) {// 密码不匹配
				sysUserService.loginFailure(account);
				bean = null;
			}
		}
		return bean;
	}

	/**
	 * 用户登陆
	 * 
	 * @param account
	 * @param pwd
	 * @return
	 */
	@Transactional
	public SysUser login(String account) {
		SysUser bean = sysUserService.findByAccountWithAll(account);
		if (bean != null) {
			if (bean.isDepartmentAdmin()) {
				logger.debug(account + " is department admin");
			}
			if (bean.isSystemAdmin()) {
				logger.debug(account + " is system admin");
			}
		}
		return bean;
	}

	/**
	 * 用户登陆
	 * 
	 * @param account
	 * @param pwd
	 * @return
	 */
	@Transactional
	public SysUser login(String account, String pwd) {
		SysUser bean = sysUserService.findByAccountWithAll(account);
		if (bean != null) {
			if (bean.isDepartmentAdmin()) {
				logger.debug(account + " is department admin");
			}
			if (bean.isSystemAdmin()) {
				logger.debug(account + " is system admin");
			}
			if (bean.getLocked() == 1) {// 帐号禁止
				return null;
			}
			if (!bean.isSystemAdmin()) {
				/**
				 * 当登录重试次数大于系统默认的重试次数并且登录时间间隔没有达到系统默认的时间间隔，登录失败
				 */
				if (bean.getLoginRetry() > conf.getInt("login.retryCount", 5)) {
					if (bean.getLockLoginTime() != null) {
						if (System.currentTimeMillis() - bean.getLockLoginTime().getTime() < 60 * 1000
								* conf.getInt("login.retryMinutes", 30)) {
							sysUserService.loginFailure(account);
							return null;
						}
					}
				}
			}
			boolean success = sysUserService.checkPassword(account, pwd);
			if (!success) {// 密码不匹配
				sysUserService.loginFailure(account);
				bean = null;
			} else if (bean.getAccountType() != 1) {

			}
		}
		return bean;
	}

	@Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

}