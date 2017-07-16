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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.util.ParamUtils;

public class ExpressionFactory {

	private static class ExpressionFactoryHolder {
		public static ExpressionFactory instance = new ExpressionFactory();
	}

	private static ValueExpression defaultValueExpression = new DefaultValueExpression();

	public static ExpressionFactory getInstance() {
		return ExpressionFactoryHolder.instance;
	}

	protected final ConcurrentMap<String, ValueExpression> concurrentMap = new ConcurrentHashMap<String, ValueExpression>();

	private ExpressionFactory() {

	}

	public Object evaluate(String expr, Map<String, Object> context) {
		ValueExpression valueExpression = null;
		if (context.containsKey("evaluator")) {
			valueExpression = concurrentMap.get(ParamUtils.getString(context, "evaluator"));
		}
		if (valueExpression == null) {
			valueExpression = defaultValueExpression;
		}
		return valueExpression.evaluate(expr, context);
	}

	public void setEvaluator(String key, ValueExpression evaluator) {
		if (key != null && evaluator != null) {
			concurrentMap.put(key, evaluator);
		}
	}
}