package com.sboss.knowledge.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sboss.knowledge.exception.BizException;
import com.sboss.knowledge.service.ContentService;
import com.sboss.knowledge.service.SpiderService;
import com.sboss.knowledge.service.UserContentService;
import com.sboss.knowledge.service.UserService;
import com.sboss.knowledge.support.FileOperations;
//import java.util.logging.Logger;
import com.sboss.knowledge.support.UrlEffectiveJudgement;
import com.sboss.knowledge.support.UrlJudgement;

@Component("getResultByUserContentService")
public class GetResultByUserContentServiceImpl implements IGetResultByUserContentService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SpiderService spiderService;
	
	@Autowired 
	ContentService contentService;
	
	@Autowired 
	UserContentService userContentService;
	
	@Autowired
	FileOperations fileOperations;
	
	@Transactional
	public JSONObject getUserContentResult(JSONObject userContent) {
		PropertyConfigurator.configure("/opt/servicemix/ailab/config/logger.properties");
		Logger logger = Logger.getLogger(GetResultByUserContentServiceImpl.class);
		logger.info("getUserContentResult--->" + userContent.toString());
		JSONObject returnJson = new JSONObject();
		//获取用户传递过来的内容信息:文本或者url
		String contentStr = userContent.getString("analysis_content");
		String wx_openid = null;
		if(userContent.containsKey("wx_openid")){
			wx_openid = userContent.getString("wx_openid");
		}
		String imei = null;
		if(userContent.containsKey("imei")){
			imei = userContent.getString("imei");
		}
		String userId = (wx_openid != null) ? wx_openid : imei;
		Long userType = userContent.getLong("role_type");
		JSONObject userInfo = new JSONObject();
		userInfo.put("userId", userId);
		userInfo.put("userType", userType);
		JSONObject contentInfo = new JSONObject();
		contentInfo.put("content", contentStr);
		JSONObject userContentJson = new JSONObject();
		userContentJson.put("contentInfo", contentInfo);
		boolean isUserExist = true;
		userContentJson.put("userId", userId);
		userContentJson.put("userType", userType);
		try {
			if(isUserExist = userService.isUserExist(userId, userType)){
				logger.info("user "+ userId + " is exist!");
			}
		} catch (BizException e) {
			e.printStackTrace();
		}		
		if(!isUserExist){
			logger.info("user "+ userId + " is not exist");
			boolean isSuccess = true;
			userService.creatNewUser(userId, userType);
			if(!isSuccess){
				logger.info("creat user " + userId +" failure");
			}else{
				logger.info("creat user success");

			}
		}
		
		//判断内容是url还是非url
		UrlJudgement urlJudger = new UrlJudgement();
		UrlEffectiveJudgement urlEffectiveJudger = new UrlEffectiveJudgement();
		Long contentId = 0L;
		String contentInfoStr = null;//"{\"title\":\"测试\",\"content\":\"交通事故肇事逃逸，尚不构成犯罪者将会受到12分的处罚，当然也要承担相应的民事责任。如果因而发生重大事故，致人重伤、死亡或者使公私财产遭受重大损失的，处三年以下有期徒刑或者拘役；交通运输肇事后逃逸或者有其他特别恶劣情节的，处三年以上七年以下有期徒刑；因逃逸致人死亡的，处七年以上有期徒刑。\"}";
		String contentFilePath = null;
//		FileOperations fileOperations = new FileOperations();
		if(!urlJudger.isURL(contentStr)){	
			if(contentStr.length() == 0){
				returnJson.put("contentId", null);
				returnJson.put("message", "You have input an empty text!");
				logger.info("return result is ---->" + returnJson.toString());
				return returnJson;
			}
			logger.info("this String is a text!");
			try {
//				String fileServerPath = "http://171.221.254.231:9000/123456.spider";
//				contentFilePath = spiderService.writeContentToServer(userContentJson.toString(), fileServerPath);
				/**
				 * 先将内容保存到本地文件系统，再将其上传到文件服务器
				 */
				String localFilePath = "E:/temp.spider";
				logger.info("localFilePath--->" + localFilePath);
				String interMedia = spiderService.writeDataToLocalFile(userContentJson.toString(), localFilePath);
				logger.info("interMedia--->" + interMedia);
				contentFilePath = spiderService.writeFileToServer(interMedia);
				/**
				 * 写完之后，销毁文件
				 */
				File file = new File(localFilePath);
				file.delete();
				
				
				logger.info("contentFilePath --->" + contentFilePath);
			} catch (Exception e) {
				e.printStackTrace();
			}			
			contentInfoStr = fileOperations.readFilefromServer(contentFilePath);
			logger.info("contentInfoStr ---> " + contentInfoStr);
			//根据存储内容返回的文件服务器链接，调用读取服务器文件接口
			JSONObject contentJson = JSONObject.fromObject(contentInfoStr);
			//调用分析接口
			IContentAnalysisService contentAnalysis = new ContentAnalysisServiceImpl();
			JSONObject contentAnalysisRes = contentAnalysis.contentAnalysis(contentJson.getJSONObject("contentInfo"));
			logger.info("anaylysis result --->" + contentAnalysisRes.toString());
			//获取当前时间戳
			Long analysisTime = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String date = sdf.format(new Date(analysisTime));
			logger.info("analysis time ---> " + date);
			//存储分析结果到文件服务器
			String analysisResultLink = null;
			try {
				/**
				 * 先将内容保存到本地文件系统，再将其上传到文件服务器
				 */
				Date temp = new Date();
				String localFilePath = "E:/temp.spider";
				String interMedia = spiderService.writeDataToLocalFile(contentAnalysisRes.toString(), localFilePath);
				analysisResultLink = spiderService.writeFileToServer(interMedia);
				
				/**
				 * 写完之后，销毁文件
				 */
				File file = new File(localFilePath);
				file.delete();
				
//				analysisResultLink = spiderService.writeContentToServer(contentAnalysisRes.toString(), "http://171.221.254.231:9000/123456.spider");
			} catch (Exception e) {
				e.printStackTrace();
			}
			JSONObject analysisRes = new JSONObject();
			analysisRes.put("sourceUrl", "");
			analysisRes.put("contentLink", contentFilePath);
			analysisRes.put("executeTime", analysisTime.toString());
			analysisRes.put("contentLength", contentStr.length());
			analysisRes.put("analysisResultLink", analysisResultLink);
			analysisRes.put("contentType", 1);
			analysisRes.put("algorithm", "1");
			//存储分析结果到数据库
			boolean saveSuccess = false;
			contentId = contentService.saveAnalysisResultToDB(analysisRes, userId, userType);
			userContentJson.put("contentId", contentId);
			try {
				JSONObject res = userContentService.saveContentInfoToDB(userContentJson);
			} catch (BizException e) {
				e.printStackTrace();
			}
			//logger.info("save analysis " + saveSuccess);
			returnJson.put("contentId", contentId);
			returnJson.put("message", "This is a text, content analysis success!");
			logger.info("return result is ---->" + returnJson.toString());
			return returnJson;
		} else if(!urlEffectiveJudger.isUrlEffective(contentStr)){
			logger.info(urlEffectiveJudger.isUrlEffective(contentStr));
			logger.info("not valid url");
			returnJson.put("contentId", null);
			returnJson.put("message", "Url is not valid!");
			logger.info("return result is ---->" + returnJson.toString());
			return returnJson;
		}
		
		//是URL并且有效
		boolean isUrlExist = false;
		
		//根据userContent查询是否已经有分析结果
		logger.info("userId:" + userId + "  userType:" + userType + "  contentStr:" + contentStr);
		isUrlExist = contentService.isUrlExisit(userId, userType, contentStr);
		if(isUrlExist){
			logger.info(contentStr + " is exist in DB");
			//如果有，根据userContent查询分析结果
			contentId = contentService.getContentIdBySourceUrl(contentStr);
			logger.info("contentId is ---> " + contentId);
			returnJson.put("contentId", contentId);
			returnJson.put("message", "Url is analysed before, the contentId is " + contentId);
		}else{
			// 调用爬虫接口
			JSONObject spiderRes = spiderService.getUrlContentBySpider(contentStr);
			// 根据爬虫结果存储内容到文件服务器，返回文件服务器链接
			String fileServerPath = "http://171.221.254.231:9000/123456.spider";
			try {
				/**
				 * 先将内容保存到本地文件系统，再将其上传到文件服务器
				 */
				String localFilePath = "E:/temp.spider";
				String interMedia = spiderService.writeDataToLocalFile(spiderRes.toString(), localFilePath);
				contentFilePath = spiderService.writeFileToServer(interMedia);
				
				/**
				 * 写完之后，销毁文件
				 */
				File file = new File(localFilePath);
				file.delete();
				
//				contentFilePath = spiderService.writeContentToServer(spiderRes.toString(), fileServerPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//读取服务器文件
			contentInfoStr = fileOperations.readFilefromServer(contentFilePath);
			System.out.println("contentInfoStr--->" + contentInfoStr);
			JSONObject contentJson = JSONObject.fromObject(contentInfoStr);
			IContentAnalysisService contentAnalysis = new ContentAnalysisServiceImpl();
			JSONObject contentAnalysisRes = contentAnalysis.contentAnalysis(contentJson);
			logger.info("anaylysis result --->" + contentAnalysisRes.toString());
			//存储分析结果到文件服务器
			String analysisResultLink = null;
			try {
				String localFilePath = "E:/temp.spider";
				String interMedia = spiderService.writeDataToLocalFile(contentAnalysisRes.toString(), localFilePath);
				analysisResultLink = spiderService.writeFileToServer(interMedia);
				
				/**
				 * 写完之后，销毁文件
				 */
  			File file = new File(localFilePath);
			file.delete();
				
//				analysisResultLink = spiderService.writeContentToServer(contentAnalysisRes.toString(), "http://171.221.254.231:9000/123456.spider");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Long analysisTime = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String date = sdf.format(new Date(analysisTime));
			JSONObject analysisRes = new JSONObject();
			analysisRes.put("sourceUrl", contentStr);
			analysisRes.put("contentLink", contentFilePath);
			analysisRes.put("executeTime", analysisTime.toString());
			analysisRes.put("contentLength", contentStr.length());
			analysisRes.put("analysisResultLink", analysisResultLink);
			analysisRes.put("contentType", 1);
			analysisRes.put("algorithm", "1");
			//存储分析结果到数据库
			boolean saveSuccess = false;
			contentId = contentService.saveAnalysisResultToDB(analysisRes, userId, userType);
			logger.info("contentId is ---> " + contentId);
			returnJson.put("contentId", contentId);
			returnJson.put("message", "Url content analysis success!");
			userContentJson.put("contentId", contentId);
			try {
				JSONObject res = userContentService.saveContentInfoToDB(userContentJson);
			} catch (BizException e) {
				e.printStackTrace();
			}
		}		
		logger.info("return result is ---->" + returnJson.toString());
		return returnJson;
	}
}
