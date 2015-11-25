package com.sboss.knowledge.dao;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.UserType;

public interface IUserTypeDAO extends SystemDAO<UserType> {
    
	UserType getUserTypeByTypeId(Long userTypeId) throws BizException;
    
}
