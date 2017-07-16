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

package com.glaf.report.gen;

import java.sql.Connection;
import java.util.Map;
import com.glaf.report.domain.Report;

public class FreemarkerReportGen extends AbstractReportGen implements ReportGen {

	public byte[] createReport(Report report, Connection connection,
			byte[] templateData, Map<String, Object> params) {
		try {
			String content = new String(templateData);
			String output = com.glaf.template.util.TemplateUtils.process(
					params, content);
			return output.getBytes("UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(
					"生成" + report.getReportFormat() + "文件出错", ex);
		}
	}

}