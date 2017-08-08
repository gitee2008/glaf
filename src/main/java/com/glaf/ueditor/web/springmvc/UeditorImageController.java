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

package com.glaf.ueditor.web.springmvc;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.base.DataFile;
import com.glaf.core.util.Paging;
import com.glaf.core.util.RequestUtils;
import com.glaf.matrix.data.query.DataFileQuery;
import com.glaf.matrix.data.service.IDataFileService;

@Controller("/ueditor/image")
@RequestMapping("/ueditor/image")
public class UeditorImageController {

	protected IDataFileService dataFileService;

	@RequestMapping("/json")
	@ResponseBody
	public byte[] json(HttpServletRequest request, HttpServletResponse response, String action) throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		int pageNo = RequestUtils.getInteger(request, "page", 1);
		int limit = RequestUtils.getInteger(request, "rows", 10);

		int start = (pageNo - 1) * limit;
		if (start < 0) {
			start = 0;
		}
		if (limit <= 0) {
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		JSONObject result = new JSONObject();
		DataFileQuery query = new DataFileQuery();
		int total = dataFileService.getDataFileCountByQueryCriteria(query);
		if (total > 0) {
			result.put("total", total);
			result.put("start", start);
			result.put("page", pageNo);
			result.put("pageSize", limit);

			List<DataFile> list = dataFileService.getDataFileList(start, limit, query);
			if (list != null && !list.isEmpty()) {
				JSONArray rowsJSON = new JSONArray();
				result.put("rows", rowsJSON);
				for (DataFile resource : list) {
					JSONObject rowJSON = resource.toJsonObject();
					rowJSON.put("id", resource.getId());
					rowJSON.put("path", resource.getPath());
					rowJSON.put("filename", resource.getFilename());
					rowJSON.put("startIndex", ++start);
					rowsJSON.add(rowJSON);
				}
			}
		} else {
			JSONArray rowsJSON = new JSONArray();
			result.put("rows", rowsJSON);
			result.put("total", total);
		}
		return result.toJSONString().getBytes("UTF-8");
	}

	@javax.annotation.Resource
	public void setDataFileService(IDataFileService dataFileService) {
		this.dataFileService = dataFileService;
	}

}
