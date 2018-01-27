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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

 
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;

import com.glaf.core.util.ClassUtils;
import com.glaf.core.util.StringTools;
import com.glaf.report.domain.Report;

public class Jxls2ReportGen extends AbstractReportGen implements ReportGen {

	public byte[] createReport(Report report, Connection connection, byte[] templateData, Map<String, Object> params) {
		Workbook template = null;
		Workbook destWorkbook = null;
		ByteArrayInputStream bais0 = null;
		BufferedInputStream bis0 = null;
		ByteArrayInputStream bais = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bos = null;
		try {
			bais0 = new ByteArrayInputStream(templateData);
			bis0 = new BufferedInputStream(bais0);

			template = WorkbookFactory.create(bis0);

			bais = new ByteArrayInputStream(templateData);
			bis = new BufferedInputStream(bais);

			baos = new ByteArrayOutputStream();
			bos = new BufferedOutputStream(baos);

			if (StringUtils.isNotEmpty(report.getPostProcessor())) {
				List<String> processors = StringTools.split(report.getPostProcessor());
				if (processors != null && !processors.isEmpty()) {
					for (String processorClass : processors) {
						try {
							Object processor = ClassUtils.instantiateObject(processorClass);
							if (processor instanceof XlsProcessor) {
								XlsProcessor p = (XlsProcessor) processor;
								p.process(destWorkbook);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}

			Context context2 = PoiTransformer.createInitialContext();

			Set<Entry<String, Object>> entrySet = params.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				context2.putVar(key, value);
				// logger.debug(key);
			}

			org.jxls.util.JxlsHelper.getInstance().processTemplate(bis, bos, context2);
			bos.flush();
			baos.flush();

			bos.flush();
			baos.flush();
			return baos.toByteArray();
		} catch (Exception ex) {
			// ex.printStackTrace();
			throw new RuntimeException("生成" + report.getReportFormat() + "文件出错", ex);
		} finally {
			if (template != null) {
				try {
					template.close();
				} catch (IOException e) {
				}
			}
			if (destWorkbook != null) {
				try {
					destWorkbook.close();
				} catch (IOException e) {
				}
			}
			com.glaf.core.util.IOUtils.closeStream(bais0);
			com.glaf.core.util.IOUtils.closeStream(bis0);
			com.glaf.core.util.IOUtils.closeStream(bais);
			com.glaf.core.util.IOUtils.closeStream(bis);
			com.glaf.core.util.IOUtils.closeStream(baos);
			com.glaf.core.util.IOUtils.closeStream(bos);
		}
	}

}