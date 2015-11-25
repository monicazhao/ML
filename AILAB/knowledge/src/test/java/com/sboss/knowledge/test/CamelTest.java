package com.sboss.knowledge.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 加载这个测试类，并在test方法中锁定，以便加载配置信息后，开启camel的route并测试
 * @author yinwenjie
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-*.xml"})
public class CamelTest {
	
	private static final Log LOGGER = LogFactory.getLog(CamelTest.class);
	
	static {
		BasicConfigurator.configure(); 
	}
	
	private static final Object WAIT_OBJECT = new Object();
	
	@Test
	public void testCamel() {
		synchronized (WAIT_OBJECT) {
			try {
				WAIT_OBJECT.wait();
			} catch (InterruptedException e) {
				CamelTest.LOGGER.error(e.getMessage(), e);
			}
		}
	}
}
