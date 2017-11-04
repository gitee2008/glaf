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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

public class FileUtils {

	public static final int BUFFER_SIZE = 65536;

	public static final int GB_SIZE = 1073741824;

	public static final int MB_SIZE = 1048576;

	public static final int KB_SIZE = 1024;

	public final static String sp = System.getProperty("file.separator");

	public final static String newline = System.getProperty("line.separator");

	/**
	 * 拷贝文件
	 * 
	 * @param src
	 *            源文件
	 * @param dest
	 *            目标文件
	 */
	public static void copy(String src, String dest) throws IOException {
		String path = "";
		if (dest.indexOf(sp) != -1) {
			path = dest.substring(0, dest.lastIndexOf(sp));
		}
		if (dest.indexOf("/") != -1) {
			path = dest.substring(0, dest.lastIndexOf("/"));
		}
		path = getJavaFileSystemPath(path);
		java.io.File dir = new java.io.File(path + sp);
		mkdirsWithExistsCheck(dir);
		src = getJavaFileSystemPath(src);
		dest = getJavaFileSystemPath(dest);
		org.apache.commons.io.FileUtils.copyFile(new File(src), new File(dest));
	}

	/**
	 * 拷贝文件
	 * 
	 * @param sourceFile
	 * @param destinationFile
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File destinationFile) throws IOException {
		org.apache.commons.io.FileUtils.copyFile(sourceFile, destinationFile);
	}

	/**
	 * 删除文件
	 * 
	 * @param filename
	 *            文件名
	 * @throws Exception
	 */
	public static void deleteFile(String filename) {
		if (filename == null) {
			return;
		}
		filename = getJavaFileSystemPath(filename);
		java.io.File file = new java.io.File(filename);
		if (file.exists() && file.isFile()) {
			boolean isOK = file.delete();
			if (!isOK) {

			}
		}
	}

	public static String displayFileSize(int fileSizeInBytes) {
		if (fileSizeInBytes >= 1073741824) {
			double size = fileSizeInBytes * 1.0D / 1024 / 1024 / 1024;
			size = Math.round(size * 10D) / 10D;
			return size + " GB";
		} else if (fileSizeInBytes >= 1048576) {
			double size = fileSizeInBytes * 1.0D / 1024 / 1024;
			size = ((int) (size * 100D)) / 100D;
			return size + " MB";
		} else if (fileSizeInBytes >= 1024) {
			double size = fileSizeInBytes * 1.0D / 1024;
			size = Math.round(size);
			return size + " KB";
		} else {
			return fileSizeInBytes + " Bytes";
		}
	}

	/**
	 * Delete a directory and all its contents. If we return false, the directory
	 * may be partially-deleted.
	 */
	public static boolean fullyDelete(File dir) throws IOException {
		if (!fullyDeleteContents(dir)) {
			return false;
		}
		return dir.delete();
	}

	/**
	 * Delete the contents of a directory, not the directory itself. If we return
	 * false, the directory may be partially-deleted.
	 */
	public static boolean fullyDeleteContents(File dir) throws IOException {
		boolean deletionSucceeded = true;
		File contents[] = dir.listFiles();
		if (contents != null) {
			for (int i = 0; i < contents.length; i++) {
				if (contents[i].isFile()) {
					if (!contents[i].delete()) {
						deletionSucceeded = false;
						continue; // continue deletion of other files/dirs under
									// dir
					}
				} else {
					// try deleting the directory
					// this might be a symlink
					boolean b = false;
					b = contents[i].delete();
					if (b) {
						// this was indeed a symlink or an empty directory
						continue;
					}
					// if not an empty directory or symlink let
					// fullydelete handle it.
					if (!fullyDelete(contents[i])) {
						deletionSucceeded = false;
						continue; // continue deletion of other files/dirs under
									// dir
					}
				}
			}
		}
		return deletionSucceeded;
	}

	/**
	 * 将文件转换为字节流
	 * 
	 * @param stream
	 * @return
	 */
	public static byte[] getBytes(File file) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			return getBytes(inputStream);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeStream(inputStream);
		}
	}

	public static byte[] getBytes(InputStream inputStream) {
		if (inputStream == null) {
			return null;
		}
		BufferedOutputStream bos = null;
		ByteArrayOutputStream output = null;
		ReadableByteChannel readChannel = null;
		WritableByteChannel writeChannel = null;
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			readChannel = Channels.newChannel(inputStream);
			output = new ByteArrayOutputStream();
			bos = new BufferedOutputStream(output);
			writeChannel = Channels.newChannel(bos);
			while (true) {
				buffer.clear();
				int count = readChannel.read(buffer);
				if (count == -1) {
					break;
				}
				buffer.flip();
				writeChannel.write(buffer);
			}
			bos.flush();
			output.flush();
			return output.toByteArray();
		} catch (IOException ex) {

			throw new RuntimeException(ex);
		} finally {
			if (output != null) {
				try {
					output.close();
					output = null;
				} catch (IOException ex) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
					bos = null;
				} catch (IOException ex) {
				}
			}
			if (readChannel != null) {
				try {
					readChannel.close();
					readChannel = null;
				} catch (IOException ex) {
				}
			}
			if (writeChannel != null) {
				try {
					writeChannel.close();
					writeChannel = null;
				} catch (IOException ex) {
				}
			}
			buffer.clear();
			buffer = null;
		}
	}

	/**
	 * 将文件转换为字节流
	 * 
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(String filename) {
		filename = getJavaFileSystemPath(filename);
		File file = new File(filename);
		return getBytes(file);
	}

	/**
	 * 获取磁盘分区空间使用率
	 */
	public static double getDiskPartitionSpaceUsedPercent(final String path) {
		if (null == path || path.isEmpty())
			return -1;

		try {
			File file = new File(path);
			if (!file.exists()) {
				boolean result = file.mkdirs();
				if (!result) {
				}
			}

			long totalSpace = file.getTotalSpace();
			long freeSpace = file.getFreeSpace();
			long usedSpace = totalSpace - freeSpace;
			if (totalSpace > 0) {
				return usedSpace / (double) totalSpace;
			}
		} catch (Exception e) {
			return -1;
		}

		return -1;
	}

	public static String getFileExt(String fileName) {
		String value = "";
		int start = 0;
		int end = 0;
		if (fileName == null) {
			return null;
		}
		start = fileName.lastIndexOf(46) + 1;
		end = fileName.length();
		value = fileName.substring(start, end);
		if (fileName.lastIndexOf(46) > 0) {
			return value;
		} else {
			return "";
		}
	}

	public static String getFilename(String filename) {
		String value = "";
		int start = 0;
		int end = 0;
		if (filename == null) {
			return value;
		}
		if (filename.indexOf("/") != -1) {
			start = filename.lastIndexOf("/") + 1;
			end = filename.length();
			value = filename.substring(start, end);
			if (filename.lastIndexOf("/") > 0) {
				return value;
			}
		} else if (filename.indexOf("\\") != -1) {
			start = filename.lastIndexOf("\\") + 1;
			end = filename.length();
			value = filename.substring(start, end);
			if (filename.lastIndexOf("\\") > 0) {
				return value;
			}
		}
		return filename;
	}

	public static String getFileName(String filePathName) {
		int pos = 0;
		pos = filePathName.lastIndexOf(47);
		if (pos != -1) {
			return filePathName.substring(pos + 1, filePathName.length());
		}
		pos = filePathName.lastIndexOf(92);
		if (pos != -1) {
			return filePathName.substring(pos + 1, filePathName.length());
		} else {
			return filePathName;
		}
	}

	public static java.io.InputStream getInputStream(String filename) {
		File file = null;
		FileInputStream fin = null;
		try {
			file = new File(filename);
			if (file != null && file.isFile()) {
				fin = new FileInputStream(file);
				return fin;
			}
			return null;
		} catch (IOException ex) {
			fin = null;
			throw new RuntimeException(ex);
		}
	}

	public static String getJavaFileSystemPath(String path) {
		if (path == null) {
			return null;
		}
		path = path.replace('\\', '/');
		return path;
	}

	public static String getWebFilePath(String path) {
		if (path == null) {
			return null;
		}
		path = path.replace('\\', '/');
		return path;
	}

	public static void mkdirs(String path) throws IOException {
		if (path == null) {
			return;
		}
		path = getJavaFileSystemPath(path);
		if (!path.endsWith(sp)) {
			path = path + sp;
		}
		java.io.File dir = new java.io.File(path);
		mkdirsWithExistsCheck(dir);
	}

	public static boolean mkdirsWithExistsCheck(File dir) {
		if (dir.mkdir() || dir.exists()) {
			return true;
		}
		File canonDir = null;
		try {
			canonDir = dir.getCanonicalFile();
		} catch (IOException e) {
			return false;
		}
		String parent = canonDir.getParent();
		return (parent != null) && (mkdirsWithExistsCheck(new File(parent)) && (canonDir.mkdir() || canonDir.exists()));
	}

	public static String readFile(File file) {
		final StringBuilder contents = new StringBuilder();
		try {
			final BufferedReader input = new BufferedReader(new FileReader(file));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return contents.toString();
	}

	public static String readFile(InputStream inputStream) {
		if (inputStream != null) {
			byte[] bytes = getBytes(inputStream);
			return new String(bytes);
		}
		return null;
	}

	public static String readFile(String filename) {
		if (filename != null) {
			byte[] bytes = getBytes(filename);
			return new String(bytes);
		}
		return null;
	}

	public static String readFileUTF8(InputStream file) {
		try (BufferedInputStream stream = new BufferedInputStream(file)) {
			byte[] buff = new byte[1024];
			StringBuilder builder = new StringBuilder();
			int read;
			while ((read = stream.read(buff)) != -1) {
				builder.append(new String(buff, 0, read, StandardCharsets.UTF_8));
			}
			return builder.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String replaceDirName(String pFileName) {
		if (pFileName == null) {
			return null;
		}
		pFileName = pFileName.replace(':', '_');
		pFileName = pFileName.replace('*', '_');
		pFileName = pFileName.replace('?', '_');
		pFileName = pFileName.replace('\"', '_');
		pFileName = pFileName.replace('<', '_');
		pFileName = pFileName.replace('>', '_');
		pFileName = pFileName.replace('|', '_');
		pFileName = pFileName.replace('\\', '/');
		return pFileName;
	}

	public static String replaceWin32DirName(String pFileName) {
		if (pFileName == null) {
			return null;
		}
		pFileName = pFileName.replace(':', '_');
		pFileName = pFileName.replace('*', '_');
		pFileName = pFileName.replace('?', '_');
		pFileName = pFileName.replace('\"', '_');
		pFileName = pFileName.replace('<', '_');
		pFileName = pFileName.replace('>', '_');
		pFileName = pFileName.replace('|', '_');
		return pFileName;
	}

	public static String replaceWin32FileName(String pFileName) {
		if (pFileName == null) {
			return null;
		}
		pFileName = pFileName.replace('\\', '_');
		pFileName = pFileName.replace('/', '_');
		pFileName = pFileName.replace(':', '_');
		pFileName = pFileName.replace('*', '_');
		pFileName = pFileName.replace('?', '_');
		pFileName = pFileName.replace('\"', '_');
		pFileName = pFileName.replace('<', '_');
		pFileName = pFileName.replace('>', '_');
		pFileName = pFileName.replace('|', '_');
		return pFileName;
	}

	public static void save(String filename, byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			save(filename, bais);
			bais.close();
			bais = null;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (bais != null) {
					bais.close();
				}
			} catch (IOException ex) {
			}
		}
	}

	public static void save(String filename, InputStream inputStream) {
		if (filename == null || inputStream == null) {
			return;
		}
		filename = StringTools.replace(filename, "\\", "/");
		String path = "";
		String sp = System.getProperty("file.separator");
		if (filename.indexOf(sp) != -1) {
			path = filename.substring(0, filename.lastIndexOf(sp));
		}
		if (filename.indexOf("/") != -1) {
			path = filename.substring(0, filename.lastIndexOf("/"));
		}
		path = getJavaFileSystemPath(path);
		java.io.File dir = new java.io.File(path + sp);
		mkdirsWithExistsCheck(dir);
		File file = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		FileOutputStream fileOutputStream = null;
		try {
			file = new File(filename);
			fileOutputStream = new FileOutputStream(file);
			bis = new BufferedInputStream(inputStream);
			bos = new BufferedOutputStream(fileOutputStream);
			int bytesRead = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
			bos.flush();
			bis.close();
			bos.close();
			bis = null;
			bos = null;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (bis != null) {
					bis.close();
					bis = null;
				}
			} catch (IOException ioe) {
			}
			try {
				if (bos != null) {
					bos.close();
					bos = null;
				}
			} catch (IOException ioe) {
			}
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
					fileOutputStream = null;
				}
			} catch (IOException ioe) {
			}
		}
	}

	public static int transfer(InputStream in, OutputStream out) {
		int total = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			int bytesRead = in.read(buffer);
			while (bytesRead != -1) {
				out.write(buffer, 0, bytesRead);
				total += bytesRead;
				bytesRead = in.read(buffer);
			}
			return total;
		} catch (IOException ex) {
			throw new RuntimeException("couldn't write bytes to output stream", ex);
		}
	}

	private FileUtils() {

	}
}