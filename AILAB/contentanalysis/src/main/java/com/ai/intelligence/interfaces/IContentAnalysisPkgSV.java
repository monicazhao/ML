package com.ai.intelligence.interfaces;

import net.sf.json.JSONObject;



public interface IContentAnalysisPkgSV {
	/**
	 * 根据文本的title和正文等内容解析文本的关键词和文本摘要
	 * @param contentJson
	 * @return
	 */
	public JSONObject contentAnalysis(JSONObject contentJson);
}
