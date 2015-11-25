package com.ai.analysis.interfaces;

import java.util.List;

/**
 * Author: raynard.pan
 * Date:2015/11/05
 * Func:基于NLPIR实现对文本的分词和关键词提取功能
 */

public interface INlpProcessPkgSV {
//	/**
//	 * 
//	 * @return
//	 */
//	public boolean nlpInit();

	/**
	 * 实现对输入字符串sInput的分词
	 * @param sIput
	 * @return
	 */
	public String wordParse(String sInput);
	/**
	 * 实现对输入字符串sIput的关键词提取
	 * @param sIput:待分析的文本
	 * @param count:关键词个数
	 * @return
	 */
	public String keyWordExtractor(String sInput, int count);
	/**
	 * 基于textRank提取文本摘要
	 * http://www.tuicool.com/articles/rMZfey
	 * @param sInput:待分析的文本
	 * @param count: sentence数量
	 * @return
	 */
	public List<String> getSummery(String sInput, int count);
	/**
	 * 释放nlp资源
	 */
	public void nlpExit();
}
