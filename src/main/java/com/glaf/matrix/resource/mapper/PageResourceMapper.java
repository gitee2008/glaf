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
package com.glaf.matrix.resource.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import com.glaf.matrix.resource.domain.*;
import com.glaf.matrix.resource.query.*;

/**
 * 
 * Mapper接口
 *
 */

@Component("com.glaf.matrix.resource.mapper.PageResourceMapper")
public interface PageResourceMapper {

	void deletePageResourceById(long id);

	void deletePageResources(PageResourceQuery query);

	PageResource getPageResourceByFileId(String fileId);

	PageResource getPageResourceById(long id);

	PageResource getPageResourceByPath(String path);

	int getPageResourceCount(PageResourceQuery query);

	List<PageResource> getPageResources(PageResourceQuery query);

	void insertPageResource(PageResource model);

	void updatePageResource(PageResource model);

}
