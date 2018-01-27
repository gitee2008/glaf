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

package com.glaf.matrix.transform.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import com.glaf.matrix.transform.domain.*;
import com.glaf.matrix.transform.query.*;

/**
 * 
 * Mapper接口
 *
 */

@Component("com.glaf.matrix.transform.mapper.RowDenormaliserMapper")
public interface RowDenormaliserMapper {

	void bulkInsertRowDenormaliser(List<RowDenormaliser> list);

	void bulkInsertRowDenormaliser_oracle(List<RowDenormaliser> list);

	void deleteRowDenormalisers(RowDenormaliserQuery query);

	void deleteRowDenormaliserById(String id);

	RowDenormaliser getRowDenormaliserById(String id);

	int getRowDenormaliserCount(RowDenormaliserQuery query);

	List<RowDenormaliser> getRowDenormalisers(RowDenormaliserQuery query);

	void insertRowDenormaliser(RowDenormaliser model);

	void updateRowDenormaliser(RowDenormaliser model);

}
