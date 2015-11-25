/**
 * 
 */
package com.sboss.knowledge.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.sboss.knowledge.support.FileOperations;

/**
 * @author monica
 * 文件操作相关接口
 */
public class FileOperationsTest {

	FileOperations fileOperations = new FileOperations();
	
//	@Test
	public void readFilefromServer(){
		String result = fileOperations.readFilefromServer("http://171.221.254.231:9000/72f3fc6fee459d1a12fbdb0325b19453.spider");
//		String result = fileOperations.readFilefromServer("http://171.221.254.231:9000/32b0e3f82be2b44af7899e4407d4e4a6.spider");
		System.out.println("result--->" + result);
		assertTrue(result.equals(null));
	}
	
	@Test
	public void test(){
		String filePath = "/resources/nlpir.properties";
		File file = new File(filePath);
		assertTrue(file.equals(null));
	}
}
