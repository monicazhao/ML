package com.ai.ailab.robot.processor;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sboss.knowledge.model.Content;
import com.sboss.knowledge.model.User;
import com.sboss.knowledge.model.UserContent;
import com.sboss.knowledge.service.ContentService;
import com.sboss.knowledge.service.UserContentService;
import com.sboss.knowledge.service.UserService;
import com.sboss.knowledge.support.FileOperations;

@Component("findAnalysisResultDetailProcessor")
public class FindAnalysisResultDetailProcessor implements Processor {

	@Autowired
	private ContentService contentService;
	
	@Autowired
	private FileOperations fileOperations;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserContentService userContentService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		//定义待返回的json对象
		JSONObject resultObj = new JSONObject();
		JSONObject params = JSONObject.fromObject(exchange.getIn().getBody(String.class));
		//对从前端传入的内容进行解析
		Long contentId = Long.parseLong(params.getString("analysis_id"));
		String userId = null;
		Long userTypeId = params.getLong("role_type");
		if(userTypeId.equals(1L)){
			userId = params.getString("wx_openid");
		}
		if(userTypeId.equals(2L)){
			userId = params.getString("imei");
		}
		
		//根据userId和userType从user表里面获取到对应的user对象
		User user = userService.queryUserByUserId(userId, userTypeId);
		//userID区别于userId，前者是user_info表的主键
		Long userID = user.getId();
		
		//根据contentId从数据库里面查询对应的content对象
		Content content = contentService.getContentByContentId(contentId);
		String analysisContentUrl = content.getContentLink();
		String analysisOriginUrl = content.getSourceUrl();
		String analysisTime = content.getExecuteTime();
		
		String analysisTitle = null;
		JSONArray analysisImageList = new JSONArray();
		if(analysisOriginUrl == null || analysisOriginUrl.isEmpty()){
			analysisTitle = "";
		}else{
			//根据analysisContentUrl从文件服务器拉取响应的文本文件
			String serverContentResult = fileOperations.readFilefromServer(analysisContentUrl);
			JSONObject serverContentResultObj = JSONObject.fromObject(serverContentResult);
			analysisTitle = serverContentResultObj.getString("title");
			//从分析结果文件中提取到图片
			String imgResList = serverContentResultObj.getString("imgList");
			String tempInter = imgResList.substring(1, imgResList.length() - 1);
			String[] InterList = tempInter.split(",");
			for(int i = 0; i < InterList.length; i++){
				String analysisImage = InterList[i];
				JSONObject singleImage = new JSONObject();
				singleImage.put("analysis_image", analysisImage);
				analysisImageList.add(singleImage);
			}
		}
		
		//根据user和content从userContent表获取对应的userContent对象
		JSONArray analysisKeywordList = new JSONArray();
		UserContent userContent = userContentService.getUserContentByUCID(contentId, userID);
		String userAnalysisResultLink = userContent.getUserAnalysisResultLink();
		
		String interObj = fileOperations.readFilefromServer(userAnalysisResultLink);
		JSONObject tempObj = JSONObject.fromObject(interObj);
		String analysisContent = tempObj.getJSONObject("analysisResult").getString("summary");
		String keyWordsList = tempObj.getJSONObject("analysisResult").getString("keyWords");
		String keyWordsInter = keyWordsList.substring(1,keyWordsList.length()-1);
		String[] keyWords = keyWordsInter.split(",");
		for(int i = 0; i < keyWords.length; i++){
			String keyWord = keyWords[i].split(":")[0];
			JSONObject singleKeyWord = new JSONObject();
			singleKeyWord.put("analysis_keyword", keyWord);
			analysisKeywordList.add(singleKeyWord);
		}
		
		resultObj.put("analysis_content_url", analysisContentUrl);
		resultObj.put("analysis_origin_url", analysisOriginUrl);
		resultObj.put("analysis_title", analysisTitle);
		resultObj.put("analysis_image_list", analysisImageList);
		resultObj.put("analysis_time", analysisTime);
		resultObj.put("analysis_content", analysisContent);
		resultObj.put("analysis_keyword_list", analysisKeywordList);
		
		exchange.getIn().setBody(resultObj);
	}
}
