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

package com.glaf.sms.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.glaf.sms.domain.SmsClient;
import com.glaf.sms.query.SmsClientQuery;

/**
 * 
 * Mapper接口
 *
 */

@Component("com.glaf.sms.mapper.SmsClientMapper")
public interface SmsClientMapper {

	void bulkInsertSmsClient(List<SmsClient> list);

	void bulkInsertSmsClient_oracle(List<SmsClient> list);

	void deleteSmsClientById(String id);

	SmsClient getSmsClientById(String id);

	SmsClient getSmsClientBySysCode(String sysCode);

	int getSmsClientCount(SmsClientQuery query);

	List<SmsClient> getSmsClients(SmsClientQuery query);

	void insertSmsClient(SmsClient model);

	void updateSmsClient(SmsClient model);

}
