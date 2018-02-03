package com.glaf.core.web.fileupload.listener;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

import com.glaf.core.web.fileupload.model.Progress;

public class FileUploadProgressListener implements ProgressListener {
	private HttpSession session;

	public FileUploadProgressListener() {

	}

	public FileUploadProgressListener(HttpSession session) {
		this.session = session;
		Progress status = new Progress();
		if (session != null) {
			session.setAttribute("fileupload_status", status);
		}
	}

	@Override
	public void update(long bytesRead, long contentLength, int pItems) {
		if (session != null) {
			Progress status = (Progress) session.getAttribute("fileupload_status");
			status.setBytesRead(bytesRead);
			status.setContentLength(contentLength);
			status.setItems(pItems);
			session.setAttribute("fileupload_status", status);
		}
	}
}
