package com.sboss.knowledge.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultText;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.exception.ResponseCode;

/**
 * 在整个框架中SQL和HQL都是存放在xml文件中的，
 * 这个工具类负责个体xml文件的解析，并存放在服务器自身的内存中
 * @author wenjie
 */
public class SQLUtils {

	public final static String INSERT_TYPE = "insert";

	public final static String SELECT_TYPE = "select";

	public final static String UPDATE_TYPE = "update";

	public final static String DELETE_TYPE = "delete";

	public final static String SQL = "sql";

	public final static String HQL = "hql";

	private List<Document> sqlFiles = Collections.emptyList();
	
	private static SQLUtils sqlUtils = new SQLUtils();
	
	/**
	 * 内存映射对象。
	 */
	private static Map<String, SQLEntity> sqlEntitys = new HashMap<String, SQLEntity>();
	
	private SQLUtils() {

	}
	
	public static SQLUtils createSQLUtils() {
		return sqlUtils;
	}

	public void setSqlFiles(List<Document> sqlFiles) throws BizException {
		this.sqlFiles = sqlFiles;
		analyzingFiles();
	}

	private String getSQLType(Element element) {
		return element.attributeValue("type").toLowerCase();
	}

	private String getExeType(Element element) {
		return element.attributeValue("exe").toLowerCase();
	}

	private String clearOther(String sql) {
		Pattern pattern = Pattern.compile("\n|\t");
		String temp = pattern.matcher(sql).replaceAll(" ");
		Pattern gt = Pattern.compile("[<]\\s*=");
		temp = gt.matcher(temp).replaceAll("<=");
		Pattern lt = Pattern.compile("[>]\\s*=");
		temp = lt.matcher(temp).replaceAll(">=");
		Pattern not = Pattern.compile("[!]\\s*=");
		temp = not.matcher(temp).replaceAll("!=");
		Pattern not2 = Pattern.compile("[<]\\s*[>]");
		temp = not2.matcher(temp).replaceAll("<>");
		return temp;
	}
	
	@SuppressWarnings("unchecked")
	private SQLEntity analysisSQLElement(Element element) {
		Iterator<Object> contents = element.content().iterator();
		StringBuffer sql = new StringBuffer();
		Map<String, String> parameter = new HashMap<String, String>();
		while (contents.hasNext()) {
			Object obj = contents.next();
			if (obj instanceof DefaultText) {
				DefaultText defaultText = (DefaultText) obj;
				sql.append(defaultText.getText().trim());
			} else if (obj instanceof Element){
				Element childNode = (Element) obj;
				String param = childNode.attributeValue("param").trim();
				sql.append("${");
				sql.append(param);
				sql.append("}");
				parameter.put(param, clearOther(childNode.getTextTrim()));
			}
			sql.append(" ");
		}
		String sqlType = getSQLType(element);
		String sqlExe = getExeType(element);
		String sqlFma = clearOther(sql.toString());
		SQLEntity sqlEntity = new SQLEntity();
		sqlEntity.setSql_type(sqlType);
		sqlEntity.setExe_type(sqlExe);
		sqlEntity.setSql(sqlFma);
		sqlEntity.setParameter(parameter);
		return sqlEntity;
	}

	@SuppressWarnings("unchecked")
	private void analyzingFiles() throws BizException {
		if(this.sqlFiles == null){
			throw new NullPointerException("文件为空");
		}
		for (Document document : sqlFiles) {
			Element root = document.getRootElement();
			List<Element> sqlChild = root.elements("sql");
			for (int i = 0; i < sqlChild.size(); i++) {
				Element element = sqlChild.get(i);
				String elementName = element.attributeValue("name");
				if(sqlEntitys.containsKey(elementName)){
					throw new BizException("名称:" + elementName + " 已经存在" , ResponseCode._406);
				}
				sqlEntitys.put(elementName, analysisSQLElement(element));
			}
		}
	}
	
	public static Map<String, SQLEntity> getSqlEntitys() {
		return sqlEntitys;
	}

	public static SQLEntity getSQlEntity(String name) {
		return sqlEntitys.get(name);
	}
	
	public static void destroy(){
		sqlEntitys.clear();
	}
	
}
