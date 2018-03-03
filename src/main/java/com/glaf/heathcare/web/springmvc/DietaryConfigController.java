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

package com.glaf.heathcare.web.springmvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.domain.Database;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.heathcare.util.DietaryCountDomainFactory;
import com.glaf.heathcare.util.DietaryDomainFactory;
import com.glaf.heathcare.util.DietaryItemDomainFactory;
import com.glaf.heathcare.util.GoodsAcceptanceDomainFactory;
import com.glaf.heathcare.util.GoodsActualQuantityDomainFactory;
import com.glaf.heathcare.util.GoodsInStockDomainFactory;
import com.glaf.heathcare.util.GoodsOutStockDomainFactory;
import com.glaf.heathcare.util.GoodsPlanQuantityDomainFactory;
import com.glaf.heathcare.util.GoodsPurchaseDomainFactory;
import com.glaf.heathcare.util.GoodsPurchasePlanDomainFactory;
import com.glaf.heathcare.util.GoodsStockDomainFactory;
import com.glaf.heathcare.util.MealFeeCountDomainFactory;
import com.glaf.heathcare.util.PersonAbsenceDomainFactory;
import com.glaf.matrix.data.util.DataFileDomainFactory;

@Controller("/sys/dietaryConfig")
@RequestMapping("/sys/dietaryConfig")
public class DietaryConfigController {
	protected static final Log logger = LogFactory.getLog(DietaryConfigController.class);

	@ResponseBody
	@RequestMapping("/updateSchema")
	public byte[] updateSchema(HttpServletRequest request) {
		long databaseId = RequestUtils.getLong(request, "databaseId");
		if (databaseId > 0) {
			DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
			Database database = cfg.getDatabase(databaseId);
			if (StringUtils.equals(database.getUseType(), "GENERAL")) {
				try {
					DietaryDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DietaryDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DietaryItemDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DietaryItemDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DietaryCountDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DietaryCountDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsAcceptanceDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsAcceptanceDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsActualQuantityDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsActualQuantityDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsInStockDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsInStockDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsOutStockDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsOutStockDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsStockDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsStockDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsPlanQuantityDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsPlanQuantityDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsPurchasePlanDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsPurchasePlanDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsPurchaseDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					GoodsPurchaseDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					MealFeeCountDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					MealFeeCountDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DataFileDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DataFileDomainFactory.createTables(databaseId, 2017);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DataFileDomainFactory.createTables(databaseId, 2018);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DataFileDomainFactory.createTables(databaseId, 2019);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DataFileDomainFactory.createTables(databaseId, 2020);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DataFileDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					PersonAbsenceDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					PersonAbsenceDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

			}

			if (StringUtils.equals(database.getUseType(), "FILE")) {
				try {
					DataFileDomainFactory.createTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DataFileDomainFactory.createTables(databaseId, 2017);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DataFileDomainFactory.createTables(databaseId, 2018);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DataFileDomainFactory.createTables(databaseId, 2019);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DataFileDomainFactory.createTables(databaseId, 2020);
				} catch (Throwable ex) {
					logger.error(ex);
				}

				try {
					DataFileDomainFactory.alterTables(databaseId);
				} catch (Throwable ex) {
					logger.error(ex);
				}
			}

			return ResponseUtils.responseResult(true);
		}
		return ResponseUtils.responseResult(false);
	}

}
