package com.ai.intelligence.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.ai.analysis.impl.NlpProcessImpl;
import com.ai.analysis.interfaces.INlpProcessPkgSV;
import com.ai.intelligence.interfaces.IContentAnalysisPkgSV;

public class ContentAnalysisImpl implements IContentAnalysisPkgSV {

	public JSONObject contentAnalysis(JSONObject contentJson) {
		String title = null;
		if(contentJson.containsKey("title")){
			title = contentJson.getString("title");
		}
		String content = contentJson.getString("content");
		String contentStr = null;
		JSONObject resJson = new JSONObject();
		if(title == null){
			contentStr = content;
		}else{
			contentStr = title + content;
			//resJson.put("summary", title);
		}
		INlpProcessPkgSV nlpSV = new NlpProcessImpl();
		//注意此处提取关键词的个数应该与文本的长度相适应
		String keyWords = nlpSV.keyWordExtractor(contentStr, 7);
		List<String> summary = nlpSV.getSummery(contentStr, 1);
		String[] keyWordList = keyWords.trim().split("#");
		Map<String,Double> keyWordMap = new HashMap<String, Double>();
		for(int idx = 0; idx < keyWordList.length - 1; idx ++){
			String[] tempStr = keyWordList[idx].split("/");
			String key = tempStr[0];
			double weight = Double.parseDouble(tempStr[2]);
			keyWordMap.put(key, weight);
		}
		
		JSONObject returnJson = new JSONObject();
		resJson.put("keyWords", keyWordMap.toString());
		/*if(contentJson.containsKey("title")){
			resJson.put("summary", contentJson.getString("title"));
		}else{
			resJson.put("summary", summary.get(0).toString());
		}*/
		resJson.put("summary", summary.get(0).toString());
		returnJson.put("analysisResult", resJson);
		return returnJson;
	}

}
