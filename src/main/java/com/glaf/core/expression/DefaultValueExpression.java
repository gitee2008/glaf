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
package com.glaf.core.expression;

import java.util.Date;
import java.util.Map;

import com.glaf.core.el.ExpressionTools;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ExpressionConstants;
import com.glaf.core.util.UUID32;

public class DefaultValueExpression implements ValueExpression {

	public Object evaluate(String expr, Map<String, Object> context) {
		Object value = null;
		if (ExpressionConstants.NOW_EXPRESSION.equals(expr)) {
			value = new Date();
		}
		if (ExpressionConstants.CURRENT_YYYY_EXPRESSION.equals(expr)) {
			value = DateUtils.getNowYearMonth() / 100;
		}
		if (ExpressionConstants.CURRENT_YYYYMM_EXPRESSION.equals(expr)) {
			value = DateUtils.getNowYearMonth();
		}
		if (ExpressionConstants.CURRENT_YYYYMMDD_EXPRESSION.equals(expr)) {
			value = DateUtils.getNowYearMonthDay();
		}
		if (ExpressionConstants.CURRENT_TIMEMILLIS_EXPRESSION.equals(expr)) {
			value = System.currentTimeMillis();
		}
		if (ExpressionConstants.ID_EXPRESSION.equals(expr)) {

		}
		if (ExpressionConstants.UUID_EXPRESSION.equals(expr)) {
			value = UUID32.getUUID();
		}
		if (value == null) {
			value = ExpressionTools.evaluate(expr, context);
		}
		return value;
	}

}
