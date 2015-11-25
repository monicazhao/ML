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

import com.sboss.knowledge.service.SpiderService;

/**
 * @author monica
 * 爬虫服务测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application*.xml" })
public class SpiderServiceTest {

	private static final Log LOGGER = LogFactory.getLog(SpiderServiceTest.class);
	
	@Autowired
	public SpiderService spiderService;
	
//	@Test
	public void getUrlContentBySpiderTest(){
		LOGGER.info("SpiderServiceTest--->getUrlContentBySpiderTest");
		JSONObject result = spiderService.getUrlContentBySpider("http://www.fromgeek.com/newspaper/32582.html");
		assertTrue(result.equals(null));
	}
	
//	@Test
	@Rollback(false)
	public void writeContentToServerTest() throws Exception{
		LOGGER.info("SpiderServiceTest--->writeContentToServerTest");
		String content = "{\"url\":\"http://www.fromgeek.com/news/32729.html\",\"contentId\":1,\"title\":\"test\",\"content\":\"This is a test for write content to server\"}";
		String fileServerPath = "http://171.221.254.231:9000/7891011.spider";
		String filePath = spiderService.writeContentToServer(content, fileServerPath);
		assertTrue(filePath.equals(null));
	}
	
	@Test
	public void writeDataToLocalFileTest() throws Exception {
		LOGGER.info("SpiderServiceTest--->writeDataToLocalFileTest");
		String content = "2015年11月19-21日，由CSDN重磅打造的“2015 中国软件开发者大会”（以下简称SDCC 2015）在北京朗丽兹西山花园酒店隆重召开。今年是第七届，大会为期三天，除了阵容强大的全体大会外，主办方还精心筹备了九大技术专场论坛，包括：架构实践论坛、前端开发论坛、数据库实战论坛、研发管理论坛、安全技术论坛、算法实战论坛、编程语言论坛、产品与设计论坛、微信开发论坛。此外，还有五场特色活动及展览展示。";
		String localFilePath = "E:/test.spider";
		spiderService.writeDataToLocalFile(content, localFilePath);
	}
	
	@Test
	public void writeFileToServerTest() throws Exception {
		LOGGER.info("SpiderServiceTest--->writeFileToServerTest");
		String localFilePath = "E:/test.spider";
		String resultPath = spiderService.writeFileToServer(localFilePath);
		System.out.println("resultPath--->" + resultPath);
	}
	
}
