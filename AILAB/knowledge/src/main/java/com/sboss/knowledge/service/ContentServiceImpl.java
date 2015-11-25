/**
 * 
 */
package com.sboss.knowledge.service;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sboss.knowledge.dao.IContentDAO;
import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.Content;

/**
 * @author monica
 * 与服务相关的方法
 */
@Component("contentService")
public class ContentServiceImpl implements ContentService{

	private static final Log LOG = LogFactory.getLog(ContentServiceImpl.class);
	
	@Autowired
	public IContentDAO contentDAO;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean isUrlExisit(String userId, Long userType, String url) {
		LOG.info("ContentServiceImpl ----> isUrlExist");
		Boolean isExist = false;
		try {
			isExist = contentDAO.isUrlExist(userId, userType, url);
		} catch (BizException e) {
			e.printStackTrace();
		}
		return isExist;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long saveAnalysisResultToDB(JSONObject analysisRes, String userId,
			Long userType) {
		LOG.info("ContentServiceImpl ----> saveAnalysiResultToDB");
		Long contentId = contentDAO.saveAnalysisResultToDB(analysisRes, userId, userType);
		return contentId;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long getContentIdBySourceUrl(String sourceUrl) {
		Long contentId = null;
		try {
			contentId = contentDAO.getContentIdBySourceUrl(sourceUrl);
		} catch (BizException e) {
			e.printStackTrace();
		}
		return contentId;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Content getContentByContentId(Long contentId) {
		Content content = null;
		try {
			content = contentDAO.getContentByContentId(contentId);
		} catch (BizException e) {
			e.printStackTrace();
		}
		return content;
	}
}
