/**
 * 
 */
package com.sboss.knowledge.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sboss.knowledge.dao.IUserDAO;
import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.User;

/**
 * @author monica
 *
 */
@Component("userService")
public class UserServiceImpl implements UserService{
    
    @Autowired
    private IUserDAO userDAO;
	
	private static final Log LOG = LogFactory.getLog(UserServiceImpl.class);

	/**
	 * 判断用户是否存在
	 * @throws BizException 
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean isUserExist(String userId, Long userTypeId) throws BizException {
		// TODO Auto-generated method stub
		LOG.info("UserServiceImpl ---> isUserExist");
		Boolean isExist = userDAO.isUserExist(userId, userTypeId);
		return isExist;
	}

	/**
	 * 创建新用户
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean creatNewUser(String userId, Long userTypeId) {
		// TODO Auto-generated method stub
		Boolean isSuccess = false;
		try{
			userDAO.creatNewUser(userId, userTypeId);
			isSuccess = true;
		}catch(Exception e){
			e.getMessage();
		}
		return isSuccess;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User queryUserByUserId(String userId, Long userTypeId)
			throws BizException {
		// TODO Auto-generated method stub
		User user = userDAO.queryUserByUserId(userId, userTypeId);
		return user;
	}

}
