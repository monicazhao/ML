/**
 * 
 */
package com.sboss.knowledge.service;

import com.sboss.knowledge.model.orm.Algorithm;

/**
 * @author monica
 * 算法相关的服务
 */
public interface AlgorithmService {

	/**
	 * 根据传入的algorithmId查找对应的algorithm记录
	 * @param algorithmId
	 * @return
	 */
	Algorithm findAlgorithmByAlId(Long algorithmId);
}
