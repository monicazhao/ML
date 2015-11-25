/**
 * 
 */
package com.sboss.knowledge.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.UserType;

/**
 * @author monica
 * 用户类型相关接口
 */
@Component("userTypeDAOImpl")
public class UserTypeDAOImpl extends AbstractRelationalDBDAO<UserType> implements IUserTypeDAO {
	@Override
	protected Class<UserType> getEntityClass() {
		return UserType.class;
	}

	/**
	 * userTypeId用户类型id是唯一的，userType表禁止动态更新
	 * @throws BizException 
	 */
	@Override
	public UserType getUserTypeByTypeId(Long userTypeId) throws BizException {        
		if (userTypeId == null) {
			return null;
		}
		// 组装查询条件
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("userTypeId", userTypeId);
		// 获取记录
		List<UserType> userTypeList = null;
		userTypeList = this.queryByHqlFile("IUserTypeDAO.getUserTypeByTypeId", conditionMap);
        if (userTypeList == null || userTypeList.isEmpty()) {
            return null;
        } 
        return userTypeList.get(0);
	}	
}
