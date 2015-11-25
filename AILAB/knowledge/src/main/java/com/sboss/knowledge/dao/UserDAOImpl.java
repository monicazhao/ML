package com.sboss.knowledge.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.User;
import com.sboss.knowledge.model.orm.UserType;

@Component("userDAOImpl")
public class UserDAOImpl extends AbstractRelationalDBDAO<User> implements IUserDAO {

	@Autowired
	private IUserTypeDAO userTypeDao;

	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}

	@Override
	public User queryUserByUserId(String userId, Long userTypeId) throws BizException {
		if (StringUtils.isEmpty(userId) || userTypeId == null) {
			return null;
		}
		// 组装查询条件
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("userId", userId);
		conditionMap.put("userTypeId", userTypeId);
		// 获取记录
		List<User> userList = null;
		userList = this.queryByHqlFile("IUserDAO.queryUserByUserId", conditionMap);
		if (userList == null || userList.isEmpty()) {
			return null;
		}
		return userList.get(0);
	}

	/**
	 * 判断用户是否存在
	 * 
	 * @throws BizException
	 */
	@Override
	public Boolean isUserExist(String userId, Long userTypeId) throws BizException {
		if (StringUtils.isEmpty(userId) || userTypeId == null) {
			return null;
		}
		// 组装查询条件
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("userId", userId);
		conditionMap.put("userTypeId", userTypeId);
		// 获取记录
		List<User> userList = null;
		userList = this.queryByHqlFile("IUserDAO.queryUserByUserId", conditionMap);
		if (userList == null || userList.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * 创建新用户（userType约定无法动态更新，即指传进来的userType必定是userType存在的）
	 * @throws BizException 
	 */
	@Override
	public void creatNewUser(String userId, Long userTypeId) throws BizException {
		// 创建用户对象
		User newUser = new User();
		newUser.setUserId(userId);
		// 根据userType获取对应的用户类型对象
		UserType userType = userTypeDao.getUserTypeByTypeId(userTypeId);
		newUser.setUserType(userType);
		this.insert(newUser);
	}

}
