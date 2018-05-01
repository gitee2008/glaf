/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.core.id;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.StringTools;

@Service("idGenerator")
@Transactional
public class MyBatisDbIdGenerator implements IdGenerator {
	public class RefreshTask implements Runnable {

		public void run() {
			try {
				lastIdMap.clear();
				nextIdMap.clear();
				// logger.info("clear IdGenerator cache.");
			} catch (Exception ex) {
				logger.error(ex);
			}
		}

	}

	protected final static Log logger = LogFactory.getLog(MyBatisDbIdGenerator.class);

	protected static ConcurrentMap<String, AtomicLong> lastIdMap = new ConcurrentHashMap<>();

	protected static ConcurrentMap<String, AtomicLong> nextIdMap = new ConcurrentHashMap<>();

	protected static ThreadLocal<Map<String, Integer>> threadLocalVaribles = new ThreadLocal<Map<String, Integer>>();

	protected static ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

	public static void clear() {
		threadLocalVaribles.remove();
	}

	protected volatile EntityDAO entityDAO;

	protected volatile long lastId = -1;

	protected volatile long nextId = 0;

	public MyBatisDbIdGenerator() {
		logger.info("----------------MyBatis3DbIdGenerator--------------");
		try {
			RefreshTask command = new RefreshTask();
			scheduledThreadPool.scheduleAtFixedRate(command, 60, 10, TimeUnit.SECONDS);
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected synchronized void getNewBlock() {
		IdBlock idBlock = entityDAO.nextDbidBlock();
		this.nextId = idBlock.getNextId();
		this.lastId = idBlock.getLastId();
		if (logger.isDebugEnabled()) {
			logger.debug("----------------NEXTID------------------------");
			logger.debug("nextId:" + nextId);
			logger.debug("lastId:" + lastId);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected synchronized void getNewBlock(String name) {
		IdBlock idBlock = entityDAO.nextDbidBlock(name);
		Long nextId2 = idBlock.getNextId();
		Long lastId2 = idBlock.getLastId();
		if (nextId2 < lastId2) {
			logger.debug("----------------NEXTID------------------------");
			AtomicLong lastId2x = lastIdMap.get(name);
			AtomicLong nextId2x = nextIdMap.get(name);
			if (lastId2x != null) {
				lastId2x.set(lastId2);
				lastIdMap.put(name, lastId2x);
				logger.debug("nextId:" + nextId2x.get());
			}
			if (nextId2x != null) {
				nextId2x.set(nextId2);
				nextIdMap.put(name, nextId2x);
				logger.debug("lastId:" + lastId2x.get());
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public synchronized String getNextId() {
		return Long.toString(this.nextId());
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public synchronized String getNextId(String name) {
		return Long.toString(this.nextId(name));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public synchronized String getNextId(String tablename, String idColumn, String createBy) {
		int day = DateUtils.getNowYearMonthDay();
		String idLike = String.valueOf(day) + "/" + createBy + "-";
		String cacheKey = tablename + "_" + idColumn + "_" + createBy + "_" + day;
		if (threadLocalVaribles.get() != null && threadLocalVaribles.get().get(cacheKey) != null) {
			int maxId = threadLocalVaribles.get().get(cacheKey);
			maxId = maxId + 1;
			threadLocalVaribles.get().put(cacheKey, maxId);
			String newId = idLike + StringTools.getDigit7Id(maxId);
			return newId;
		}
		int maxId = entityDAO.getTableUserMaxId(tablename, idColumn, createBy);
		if (threadLocalVaribles.get() == null) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put(cacheKey, maxId);
			threadLocalVaribles.set(map);
		} else {
			threadLocalVaribles.get().put(cacheKey, maxId);
		}
		String newId = idLike + StringTools.getDigit7Id(maxId);
		return newId;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public synchronized Long nextId() {
		if (lastId < nextId) {
			this.getNewBlock();
		}
		return nextId++;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public synchronized Long nextId(String name) {
		AtomicLong lastId2 = lastIdMap.get(name);
		AtomicLong nextId2 = nextIdMap.get(name);
		if (lastId2 == null) {
			lastId2 = new AtomicLong(0L);
			lastIdMap.put(name, lastId2);
		}
		if (nextId2 == null) {
			nextId2 = new AtomicLong(1L);
			nextIdMap.put(name, nextId2);
		}
		if (lastId2.get() < nextId2.get()) {
			this.getNewBlock(name);
		}
		Long value = nextId2.incrementAndGet();
		return value;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public synchronized Long nextId(String tablename, String idColumn) {
		long maxId = entityDAO.getMaxId(tablename, idColumn);
		return maxId + 1;
	}

	public synchronized void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}
}
