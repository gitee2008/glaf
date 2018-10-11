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

package com.glaf.sms.send;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.endpoint.DefaultEndpointResolver;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.glaf.core.util.ParamUtils;

/**
 * Created on 17/6/7. 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可) 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar 2:aliyun-java-sdk-dysmsapi.jar
 *
 * 备注:Demo工程编码采用UTF-8 国际短信发送请勿参照此DEMO
 */
public class AliyunSmsSenderUtils {
	protected final static Logger logger = LoggerFactory.getLogger(AliyunSmsSenderUtils.class);

	// 产品名称:云通信短信API产品,开发者无需替换
	static final String PRODUCT = "Dysmsapi";

	// 产品域名,开发者无需替换
	static final String DOMAIN = "dysmsapi.aliyuncs.com";

	public static QuerySendDetailsResponse querySendDetails(String bizId, String phoneNumber, String accessKeyId,
			String accessKeySecret) throws ClientException {
		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "60000");
		System.setProperty("sun.net.client.defaultReadTimeout", "60000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		// DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
		DefaultEndpointResolver.predefinedEndpointResolver.putEndpointEntry("cn-hangzhou", PRODUCT, DOMAIN);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象
		QuerySendDetailsRequest request = new QuerySendDetailsRequest();
		// 必填-号码
		request.setPhoneNumber(phoneNumber);
		// 可选-流水号
		request.setBizId(bizId);
		// 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		request.setSendDate(ft.format(new Date()));
		// 必填-页大小
		request.setPageSize(10L);
		// 必填-当前页码从1开始计数
		request.setCurrentPage(1L);

		// hint 此处可能会抛出异常，注意catch
		QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

		return querySendDetailsResponse;
	}

	public static Map<String, Object> send(Map<String, Object> params) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			SendSmsResponse response = sendSms(params);
			if (response.getCode() != null && response.getCode().equals("OK")) {
				String phoneNumber = ParamUtils.getString(params, "phoneNumber");
				String accessKeyId = ParamUtils.getString(params, "accessKeyId");
				String accessKeySecret = ParamUtils.getString(params, "accessKeySecret");
				QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId(), phoneNumber,
						accessKeyId, accessKeySecret);
				JSONArray details = new JSONArray();

				for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse
						.getSmsSendDetailDTOs()) {
					JSONObject detailDTO = new JSONObject();
					detailDTO.put("Content", smsSendDetailDTO.getContent());
					detailDTO.put("PhoneNum", smsSendDetailDTO.getPhoneNum());
					detailDTO.put("SendDate", smsSendDetailDTO.getSendDate());
					details.add(detailDTO);
				}

				dataMap.put("code", 200);
				dataMap.put("msg", details.toString());

			} else {
				dataMap.put("code", 400);
			}

		} catch (ClientException ex) {
			dataMap.put("code", 400);
			logger.error("Aliyun Sms Send Error", ex);
		}
		return dataMap;
	}

	// public static void main(String args[]) throws ClientException{
	// //发短信
	// SendSmsResponse response = sendSms();
	// System.out.println("短信接口返回的数据----------------");
	// System.out.println("Code=" + response.getCode());
	// System.out.println("Message=" + response.getMessage());
	// System.out.println("RequestId=" + response.getRequestId());
	// System.out.println("BizId=" + response.getBizId());
	// }

	public static SendSmsResponse sendSms(Map<String, Object> params) throws ClientException {
		// 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
		String accessKeyId = ParamUtils.getString(params, "accessKeyId");
		String accessKeySecret = ParamUtils.getString(params, "accessKeySecret");
		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "60000");
		System.setProperty("sun.net.client.defaultReadTimeout", "60000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		//DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
		DefaultEndpointResolver.predefinedEndpointResolver.putEndpointEntry("cn-hangzhou", PRODUCT, DOMAIN);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(ParamUtils.getString(params, "phoneNumber"));
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(ParamUtils.getString(params, "signName"));
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(ParamUtils.getString(params, "templateCode"));
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam("{\"code\":\"" + ParamUtils.getString(params, "randomCode") + "\"}");

		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("yourOutId");

		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

		return sendSmsResponse;
	}

	private AliyunSmsSenderUtils() {

	}

}
