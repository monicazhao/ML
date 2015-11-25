/**
 * 
 */
package com.sboss.knowledge.dao;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.Algorithm;

/**
 * @author monica
 * 算法相关的接口
 */
public interface IAlgorithmDAO extends SystemDAO<Algorithm> {

	/**
	 * 根据传入的algorithmId查找对应的algorithm记录
	 * @param algorithmId
	 * @return
	 * @throws BizException 
	 */
	Algorithm findAlgorithmByAlId(Long algorithmId) throws BizException;
}
