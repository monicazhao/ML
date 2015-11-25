/**
 * 
 */
package com.sboss.knowledge.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.UserContent;

/**
 * @author monica
 * 用户内容相关的接口
 */
public interface UserContentService {

	/**
	 * 保存用户内容信息
	 * @param userContent
	 * @return
	 */
	JSONObject saveContentInfoToDB(JSONObject userContent) throws BizException;
	
	/**
	 * 查询当前用户的历史解析记录列表
	 * @param userInfo
	 * @return
	 */
	JSONArray queryAnalysisResult(JSONObject userInfo) throws BizException;
	
	/**
	 * 根据contentId和user_info表的主键查询对应的usercontent对象
	 * @param contentId
	 * @param userID
	 * @return
	 */
	UserContent getUserContentByUCID(Long contentId, Long userID) throws BizException;
	
}
