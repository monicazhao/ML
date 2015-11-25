/**
 * 
 */
package com.sboss.knowledge.test;

import static org.junit.Assert.assertTrue;
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

import com.sboss.knowledge.model.orm.Content;
import com.sboss.knowledge.service.ContentService;

/**
 * @author monica
 * 内容祥光方法测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application*.xml" })
public class ContentServiceTest {

	private static final Log LOGGER = LogFactory.getLog(ContentServiceTest.class);
	
	@Autowired
	public ContentService contentService;
	
	@Test
	@Transactional
	public void isUrlExisitTest(){
		ContentServiceTest.LOGGER.info("测试开始");
		Boolean isExist = contentService.isUrlExisit("270627661", 1L, "www.baidu.com");
		assertTrue(isExist.equals(true));	
	}
	
//	@Test
	@Transactional
	@Rollback(false)
	public void saveAnalysisResultToDBTest(){
		JSONObject analysisRes = new JSONObject();
		analysisRes.put("sourceUrl", "");
		analysisRes.put("contentLink", "http://i.firefoxchina.cn/");
		analysisRes.put("analysisResultLink", "http://i.firefoxchina.cn/");
		analysisRes.put("executeTime", "2015-11-16 20:20:20");
		analysisRes.put("contentType", 1);
		analysisRes.put("contentLength", 256);
		analysisRes.put("algorithm", "1");
		Long contentId = contentService.saveAnalysisResultToDB(analysisRes, "270627661", 1L);
		System.out.println("contentId--->" + contentId);
	}
	
//	@Test
	@Transactional
	public void getContentIdBySourceUrlTest(){
		Long contentId = contentService.getContentIdBySourceUrl("www.baidu.com");
		System.out.println("contentId--->" + contentId);
		assertTrue(contentId.equals(1L));
	}
	
//	@Test
	@Transactional
	public void getContentByContentIdTest(){
		Content content = contentService.getContentByContentId(1L);
		System.out.println("contentId--->" + content.getId());
		assertTrue(content.getId().equals(1L));
	}
}
