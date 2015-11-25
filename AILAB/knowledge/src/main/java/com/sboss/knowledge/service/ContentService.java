/**
 * 
 */
package com.sboss.knowledge.service;

import com.sboss.knowledge.model.orm.Content;

import net.sf.json.JSONObject;

/**
 * @author monica
 * 与服务相关的方法
 */
public interface ContentService {

	Boolean isUrlExisit(String userId, Long userType, String url);
	
	/**
	 * 保存分析结果
	 * @param analysisRes
	 * @param userId
	 * @param userType
	 * @return
	 * 注意：分析结果应当有两种1）与用户无关（直接在传入的文本内容基础上进行解析）；2）与用户有关（结合文本内容和用户兴趣进行分析）
	 * 项目前期只考虑与用户无关的内容分析结果，因此只需要写content表
	 */
	Long saveAnalysisResultToDB(JSONObject analysisRes, String userId, Long userType);
	
	/**
	 * 根据sourceurl查询对应的contentId
	 * @param sourceUrl
	 * @return
	 */
	Long getContentIdBySourceUrl(String sourceUrl);
	
	/**
	 * 根据contentId查询content对象
	 * @param contentId
	 * @return
	 */
	Content getContentByContentId(Long contentId);
}
