/**
 * 
 */
package com.sboss.knowledge.dao;

import net.sf.json.JSONObject;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.Content;

/**
 * @author monica
 * 与服务相关的接口方法
 */
public interface IContentDAO extends SystemDAO<Content> {

	/**
	 * 判断前端传入的url是否存在
	 * @param userId
	 * @param userType
	 * @param url
	 * @return
	 * @throws BizException 
	 */
	public Boolean isUrlExist(String userId, Long userType, String url) throws BizException;
	
	/**
	 * 保存分析结果
	 * @param analysisRes
	 * @param userId
	 * @param userType
	 * @return
	 */
	Long saveAnalysisResultToDB(JSONObject analysisRes, String userId, Long userType);
	
	/**
	 * 根据sourceurl查询对应的contentId
	 * @param sourceUrl
	 * @return
	 * @throws BizException 
	 */
	Long getContentIdBySourceUrl(String sourceUrl) throws BizException;
	
	/**
	 * 根据contentId查询content对象
	 * @param contentId
	 * @return
	 * @throws BizException 
	 */
	Content getContentByContentId(Long contentId) throws BizException;
}
