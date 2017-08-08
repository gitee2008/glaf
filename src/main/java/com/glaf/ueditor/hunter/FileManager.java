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

package com.glaf.ueditor.hunter;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.glaf.core.base.DataFile;
import com.glaf.matrix.data.factory.DataFileFactory;
import com.glaf.matrix.data.query.DataFileQuery;
import com.glaf.ueditor.PathFormat;
import com.glaf.ueditor.define.AppInfo;
import com.glaf.ueditor.define.BaseState;
import com.glaf.ueditor.define.MultiState;
import com.glaf.ueditor.define.State;

public class FileManager {

	private String dir = null;
	private String dbPath = null;
	private String rootPath = null;
	private String[] allowFiles = null;
	private int count = 0;

	public FileManager(Map<String, Object> conf) {
		this.rootPath = (String) conf.get("rootPath");
		this.dbPath = (String) conf.get("dbPath");
		this.dir = this.rootPath + (String) conf.get("dir");
		this.allowFiles = this.getAllowFiles(conf.get("allowFiles"));
		this.count = (Integer) conf.get("count");
	}

	private String[] getAllowFiles(Object fileExt) {

		String[] exts = null;
		String ext = null;

		if (fileExt == null) {
			return new String[0];
		}

		exts = (String[]) fileExt;

		for (int i = 0, len = exts.length; i < len; i++) {

			ext = exts[i];
			exts[i] = ext.replace(".", "");

		}

		return exts;

	}

	private String getPath(File file) {

		String path = file.getAbsolutePath();

		return path.replace(this.rootPath, "/");

	}

	private State getState(Object[] files) {

		MultiState state = new MultiState(true);
		BaseState fileState = null;

		File file = null;

		for (Object obj : files) {
			if (obj == null) {
				break;
			}
			file = (File) obj;
			fileState = new BaseState(true);
			fileState.putInfo("url", PathFormat.format(this.getPath(file)));
			state.addState(fileState);
		}

		return state;

	}

	public State listFile(int index) {

		File dir = new File(this.dir);
		State state = null;

		if (!dir.exists()) {
			return new BaseState(false, AppInfo.NOT_EXIST);
		}

		if (!dir.isDirectory()) {
			return new BaseState(false, AppInfo.NOT_DIRECTORY);
		}

		Collection<File> list = FileUtils.listFiles(dir, this.allowFiles, true);

		if (index < 0 || index > list.size()) {
			state = new MultiState(true);
		} else {
			Object[] fileList = Arrays.copyOfRange(list.toArray(), index, index + this.count);
			state = this.getState(fileList);
		}

		state.putInfo("start", index);
		state.putInfo("total", list.size());

		return state;

	}

	public State listFileFromDB(int index) {
		MultiState state = null;
		if (dbPath == null) {
			dbPath = "/ueditor/";
		}
		DataFileQuery query = new DataFileQuery();
		query.setPathLike(dbPath);
		List<DataFile> dataList = DataFileFactory.getInstance().getDataFileList(query);
		state = new MultiState(true);
		BaseState fileState = null;
		for (DataFile file : dataList) {
			fileState = new BaseState(true);
			fileState.putInfo("url", this.rootPath + file.getPath());
			state.addState(fileState);
		}
		state.putInfo("start", index);
		state.putInfo("total", dataList.size());

		return state;

	}

}
