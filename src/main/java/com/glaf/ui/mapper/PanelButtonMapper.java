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
package com.glaf.ui.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.glaf.ui.model.PanelButton;
import com.glaf.ui.query.PanelButtonQuery;

/**
 * 
 * Mapper接口
 *
 */

@Component("com.glaf.ui.mapper.PanelButtonMapper")
public interface PanelButtonMapper {

	void deletePanelButtons(PanelButtonQuery query);

	void deletePanelButtonById(String id);

	PanelButton getPanelButtonById(String id);

	int getPanelButtonCount(PanelButtonQuery query);

	List<PanelButton> getPanelButtons(PanelButtonQuery query);

	void insertPanelButton(PanelButton model);

	void updatePanelButton(PanelButton model);
	
	List<PanelButton> getPanelButtonByParentId(String pid);

}
