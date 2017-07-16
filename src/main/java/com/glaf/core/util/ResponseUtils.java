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

package com.glaf.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.MessageProperties;

public class ResponseUtils {
	private static Log logger = LogFactory.getLog(ResponseUtils.class);

	protected static Configuration conf = BaseConfiguration.create();

	protected static ConcurrentMap<String, String> mimeMapping = new ConcurrentHashMap<String, String>();

	protected static ConcurrentMap<String, Integer> etagMap = new ConcurrentHashMap<String, Integer>();

	protected static ConcurrentMap<String, String> etag2Map = new ConcurrentHashMap<String, String>();

	protected static ConcurrentMap<String, Boolean> etag3Map = new ConcurrentHashMap<String, Boolean>();

	public static void download(HttpServletRequest request, HttpServletResponse response, byte[] bytes, String filename)
			throws IOException, ServletException {
		InputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(bytes);
			download(request, response, inputStream, filename);
		} catch (Exception ex) {
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ex) {
			}
		}
	}

	public static void download(HttpServletRequest request, HttpServletResponse response, InputStream inputStream,
			String filename) {
		output(request, response, inputStream, filename, null);
	}

	public static String getGMT(Date dateCST) {
		DateFormat df = new SimpleDateFormat("EEE, d-MMM-yyyy HH:mm:ss z", Locale.ENGLISH);
		df.setTimeZone(TimeZone.getTimeZone("GMT")); // modify Time Zone.
		return (df.format(dateCST));
	}

	public static boolean isGZIPSupported(HttpServletRequest req) {
		String browserEncodings = req.getHeader("accept-encoding");
		boolean supported = ((browserEncodings != null) && (browserEncodings.indexOf("gzip") != -1));
		String userAgent = req.getHeader("user-agent");
		if ((userAgent != null) && userAgent.startsWith("httpunit")) {
			logger.debug("httpunit detected, disabling filter...");
			return false;
		} else {
			return supported;
		}
	}

	public static void output(HttpServletRequest request, HttpServletResponse response, byte[] bytes, String filename,
			String contentType) throws IOException, ServletException {
		InputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(bytes);
			output(request, response, inputStream, filename, contentType);
		} catch (Exception ex) {
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ex) {
			}
		}
	}

	public static void output(HttpServletRequest request, HttpServletResponse response, InputStream inputStream,
			String filename, String programContentType) {
		String contentDisposition = null;
		String fileOrgName = filename;
		filename = filename.trim();
		logger.debug("filename:" + filename);
		int dot = filename.lastIndexOf(".");
		String ext = filename.substring(dot + 1);
		String contentType = "";
		boolean requiredZip = false;
		if (StringUtils.equalsIgnoreCase(ext, "jpg") || StringUtils.equalsIgnoreCase(ext, "jpeg")
				|| StringUtils.equalsIgnoreCase(ext, "gif") || StringUtils.equalsIgnoreCase(ext, "png")) {
			contentType = "image/" + ext;
			requiredZip = false;
		} else if (StringUtils.equalsIgnoreCase(ext, "bmp")) {
			contentType = "image/bmp";
			requiredZip = true;
		} else if (StringUtils.equalsIgnoreCase(ext, "svg")) {
			contentType = "image/svg+xml";
			requiredZip = true;
		} else if (StringUtils.equalsIgnoreCase(ext, "css")) {
			contentType = "text/css";
			requiredZip = true;
		} else if (StringUtils.equalsIgnoreCase(ext, "txt")) {
			contentType = "text/plain";
			requiredZip = true;
		} else if (StringUtils.equalsIgnoreCase(ext, "htm") || StringUtils.equalsIgnoreCase(ext, "html")) {
			contentType = "text/html";
			requiredZip = true;
		} else if (StringUtils.equalsIgnoreCase(ext, "js")) {
			contentType = "application/javascript";
			requiredZip = true;
		} else if (StringUtils.equalsIgnoreCase(ext, "ttf")) {
			contentType = "application/x-font-ttf";
			requiredZip = true;
		} else if (StringUtils.equalsIgnoreCase(ext, "eot")) {
			contentType = "application/vnd.ms-fontobject";
		} else if (StringUtils.equalsIgnoreCase(ext, "woff")) {
			contentType = "application/x-font-woff";
		} else if (StringUtils.equalsIgnoreCase(ext, "swf")) {
			contentType = "application/x-shockwave-flash";
		} else {
			contentType = mimeMapping.get(ext.trim().toLowerCase());
		}

		logger.debug("contentType:" + contentType);

		if (requiredZip && isGZIPSupported(request) && conf.getBoolean("gzipEnabled", true)) {
			requiredZip = true;
		} else {
			requiredZip = false;
		}

		OutputStream outputStream = null;

		try {

			String etag = request.getHeader("If-None-Match");
			if (etag != null && etagMap.containsKey(etag)) {
				Date date = new Date();
				date.setTime(System.currentTimeMillis() - DateUtils.DAY);
				String modDate = getGMT(date);
				date.setTime(System.currentTimeMillis() + DateUtils.DAY * 30);
				String expDate = getGMT(date);
				response.setHeader("Age", String.valueOf(DateUtils.DAY * 30));
				response.setHeader("Cache-Control", "max-age=315360000");
				response.setHeader("Connection", "keep-alive");
				response.setHeader("ETag", etag);
				response.setHeader("Last-Modified", modDate);
				response.setHeader("Expires", expDate);
				response.setDateHeader("Date", System.currentTimeMillis());
				response.setHeader("Pragma", "Pragma"); // HTTP/1.0
				if (etag3Map.get(etag) && requiredZip) {
					response.setHeader("Content-Encoding", "gzip");
				} else {
					String encoding = request.getHeader("Accept-Encoding");
					if (encoding != null && encoding.indexOf("gzip") != -1) {
						response.setHeader("Content-Encoding", "gzip");
					} else if (encoding != null && encoding.indexOf("compress") != -1) {
						response.setHeader("Content-Encoding", "compress");
					}
				}
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setContentType(etag2Map.get(etag));
				response.setContentLength(etagMap.get(etag));
				outputStream = response.getOutputStream();
				outputStream.flush();
				IOUtils.closeStream(outputStream);
				response.flushBuffer();
				logger.debug(fileOrgName + "内容已经在浏览器中缓存。");
				return;
			}

			boolean zipFlag = false;

			request.setCharacterEncoding("UTF-8");
			String encoding = request.getHeader("Accept-Encoding");

			if (encoding != null) {
				encoding = encoding.toLowerCase();
			}
			if (encoding != null && encoding.indexOf("gzip") != -1) {
				zipFlag = true;
				response.setHeader("Content-Encoding", "gzip");
				outputStream = new java.util.zip.GZIPOutputStream(response.getOutputStream());
			} else if (encoding != null && encoding.indexOf("compress") != -1) {
				zipFlag = true;
				response.setHeader("Content-Encoding", "compress");
				outputStream = new java.util.zip.ZipOutputStream(response.getOutputStream());
			} else {
				outputStream = response.getOutputStream();
			}

			String userAgent = request.getHeader("User-Agent");
			logger.debug("User-Agent:" + userAgent);
			if (userAgent != null) {
				if (userAgent.indexOf("MSIE") != -1) {
					filename = new String(filename.getBytes("GBK"), "ISO8859_1");
				} else {
					filename = new String(filename.getBytes("UTF-8"), "ISO8859_1");
				}
			} else {
				filename = new String(filename.getBytes("GBK"), "ISO8859_1");
			}

			if (userAgent != null) {
				if (userAgent.indexOf("MSIE 5.5") != -1) {
					contentDisposition = "attachment;filename=\"" + filename + "\"";
				} else if (userAgent.indexOf("MSIE 6.0b") != -1) {
					filename = new String(fileOrgName.getBytes("GBK"), "ISO8859_1");
					contentDisposition = "attachment;filename=\"" + filename + "\"";
				} else if (userAgent.indexOf("Gecko") != -1) {
					filename = new String(fileOrgName.getBytes("GBK"), "ISO8859_1");
					contentDisposition = "attachment;filename=\"" + filename + "\"";
				}
			}
			if (contentDisposition == null) {
				contentDisposition = "attachment;filename=\"" + filename + "\"";
			}

			logger.debug("convert filename:" + filename);
			logger.debug("content disposition:" + contentDisposition);

			Date date = new Date();
			date.setTime(System.currentTimeMillis() - DateUtils.DAY);
			String modDate = getGMT(date);
			date.setTime(System.currentTimeMillis() + DateUtils.DAY * 30);
			String expDate = getGMT(date);
			response.setHeader("Age", String.valueOf(DateUtils.DAY * 30));
			response.setHeader("Cache-Control", "max-age=315360000");
			response.setHeader("Connection", "keep-alive");
			response.setHeader("Last-Modified", modDate);
			response.setHeader("Expires", expDate);
			response.setDateHeader("Date", System.currentTimeMillis());
			response.setHeader("Pragma", "Pragma"); // HTTP/1.0
			response.setHeader("Content-Transfer-Encoding", "base64");
			response.setHeader("Content-Disposition", contentDisposition);
			if (programContentType != null) {
				response.setContentType(programContentType);
			} else {
				response.setContentType("application/octet-stream");
			}
			long size = IOUtils.write(inputStream, outputStream);
			etag = DigestUtils.sha1Hex(inputStream);
			response.setHeader("ETag", etag);
			etagMap.put(etag, (int) size);
			etag2Map.put(etag, programContentType != null ? programContentType : "application/octet-stream");
			etag3Map.put(etag, zipFlag);
			outputStream.flush();
			IOUtils.closeStream(inputStream);
			IOUtils.closeStream(outputStream);

			logger.debug("etag:" + etag);
			logger.debug("file size:" + size);

			inputStream = null;
			outputStream = null;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeStream(inputStream);
			IOUtils.closeStream(outputStream);
		}
	}

	public static byte[] responseJsonResult(boolean success) {
		if (success) {
			Map<String, Object> jsonMap = new java.util.HashMap<String, Object>();
			jsonMap.put("statusCode", 200);
			jsonMap.put("msg", MessageProperties.getString("res_op_ok"));
			jsonMap.put("message", MessageProperties.getString("res_op_ok"));
			JSONObject object = new JSONObject(jsonMap);
			try {
				return object.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		} else {
			Map<String, Object> jsonMap = new java.util.HashMap<String, Object>();
			jsonMap.put("statusCode", 500);
			jsonMap.put("msg", MessageProperties.getString("res_op_error"));
			jsonMap.put("message", MessageProperties.getString("res_op_error"));
			JSONObject object = new JSONObject(jsonMap);
			try {
				return object.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	public static byte[] responseJsonResult(boolean success, Map<String, Object> dataMap) {
		if (success) {
			Map<String, Object> jsonMap = new java.util.HashMap<String, Object>();
			jsonMap.put("statusCode", 200);

			Set<Entry<String, Object>> entrySet = dataMap.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				jsonMap.put(key, value);
			}

			JSONObject object = new JSONObject(jsonMap);
			try {
				return object.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		} else {
			Map<String, Object> jsonMap = new java.util.HashMap<String, Object>();
			jsonMap.put("statusCode", 500);

			Set<Entry<String, Object>> entrySet = dataMap.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				jsonMap.put(key, value);
			}
			JSONObject object = new JSONObject(jsonMap);
			try {
				return object.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	public static byte[] responseJsonResult(boolean success, String message) {
		if (success) {
			Map<String, Object> jsonMap = new java.util.HashMap<String, Object>();
			jsonMap.put("statusCode", 200);
			jsonMap.put("message", message);
			JSONObject object = new JSONObject(jsonMap);
			try {
				return object.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		} else {
			Map<String, Object> jsonMap = new java.util.HashMap<String, Object>();
			jsonMap.put("statusCode", 500);
			jsonMap.put("message", message);
			JSONObject object = new JSONObject(jsonMap);
			try {
				return object.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	public static byte[] responseJsonResult(int statusCode, String message) {
		Map<String, Object> jsonMap = new java.util.HashMap<String, Object>();
		jsonMap.put("statusCode", statusCode);
		jsonMap.put("message", message);
		JSONObject object = new JSONObject(jsonMap);
		try {
			return object.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	/**
	 * 返回响应的结果，根据系统默认配置返回
	 * 
	 * @param success
	 * @return
	 */
	public static byte[] responseResult(boolean success) {
		String responseDataType = MessageProperties.getString("responseDataType");
		if ("xml".equalsIgnoreCase(responseDataType)) {
			responseXmlResult(success);
		}
		return responseJsonResult(success);
	}

	/**
	 * 返回响应的结果
	 * 
	 * @param responseDataType
	 *            json或xml，默认是json格式
	 * @param success
	 * @return
	 */
	public static byte[] responseResult(String responseDataType, boolean success) {
		if ("xml".equalsIgnoreCase(responseDataType)) {
			responseXmlResult(success);
		}
		return responseJsonResult(success);
	}

	public static byte[] responseXmlResult(boolean success) {
		if (success) {
			StringBuilder buffer = new StringBuilder(500);
			buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			buffer.append("<response>");
			buffer.append("\n    <statusCode>200</statusCode>");
			buffer.append("\n    <message>").append(MessageProperties.getString("res_op_ok")).append("</message>");
			buffer.append("\n</response>");
			try {
				return buffer.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		} else {
			StringBuilder buffer = new StringBuilder(500);
			buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			buffer.append("<response>");
			buffer.append("\n    <statusCode>500</statusCode>");
			buffer.append("\n    <message>").append(MessageProperties.getString("res_op_error")).append("</message>");
			buffer.append("\n</response>");
			try {
				return buffer.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	public static byte[] responseXmlResult(boolean success, Map<String, Object> dataMap) {
		if (success) {
			StringBuilder buffer = new StringBuilder(500);
			buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			buffer.append("<response>");
			buffer.append("\n    <statusCode>200</statusCode>");
			Set<Entry<String, Object>> entrySet = dataMap.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				buffer.append("\n    <").append(key).append(">").append(value).append("</").append(key).append(">");
			}
			buffer.append("\n</response>");
			try {
				return buffer.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		} else {
			StringBuilder buffer = new StringBuilder(500);
			buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			buffer.append("<response>");
			buffer.append("\n    <statusCode>500</statusCode>");
			Set<Entry<String, Object>> entrySet = dataMap.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				buffer.append("\n    <").append(key).append(">").append(value).append("</").append(key).append(">");
			}
			buffer.append("\n</response>");
			try {
				return buffer.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	public static byte[] responseXmlResult(boolean success, String message) {
		if (success) {
			StringBuilder buffer = new StringBuilder(500);
			buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			buffer.append("<response>");
			buffer.append("\n    <statusCode>200</statusCode>");
			buffer.append("\n    <message>").append(message).append("</message>");
			buffer.append("\n</response>");
			try {
				return buffer.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		} else {
			StringBuilder buffer = new StringBuilder(500);
			buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			buffer.append("<response>");
			buffer.append("\n    <statusCode>500</statusCode>");
			buffer.append("\n    <message>").append(message).append("</message>");
			buffer.append("\n</response>");
			try {
				return buffer.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

}