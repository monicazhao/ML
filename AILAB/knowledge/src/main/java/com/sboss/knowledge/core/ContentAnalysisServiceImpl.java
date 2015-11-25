package com.sboss.knowledge.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;


@Component("contentAnalysisService")
public class ContentAnalysisServiceImpl implements IContentAnalysisService {

	private static final Log LOGGER = LogFactory.getLog(ContentAnalysisServiceImpl.class);
	
	public JSONObject contentAnalysis(JSONObject contentJson) {
		/**
		 * 获取系统换行符
		 */
		String lineSeparator = (String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));
		System.out.println("lineSeparator---->" + lineSeparator);
		
		System.out.println("contentJson--->" + contentJson);
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
		//String keyWords = nlpSV.keyWordExtractor(contentStr, 7);
		List<String> keyWordList = nlpSV.getKeyWord(contentStr, 7);
		LOGGER.info("keyWords---->" + keyWordList);
		System.out.println("keyWords---->" + keyWordList);
		List<String> summary = nlpSV.getSummery(contentStr, 1);
		//String[] keyWordList = keyWords.trim().split("#");
		Map<String,Double> keyWordMap = new HashMap<String, Double>();
		for(int idx = 0; idx < keyWordList.size(); idx ++){
		/*	String[] tempStr = keyWordList.get(idx);
			String key = tempStr[0];
			double weight = Double.parseDouble(tempStr[2]);*/
			String key =  keyWordList.get(idx);
			keyWordMap.put(key, 0.0);
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
		System.out.println("returnJson---->" + returnJson);
		return returnJson;
	}

}
