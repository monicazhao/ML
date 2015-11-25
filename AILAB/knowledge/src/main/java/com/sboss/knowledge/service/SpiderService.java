/**
 * 
 */
package com.sboss.knowledge.service;

import net.sf.json.JSONObject;

/**
 * @author monica
 * 根据前端传入的网页爬取相关内容
 */
public interface SpiderService {
	
	/**
	 * 对传入的urlContent内容进行解析，获得指定html标签对应的内容
	 * @param urlContent
	 * @return
	 */
	JSONObject getUrlContentBySpider(String url);
	
	/**
	 * 将从给定url中解析出的内容作为字符串上传到文件服务器，并返回文件地址
	 * @param content
	 * @return
	 */
	String writeContentToServer(String content, String fileServerPath) throws Exception;
	
	/**
	 * 将文本内容写入本地文件系统
	 * @param content
	 * @param localFilePath
	 * @return
	 */
	public String writeDataToLocalFile(String data, String localFilePath) throws Exception;
	
	/**
	 * 将本地文件上传到文件服务器
	 * @param localFilePath
	 * @param fileServerPath
	 * @return
	 * @throws Exception
	 */
	public String writeFileToServer(String localFilePath) throws Exception;
}
