/**
 * 
 */
package com.sboss.knowledge.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sboss.knowledge.dao.IUserContentDAO;
import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.UserContent;

/**
 * @author monica
 * 用户内容相关的接口
 */
@Component("userContentService")
public class UserContentServiceImpl implements UserContentService{

	private static final Log LOG = LogFactory.getLog(UserContentServiceImpl.class);
	
	@Autowired
	private IUserContentDAO userContentDAO;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JSONObject saveContentInfoToDB(JSONObject userContent)
			throws BizException {
		// TODO Auto-generated method stub
		LOG.info("UserContentServiceImpl --- > saveContentInfoToDB");
		JSONObject result = userContentDAO.saveContentInfoToDB(userContent);
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JSONArray queryAnalysisResult(JSONObject userInfo)
			throws BizException {
		// TODO Auto-generated method stub
		LOG.info("UserContentServiceImpl ---> queryAnalysisResult");
		JSONArray resultArray = userContentDAO.queryAnalysisResult(userInfo);
		return resultArray;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public UserContent getUserContentByUCID(Long contentId, Long userID)
			throws BizException {
		// TODO Auto-generated method stub
		LOG.info("UserContentServiceImpl ---> getUserContentByUCID");
		UserContent userContent = userContentDAO.getUserContentByUCID(contentId, userID);
		return userContent;
	}

}
