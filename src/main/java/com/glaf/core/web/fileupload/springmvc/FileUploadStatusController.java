package com.glaf.core.web.fileupload.springmvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.glaf.core.web.fileupload.model.Progress;

@Controller
@RequestMapping("/fileUpload/fileStatus")
public class FileUploadStatusController {
	
	@RequestMapping(value = "/getStatus", method = RequestMethod.POST)
	@ResponseBody
	public byte[] initCreateInfo(HttpServletRequest request) throws Exception {
		Progress status = (Progress) request.getSession().getAttribute("fileupload_status");
		if (status == null) {
			status = new Progress();
		}
		return status.toString().getBytes("UTF-8");
	}
}
