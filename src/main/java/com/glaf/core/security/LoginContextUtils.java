package com.glaf.core.security;

import java.util.Collection;
import java.util.Iterator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.identity.Tenant;
import com.glaf.core.identity.User;
import com.glaf.core.identity.impl.TenantImpl;
import com.glaf.core.identity.impl.UserImpl;

public class LoginContextUtils {

	public static LoginContext clone(LoginContext loginContext) {
		LoginContext m = new LoginContext();
		m.setSkin(loginContext.getSkin());
		m.setUser(loginContext.getUser());
		m.setTenant(loginContext.getTenant());
		m.setOrganizationId(loginContext.getOrganizationId());
		m.setSystemType(loginContext.getSystemType());
		m.setCurrentAccessLevel(loginContext.getCurrentAccessLevel());
		m.setCurrentSystemName(loginContext.getCurrentSystemName());

		if (loginContext.getAgents() != null) {
			for (String x : loginContext.getAgents()) {
				m.addAgent(x);
			}
		}

		if (loginContext.getFunctions() != null) {
			for (String x : loginContext.getFunctions()) {
				m.addFunction(x);
			}
		}

		if (loginContext.getObservers() != null) {
			for (String x : loginContext.getObservers()) {
				m.addObserver(x);
			}
		}

		if (loginContext.getPermissions() != null) {
			for (String x : loginContext.getPermissions()) {
				m.addPermission(x);
			}
		}

		if (loginContext.getRoles() != null) {
			for (String x : loginContext.getRoles()) {
				m.addRole(x);
			}
		}

		if (loginContext.getManagedTenantIds() != null) {
			for (String x : loginContext.getManagedTenantIds()) {
				m.addManagedTenantId(x);
			}
		}

		if (loginContext.getSubOrganizationIds() != null) {
			for (Long x : loginContext.getSubOrganizationIds()) {
				m.addSubOrganizationId(x);
			}
		}

		if (loginContext.getDatabaseIds() != null) {
			for (Long x : loginContext.getDatabaseIds()) {
				m.addDatabaseId(x);
			}
		}

		return m;
	}

	public static LoginContext jsonToObject(JSONObject jsonObject) {
		LoginContext loginContext = new LoginContext();
		if (jsonObject.containsKey("user")) {
			JSONObject json = jsonObject.getJSONObject("user");
			User user = new UserImpl();
			user = (User) user.jsonToObject(json);
			loginContext.setUser(user);
		}

		if (jsonObject.containsKey("tenant")) {
			JSONObject json = jsonObject.getJSONObject("tenant");
			Tenant tenant = new TenantImpl();
			tenant = (Tenant) tenant.jsonToObject(json);
			loginContext.setTenant(tenant);
		}

		if (jsonObject.containsKey("currentAccessLevel")) {
			loginContext.setCurrentAccessLevel(jsonObject.getInteger("currentAccessLevel"));
		}

		if (jsonObject.containsKey("currentSystemName")) {
			loginContext.setCurrentSystemName(jsonObject.getString("currentSystemName"));
		}

		if (jsonObject.containsKey("organizationId")) {
			loginContext.setOrganizationId(jsonObject.getLong("organizationId"));
		}

		if (jsonObject.containsKey("systemType")) {
			loginContext.setSystemType(jsonObject.getInteger("systemType"));
		}

		if (jsonObject.containsKey("skin")) {
			loginContext.setSkin(jsonObject.getString("skin"));
		}

		if (jsonObject.containsKey("roles")) {
			JSONArray jsonArray = jsonObject.getJSONArray("roles");
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				String role = (String) iterator.next();
				loginContext.addRole(role);
			}
		}

		if (jsonObject.containsKey("managedTenantIds")) {
			JSONArray jsonArray = jsonObject.getJSONArray("managedTenantIds");
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				String tenantId = (String) iterator.next();
				loginContext.addManagedTenantId(tenantId);
			}
		}

		if (jsonObject.containsKey("subOrganizationIds")) {
			JSONArray jsonArray = jsonObject.getJSONArray("subOrganizationIds");
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				String subOrganizationId = (String) iterator.next();
				loginContext.addSubOrganizationId(Long.parseLong(subOrganizationId));
			}
		}

		if (jsonObject.containsKey("databaseIds")) {
			JSONArray jsonArray = jsonObject.getJSONArray("databaseIds");
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				String databaseId = (String) iterator.next();
				loginContext.addDatabaseId(Long.parseLong(databaseId));
			}
		}

		if (jsonObject.containsKey("agents")) {
			JSONArray jsonArray = jsonObject.getJSONArray("agents");
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				String agent = (String) iterator.next();
				loginContext.addAgent(agent);
			}
		}

		if (jsonObject.containsKey("functions")) {
			JSONArray jsonArray = jsonObject.getJSONArray("functions");
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				String function = (String) iterator.next();
				loginContext.addFunction(function);
			}
		}

		if (jsonObject.containsKey("permissions")) {
			JSONArray jsonArray = jsonObject.getJSONArray("permissions");
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				String permission = (String) iterator.next();
				loginContext.addPermission(permission);
			}
		}

		if (jsonObject.containsKey("observers")) {
			JSONArray jsonArray = jsonObject.getJSONArray("observers");
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				String observer = (String) iterator.next();
				loginContext.addObserver(observer);
			}
		}

		return loginContext;
	}

	public static JSONObject toJsonObject(LoginContext loginContext) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("skin", loginContext.getSkin());
		jsonObject.put("actorId", loginContext.getActorId());
		jsonObject.put("systemType", loginContext.getSystemType());
		jsonObject.put("currentAccessLevel", loginContext.getCurrentAccessLevel());
		jsonObject.put("currentSystemName", loginContext.getCurrentSystemName());
		jsonObject.put("organizationId", loginContext.getOrganizationId());

		if (loginContext.getUser() != null) {
			jsonObject.put("user", loginContext.getUser().toJsonObject());
		}

		if (loginContext.getTenant() != null) {
			jsonObject.put("tenant", loginContext.getTenant().toJsonObject());
		}

		Collection<String> roles = loginContext.getRoles();
		if (roles != null && !roles.isEmpty()) {
			JSONArray jsonArray = new JSONArray();
			for (String role : roles) {
				jsonArray.add(role);
			}
			jsonObject.put("roles", jsonArray);
		}

		Collection<String> managedTenantIds = loginContext.getManagedTenantIds();
		if (managedTenantIds != null && !managedTenantIds.isEmpty()) {
			JSONArray jsonArray = new JSONArray();
			for (String tenantId : managedTenantIds) {
				jsonArray.add(tenantId);
			}
			jsonObject.put("managedTenantIds", jsonArray);
		}

		Collection<Long> subOrganizationIds = loginContext.getSubOrganizationIds();
		if (subOrganizationIds != null && !subOrganizationIds.isEmpty()) {
			JSONArray jsonArray = new JSONArray();
			for (Long subOrganizationId : subOrganizationIds) {
				jsonArray.add(String.valueOf(subOrganizationId));
			}
			jsonObject.put("subOrganizationIds", jsonArray);
		}

		Collection<Long> databaseIds = loginContext.getDatabaseIds();
		if (databaseIds != null && !databaseIds.isEmpty()) {
			JSONArray jsonArray = new JSONArray();
			for (Long databaseId : databaseIds) {
				jsonArray.add(String.valueOf(databaseId));
			}
			jsonObject.put("databaseIds", jsonArray);
		}

		Collection<String> agents = loginContext.getAgents();
		if (agents != null && !agents.isEmpty()) {
			JSONArray jsonArray = new JSONArray();
			for (String agentId : agents) {
				jsonArray.add(agentId);
			}
			jsonObject.put("agents", jsonArray);
		}

		Collection<String> functions = loginContext.getFunctions();
		if (functions != null && !functions.isEmpty()) {
			JSONArray jsonArray = new JSONArray();
			for (String function : functions) {
				jsonArray.add(function);
			}
			jsonObject.put("functions", jsonArray);
		}

		Collection<String> permissions = loginContext.getPermissions();
		if (permissions != null && !permissions.isEmpty()) {
			JSONArray jsonArray = new JSONArray();
			for (String permission : permissions) {
				jsonArray.add(permission);
			}
			jsonObject.put("permissions", jsonArray);
		}

		Collection<String> observers = loginContext.getObservers();
		if (observers != null && !observers.isEmpty()) {
			JSONArray jsonArray = new JSONArray();
			for (String observer : observers) {
				jsonArray.add(observer);
			}
			jsonObject.put("observers", jsonArray);
		}

		return jsonObject;
	}

	private LoginContextUtils() {

	}

}
