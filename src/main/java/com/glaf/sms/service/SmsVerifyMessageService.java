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

package com.glaf.sms.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.sms.domain.*;
import com.glaf.sms.query.*;

@Transactional(readOnly = true)
public interface SmsVerifyMessageService {

	@Transactional
	void bulkInsert(List<SmsVerifyMessage> list);

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(String id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(List<String> ids);

	int getSmsCount(String mobile, long dateAfter);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	SmsVerifyMessage getSmsVerifyMessage(String id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getSmsVerifyMessageCountByQueryCriteria(SmsVerifyMessageQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<SmsVerifyMessage> getSmsVerifyMessagesByQueryCriteria(int start, int pageSize, SmsVerifyMessageQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<SmsVerifyMessage> list(SmsVerifyMessageQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(SmsVerifyMessage smsVerifyMessage);
	
	@Transactional
	void update(SmsVerifyMessage smsVerifyMessage);

	/**
	 * 验证在某个时间以后的验证码是否正确
	 * 
	 * @param mobile
	 *            手机号码
	 * @param verificationCode
	 *            验证码
	 * @param dateAfter
	 *            最后时间戳
	 * @return
	 */
	boolean verify(String mobile, String verificationCode, long dateAfter);

}
