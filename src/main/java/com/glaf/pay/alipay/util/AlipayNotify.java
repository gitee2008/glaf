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

package com.glaf.pay.alipay.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.glaf.pay.alipay.config.AlipayConfig;

public class AlipayNotify {
	/**
	 * *功能：获取远程服务器ATN结果
	 * 
	 * @param urlValue
	 *            指定URL路径地址
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	public static String checkUrl(String urlValue) {
		String inputLine = "";
		try {
			URL url = new URL(urlValue);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			inputLine = in.readLine();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return inputLine;
	}

	/**
	 * *功能：根据反馈回来的信息，生成签名结果
	 * 
	 * @param params
	 *            通知返回来的参数数组
	 * @param key
	 *            安全校验码
	 * @return 生成的签名结果
	 */
	public static String getMySign(Map<String, Object> params, String key) {
		Map<String, Object> sParaNew = AlipayFunction.paraFilter(params);// 过滤空值、sign与sign_type参数
		String mysign = AlipayFunction.buildSign(sParaNew, key);// 获得签名结果
		return mysign;
	}

	/**
	 * *功能：获取远程服务器ATN结果,验证返回URL
	 * 
	 * @param notify_id
	 *            通知校验ID
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	public static String verify(String notify_id) {
		// 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
		String transport = AlipayConfig.transport;
		String partner = AlipayConfig.partner;
		String veryfy_url = "";
		if (transport.equalsIgnoreCase("https")) {
			veryfy_url = "https://www.alipay.com/cooperate/gateway.do?service=notify_verify";
		} else {
			veryfy_url = "http://notify.alipay.com/trade/notify_query.do?";
		}
		veryfy_url = veryfy_url + "&partner=" + partner + "¬ify_id=" + notify_id;

		String responseTxt = checkUrl(veryfy_url);

		return responseTxt;
	}
}