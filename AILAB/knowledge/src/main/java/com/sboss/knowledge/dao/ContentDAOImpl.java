/**
 * 
 */
package com.sboss.knowledge.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.Algorithm;
import com.sboss.knowledge.model.orm.Content;

/**
 * @author monica 与服务相关的接口方法
 */
@Component("contentDAOImpl")
public class ContentDAOImpl extends AbstractRelationalDBDAO<Content> implements IContentDAO {

	private static final Log LOG = LogFactory.getLog(ContentDAOImpl.class);

	@Autowired
	private IAlgorithmDAO algorithmDAO;

	@Override
	protected Class<Content> getEntityClass() {
		return Content.class;
	}

	@Override
	public Boolean isUrlExist(String userId, Long userType, String url) throws BizException {
		LOG.info("isUrlExist---->" + url);
		// 组装条件
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("sourceUrl", url);
		// 获取记录(由于使用的hql)
		List<Content> list = null;
		list = this.queryByHqlFile("IContentDAO.isUrlExist", condition);
		if (list == null || list.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public Long saveAnalysisResultToDB(JSONObject analysisRes, String userId, Long userType) {

		boolean isSuccess = false;
		/**
		 * analysisRes里面包含了若干字段，对其进行解析，得到对应的表字段内容
		 */
		String sourceUrl = "";
		String contentLink = "";
		String analysisResultLink = "";
		String executeTime = "";
		int contentType = 0;
		Long contentLength = null;
		String algorithmId = "";
		Content content = new Content();
		try {

			sourceUrl = analysisRes.getString("sourceUrl");
			contentLink = analysisRes.getString("contentLink");
			analysisResultLink = analysisRes.getString("analysisResultLink");
			contentType = analysisRes.getInt("contentType");
			contentLength = analysisRes.getLong("contentLength");
			algorithmId = analysisRes.getString("algorithm");
			executeTime = analysisRes.getString("executeTime");
			// 根据algorithmId找到对应的algorithm对象
			Algorithm algorithm = algorithmDAO.findAlgorithmByAlId(Long.parseLong(algorithmId));
			// 封装成待返回的对象
			content.setAlgorithmId(algorithm);
			content.setAnalysisResultLink(analysisResultLink);
			content.setContentLength(contentLength);
			content.setContentLink(contentLink);
			content.setContentType(contentType);
			content.setExecuteTime(executeTime);
			content.setSourceUrl(sourceUrl);
			this.insert(content);
			isSuccess = true;
		} catch (Exception e) {
			e.getMessage();
		}

		if (isSuccess) {
			return content.getId();
		} else {
			return null;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long getContentIdBySourceUrl(String sourceUrl) throws BizException {
		Long contentId = null;
		// 组装条件
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("sourceUrl", sourceUrl);
		// 获取记录(由于使用的hql)
		List<Content> list = null;
		list = this.queryByHqlFile("IContentDAO.isUrlExist", condition);
		boolean isUrlExist = true;
		if (list == null || list.isEmpty()) {
			isUrlExist = false;
		}
		if (isUrlExist == true) {
			contentId = list.get(0).getId();
		}
		return contentId;
	}

	@Override
	public Content getContentByContentId(Long contentId) throws BizException {
		// 组装条件
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("contentId", contentId);
		// 获取记录(由于使用的hql)
		List<Content> list = null;
		list = this.queryByHqlFile("IContentDAO.getContentByContentId", condition);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}
