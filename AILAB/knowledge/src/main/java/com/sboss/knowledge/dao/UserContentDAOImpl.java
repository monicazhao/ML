/**
 * 
 */
package com.sboss.knowledge.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.Content;
import com.sboss.knowledge.model.orm.User;
import com.sboss.knowledge.model.orm.UserContent;

/**
 * @author monica 与用户内容信息相关的接口
 */
@Component("userContentDAOImpl")
public class UserContentDAOImpl extends AbstractRelationalDBDAO<UserContent> implements IUserContentDAO {

	@Autowired
	private IUserTypeDAO userTypeDAO;

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IContentDAO contentDAO;

	@Override
	protected Class<UserContent> getEntityClass() {
		return UserContent.class;
	}

	@Override
	public JSONObject saveContentInfoToDB(JSONObject userContent) throws BizException {
		JSONObject result = new JSONObject();
		/**
		 * 对传入的userContent进行解析，获取对应表字段的值 根据传入的contentId查询对应的内容信息
		 */
		try {
			Long contentId = userContent.getLong("contentId");
			Content content = contentDAO.getContentByContentId(contentId);
			/**
			 * 根据userId和userTypeId获取用户信息
			 */
			String userId = userContent.getString("userId");
			Long userTypeId = userContent.getLong("userType");
			User user = userDAO.queryUserByUserId(userId, userTypeId);

			// 封装成待写入数据库的类型
			UserContent userContentRes = new UserContent();
			userContentRes.setConcern(false);
			userContentRes.setContent(content);
			userContentRes.setUser(user);
			userContentRes.setUserAnalysisResultLink(content.getAnalysisResultLink());
			this.insert(userContentRes);

			result.put("isSuccess", true);
			result.put("contentLink", content.getContentLink());
			result.put("contentId", contentId);
			// 组装成待返回的内容
		} catch (Exception e) {
			e.getMessage();
			result.put("isSuccess", false);
			result.put("contentLink", null);
			result.put("contentId", null);
		}
		return result;
	}

	@Override
	public JSONArray queryAnalysisResult(JSONObject userInfo) throws BizException {
		// 定义待返回的列表
		JSONArray analysis_result_list = new JSONArray();
		String userId = userInfo.getString("userId");
		Long userTypeId = userInfo.getLong("userType");
		User user = userDAO.queryUserByUserId(userId, userTypeId);
		// 组装条件
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("userID", user.getId());
		// 获取记录(由于使用的hql)
		List<UserContent> list = null;
		list = this.queryByHqlFile("IUserContentDAO.queryAnalysisResult", condition);
		if (list == null || list.isEmpty()) {
			return null;
		}
		for (UserContent userContent : list) {
			JSONObject singleObj = new JSONObject();
			singleObj.put("contentId", userContent.getContent().getId());
			analysis_result_list.add(singleObj);
		}
		return analysis_result_list;
	}

	@Override
	public UserContent getUserContentByUCID(Long contentId, Long userID) throws BizException {
		if (contentId == null || userID == null) {
			//TODO: log it
			return null;
		}
		// 组装条件
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("userID", userID);
		condition.put("contentId", contentId);
		// 获取记录(由于使用的hql)
		List<UserContent> list = null;
		list = this.queryByHqlFile("IUserContentDAO.getUserContentByUCID", condition);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}
