/**
 * 
 */
package com.sboss.knowledge.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sboss.knowledge.dao.IAlgorithmDAO;
import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.Algorithm;

/**
 * @author monica
 * 算法相关的接口
 */
@Component("algorithmService")
public class AlgorithmServiceImpl implements AlgorithmService{

	private static final Log LOG = LogFactory.getLog(AlgorithmServiceImpl.class);
	
	@Autowired
	public IAlgorithmDAO algorithmDAO;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Algorithm findAlgorithmByAlId(Long algorithmId) {
		// TODO Auto-generated method stub
		LOG.info("AlgorithmServiceImpl --- > findAlgorithmByAlId");
		Algorithm algorithm = null;
		try {
			algorithm = algorithmDAO.findAlgorithmByAlId(algorithmId);
		} catch (BizException e) {
			e.printStackTrace();
		}
		return algorithm;
	}

}
