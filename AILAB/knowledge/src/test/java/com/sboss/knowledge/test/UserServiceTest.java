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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.service.UserService;

/**
 * @author monica
 * 用户信息方法测试
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application*.xml" })
public class UserServiceTest {
    
    private static final Log LOGGER = LogFactory.getLog(UserServiceTest.class);
	
	@Autowired
	private UserService userService;
	
	@Test
	@Transactional
	public void isUserExistTest() throws BizException{
		UserServiceTest.LOGGER.info("测试开始");
		Boolean isExist = userService.isUserExist("270627661", 1L);
		assertTrue(isExist.equals(true));
	}
	
	//@Test
	@Transactional
	@Rollback(false)
	public void creatNewUser(){
		UserServiceTest.LOGGER.info("测试开始");
		Boolean isSuccess = userService.creatNewUser("2725122578", 1L);
		assertTrue(isSuccess.equals(true));
	}
}
