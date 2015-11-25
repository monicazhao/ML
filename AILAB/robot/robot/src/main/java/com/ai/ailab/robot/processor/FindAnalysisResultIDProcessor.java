package com.ai.ailab.robot.processor;

import net.sf.json.JSONObject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ai.intelligence.interfaces.IGetResultByUserContentPkgSV;

@Component("findAnalysisResultIDProcessor")
public class FindAnalysisResultIDProcessor implements Processor {

	@Autowired
	private IGetResultByUserContentPkgSV getResultByUserContentPkgSV;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		JSONObject userContent = new JSONObject();
		JSONObject params = JSONObject.fromObject(exchange.getIn().getBody(String.class));
		Long userType = params.getLong("role_type");
		String userId = null;
		if(userType.equals(1L)){
			userId = params.getString("wx_openid");
			userContent.put("wx_openid", userId);
		}
		if(userType.equals(2L)){
			userId = params.getString("imei");
			userContent.put("imei", userId);
		}
		String source = params.getString("analysis_content");		
		userContent.put("role_type", userType);
		userContent.put("analysis_content", source);
		
		JSONObject analysisObj = getResultByUserContentPkgSV.getUserContentResult(userContent);
		
		JSONObject userInfoObj = new JSONObject();
		userInfoObj.put("userId", userId);
		userInfoObj.put("userType", userType);	
		Long analysisId = analysisObj.getLong("contentId");
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("userInfo", userInfoObj);
		resultObj.put("analysis_id", analysisId);	
		exchange.getIn().setBody(resultObj);
	}
}
