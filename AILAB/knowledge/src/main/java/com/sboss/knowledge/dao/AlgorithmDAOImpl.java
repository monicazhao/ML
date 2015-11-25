/**
 * 
 */
package com.sboss.knowledge.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.model.orm.Algorithm;

/**
 * @author idot
 *
 */
@Component("algorithmDAOImpl")
public class AlgorithmDAOImpl extends AbstractRelationalDBDAO<Algorithm> implements IAlgorithmDAO {

	@Override
	protected Class<Algorithm> getEntityClass() {
		return Algorithm.class;
	}

	@Override
	public Algorithm findAlgorithmByAlId(Long algorithmId) throws BizException {
		// 组装条件
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("algorithmid", algorithmId);
		// 获取记录(由于使用的hql)
		List<Algorithm> result = null;
		result = this.queryByHqlFile("IAlgorithmDAO.findAlgorithmByAlId", condition);
		if (result == null || result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

}
