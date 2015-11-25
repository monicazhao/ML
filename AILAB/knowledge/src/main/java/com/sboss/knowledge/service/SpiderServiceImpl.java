/**
 * 
 */
package com.sboss.knowledge.service;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.sboss.knowledge.base.SocketHttpRequesterStr;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author monica
 *
 */
@Component("spiderService")
public class SpiderServiceImpl implements SpiderService{

	private String getUrlContentByUrl(String url){
		// TODO Auto-generated method stub
		
		String result = "";	
        try {
        	URL targetUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) targetUrl.openConnection();  
            conn.setDoOutput(true);  
            conn.setRequestMethod("POST");  
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));  
            String line;  
            while ((line = in.readLine()) != null) {  
                result += line;  
            }  
            in.close();  
        } catch (Exception e) {  
            e.getMessage();
        }
		return result;
	}

	/**
	 * 由于不同网站的网页结构存在差异，前期只需要满足“极客网”的内容爬取
	 */
	@Override
	public JSONObject getUrlContentBySpider(String url){
		// TODO Auto-generated method stub
		JSONObject contentRes = new JSONObject();
		String urlContent = getUrlContentByUrl(url);
		//对返回的urlContent进行解析，获取指定标签对应 的内容
		Document doc = Jsoup.parse(urlContent);
		String title = doc.select("hgroup").text();
		//获取url对应的核心内容
		Elements article = doc.select("article");
		String content = article.text();
		//获取文章对应的图片
		JSONArray imgList = new JSONArray();
		Elements imgs = article.select("img");
		for(Element img : imgs){
			String singleImg = img.attr("src");
			imgList.add(singleImg);
		}
		contentRes.put("url", url);
		contentRes.put("title", title);
		contentRes.put("content", content);
		contentRes.put("imgList", imgList);
		System.out.println("contentRes --->" + contentRes);
		return contentRes;
	}
	
	/**
	 * 将从给定url中解析出的内容作为字符串上传到文件服务器，并返回文件地址
	 */
	@Override
	public String writeContentToServer(String content, String fileServerPath) throws Exception{
		// TODO Auto-generated method stub
		String tempStr = "";
		tempStr = new String(content.getBytes(),"utf-8");
		HashMap<String, String> urlmap = new HashMap<String, String>();
		String result = SocketHttpRequesterStr.uploadStr(tempStr, urlmap, fileServerPath);
		System.out.println("result--->" + result);
		//返回的result是一个JSONObject，对其进行解析，取得文件在文件服务器上对应的地址，并且返回
		JSONObject tempRes = JSONObject.fromObject(result);
		JSONObject data = tempRes.getJSONObject("data");
		//定义返回的变量
		String finalPath = data.getString("url");
		System.out.println("finalPath--->" + finalPath);
		return finalPath;
		
	}
	
	/**
	 * 将文本内容写入本地文件系统
	 * @param content
	 * @param localFilePath
	 * @return
	 */
	public String writeDataToLocalFile(String data, String localFilePath)
			throws Exception {
		
		System.out.println("writeDataToLocalFile--->data" + data);
		// TODO Auto-generated method stub
		File file =new File(localFilePath);
	    if(!file.exists()){
	    	file.createNewFile();
	    }
	    FileOutputStream out=new FileOutputStream(file,false); //如果追加方式用true  
	    out.write(data.getBytes("UTF-8"));
	    out.close();
	    return localFilePath;
	}

	@Override
	public String writeFileToServer(String localFilePath)
			throws Exception {
		// TODO Auto-generated method stub
		File localFile = new File(localFilePath);
		HashMap<String, String> urlmap = new HashMap<String, String>();
		String result = SocketHttpRequesterStr.uploadFile(localFile, urlmap);
		System.out.println("result--->" + result);
		//返回的result是一个JSONObject，对其进行解析，取得文件在文件服务器上对应的地址，并且返回
		JSONObject tempRes = JSONObject.fromObject(result);
		JSONObject data = tempRes.getJSONObject("data");
		//定义返回的变量
		String finalPath = data.getString("url");
		System.out.println("finalPath--->" + finalPath);
		return finalPath;
	}
	
//	public static void main(String[] args) throws Exception {
//		String data = "上的佛光的哦地方地方对方是否额尔";
//		
//		System.out.println("writeDataToLocalFile--->data" + data);
//		// TODO Auto-generated method stub
//		File file =new File("e:\\test.sp");
//	    if(!file.exists()){
//	    	file.createNewFile();
//	    }
//	    FileOutputStream out=new FileOutputStream(file,false); //如果追加方式用true  
//	    out.write(data.getBytes("UTF-8"));
//	    out.close();
//	}
}
