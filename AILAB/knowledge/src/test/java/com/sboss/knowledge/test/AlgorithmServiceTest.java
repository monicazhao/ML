/**
 * 
 */
package com.sboss.knowledge.test;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sboss.knowledge.model.orm.Algorithm;
import com.sboss.knowledge.service.AlgorithmService;

/**
 * @author monica
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application*.xml" })
public class AlgorithmServiceTest {

	private static final Log LOGGER = LogFactory.getLog(AlgorithmServiceTest.class);
	
	@Autowired
	public AlgorithmService algorithmService;
	
	@Test
	public void findAlgorithmByAlIdTest(){
		LOGGER.info("AlgorithmServiceTest --- > findAlgorithmByAlIdTest");
		Algorithm algorithm = algorithmService.findAlgorithmByAlId(1L);
		assertTrue(algorithm.getAlgorithmId().equals(1L));
	}
}
