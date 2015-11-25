package com.sboss.knowledge.base;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.SQLUtils;

/**
 * 为Spring-Camel工程提供HQL文件初始化的监听类。
 * @author wenjie
 */
public class SQLFileListener implements ApplicationListener<ContextRefreshedEvent> {
	/**
	 * 日志信息
	 */
	private static final Log LOGGER = LogFactory.getLog(SQLFileListener.class);

	/**
	 * SQL/HQL文件的相对路径
	 */
	private List<String> hqlfiles;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() != null) {
			return;
		}
		if(hqlfiles == null || hqlfiles.isEmpty()) {
			SQLFileListener.LOGGER.warn("没有发现任何HQL/SQL文件信息，请检查！");
			return;
		}
		ClassLoader classLoader = SQLFileListener.class.getClassLoader();
		
		//1、=========================
		List<Document> xmlDocs = new ArrayList<>();
		Pattern pattern = Pattern.compile(".sql.xml$");
		for (String hqlfile : hqlfiles) {
			if(!pattern.matcher(hqlfile).find()) {
				SQLFileListener.LOGGER.warn("======" + hqlfile + "：错误的文件名");
				continue;
			}
			
			//寻找这个文件
			InputStream fileInputStream = classLoader.getResourceAsStream(hqlfile);
			if(fileInputStream == null) {
				SQLFileListener.LOGGER.warn("======" + hqlfile + "：没有找到这个文件，在bundle中！");
				continue;
			}
			
			//开始做XML解析，解析成document
			SAXReader doc = new SAXReader();
			Document document;
			try {
				document = doc.read(fileInputStream);
			} catch (DocumentException e) {
				SQLFileListener.LOGGER.warn(e.getMessage(), e);
				continue;
			}
			
			SQLFileListener.LOGGER.info("======" + hqlfile + "：加载->解析 成功！");
			xmlDocs.add(document);
		}
		
		//2、==================
		if(xmlDocs != null && !xmlDocs.isEmpty()) {
			//TODO SQLUtils应该使用享元模式改写
			SQLUtils sqlUtils = SQLUtils.createSQLUtils();
			try {
				//开始进行sql、hql文件的解析
				sqlUtils.setSqlFiles(xmlDocs);
			} catch (BizException e) {
				SQLFileListener.LOGGER.error(e.getMessage() , e);
			}
		}
	}

	public List<String> getHqlfiles() {
		return hqlfiles;
	}

	public void setHqlfiles(List<String> hqlfiles) {
		this.hqlfiles = hqlfiles;
	}
}