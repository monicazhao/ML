package com.sboss.knowledge.model;

import java.util.Collections;
import java.util.Map;

/**
 * 解析SQL File后的封装对象，基本上业务开发人员不会直接使用该数据对象
 * @author yinwenjie
 */
public class SQLEntity {

	private String exe_type;
	
	private String sql_type;
	
	private String sql;
	
	private Map<String, String> parameter = Collections.emptyMap();

	public String getExe_type() {
		return exe_type;
	}

	public void setExe_type(String exe_type) {
		this.exe_type = exe_type;
	}

	public String getSql_type() {
		return sql_type;
	}

	public void setSql_type(String sql_type) {
		this.sql_type = sql_type;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}
}
