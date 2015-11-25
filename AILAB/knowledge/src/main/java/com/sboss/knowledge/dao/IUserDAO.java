package com.sboss.knowledge.dao;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.User;

public interface IUserDAO extends SystemDAO<User> {
    
    /**
     * 根据userId和userTypeId（区别于id）查询用户信息
     * @param userId
     * @param userTypeId
     * @return
     */
    public User queryUserByUserId(String userId, Long userTypeId) throws BizException;
    
	/**
	 * 接口名：判断用户是否存在
	 * @param userId  微信公众号openID或设备唯一串号
	 * @param userType  用户角色
	 * @return
	 * @throws BizException 
	 */
	public Boolean isUserExist(String userId, Long userType) throws BizException;
	
	/**
	 * 接口名：创建新用户
	 * @param userId  微信公众号openID或设备唯一串号
	 * @param userTypeId 用户角色（前后端约定用于角色不会动态新增，即指传进来的userType在userType表是必定存在的）
	 * @return
	 * @throws BizException 
	 */
	public void creatNewUser(String userId, Long userTypeId) throws BizException;
    
}
