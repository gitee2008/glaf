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

package com.glaf.base.modules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.codec.digest.DigestUtils;

import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.JdbcUtils;

public class InitUserPasswordBean {

	public void initPassword() {
		Connection conn = null;
		PreparedStatement psmt = null;
		PreparedStatement psmt2 = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			String sql = " select USERID from SYS_USER where PASSWORD_HASH is null and LOCKED = 0 ";
			String pwd_hash = DigestUtils.md5Hex(DigestUtils.sha512Hex("888888"));
			psmt = conn.prepareStatement(sql);
			psmt2 = conn.prepareStatement(" update SYS_USER set PASSWORD_HASH = ? where USERID = ? ");
			rs = psmt.executeQuery();
			while (rs.next()) {
				psmt2.setString(1, pwd_hash);
				psmt2.setString(2, rs.getString(1));
				psmt2.addBatch();
			}
			psmt2.executeBatch();
			conn.commit();
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(psmt2);
			JdbcUtils.close(conn);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(psmt2);
			JdbcUtils.close(conn);
		}
	}

}
