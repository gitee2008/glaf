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

package com.glaf.matrix.data.factory;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.bson.types.Binary;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import com.glaf.core.base.DataFile;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.ServerEntity;
import com.glaf.core.query.ServerEntityQuery;
import com.glaf.core.security.SecurityUtils;
import com.glaf.core.service.IServerEntityService;
import com.glaf.core.util.ByteBlockChopper;
import com.glaf.core.util.hash.JenkinsHash;

import com.glaf.matrix.data.domain.DataFileEntity;
import com.glaf.matrix.data.mongo.MongoDBClient;
import com.glaf.matrix.data.mongo.object.MongoDBConfig;
import com.glaf.matrix.data.mongo.object.MongoDBCredential;
import com.glaf.matrix.data.mongo.object.MongoDBDriver;
import com.glaf.matrix.data.mongo.util.Constants;

public class MongoFileStorageFactory {
	private static class RedisSingletonHolder {
		public static MongoFileStorageFactory instance = new MongoFileStorageFactory();
	}

	public class RefreshTask implements Runnable {

		public void run() {
			try {
				init();
			} catch (Exception ex) {
				logger.error(ex);
			}
		}

	}

	protected final static Log logger = LogFactory.getLog(MongoFileStorageFactory.class);

	protected static ConcurrentMap<Integer, String> mongoPosMap = new ConcurrentHashMap<Integer, String>();

	protected static ConcurrentMap<String, MongoDatabase> mongoDBMap = new ConcurrentHashMap<String, MongoDatabase>();

	protected static ConcurrentMap<String, ServerEntity> serverMap = new ConcurrentHashMap<String, ServerEntity>();

	protected static ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

	protected static volatile IServerEntityService serverEntityService;

	protected static volatile AtomicBoolean loaded = new AtomicBoolean(false);

	protected static final String DEFAULT_REGION = "default";

	public static MongoFileStorageFactory getInstance() {
		return RedisSingletonHolder.instance;
	}

	public static IServerEntityService getServerEntityService() {
		if (serverEntityService == null) {
			serverEntityService = ContextFactory.getBean("serverEntityService");
		}
		return serverEntityService;
	}

	private MongoFileStorageFactory() {
		startScheduler();
	}

	/**
	 * 通过文件编号删除内容
	 * 
	 * @param fileId
	 */
	public void deleteById(String fileId) {
		this.deleteById(DEFAULT_REGION, fileId);
	}

	/**
	 * 通过文件编号删除内容，删除全部节点
	 * 
	 * @param region
	 * @param fileId
	 */
	public void deleteById(String region, String fileId) {
		int size = mongoDBMap.size();
		if (size == 0) {
			if (!loaded.get()) {
				this.init();
				loaded.set(true);
			}
			size = mongoDBMap.size();
			if (size == 0) {
				logger.warn("mongodb server is empty!!!");
			}
			return;
		}
		int pos = 0;
		MongoDatabase db = null;
		int hash = JenkinsHash.getInstance().hash(fileId.getBytes());
		hash = Math.abs(hash % Constants.DATABASE_PARTITION_SIZE);

		while (pos < size) {
			String key = mongoPosMap.get(pos++);
			try {
				db = mongoDBMap.get(key);
				ServerEntity serverEntity = serverMap.get(key);
				if (serverEntity != null && db != null) {

					DataFile model = new DataFileEntity();
					model.setId(fileId);

					String collectionName = Constants.TABLE_PREFIX + hash;
					logger.debug("collectionName:" + collectionName);
					MongoCollection<Document> coll = db.getCollection(collectionName);
					boolean embedded = false;
					if (coll != null) {
						Document document = coll.find(eq(Constants.QUALIFIER_ID, fileId)).first();
						logger.debug("document:" + document);
						if (document != null && document.containsKey("embedded")) {
							embedded = document.getBoolean("embedded", false);
						}
						if (document != null) {
							coll.deleteOne(document);
						}
					}

					if (embedded) {
						collectionName = Constants.LOB_TABLE_PREFIX + hash;
						logger.debug("collectionName:" + collectionName);
						MongoCollection<Document> coll2 = db.getCollection(collectionName);
						if (coll2 != null) {
							BasicDBObject filter = new BasicDBObject();
							filter.put(Constants.QUALIFIER_ID, fileId);
							logger.debug("filter:" + filter.toString());
							coll2.deleteMany(filter);
						}
					} else {
						collectionName = Constants.LOB_TABLE_PREFIX + hash;
						logger.debug("->collectionName:" + collectionName);
						MongoCollection<Document> coll2 = db.getCollection(collectionName);
						if (coll2 != null) {
							BasicDBObject filter = new BasicDBObject();
							filter.put(Constants.QUALIFIER_REF_ID, fileId);
							logger.debug("filter:" + filter.toString());
							coll2.deleteMany(filter);
						}
					}
				}
			} catch (Exception ex) {
				logger.error("mongodb error", ex);
			}
		}
	}

	/**
	 * 通过文件编号获取内容
	 * 
	 * @param fileId
	 * @return
	 */
	public byte[] getData(String fileId) {
		return this.getData(DEFAULT_REGION, fileId);
	}

	/**
	 * 通过文件编号获取内容
	 * 
	 * @param region
	 * @param fileId
	 * @return
	 */
	public byte[] getData(String region, String fileId) {
		DataFile dataFile = this.getDataFile(region, fileId);
		if (dataFile != null) {
			return dataFile.getData();
		}
		return null;
	}

	/**
	 * 通过文件编号获取内容
	 * 
	 * @param region
	 * @param fileId
	 * @return
	 */
	public DataFile getDataFile(String region, String fileId) {
		int size = mongoDBMap.size();
		if (size == 0) {
			if (!loaded.get()) {
				this.init();
				loaded.set(true);
			}
			size = mongoDBMap.size();
			if (size == 0) {
				logger.warn("mongodb  server is empty!!!");
			}
			return null;
		}
		int pos = 0;
		String key = null;
		Document document = null;
		MongoDatabase db = null;
		ServerEntity serverEntity = null;
		MongoCollection<Document> coll = null;
		MongoCursor<Document> cur = null;
		logger.debug("fileId:" + fileId);
		int hash = JenkinsHash.getInstance().hash(fileId.getBytes());
		hash = Math.abs(hash % Constants.DATABASE_PARTITION_SIZE);
		String collectionName = Constants.TABLE_PREFIX + hash;
		logger.debug("collectionName:" + collectionName);

		while (pos < size) {
			key = mongoPosMap.get(pos++);
			db = mongoDBMap.get(key);
			try {
				db = mongoDBMap.get(key);
				serverEntity = serverMap.get(key);
				if (serverEntity != null && db != null) {
					coll = db.getCollection(collectionName);
					document = coll.find(eq(Constants.QUALIFIER_ID, fileId)).first();
					logger.debug("document:" + document);
					if (document != null) {
						DataFile model = new DataFileEntity();
						model.setId(fileId);
						model.setCreateBy((String) document.get(Constants.QUALIFIER_CREATEBY));

						if (document.containsKey(Constants.QUALIFIER_FILENAME)) {
							model.setFilename((String) document.get(Constants.QUALIFIER_FILENAME));
						}
						if (document.containsKey(Constants.QUALIFIER_CONTENTTYPE)) {
							model.setContentType((String) document.get(Constants.QUALIFIER_CONTENTTYPE));
						}
						if (document.containsKey(Constants.QUALIFIER_SIZE)) {
							model.setSize((Long) document.get(Constants.QUALIFIER_SIZE));
						}
						if (document.containsKey(Constants.QUALIFIER_LASTMODIFIED)) {
							model.setLastModified((Long) document.get(Constants.QUALIFIER_LASTMODIFIED));
						}
						if (document.containsKey(Constants.QUALIFIER_STATUS)) {
							model.setStatus((Integer) document.get(Constants.QUALIFIER_STATUS));
						}
						if (document.containsKey(Constants.QUALIFIER_CREATEDATE)) {
							long ts = (Long) document.get(Constants.QUALIFIER_CREATEDATE);
							model.setCreateDate(new Date(ts));
						}

						boolean embedded = false;

						// BasicDBObject filter = new BasicDBObject();
						// filter.put(Constants.QUALIFIER_ID, id);
						// logger.debug("filter:" + filter.toString());
						if (document.containsKey("embedded")) {
							embedded = document.getBoolean("embedded", false);
						}

						byte[] data = null;
						if (embedded) {
							collectionName = Constants.LOB_TABLE_PREFIX + hash;
							logger.debug("collectionName:" + collectionName);
							MongoCollection<Document> coll2 = db.getCollection(collectionName);
							if (coll2 != null) {
								Document document2 = coll2.find(eq(Constants.QUALIFIER_ID, fileId)).first();
								logger.debug("document2:" + document2);
								if (document2 != null) {
									if (document2.containsKey(Constants.QUALIFIER_BYTES)) {
										Object object = document2.get(Constants.QUALIFIER_BYTES);
										if (object instanceof byte[]) {
											data = (byte[]) object;
										} else if (object instanceof Binary) {
											Binary binary = (Binary) object;
											data = binary.getData();
										}
									}
								}
							}
						} else {
							collectionName = Constants.LOB_TABLE_PREFIX + hash;
							logger.debug("->collectionName:" + collectionName);
							MongoCollection<Document> coll2 = db.getCollection(collectionName);
							if (coll2 != null) {
								List<byte[]> byteBlocks = new ArrayList<byte[]>();
								BasicDBObject filter = new BasicDBObject();
								filter.put(Constants.QUALIFIER_REF_ID, fileId);
								logger.debug("filter:" + filter.toString());
								cur = coll2.find(filter).iterator();
								try {
									while (cur.hasNext()) {
										Document doc = cur.next();
										logger.debug("->document:" + doc.get(Constants.QUALIFIER_BLOCK));
										if (doc.containsKey(Constants.QUALIFIER_BYTES)
												&& doc.containsKey(Constants.QUALIFIER_BLOCK)) {
											Integer index = (Integer) doc.get(Constants.QUALIFIER_BLOCK);
											Object object = doc.get(Constants.QUALIFIER_BYTES);
											if (object instanceof byte[]) {
												data = (byte[]) object;
											} else if (object instanceof Binary) {
												Binary binary = (Binary) object;
												data = binary.getData();
											}
											byteBlocks.add(index, data);
										}
									}
								} finally {
									cur.close();
								}

								if (byteBlocks.size() > 0) {
									data = ByteBlockChopper.glueChopsBackTogether(byteBlocks,
											Constants.FILE_PARTITION_SIZE);
								}
							}
						}

						model.setData(data);
						return model;
					}
				}
			} catch (Exception ex) {
				logger.error("mongodb get error", ex);
			}
		}
		return null;
	}

	protected String getJsonKey(String region, String fileId) {
		return (region + ":json:" + fileId);
	}

	protected byte[] getKey(String region, String fileId) {
		return (region + ":S:" + fileId).getBytes();
	}

	public void init() {
		ServerEntityQuery query = new ServerEntityQuery();
		query.type("mongodb");
		query.active("1");
		List<ServerEntity> servers = getServerEntityService().list(query);
		if (servers != null && !servers.isEmpty()) {
			int index = 0;
			for (ServerEntity serverEntity : servers) {
				if (!StringUtils.startsWith(serverEntity.getMapping(), "file_")) {
					continue;
				}
				String host = serverEntity.getHost();
				int port = serverEntity.getPort();
				String password = serverEntity.getPassword();
				String key = serverEntity.getKey();
				MongoDatabase db = null;
				try {
					db = mongoDBMap.get(serverEntity.getName());
					if (db == null) {
						logger.debug("mongodb database:" + serverEntity.getDbname());
						MongoDBDriver mongoDBDriver = new MongoDBDriver();
						MongoDBConfig mongoDBConfig = new MongoDBConfig();
						mongoDBConfig.setAddresses(host + ":" + port);
						MongoDBCredential credential = new MongoDBCredential();
						credential.setDatabaseName(serverEntity.getDbname());
						credential.setUsername(serverEntity.getUser());
						password = SecurityUtils.decode(key, password);
						// logger.debug("password:" + password);
						credential.setPassword(password);
						mongoDBConfig.setCredential(credential);
						mongoDBDriver.setConfiguration(mongoDBConfig);
						mongoDBDriver.init();
						MongoDBClient client = new MongoDBClient();
						client.setMongoDBDriver(mongoDBDriver);
						client.setDatabaseName(serverEntity.getDbname());
						client.getDatabase().getCollection("test").drop();
						mongoDBMap.put(serverEntity.getName(), client.getDatabase());
						mongoPosMap.put(index++, serverEntity.getName());
						serverMap.put(serverEntity.getName(), serverEntity);
						logger.debug(client.getDatabase() + " success.");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error("mongodb connection error", ex);
				}
			}
		}
	}

	/**
	 * 存储内容
	 * 
	 * @param fileId
	 * @param data
	 */
	public void saveData(String fileId, byte[] data) {
		this.saveData(DEFAULT_REGION, fileId, data);
	}

	/**
	 * 存储内容到mongo，写到全部节点
	 * 
	 * @param region
	 * @param fileId
	 * @param data
	 */
	public void saveData(String region, String fileId, byte[] data) {
		DataFile model = new DataFileEntity();
		model.setId(fileId);
		model.setData(data);
		this.saveDataFile(region, fileId, model);
	}

	/**
	 * 存储内容到mongo，写到全部节点
	 * 
	 * @param region
	 * @param fileId
	 * @param data
	 */
	public void saveDataFile(String region, String fileId, DataFile model) {
		int size = mongoDBMap.size();
		if (size == 0) {
			if (!loaded.get()) {
				this.init();
				loaded.set(true);
			}
			size = mongoDBMap.size();
			if (size == 0) {
				logger.warn("mongodb server is empty!!!");
			}
			return;
		}
		int pos = 0;
		String key = null;
		MongoDatabase db = null;
		ServerEntity serverEntity = null;
		MongoCollection<Document> coll = null;

		byte[] data = model.getData();
		logger.debug("fileId:" + fileId);
		long fileSzie = data.length;
		String md5 = DigestUtils.md5Hex(data);
		int hash = JenkinsHash.getInstance().hash(fileId.getBytes());
		hash = Math.abs(hash % Constants.DATABASE_PARTITION_SIZE);
		String collectionName = Constants.TABLE_PREFIX + hash;
		logger.debug("collectionName:" + collectionName);
		while (pos < size) {
			key = mongoPosMap.get(pos++);
			logger.debug("key:" + key);
			try {
				db = mongoDBMap.get(key);
				serverEntity = serverMap.get(key);
				if (serverEntity != null && db != null) {
					coll = db.getCollection(collectionName);
					if (coll != null) {
						Document document = new Document();
						document.put(Constants.QUALIFIER_ID, model.getId());
						document.put(Constants.QUALIFIER_MD5, md5);
						document.put(Constants.QUALIFIER_HASH, hash);
						document.put(Constants.QUALIFIER_CONTENTTYPE, model.getContentType());
						document.put(Constants.QUALIFIER_CREATEDATE, model.getCreateDate().getTime());
						document.put(Constants.QUALIFIER_CREATEBY, model.getCreateBy());
						document.put(Constants.QUALIFIER_FILENAME, model.getFilename());
						document.put(Constants.QUALIFIER_LASTMODIFIED, model.getLastModified());
						document.put(Constants.QUALIFIER_SIZE, model.getSize());
						document.put(Constants.QUALIFIER_TYPE, model.getType());
						document.put(Constants.QUALIFIER_JSON, model.toJsonObject().toJSONString());

						if (fileSzie < Constants.FILE_PARTITION_SIZE) {
							document.put("embedded", true);
						} else {
							document.put("embedded", false);
						}
						coll.insertOne(document);
						logger.debug(collectionName + " insert doc:" + model.getId());
					}

					if (fileSzie < Constants.FILE_PARTITION_SIZE) {
						/**
						 * 按记录编号维度存放数据
						 */
						collectionName = Constants.LOB_TABLE_PREFIX + hash;
						coll = db.getCollection(collectionName);
						if (coll != null) {
							Document document = new Document();
							document.put(Constants.QUALIFIER_ID, model.getId());
							document.put(Constants.QUALIFIER_MD5, md5);
							document.put(Constants.QUALIFIER_HASH, hash);
							document.put(Constants.QUALIFIER_CONTENTTYPE, model.getContentType());
							document.put(Constants.QUALIFIER_CREATEDATE, model.getCreateDate().getTime());
							document.put(Constants.QUALIFIER_CREATEBY, model.getCreateBy());
							document.put(Constants.QUALIFIER_FILENAME, model.getFilename());
							document.put(Constants.QUALIFIER_LASTMODIFIED, model.getLastModified());
							document.put(Constants.QUALIFIER_SIZE, model.getSize());
							document.put(Constants.QUALIFIER_TYPE, model.getType());
							document.put(Constants.QUALIFIER_BYTES, model.getData());

							if (fileSzie < Constants.FILE_PARTITION_SIZE) {
								document.put("embedded", true);
							}
							coll.insertOne(document);
							logger.debug(collectionName + " insert doc:" + model.getId());
						}
					} else {
						/**
						 * 按记录编号维度存放附件数据
						 */
						List<byte[]> byteBlocks = ByteBlockChopper.chopItUp(model.getData(),
								Constants.FILE_PARTITION_SIZE);
						int len = byteBlocks.size();
						collectionName = Constants.LOB_TABLE_PREFIX + hash;
						coll = db.getCollection(collectionName);
						if (coll != null) {
							for (int index = 0; index < len; index++) {
								byte[] bytes = byteBlocks.get(index);
								Document document = new Document();
								document.put(Constants.QUALIFIER_ID, model.getId() + "_" + index);
								document.put(Constants.QUALIFIER_REF_ID, model.getId());
								document.put(Constants.QUALIFIER_BLOCK, index);
								document.put(Constants.QUALIFIER_BYTES, bytes);
								coll.insertOne(document);
								logger.debug("insert part:" + (index + 1));
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("mongodb set error", ex);
			}
		}
	}

	public void startScheduler() {
		RefreshTask command = new RefreshTask();
		scheduledThreadPool.scheduleAtFixedRate(command, 60, 30, TimeUnit.SECONDS);// 每10秒检查一次
	}

}