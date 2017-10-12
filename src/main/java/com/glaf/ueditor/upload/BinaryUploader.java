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

package com.glaf.ueditor.upload;

import com.glaf.core.security.LoginContext;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.UUID32;
import com.glaf.ueditor.PathFormat;
import com.glaf.ueditor.define.AppInfo;
import com.glaf.ueditor.define.BaseState;
import com.glaf.ueditor.define.FileType;
import com.glaf.ueditor.define.State;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class BinaryUploader {

	public static final State save(HttpServletRequest request, Map<String, Object> conf) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files = multipartRequest.getFiles((String) conf.get("fieldName"));
		boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		if (isAjaxUpload) {
			try {
				multipartRequest.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		try {
			Iterator<MultipartFile> iterator = files.iterator();
			MultipartFile file = null;
			while (iterator.hasNext()) {
				file = iterator.next();
			}

			if (file == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			String savePath = (String) conf.get("savePath");
			String originFileName = file.getOriginalFilename();
			String fileRelName = file.getOriginalFilename();
			if (originFileName != null) {
				String fileType = FileType.getSuffixByFilename(originFileName);
				String contentType = file.getContentType();
				originFileName = originFileName.substring(0, originFileName.length() - fileType.length());
				savePath = savePath + fileType;

				long maxSize = ((Long) conf.get("maxSize")).longValue();

				if (!validType(fileType, (String[]) conf.get("allowFiles"))) {
					return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
				}

				savePath = PathFormat.parse(savePath, originFileName);

				LoginContext loginContext = RequestUtils.getLoginContext(request);
				String fileId = UUID32.getUUID();

				State storageState = StorageManager.saveFile(loginContext.getTenantId(), loginContext.getActorId(),
						fileId, file.getBytes(), contentType, savePath, originFileName + fileType, fileRelName,
						"ueditor", maxSize);

				if (storageState.isSuccess()) {
					storageState.putInfo("url", PathFormat.format(savePath));
					storageState.putInfo("type", fileType);
					storageState.putInfo("original", originFileName + fileType);
				}

				return storageState;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		}
		return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
