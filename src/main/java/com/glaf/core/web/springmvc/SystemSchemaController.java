package com.glaf.core.web.springmvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.core.base.ConnectionDefinition;
import com.glaf.core.config.DBConfiguration;
import com.glaf.core.config.Environment;
import com.glaf.core.config.ViewProperties;
import com.glaf.core.entity.hibernate.HibernateBeanFactory;
import com.glaf.core.entity.jpa.EntitySchemaUpdate;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

@Controller("/sys/schema")
@RequestMapping("/sys/schema")
public class SystemSchemaController {
	protected final static Log logger = LogFactory.getLog(SystemSchemaController.class);

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);

		List<ConnectionDefinition> rows = DBConfiguration.getConnectionDefinitions();
		request.setAttribute("rows", rows);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		String x_view = ViewProperties.getString("system_schema.list");
		if (StringUtils.isNotEmpty(x_view)) {
			return new ModelAndView(x_view, modelMap);
		}

		return new ModelAndView("/sys/schema/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/rebuild")
	public byte[] rebuild(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		String systemName = request.getParameter("systemName");
		Environment.setCurrentSystemName(systemName);
		try {
			HibernateBeanFactory.reload();
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		try {
			EntitySchemaUpdate bean = new EntitySchemaUpdate();
			bean.updateDDL(systemName);
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@ResponseBody
	@RequestMapping("/rebuildAll")
	public byte[] rebuildAll(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		List<ConnectionDefinition> rows = DBConfiguration.getConnectionDefinitions();
		try {
			if (rows != null && !rows.isEmpty()) {
				for (ConnectionDefinition conn : rows) {
					Environment.setCurrentSystemName(conn.getName());
					HibernateBeanFactory.reload();
					EntitySchemaUpdate bean = new EntitySchemaUpdate();
					bean.updateDDL(conn.getName());
				}
			}
			return ResponseUtils.responseJsonResult(true);
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

}
