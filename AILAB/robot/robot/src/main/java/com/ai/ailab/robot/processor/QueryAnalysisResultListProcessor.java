package com.ai.ailab.robot.processor;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sboss.knowledge.service.UserContentService;

@Component("queryAnalysisResultListProcessor")
public class QueryAnalysisResultListProcessor implements Processor {

	@Autowired
	private UserContentService userContentService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		JSONObject params = JSONObject.fromObject(exchange.getIn().getBody(String.class));
		Long userType = params.getLong("role_type");
		String userId = null;
		if(userType.equals(1L)){
			userId = params.getString("wx_openid");
		}
		if(userType.equals(2L)){
			userId = params.getString("imei");
		}
		
		JSONObject userInfoObj = new JSONObject();
		userInfoObj.put("userId", userId);
		userInfoObj.put("userType", userType);
		
		JSONArray analysisResList = userContentService.queryAnalysisResult(userInfoObj);
		String temp = analysisResList.toString().replace("contentId", "analysis_id");
		
		JSONArray resultArr = new JSONArray();
		resultArr.add(JSONArray.fromObject(temp));
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("analysis_result_list", resultArr);
		
		exchange.getIn().setBody(resultObj);
	}
}
