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

package com.glaf.matrix.sync.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import com.glaf.matrix.sync.domain.*;
import com.glaf.matrix.sync.query.*;

/**
 * 
 * Mapper接口
 *
 */

@Component("com.glaf.matrix.sync.mapper.SyncAppMapper")
public interface SyncAppMapper {

	void deleteSyncApps(SyncAppQuery query);

	void deleteSyncAppById(Long id);

	SyncApp getSyncAppById(Long id);

	int getSyncAppCount(SyncAppQuery query);

	List<SyncApp> getSyncApps(SyncAppQuery query);

	void insertSyncApp(SyncApp model);

	void updateSyncApp(SyncApp model);

}
