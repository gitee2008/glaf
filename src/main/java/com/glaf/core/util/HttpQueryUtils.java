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

package com.glaf.core.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpQueryUtils {
	protected final static Log logger = LogFactory.getLog(HttpQueryUtils.class);

	protected static Boolean getBooleanValue(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (request.getAttribute(name) != null) {
			value = (String) request.getAttribute(name);
		}
		if (StringUtils.equalsIgnoreCase(value, "true")) {
			return true;
		}
		return false;
	}

	protected static Object getObjectValue(HttpServletRequest request, String name) {
		Object value = getParameter(request, name);
		if (request.getAttribute(name) != null) {
			value = request.getAttribute(name);
		}
		return value;
	}

	public static String getParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (StringUtils.isEmpty(value)) {
			String[] values = request.getParameterValues(name);
			if (values != null && values.length > 0) {
				StringBuilder buff = new StringBuilder(1000);
				for (int i = 0; i < values.length; i++) {
					if (i < values.length - 1) {
						if (StringUtils.isNotEmpty(values[i])) {
							buff.append(values[i]).append(',');
						}
					} else {
						if (StringUtils.isNotEmpty(values[i])) {
							buff.append(values[i]);
						}
					}
				}
				if (StringUtils.isNotEmpty(buff.toString())) {
					value = buff.toString();
				}
			}
		}
		return value;
	}

	protected static String getStringValue(HttpServletRequest request, String name) {
		String value = getParameter(request, name);
		if (request.getAttribute(name) != null) {
			value = (String) request.getAttribute(name);
		}
		return value;
	}

}