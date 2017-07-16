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
package com.glaf.ui.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class PanelButtonQuery extends DataQuery {
        private static final long serialVersionUID = 1L;
	protected List<String> ids;
	protected Collection<String> appActorIds;
  	protected Integer pid;
  	protected Integer pidGreaterThanOrEqual;
  	protected Integer pidLessThanOrEqual;
  	protected List<Integer> pids;
  	protected Integer openType;
  	protected Integer openTypeGreaterThanOrEqual;
  	protected Integer openTypeLessThanOrEqual;
  	protected List<Integer> openTypes;
  	protected String imgUrl;
  	protected String imgUrlLike;
  	protected List<String> imgUrls;
  	protected String href;
  	protected String hrefLike;
  	protected List<String> hrefs;
  	protected String script;
  	protected String scriptLike;
  	protected List<String> scripts;

    public PanelButtonQuery() {

    }

    public Collection<String> getAppActorIds() {
	return appActorIds;
    }

    public void setAppActorIds(Collection<String> appActorIds) {
	this.appActorIds = appActorIds;
    }


    public Integer getPid(){
        return pid;
    }

    public Integer getPidGreaterThanOrEqual(){
        return pidGreaterThanOrEqual;
    }

    public Integer getPidLessThanOrEqual(){
	return pidLessThanOrEqual;
    }

    public List<Integer> getPids(){
	return pids;
    }

    public Integer getOpenType(){
        return openType;
    }

    public Integer getOpenTypeGreaterThanOrEqual(){
        return openTypeGreaterThanOrEqual;
    }

    public Integer getOpenTypeLessThanOrEqual(){
	return openTypeLessThanOrEqual;
    }

    public List<Integer> getOpenTypes(){
	return openTypes;
    }

    public String getImgUrl(){
        return imgUrl;
    }

    public String getImgUrlLike(){
	    if (imgUrlLike != null && imgUrlLike.trim().length() > 0) {
		if (!imgUrlLike.startsWith("%")) {
		    imgUrlLike = "%" + imgUrlLike;
		}
		if (!imgUrlLike.endsWith("%")) {
		   imgUrlLike = imgUrlLike + "%";
		}
	    }
	return imgUrlLike;
    }

    public List<String> getImgUrls(){
	return imgUrls;
    }


    public String getHref(){
        return href;
    }

    public String getHrefLike(){
	    if (hrefLike != null && hrefLike.trim().length() > 0) {
		if (!hrefLike.startsWith("%")) {
		    hrefLike = "%" + hrefLike;
		}
		if (!hrefLike.endsWith("%")) {
		   hrefLike = hrefLike + "%";
		}
	    }
	return hrefLike;
    }

    public List<String> getHrefs(){
	return hrefs;
    }


    public String getScript(){
        return script;
    }

    public String getScriptLike(){
	    if (scriptLike != null && scriptLike.trim().length() > 0) {
		if (!scriptLike.startsWith("%")) {
		    scriptLike = "%" + scriptLike;
		}
		if (!scriptLike.endsWith("%")) {
		   scriptLike = scriptLike + "%";
		}
	    }
	return scriptLike;
    }

    public List<String> getScripts(){
	return scripts;
    }


 

    public void setPid(Integer pid){
        this.pid = pid;
    }

    public void setPidGreaterThanOrEqual(Integer pidGreaterThanOrEqual){
        this.pidGreaterThanOrEqual = pidGreaterThanOrEqual;
    }

    public void setPidLessThanOrEqual(Integer pidLessThanOrEqual){
	this.pidLessThanOrEqual = pidLessThanOrEqual;
    }

    public void setPids(List<Integer> pids){
        this.pids = pids;
    }


    public void setOpenType(Integer openType){
        this.openType = openType;
    }

    public void setOpenTypeGreaterThanOrEqual(Integer openTypeGreaterThanOrEqual){
        this.openTypeGreaterThanOrEqual = openTypeGreaterThanOrEqual;
    }

    public void setOpenTypeLessThanOrEqual(Integer openTypeLessThanOrEqual){
	this.openTypeLessThanOrEqual = openTypeLessThanOrEqual;
    }

    public void setOpenTypes(List<Integer> openTypes){
        this.openTypes = openTypes;
    }


    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public void setImgUrlLike( String imgUrlLike){
	this.imgUrlLike = imgUrlLike;
    }

    public void setImgUrls(List<String> imgUrls){
        this.imgUrls = imgUrls;
    }


    public void setHref(String href){
        this.href = href;
    }

    public void setHrefLike( String hrefLike){
	this.hrefLike = hrefLike;
    }

    public void setHrefs(List<String> hrefs){
        this.hrefs = hrefs;
    }


    public void setScript(String script){
        this.script = script;
    }

    public void setScriptLike( String scriptLike){
	this.scriptLike = scriptLike;
    }

    public void setScripts(List<String> scripts){
        this.scripts = scripts;
    }




    public PanelButtonQuery pid(Integer pid){
	if (pid == null) {
            throw new RuntimeException("pid is null");
        }         
	this.pid = pid;
	return this;
    }

    public PanelButtonQuery pidGreaterThanOrEqual(Integer pidGreaterThanOrEqual){
	if (pidGreaterThanOrEqual == null) {
	    throw new RuntimeException("pid is null");
        }         
	this.pidGreaterThanOrEqual = pidGreaterThanOrEqual;
        return this;
    }

    public PanelButtonQuery pidLessThanOrEqual(Integer pidLessThanOrEqual){
        if (pidLessThanOrEqual == null) {
            throw new RuntimeException("pid is null");
        }
        this.pidLessThanOrEqual = pidLessThanOrEqual;
        return this;
    }

    public PanelButtonQuery pids(List<Integer> pids){
        if (pids == null) {
            throw new RuntimeException("pids is empty ");
        }
        this.pids = pids;
        return this;
    }


    public PanelButtonQuery openType(Integer openType){
	if (openType == null) {
            throw new RuntimeException("openType is null");
        }         
	this.openType = openType;
	return this;
    }

    public PanelButtonQuery openTypeGreaterThanOrEqual(Integer openTypeGreaterThanOrEqual){
	if (openTypeGreaterThanOrEqual == null) {
	    throw new RuntimeException("openType is null");
        }         
	this.openTypeGreaterThanOrEqual = openTypeGreaterThanOrEqual;
        return this;
    }

    public PanelButtonQuery openTypeLessThanOrEqual(Integer openTypeLessThanOrEqual){
        if (openTypeLessThanOrEqual == null) {
            throw new RuntimeException("openType is null");
        }
        this.openTypeLessThanOrEqual = openTypeLessThanOrEqual;
        return this;
    }

    public PanelButtonQuery openTypes(List<Integer> openTypes){
        if (openTypes == null) {
            throw new RuntimeException("openTypes is empty ");
        }
        this.openTypes = openTypes;
        return this;
    }


    public PanelButtonQuery imgUrl(String imgUrl){
	if (imgUrl == null) {
	    throw new RuntimeException("imgUrl is null");
        }         
	this.imgUrl = imgUrl;
	return this;
    }

    public PanelButtonQuery imgUrlLike( String imgUrlLike){
        if (imgUrlLike == null) {
            throw new RuntimeException("imgUrl is null");
        }
        this.imgUrlLike = imgUrlLike;
        return this;
    }

    public PanelButtonQuery imgUrls(List<String> imgUrls){
        if (imgUrls == null) {
            throw new RuntimeException("imgUrls is empty ");
        }
        this.imgUrls = imgUrls;
        return this;
    }


    public PanelButtonQuery href(String href){
	if (href == null) {
	    throw new RuntimeException("href is null");
        }         
	this.href = href;
	return this;
    }

    public PanelButtonQuery hrefLike( String hrefLike){
        if (hrefLike == null) {
            throw new RuntimeException("href is null");
        }
        this.hrefLike = hrefLike;
        return this;
    }

    public PanelButtonQuery hrefs(List<String> hrefs){
        if (hrefs == null) {
            throw new RuntimeException("hrefs is empty ");
        }
        this.hrefs = hrefs;
        return this;
    }


    public PanelButtonQuery script(String script){
	if (script == null) {
	    throw new RuntimeException("script is null");
        }         
	this.script = script;
	return this;
    }

    public PanelButtonQuery scriptLike( String scriptLike){
        if (scriptLike == null) {
            throw new RuntimeException("script is null");
        }
        this.scriptLike = scriptLike;
        return this;
    }

    public PanelButtonQuery scripts(List<String> scripts){
        if (scripts == null) {
            throw new RuntimeException("scripts is empty ");
        }
        this.scripts = scripts;
        return this;
    }



    public String getOrderBy() {
        if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

            if ("pid".equals(sortColumn)) {
                orderBy = "E.PID_" + a_x;
            } 

            if ("openType".equals(sortColumn)) {
                orderBy = "E.OPENTYPE_" + a_x;
            } 

            if ("imgUrl".equals(sortColumn)) {
                orderBy = "E.IMGURL_" + a_x;
            } 

            if ("href".equals(sortColumn)) {
                orderBy = "E.HREF_" + a_x;
            } 

            if ("script".equals(sortColumn)) {
                orderBy = "E.SCRIPT_" + a_x;
            } 

        }
        return orderBy;
    }

    @Override
    public void initQueryColumns(){
        super.initQueryColumns();
        addColumn("id", "ID_");
        addColumn("pid", "PID_");
        addColumn("openType", "OPENTYPE_");
        addColumn("imgUrl", "IMGURL_");
        addColumn("href", "HREF_");
        addColumn("script", "SCRIPT_");
    }

}