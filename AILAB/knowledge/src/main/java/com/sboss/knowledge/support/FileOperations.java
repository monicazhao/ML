package com.sboss.knowledge.support;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

/**
 * @author monica 与文件操作相关的服务
 */
@Component("fileOperations")
public class FileOperations{

	public String readFilefromServer(String filePath) {
		// 定义返回结果
		String fileContentStr = "";
		JSONObject lineJson = null;
		//JSONObject singleContentResult = new JSONObject();
		try {
			URL url = new URL(filePath);
			System.out.println("url = " + url);
			GetMethod method = new GetMethod(url.toString());
			
			method.setRequestHeader("Contern-Type", "application/octet-stream");
			HttpClient client = new HttpClient();
			int code = client.executeMethod(method);
			if(code != 200) {
				throw new RuntimeException("code = " + code);
			}
			
			// 从服务器端拿到响应结果，准备开始取得内容
			InputStream in = new BufferedInputStream(method.getResponseBodyAsStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] contexts = new byte[2048];
			int realLEN = 0;
			while((realLEN = in.read(contexts, 0, 2048)) != -1) {
				out.write(contexts, 0, realLEN);
			}
			byte[] textContext = out.toByteArray();
			in.close();
			out.close();
			fileContentStr = new String (textContext , "UTF-8");
			// 输出
			System.out.println("text = " + fileContentStr);
//			
//			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
//			String lines = null;
//			while ((lines = reader.readLine()) != null) {
//				
//				System.out.println("lines---->" + lines);
//				
//				if(lines.startsWith("{")){
//					lineJson = JSONObject.fromObject(lines);
//				}else{
//					lineJson = JSONObject.fromObject("{" + lines + "}");
//				}
///*				Long contentId = lineJson.getLong("contentId");
//				String title = lineJson.getString("title");
//				String content = lineJson.getString("content");
//				singleContentResult.put("contentId", contentId);
//				singleContentResult.put("title", title);
//				singleContentResult.put("content", content);*/
//			}
//			reader.close();
//			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//fileContentStr = singleContentResult.toString();
		//返回从文件服务器获取到的内容
		
		return fileContentStr;
	}
	
	public static void main(String[] args) throws Exception {
		URL url = new URL("http://171.221.254.231:9000/9347998749f0bca8668b4f85fad3c026.spider");
		System.out.println("url = " + url);
		GetMethod method = new GetMethod(url.toString());
		
		method.setRequestHeader("Contern-Type", "");
		HttpClient client = new HttpClient();
		int code = client.executeMethod(method);
		if(code != 200) {
			throw new RuntimeException("code = " + code);
		}
		
		Header[] headers = method.getResponseHeaders();
		for (Header header : headers) {
			System.out.println(header.getName() + "===" + header.getValue());
		}
		byte[] responseContexts = method.getResponseBody();
		String tt = new String(responseContexts);
		System.out.println("responseContexts = " + tt);
	}
}
