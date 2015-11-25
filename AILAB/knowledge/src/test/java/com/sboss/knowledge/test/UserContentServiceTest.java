/**
 * 
 */
package com.sboss.knowledge.test;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.service.UserContentService;

/**
 * @author monica
 * 用户内容相关接口测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application*.xml" })
public class UserContentServiceTest {

	private static final Log LOGGER = LogFactory.getLog(UserContentServiceTest.class);
	
	@Autowired
	private UserContentService userContentService;
	
//	@Test
	@Transactional
	@Rollback(false)
	public void saveContentInfoToDBTest() throws BizException{
		LOGGER.info("UserContentServiceTest--->saveContentInfoToDBTest");
		JSONObject userContent = new JSONObject();
		userContent.put("contentId", 5L);
		userContent.put("userId", "2725122578");
		userContent.put("userType", 1L);
		userContentService.saveContentInfoToDB(userContent);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void queryAnalysisResultTest() throws BizException{
		LOGGER.info("UserContentServiceTest ---> queryAnalysisResultTest");
		JSONObject userInfo = new JSONObject();
		userInfo.put("userId", "270627661");
		userInfo.put("userType", 1L);
		userContentService.queryAnalysisResult(userInfo);
	}
}
