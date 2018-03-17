package com.glaf.matrix.data.mongo.util;

import com.glaf.core.util.FileUtils;

public class Constants {

	public static final String DATABASE_PREFIX = "repo_db_";

	public static final String TABLE_PREFIX = "repo_data_";

	public static final String LOB_TABLE_PREFIX = "repo_lob_";

	public static final int FILE_PARTITION_SIZE = FileUtils.MB_SIZE * 12;

	public static final int DATABASE_PARTITION_SIZE = 16;// 数据库分区

	public static final int TABLE_PARTITION_SIZE = 64;// 表分区

	public static final String FAMILY_NAME = "info";

	public static final String QUALIFIER_ID = "q01";

	public static final String QUALIFIER_MD5 = "q02";

	public static final String QUALIFIER_HASH = "q03";

	public static final String QUALIFIER_USERHASH = "q04";

	public static final String QUALIFIER_CONTENTTYPE = "q05";

	public static final String QUALIFIER_CREATEBY = "q06";

	public static final String QUALIFIER_CREATEDATE = "q07";

	public static final String QUALIFIER_FILENAME = "q12";

	public static final String QUALIFIER_SIZE = "q13";

	public static final String QUALIFIER_TITLE = "q14";

	public static final String QUALIFIER_TYPE = "q15";

	public static final String QUALIFIER_LASTMODIFIED = "q16";

	public static final String QUALIFIER_SPLIT = "q17";

	public static final String QUALIFIER_STATUS = "q19";

	public static final String QUALIFIER_PATH = "q21";

	public static final String QUALIFIER_BLOCKSIZE = "q22";

	public static final String QUALIFIER_REF_ID = "refId";

	public static final String QUALIFIER_BLOCK = "block";

	public static final String QUALIFIER_JSON = "json";

	public static final String QUALIFIER_BYTES = "bytes";

	public static final String QUALIFIER_BYTES_PREFIX = "bytes_";

}