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

package com.glaf.heathcare.bean;

import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.util.Constants;
import com.glaf.core.util.JdbcUtils;

public class DietaryClearBean {
	protected final static Log logger = LogFactory.getLog(DietaryClearBean.class);

	public void removePlanData(String tenantId, int fullday, int dateAfter) {
		logger.debug("hash:" + IdentityFactory.getTenantHash(tenantId));
		Connection conn = null;
		PreparedStatement psmt = null;
		PreparedStatement psmt2 = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(" select ID_ from HEALTH_DIETARY").append(IdentityFactory.getTenantHash(tenantId))
					.append(" where TENANTID_ = ? and FULLDAY_ = ? ");
			psmt = conn.prepareStatement(sqlBuffer.toString());
			psmt.setString(1, tenantId);
			psmt.setInt(2, fullday);

			rs = psmt.executeQuery();
			while (rs.next()) {
				sqlBuffer.delete(0, sqlBuffer.length());
				sqlBuffer.append(" delete from HEALTH_DIETARY_ITEM").append(IdentityFactory.getTenantHash(tenantId))
						.append(" where TENANTID_ = ? and DIETARYID_ = ? ");
				psmt2 = conn.prepareStatement(sqlBuffer.toString());
				psmt2.setString(1, tenantId);
				psmt2.setLong(2, rs.getLong(1));
				psmt2.executeUpdate();
				JdbcUtils.close(psmt2);
			}
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);

			sqlBuffer.delete(0, sqlBuffer.length());
			sqlBuffer.append(" delete from HEALTH_DIETARY").append(IdentityFactory.getTenantHash(tenantId))
					.append(" where TENANTID_ = ? and FULLDAY_ = ? ");
			psmt2 = conn.prepareStatement(sqlBuffer.toString());
			psmt2.setString(1, tenantId);
			psmt2.setInt(2, fullday);
			psmt2.executeUpdate();
			JdbcUtils.close(psmt2);

			sqlBuffer.delete(0, sqlBuffer.length());
			sqlBuffer.append(" delete from HEALTH_DIETARY_COUNT").append(IdentityFactory.getTenantHash(tenantId))
					.append(" where TENANTID_ = ? and FULLDAY_ = ? ");
			psmt2 = conn.prepareStatement(sqlBuffer.toString());
			psmt2.setString(1, tenantId);
			psmt2.setInt(2, fullday);
			psmt2.executeUpdate();
			JdbcUtils.close(psmt2);

			sqlBuffer.delete(0, sqlBuffer.length());
			sqlBuffer.append(" delete from GOODS_PLAN_QUANTITY").append(IdentityFactory.getTenantHash(tenantId))
					.append(" where TENANTID_ = ? and FULLDAY_ = ? ");
			psmt2 = conn.prepareStatement(sqlBuffer.toString());
			psmt2.setString(1, tenantId);
			psmt2.setInt(2, fullday);
			psmt2.executeUpdate();
			JdbcUtils.close(psmt2);

			sqlBuffer.delete(0, sqlBuffer.length());
			sqlBuffer.append(" delete from GOODS_PURCHASE").append(IdentityFactory.getTenantHash(tenantId))
					.append(" where TENANTID_ = ? and FULLDAY_ = ? ");
			psmt2 = conn.prepareStatement(sqlBuffer.toString());
			psmt2.setString(1, tenantId);
			psmt2.setInt(2, fullday);
			psmt2.executeUpdate();
			JdbcUtils.close(psmt2);

			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt2);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

	public void removePlanData(String tenantId, int year, int semester, int week, int dateAfter) {
		logger.debug("hash:" + IdentityFactory.getTenantHash(tenantId));
		SysTenantService sysTenantService = ContextFactory.getBean("sysTenantService");
		SysTenant tenant = sysTenantService.getSysTenantByTenantId(tenantId);
		Connection conn = null;
		PreparedStatement psmt = null;
		PreparedStatement psmt2 = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(" select ID_ from HEALTH_DIETARY").append(IdentityFactory.getTenantHash(tenantId))
					.append(" where TENANTID_ = ? and WEEK_ = ? and SEMESTER_ = ? and YEAR_ = ? ");
			if (tenant.getLimit() != Constants.UNLIMIT) {
				sqlBuffer.append(" and FULLDAY_ > ? ");
			}
			psmt = conn.prepareStatement(sqlBuffer.toString());
			psmt.setString(1, tenantId);
			psmt.setInt(2, week);
			psmt.setInt(3, semester);
			psmt.setInt(4, year);
			if (tenant.getLimit() != Constants.UNLIMIT) {
				psmt.setInt(5, dateAfter);
			}
			rs = psmt.executeQuery();
			while (rs.next()) {
				sqlBuffer.delete(0, sqlBuffer.length());
				sqlBuffer.append(" delete from HEALTH_DIETARY_ITEM").append(IdentityFactory.getTenantHash(tenantId))
						.append(" where TENANTID_ = ? and DIETARYID_ = ? ");
				psmt2 = conn.prepareStatement(sqlBuffer.toString());
				psmt2.setString(1, tenantId);
				psmt2.setLong(2, rs.getLong(1));
				psmt2.executeUpdate();
				JdbcUtils.close(psmt2);
			}
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);

			sqlBuffer.delete(0, sqlBuffer.length());
			sqlBuffer.append(" delete from HEALTH_DIETARY").append(IdentityFactory.getTenantHash(tenantId))
					.append(" where TENANTID_ = ? and WEEK_ = ? and SEMESTER_ = ? and YEAR_ = ? ");
			if (tenant.getLimit() != Constants.UNLIMIT) {
				sqlBuffer.append(" and FULLDAY_ > ? ");
			}
			psmt2 = conn.prepareStatement(sqlBuffer.toString());
			psmt2.setString(1, tenantId);
			psmt2.setInt(2, week);
			psmt2.setInt(3, semester);
			psmt2.setInt(4, year);
			if (tenant.getLimit() != Constants.UNLIMIT) {
				psmt2.setInt(5, dateAfter);
			}
			psmt2.executeUpdate();
			JdbcUtils.close(psmt2);

			sqlBuffer.delete(0, sqlBuffer.length());
			sqlBuffer.append(" delete from HEALTH_DIETARY_COUNT").append(IdentityFactory.getTenantHash(tenantId))
					.append(" where TENANTID_ = ? and WEEK_ = ? and SEMESTER_ = ? and YEAR_ = ? ");
			if (tenant.getLimit() != Constants.UNLIMIT) {
				sqlBuffer.append(" and FULLDAY_ > ? ");
			}
			psmt2 = conn.prepareStatement(sqlBuffer.toString());
			psmt2.setString(1, tenantId);
			psmt2.setInt(2, week);
			psmt2.setInt(3, semester);
			psmt2.setInt(4, year);
			if (tenant.getLimit() != Constants.UNLIMIT) {
				psmt2.setInt(5, dateAfter);
			}
			psmt2.executeUpdate();
			JdbcUtils.close(psmt2);

			sqlBuffer.delete(0, sqlBuffer.length());
			sqlBuffer.append(" delete from GOODS_PLAN_QUANTITY").append(IdentityFactory.getTenantHash(tenantId))
					.append(" where TENANTID_ = ? and WEEK_ = ? and SEMESTER_ = ? and YEAR_ = ? ");
			if (tenant.getLimit() != Constants.UNLIMIT) {
				sqlBuffer.append(" and FULLDAY_ > ? ");
			}
			psmt2 = conn.prepareStatement(sqlBuffer.toString());
			psmt2.setString(1, tenantId);
			psmt2.setInt(2, week);
			psmt2.setInt(3, semester);
			psmt2.setInt(4, year);
			if (tenant.getLimit() != Constants.UNLIMIT) {
				psmt2.setInt(5, dateAfter);
			}
			psmt2.executeUpdate();
			JdbcUtils.close(psmt2);

			sqlBuffer.delete(0, sqlBuffer.length());
			sqlBuffer.append(" delete from GOODS_PURCHASE_PLAN").append(IdentityFactory.getTenantHash(tenantId))
					.append(" where TENANTID_ = ? and WEEK_ = ? and SEMESTER_ = ? and YEAR_ = ? ");
			if (tenant.getLimit() != Constants.UNLIMIT) {
				sqlBuffer.append(" and FULLDAY_ > ? ");
			}
			psmt2 = conn.prepareStatement(sqlBuffer.toString());
			psmt2.setString(1, tenantId);
			psmt2.setInt(2, week);
			psmt2.setInt(3, semester);
			psmt2.setInt(4, year);
			if (tenant.getLimit() != Constants.UNLIMIT) {
				psmt2.setInt(5, dateAfter);
			}
			psmt2.executeUpdate();
			JdbcUtils.close(psmt2);

			sqlBuffer.delete(0, sqlBuffer.length());
			sqlBuffer.append(" delete from HEALTH_DIETARY_STATISTICS")
					.append(" where TENANTID_ = ? and WEEK_ = ? and SEMESTER_ = ? and YEAR_ = ? ");
			psmt2 = conn.prepareStatement(sqlBuffer.toString());
			psmt2.setString(1, tenantId);
			psmt2.setInt(2, week);
			psmt2.setInt(3, semester);
			psmt2.setInt(4, year);
			psmt2.executeUpdate();
			JdbcUtils.close(psmt2);

			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt2);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

}
