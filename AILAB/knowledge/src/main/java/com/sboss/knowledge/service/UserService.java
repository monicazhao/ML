/**
 * 
 */
package com.sboss.knowledge.service;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.User;

/**
 * @author monica
 *
 */
public interface UserService {
	
	/**
	 * 接口名：判断用户是否存在
	 * @param userId  微信公众号openID或设备唯一串号
	 * @param userType  用户角色
	 * @return
	 * @throws BizException 
	 */
	Boolean isUserExist(String userId, Long userTypeId) throws BizException;
	
	/**
	 * 接口名：创建新用户
	 * @param userId  微信公众号openID或设备唯一串号
	 * @param userType 用户角色
	 * @return
	 */
	Boolean creatNewUser(String userId, Long userTypeId);
	
    /**
     * 根据userId和userTypeId（区别于id）查询用户信息
     * @param userId
     * @param userTypeId
     * @return
     */
    public User queryUserByUserId(String userId, Long userTypeId) throws BizException;
}
