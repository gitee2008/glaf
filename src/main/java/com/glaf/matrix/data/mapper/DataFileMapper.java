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

package com.glaf.matrix.data.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.glaf.core.base.DataFile;
import com.glaf.matrix.data.query.DataFileQuery;

@Component
public interface DataFileMapper {

	void deleteDataFileById(DataFileQuery query);

	void deleteDataFiles(DataFileQuery query);

	void deleteDataFilesByBusinessKey(DataFileQuery query);

	void deleteDataFilesByFileId(DataFileQuery query);

	DataFile getDataFileById(DataFileQuery query);

	int getDataFileCount(DataFileQuery query);

	List<DataFile> getDataFiles(DataFileQuery query);

	List<DataFile> getDataFilesByFileId(DataFileQuery query);

	List<DataFile> getDataFilesByFilename(DataFileQuery query);

	void insertDataFile(DataFile model);

	void insertDataFile_postgres(DataFile model);

	void updateDataFile(DataFile model);

}